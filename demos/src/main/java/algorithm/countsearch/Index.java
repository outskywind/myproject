package algorithm.countsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

/**
 * Created by quanchengyun on 2018/11/27.
 */
public class Index {

    static String  index_file = "d:/demos/index.idx";


    static int TOTAL = 100000000;
    static int  chunk_size = 1000000 ;

    static int trip = TOTAL/chunk_size ;

    static int index_bucket_size = 8 ;
    static int index_item_num = TOTAL/index_bucket_size ;
    static long index_bucket_DIV_CAPACITY = Long.MAX_VALUE/index_item_num +1;

    MappedByteBuffer indexBuffer =  getIndexBuffer();

    int data_start = index_item_num*8;//字节数
    //下一个可写入数据指针字节数
    int data_offset = 0;

    static long mask32Bit  = (1L<<32)-1;
    //dataCache
    static CircleQueue dataCache = new CircleQueue(16);

    RandomAccessFile indexDataFile = getFile();
    private boolean isIndexed=false;


    static class CircleQueue{
        public  Bucket[] array;
        public int size;
        public  int front;
        public int rear;

        public CircleQueue(int size){
            array = new Bucket[size];
            this.size=size;
            front=0;
            rear=0;
        }

        public Bucket find(int key){
            if(rear==front) {
                return null;
            }
            for(int i= front;(i+1)%size<rear;i++){
                Bucket item = array[i%size];
                if(item.start_offset<=key && key <=(item.start_offset+chunk_size*12)){
                    return item;
                }
            }
            return null;
        }

        //队列直接替换最久未被使用
        public void enqueue(Bucket value){
            //满
            if((rear+1)%size==front){
                dequeue();
            }
            array[rear]=value;
            rear=(rear+1)%size;
        }

        public Bucket dequeue(){
            //空
            if(rear==front) {
                return null;
            }
            else{
                int temp=front;
                front=(front+1)%size;
                //被销毁之前更新到磁盘
                array[temp].buffer.force();
                return array[temp];
            }
        }

        public boolean isEmpty() {
            return size == 0;
        }
        public int getLength(){
            if(rear>front){
                return  rear-front;
            }
            else {
                return array.length-1;
            }
        }
    }


    static class Bucket{
        int start_offset=0;
        MappedByteBuffer buffer;
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
            MappedByteBuffer buffer=null;
            for(int j=0;j<trip;j++){
                //each trip indexing 1M data
                long[] data_chunk = new long[chunk_size];
                long position = j*chunk_size*8;
                buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,position,chunk_size*8);
                LongBuffer lb = buffer.load().asLongBuffer().asReadOnlyBuffer();
                lb.get(data_chunk);
                indexing(data_chunk);
            }
            //更新索引到磁盘,可以全部写完后只同步一次索引到磁盘
            //indexBuffer
            indexBuffer.force();
            this.isIndexed=true;
            if(buffer!=null)buffer.clear();
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

     //指针都是相对数据文件起始data_start的偏移字节数
    private void indexing(long[] data_chunk) throws Exception{
        if(!indexBuffer.isLoaded())indexBuffer.load();
        //IntBuffer 方便编辑修改
        IntBuffer indexBuff = indexBuffer.asIntBuffer();
        //100万数据块<value8字节,next指针4字节>
        //IntBuffer 方便写入
        IntBuffer dataBuff  = ByteBuffer.allocate(data_chunk.length*12).asIntBuffer();
        //int data_position =
        for(long data:data_chunk){
            //1.定位索引项,更新索引的指针
            int k = indexKey(data);
            //索引项对应的缓存偏移字节
            int head = k*8;
            int tail = head+4;
            //1.修改索引指针和尾数据指针
            if(indexBuff.get(head/4)==0){
                indexBuff.put(head/4,data_offset);
                indexBuff.put(tail/4,data_offset);
            }
            else{
                //插入尾部
                //!!每一个数据节点尽可能缓存到内存中
                int tail_offset = indexBuff.get(tail/4);
                indexBuff.put(tail/4,data_offset);
                //find the tail data
                Bucket tail_bucket = dataCache.find(tail_offset);
                //加载到内存
                if(tail_bucket==null){
                    tail_bucket = loadData(tail_offset);
                    dataCache.enqueue(tail_bucket);
                }
                //找到这个索引的尾数据的next指针
                int next = tail_offset+8;
                int idx = next - tail_bucket.start_offset;
                //修改它指向当前插入的数据
                try{
                    tail_bucket.buffer.asIntBuffer().put(idx/4,data_offset);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }
            //2.顺序填充数据
            //指针计算字节数data_offset
            int high = (int)((data >>>32) & mask32Bit);
            int low = (int)(data & mask32Bit);
            //3int=12字节
            dataBuff.put(high).put(low).put(0);
            //每插入一个数据项，当前数据指针+12字节
            data_offset+=12;
        }
    }

    int indexKey(long v){
        return (int)(v/index_bucket_DIV_CAPACITY);
    }

    //tailOffset 是字节数偏移
    private Bucket loadData(int tail_offset) throws Exception {
        Bucket bucket = new Bucket();
        int chunk_bytes = chunk_size*12;
        bucket.start_offset = chunk_bytes*(tail_offset/chunk_bytes);
        bucket.buffer = indexDataFile.getChannel().map(FileChannel.MapMode.READ_WRITE,data_start+bucket.start_offset,chunk_bytes);
        if(!bucket.buffer.isLoaded()){
            bucket.buffer.load();
        }
        return bucket;
    }



    RandomAccessFile getFile() {
        try{
            File f = new File(index_file);
            if(!f.getParentFile().exists()){
                f.getParentFile().mkdir();
            }
            if(!f.exists()){
                f.createNewFile();
            }
            return new RandomAccessFile(f,"rw");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 0 - index_item_num*8 字节区属于索引
     * @return
     */
    MappedByteBuffer  getIndexBuffer() {
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
            return byteBuffers.load();
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

    /**
     * 查找索引数据文件，需要读取这个bucket中包含所有的元素
     * 随机均匀分布的情况下，平均8次
     * @param v
     * @throws Exception
     */
    private int find(long v) throws Exception{
        if(!this.isIndexed){
            this.build();
        }
        int head  = this.indexBuffer.get(indexKey(v));
        if(head==0){
            return 0;
        }
        int count =0 ;
        int next = head;
        while(next!=0){
            MappedByteBuffer mp = indexDataFile.getChannel().map(FileChannel.MapMode.READ_ONLY,data_start+next,12).load();
            IntBuffer intBuffer = mp.asIntBuffer();
            long high =intBuffer.get(0);
            long low = intBuffer.get(1);
            next = intBuffer.get(2);
            long v2 = (high<<32) + low;
            if(v2==v){
                count++;
            }
        }
        return count;
    }


    //
    public static void  main(String[]  args){
        //System.out.print(64-Long.numberOfLeadingZeros(1L<<50));
        long  start = System.nanoTime();
        Index index = new Index();
        try{
            Random r = new Random();
            for(int i=0;i<1;i++){
                long v= r.nextLong();
                v = v<0?-v-1:v;
                System.out.println(v+" 出现次数："+index.find(v));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.print("生成索引数据："+ (System.nanoTime() - start)/1000000+"ms");
    }


}
