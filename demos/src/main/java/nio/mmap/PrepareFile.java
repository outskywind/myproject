package nio.mmap;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PrepareFile {



    @Test
    public void test(){

        String file = "d:/test.dt";
        byte[] bytes = new byte[4096+1024];

        try {
            RandomAccessFile f = new RandomAccessFile(file,"rw");
            MappedByteBuffer buf = f.getChannel().map(FileChannel.MapMode.READ_WRITE,0,4096+1024);
            buf.put(bytes);
            buf.force();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
