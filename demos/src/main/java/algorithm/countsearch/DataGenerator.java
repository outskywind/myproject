package algorithm.countsearch;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by quanchengyun on 2018/11/28.
 */
public class DataGenerator {

    public  static String  dataFile = "d:/demos/data.dt";
    public  static String  testFile = "d:/demos/test.dt";

    public  int chunk_size = 1000000 ;
    public  int trip = 100;
    //100个检测数
    public  KCounter[] testNums  = new KCounter[100];

     class KCounter{
        long number;
        int count;
    }

    public void generate() throws IOException{
        long  start = System.nanoTime();
        File f = new File(dataFile);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(f.exists()){
            return ;
        }
        f.createNewFile();
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
                    num = num<0?-num-1:num;
                    lb.put(num);
                    //for test
                    for(int k=0;k<testNums.length;k++){
                        if(testNums[k]==null){
                            KCounter kCounter = new KCounter();
                            kCounter.number = num;
                            kCounter.count = 1;
                            testNums[k]=kCounter;
                            break;
                        }
                        if(testNums[k].number==num){
                            testNums[k].count++;
                        }
                    }
                }
                buffer.force();
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("生成数据："+ (System.nanoTime() - start)/1000000+"ms");

        File tf = new File(testFile);
        if(!tf.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(tf.exists()){
            tf.delete();
        }
        tf.createNewFile();
        LongBuffer longBuffer = ByteBuffer.allocate(100*16).asLongBuffer();
        for(KCounter test: testNums){
            System.out.println(test.number+" count:"+test.count);
        }
    }

}
