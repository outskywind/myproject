package nio.mmap;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MMapTest {




    Object lock=new Object();

    CyclicBarrier barrier = new CyclicBarrier(2);


    public   void  test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try{

                RandomAccessFile f = new RandomAccessFile("d:/test.dt","rw");
                System.out.println("length="+f.length());
                MappedByteBuffer buf = f.getChannel().map(FileChannel.MapMode.READ_WRITE,0,f.length()).load();
                barrier.await();
                byte[] reads = new byte[1024];
                buf.get(reads,0,reads.length);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    barrier.await();
                    RandomAccessFile ff = new RandomAccessFile("d:/test.dt","rw");
                    ff.setLength(4096);
                    Thread.sleep(5000);
                    System.out.println("exit --");
                    ff.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();
    }


    public static void main(String[] args){
        MMapTest mMapTest = new MMapTest();

        try {
            mMapTest.test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
