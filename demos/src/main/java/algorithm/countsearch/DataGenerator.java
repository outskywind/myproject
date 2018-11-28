package algorithm.countsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by quanchengyun on 2018/11/28.
 */
public class DataGenerator {


    public static String  dataFile = "d:/demos/data.dt";


    public static int chunk_size = 1000000 ;
    public static int trip = 100;


    public static void main(String[] args){
        long  start = System.nanoTime();
        File f = new File(dataFile);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(f.exists()){
            return ;
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RandomAccessFile file = new RandomAccessFile(dataFile,"rw");
            Random random = new Random();
            MappedByteBuffer buffer;
            for(int j=0;j<trip;j++){
                long position = j*chunk_size*8;
                buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE,position,chunk_size*8);
                LongBuffer lb = buffer.load().asLongBuffer();
                for(int i=0; i<chunk_size;i++){
                    long num = random.nextLong();
                    lb.put(num<0?-num-1:num);
                }
                buffer.force();
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("生成数据："+ (System.nanoTime() - start)/1000000+"ms");
    }

}
