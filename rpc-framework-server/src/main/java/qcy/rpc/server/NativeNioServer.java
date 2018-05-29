package qcy.rpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qcy.rpc.buffer.DirectBufferPool;

public class NativeNioServer {

    /**
     * NIO2 其实是AIO,与之前的NIO机制不一样 这个是proactor模式; NIO 1 是reactor 模式 
     * linux 下的 AIO 并不是内核实现的，而是用户线程实现的
     * posix aio , glibc 库实现的
     * 
     * @param port
     */
    public void start(int port) {
        
        //1. server socket channel,Nio2
        try {
            // channel ,default reactor group[thread pool]
            // 这个就是Acceptor
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
            // bind InetSocketAddress, actually ,channel 就是socket 与 应用程序之间的通道。
            // channel 由绑定的socket端口，监听的事件，处理的线程组成。
            serverChannel.bind(new InetSocketAddress(port), 128);

            // worker reactor group
            // Executors.newWorkStealingPool() 用的是forkjoinpool ，是分解子任务后，再组装子任务结果，就可以获得最终结果的
            // 异步任务类型使用
            //AsynchronousChannelGroup workerGroup = AsynchronousChannelGroup
            //        .withThreadPool(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1));
            final ExecutorService executor =
                    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);

            while (true) {
                // create connection asynchronously
                // completion handler 将会与serverChannel 的 initiate thread 是同一个线程池调用
                // 高并发时，如果用户连接数高，那么将会耗尽线程池可用线程。导致无法及时接收新的连接请求
                // 因此将在这里分配给worker线程池处理socketChannel的IO
                // 默认的 AsynchronousChannelGroup 就是 completion dispatcher
                // 这里与论文不一样，不是把Acceptor 自己作为 completionHandler 传递。,简化了一步
                // 而是他创建出来的Http handler:[AsynchronousSocketChannel]
                serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                	@Override
                    public void completed(final AsynchronousSocketChannel result, final Object attachment) {
                        executor.submit(new SocketChannelEvent(result));
                    }
					@Override
					public void failed(Throwable exc, Object attachment) {
					}
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SocketChannelEvent implements Callable {
        private AsynchronousSocketChannel socketChannel;
        // private Selector socketSelector;

        public SocketChannelEvent(AsynchronousSocketChannel channel) {
            this.socketChannel = channel;
            // this.socketSelector = socketSelector;
        }

        /**
         * socket channel 的 Read/Write 处理 这里是从default Group线程池里调度的 那么如果使用thradlocal
         * 变量保存池化的byteBuffer 将会是这个线程使用。
         */
        @Override
        public Object call() throws Exception {
            // implements the worker reactor; NIO2 异步通道
            // 直接内存 不会由GC回收，切记切记，需要手动释放
            // 而且分配时比较耗时，应该使用缓存的ByteBuffer 内存池。
            // 1M大小， linux socket 默认socket buffer为8k
            // -XX:MaxDirectMemorySize= 需要设置大小，默认为-Xmx相同
            // 这个是每个线程threadlocal的。NIO中worker线程为2cpu-1个，因此没有问题。
            // 那么就要尽量保持buffer重用，足够大小
            final ByteBuffer b = DirectBufferPool.getBuffer();
            // sun 的实现中，如果buffer已满，将会返回 0 result。
            socketChannel.read(b, b, new Dispatcher(socketChannel));
            return null;
        }
    }

    // 分发处理业务,读取到ByteBuffer完成之后才回调 dispatcher处理
    // Attention：java原生的NIO没有处理半包情况
    public class Dispatcher implements CompletionHandler<Integer, ByteBuffer>
    {
        // channel操作写回用
        private AsynchronousSocketChannel socketChannel;
        // 8k的初始容量,扩充1.5倍
        private byte[] request = new byte[8192];
        private int tail = 0;

        public Dispatcher(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        /**
         * socket is stream ,so the content bytes will come continually so the program should repeat
         * reading until reach the end;
         */
        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            try {
                System.out.println("read resultCode:" + result);
                attachment.flip();
                byte[] bytes = attachment.array();
                // 判断是否需要扩充
                if (tail + bytes.length > request.length) {
                    byte[] extend = new byte[(int) (request.length * 1.5)];
                    System.arraycopy(request, 0, extend, 0, request.length);
                    request = extend;
                }
                // append to request
                System.arraycopy(bytes, 0, request, tail, bytes.length);
                tail += bytes.length;
                // -1 已经读取完成
                while (result != -1) {
                    System.out.println("result not -1:continue to read");
                    attachment.clear();
                    // continue to read to this dispatcher
                    socketChannel.read(attachment, attachment, this);
                }
                // then go on, finished reading
                System.out.println("this channel read finished resultCode:" + result);
                System.out.println("read content:" + new String(request, "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
        }

    }

    public static void main(String[] args) {

    }

}
