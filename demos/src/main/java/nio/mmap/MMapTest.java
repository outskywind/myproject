package nio.mmap;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

public class MMapTest {

   CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);

    public   void  test(String file)  {
        Thread t1 = new Thread(() -> {
            try{
                RandomAccessFile f = new RandomAccessFile(file,"rw");
                System.out.println("length="+f.length());
                MappedByteBuffer buf = f.getChannel().map(FileChannel.MapMode.READ_WRITE,0,f.length()).load();
                latch2.countDown();//done
                latch1.await();//wait here
                byte[] reads = new byte[1024];
                buf.position(4096);
                buf.get(reads,0,reads.length);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                latch2.await();//wait here
                RandomAccessFile ff = new RandomAccessFile(file,"rw");
                ff.setLength(4096);
                latch1.countDown(); //done
                System.out.println("exit --");
                ff.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }


    public static void main(String[] args){
        MMapTest mMapTest = new MMapTest();
        String file=args[0];
        mMapTest.test(file);
    }


}
