package algorithm.countsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by quanchengyun on 2018/11/27.
 */
public class Index {

    static String  index_file = "d:/demos/index.idx";


    static int TOTAL = 100000000;

    //static int bucketSize = 64 ;

    //static Bucket[] buckets = new Bucket[bucketSize];

    static int index_bucket_size = 8 ;
    static int index_item_num = TOTAL/8 ;
    static long index_bucket_DIV_CAPACITY = Long.MAX_VALUE/index_item_num +1;

    IntBuffer indexBuffer =  getIndexBuffer();
    int data_pointer = index_item_num*8;

    /*static {
        for(int i=0;i<bucketSize;i++){
            buckets[i]=new Bucket();
        }
    }*/

    static class Bucket{
        int num=0;
        LongBuffer buffer;
        FileChannel channel;
    }

    /**
     * 16bit   point to  root  bucket
     *
     * 16bit     point to  sublevel  bucket
     *
     * 16bit       point to  subsublevel  bucket
     *
     * 16bit         point to  leaf to get the data
     */

    /**
     * at last we came back to  b+ tree
     * build index first
     */
    public void build() throws Exception {
        File f = new File(DataGenerator.dataFile);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(!f.exists()){
            throw new FileNotFoundException("d:/demos/data.dt 源数据文件未生成");
        }
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(DataGenerator.dataFile,"r");
            MappedByteBuffer buffer;
            for(int j=0;j<DataGenerator.trip;j++){
                //each trip indexing 1M data
                long[] data_chunk = new long[DataGenerator.chunk_size];
                long position = j*DataGenerator.chunk_size*8;
                buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,position,DataGenerator.chunk_size*8);
                LongBuffer lb = buffer.load().asLongBuffer().asReadOnlyBuffer();
                lb.get(data_chunk);
                //
                //bucketing(data_chunk);
                indexing(data_chunk);
                //
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (file!=null){
                file.close();
            }
        }
        //then we count each data in each bucket
        /*for(Bucket bucket:buckets){
            if(bucket.num==0){
                continue;
            }
            //read into the mem and count
            MappedByteBuffer byteBuffers = bucket.channel.map(FileChannel.MapMode.READ_WRITE,0,bucket.num*8).load();
            //byteBuffers.asLongBuffer()
        }*/

     }

    private void indexing(long[] data_chunk) throws Exception{
        //100万数据块<数据8字节,下一个指针4字节>
        IntBuffer buffer  = ByteBuffer.allocate(data_chunk.length*12).asIntBuffer();
        //int data_position =
        for(long data:data_chunk){
            //1.定位索引项
            int k = (int)(data/index_bucket_DIV_CAPACITY);
            //头指针未设置,说明是第一个,因为是IntBuffer
            int head = k*2;
            int tail = head+1;
            if(indexBuffer.get(head)==0){
                indexBuffer.put(head,data_pointer);
                indexBuffer.put(tail,data_pointer);
            }
            else{
                indexBuffer.get(tail);
            }

            //顺序填充数据，修改相应指针


            //buffer.put(k,);
            //每插入一个数据项
            data_pointer+=12;
        }



       //xieru
        indexBuffer.put();



    }

    /**
     * 0 - index_item_num*4 字节区属于索引
     * @return
     */
    IntBuffer  getIndexBuffer() {
        try{
            File f = new File(index_file);
            if(!f.getParentFile().exists()){
                f.getParentFile().mkdir();
            }
            if(!f.exists()){
                f.createNewFile();
            }
            RandomAccessFile file = new RandomAccessFile(f,"rw");
            MappedByteBuffer byteBuffers = file.getChannel().map(FileChannel.MapMode.READ_WRITE,0,index_item_num*8);
            return byteBuffers.load().asIntBuffer();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


   /* private void bucketing(long[] data_chunk) throws Exception{
        for(long data : data_chunk){
            int k  =  64-Long.numberOfLeadingZeros(data);
            k=k==64?0:k;
            buckets[k].num++ ;
            if(buckets[k].buffer==null){
                buckets[k].buffer = ByteBuffer.allocate(data_chunk.length*8).asLongBuffer();
            }
            if(buckets[k].channel==null){
                buckets[k].channel  =  getBukcetFile(k);
            }
            buckets[k].buffer.put(data);
        }//
        for(int i=0;i<buckets.length;i++){
            //有的没有
            if(buckets[i].buffer!=null){
                buckets[i].buffer.flip();
                //写磁盘以保证下一次
                long position = buckets[i].channel.position();
                MappedByteBuffer byteBuffers = buckets[i].channel.map(FileChannel.MapMode.READ_WRITE,position,buckets[i].buffer.limit()*8);
                byteBuffers.load().asLongBuffer().put(buckets[i].buffer);
                byteBuffers.force();
                //buckets[i].channel.force(true);
                //释放此buffer
                buckets[i].buffer.clear();
                //重置
                buckets[i].buffer=null;
            }
        }
    }

    FileChannel getBukcetFile(int k) throws Exception{
        File f = new File(index_path+k);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(!f.exists()){
            f.createNewFile();
        }
        RandomAccessFile file = new RandomAccessFile(f,"rw");
        return  file.getChannel();
    }*/

    //
    public static void  main(String[]  args){
        //System.out.print(64-Long.numberOfLeadingZeros(1L<<50));
        long  start = System.nanoTime();
        Index index = new Index();
        try{
            index.build();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.print("生成索引数据："+ (System.nanoTime() - start)/1000000+"ms");
    }



}
