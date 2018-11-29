package algorithm.countsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;

public class HybridIndex {


    static String  index_path = "d:/demos/index/";
    static int TOTAL = 100000000;
    static int chunk_size = 1000000 ;

    //桶大小取100
    static int index_bucket_CAPACITY = 100 ;
    static int index_item_num = TOTAL/index_bucket_CAPACITY ;
    static long index_bucket_DIV_FRACTION = Long.MAX_VALUE/index_item_num +1;

    List<Long>[] bucketsInMem = new LinkedList[index_item_num];

    static int data_partition = 100;
    static int data_start = index_item_num*8;
    Pt[] index_in_mem = new Pt[data_partition];

    class Pt{
        int[] count = new int[index_item_num];
        int[] start = new int[index_item_num];
    }


    MappedByteBuffer[] indexMbbs= new MappedByteBuffer[data_partition];
    //MappedByteBuffer[] dataMbbs= new MappedByteBuffer[data_partition];


    ExecutorService executor = new ThreadPoolExecutor(1,2*Runtime.getRuntime().availableProcessors(),1, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(100));

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
            for(int j=0;j<data_partition;j++){
                //each trip indexing 1M data
                long[] data_chunk = new long[DataGenerator.chunk_size];
                long position = j*DataGenerator.chunk_size*8;
                buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,position,DataGenerator.chunk_size*8);
                LongBuffer lb = buffer.load().asLongBuffer().asReadOnlyBuffer();
                lb.get(data_chunk);
                indexing(data_chunk,j);
            }
            if(buffer!=null)buffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (file!=null){
                file.close();
            }
        }
    }

    private void indexing(long[] data_chunk,int k) throws Exception{

        MappedByteBuffer indexMbb = getIndexMBB(k);
        MappedByteBuffer dataMbb = getDataMBB(k,data_chunk.length);
        indexMbbs[k]=indexMbb;
        //内存构建
        for(int i=0;i<data_chunk.length;i++){
            int key = indexKey(data_chunk[i]);
            if(bucketsInMem[key]==null){
                bucketsInMem[key] = new LinkedList();
            }
            bucketsInMem[key].add(data_chunk[i]);
        }
        //索引
        index_in_mem[k]=new Pt();
        Pt p = index_in_mem[k];
        for(int i=0;i<bucketsInMem.length;i++){
            if(bucketsInMem[i]!=null){
                p.count[i] = bucketsInMem[i].size();
            }
        }
        for(int i=1;i<index_in_mem[k].count.length;i++){
            p.start[i]=p.start[i-1]+p.count[i-1];
        }
        //int[] offsets = new int[index_in_mem[k].count.length*2];
        IntBuffer ib = indexMbb.asIntBuffer();
        for(int i=0;i<index_in_mem[k].count.length;i++){
            //offsets[2*i] = p.start[i];
            //offsets[2*i+1] = p.start[i];
            ib.put(p.start[i]).put(p.count[i]);

        }
        LongBuffer lb = dataMbb.asLongBuffer();
        for(int i=0;i<bucketsInMem.length;i++){
            if(bucketsInMem[i]!=null){
                Iterator<Long> it =bucketsInMem[i].iterator();
                while(it.hasNext()){
                    try{
                        lb.put(it.next());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
        indexMbb.force();
        indexMbb.clear();
        dataMbb.force();
        dataMbb.clear();
    }

    int indexKey(long v){
        return (int)(v/index_bucket_DIV_FRACTION);
    }

    MappedByteBuffer getIndexMBB(int k ) throws Exception{
        File f = new File(index_path+k);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(!f.exists()){
            f.createNewFile();
        }
        RandomAccessFile file = new RandomAccessFile(f,"rw");
        return  file.getChannel().map(FileChannel.MapMode.READ_WRITE,0,index_item_num*8);
    }

    MappedByteBuffer getDataMBB(int k ,int capacity ) throws Exception{
        File f = new File(index_path+k);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(!f.exists()){
            f.createNewFile();
        }
        RandomAccessFile file = new RandomAccessFile(f,"rw");
        return  file.getChannel().map(FileChannel.MapMode.READ_WRITE,data_start,capacity*8).load();
    }

    MappedByteBuffer fetchDataRecordMBB(int k ,int position , int capacity ) throws Exception{
        File f = new File(index_path+k);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdir();
        }
        if(!f.exists()){
            f.createNewFile();
        }
        RandomAccessFile file = new RandomAccessFile(f,"rw");
        return  file.getChannel().map(FileChannel.MapMode.READ_ONLY,data_start+position*8,capacity*8).load();
    }


    private int find(long v) throws Exception{
        if(!this.isIndexed()){
            this.build();
        }
        List<Future<Integer>> results = new ArrayList<>(data_partition);
        for(int i=0;i<data_partition;i++){
            final int p = i;
            final int key = indexKey(v);
            Future<Integer>  result = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int result = 0;
                    MappedByteBuffer indexMbb = indexMbbs[p];
                    if(!indexMbb.isLoaded()){
                        indexMbb.load();
                    }
                    int start = indexMbb.asIntBuffer().get(key*2);
                    int count = indexMbb.asIntBuffer().get(key*2+1);
                    MappedByteBuffer dataMbb = fetchDataRecordMBB(p,start,count);
                    long[] data = new long[count];
                    dataMbb.asLongBuffer().get(data);
                    for(long d:data){
                        if (d == v){
                            result ++;
                        }
                    }
                    return result;
                }
            });
            results.add(result);
        }
        //
        int count = 0;
        for(Future<Integer> r:results){
            count+= r.get();
        }
        return count;
    }


    private boolean isIndexed() {
        File f = new File(index_path+0);
        return f.exists();
    }


    public static void  main(String[]  args){
        //System.out.print(64-Long.numberOfLeadingZeros(1L<<50));
        long  start = System.nanoTime();
        HybridIndex index = new HybridIndex();
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


