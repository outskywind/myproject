package org.lotus.tecent.search;

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

    public  static String  dataFile = "d:/demos/data.dt";
    public  static String  testFile = "d:/demos/test.dt";

    public  int chunk_size = 1000000 ;
    public  int trip = 100;
    //100个检测数
    public  KCounter[] testNums  = new KCounter[100];
    public int testNumsSize=0;

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
                            testNumsSize++;
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
        System.out.println("生成数据："+ (System.nanoTime() - start)/1000000+"ms");

        File tf = new File(testFile);
        if(!tf.getParentFile().exists()){
            tf.getParentFile().mkdir();
        }
        if(tf.exists()){
            tf.delete();
        }
        tf.createNewFile();
        MappedByteBuffer ioBuff = new RandomAccessFile(tf,"rw").getChannel().map(FileChannel.MapMode.READ_WRITE , 0 ,testNums.length*16 ).load();
        LongBuffer longBuffer = ioBuff.asLongBuffer();
        for(KCounter test: testNums){
            System.out.println(test.number+" count:"+test.count);
            longBuffer.put(test.number).put(test.count);
        }
        ioBuff.force();
    }

    public KCounter[] getTestNums() throws IOException{
         if (this.testNumsSize==testNums.length) {
             return testNums;
         }
        File tf = new File(testFile);
        if(!tf.getParentFile().exists()){
            tf.getParentFile().mkdir();
        }
        if(!tf.exists()){
            throw new FileNotFoundException(testFile+" 未生成，先执行generate()");
        }
        LongBuffer longBuffer = new RandomAccessFile(tf,"r").getChannel().map(FileChannel.MapMode.READ_ONLY , 0 ,testNums.length*16 ).load().asLongBuffer();
        for(int i=0;i<testNums.length;i++){
            //System.out.println(test.number+" count:"+test.count);
            long number = longBuffer.get();
            long count = longBuffer.get();
            testNums[i]=new KCounter();
            testNums[i].number=number;
            testNums[i].count=(int)count;
            testNumsSize++;
        }
        return testNums;
    }

    public boolean isDataGerated(){
        File f = new File(DataGenerator.dataFile);
        return f.exists();
    }

}
