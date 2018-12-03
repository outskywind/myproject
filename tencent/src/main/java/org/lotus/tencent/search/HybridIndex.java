package org.lotus.tecent.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;


/**
 * 时间复杂度分析，从分区的索引数据文件中检索一个key，需要 2次 Tio
 * 从检索出的桶中数据大小n 因为是随机分布的，所以期望值为 N/index_item_num
 * 设遍历一个内存元素为 Ta
 * 因为有Ni个索引数据文件分片（假定Ni固定为100），T1 = 100*2Tio+ n*Ta = 100(2Tio+ N*Ta/index_item_num)
 * 当 index_item_num 为固定值时，T1 是一个 O(N) 的线性关系
 * 比较分析顺序遍历：
 * 每次比较需要 N 次 ， 需要的磁盘Tio 次数假定为 N的线性关系 cN
 * 那么总时间为 T2 = N*Ta+cN*Tio = N(Ta+cTio)
 *
 * 当N->极限大时 lim(T1) = (100*Ta/index_item_num)N
 *              lim(T2) = (Ta +cTio)N
 *             lim(T1/T2)=(100/index_item_num)*Ta/(cTio+Ta) < (100/(index_item_num*c))*Ta/Tio ,通常 Ta <<< Tio
 *             因此index_item_num*c>=100时，即可达到远远小于T2的检索时间
 */
public class HybridIndex {

    static String  index_path = "d:/demos/index/";
    static int TOTAL = 100000000;
    static int chunk_size = 1000000 ;

    //桶大小取100
    static int index_bucket_CAPACITY = 100 ;
    static int index_item_num = TOTAL/index_bucket_CAPACITY ;
    static long index_bucket_DIV_FRACTION = Long.MAX_VALUE/index_item_num +1;

    static int data_partition = TOTAL/chunk_size;
    static int data_start = index_item_num*8;
    //
    //Pt[] index_in_mem = new Pt[data_partition];

    class Pt{
        int[] count = new int[index_item_num];
        int[] start = new int[index_item_num];
    }


    FileChannel[] indexMbbs= new FileChannel[data_partition];


    ExecutorService executor = new ThreadPoolExecutor(0,2*Runtime.getRuntime().availableProcessors(),5, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100));

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
                long[] data_chunk = new long[chunk_size];
                long position = j*chunk_size*8;
                buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,position,chunk_size*8);
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
        //内存构建
        List<Long>[] bucketsInMem = new LinkedList[index_item_num];
        for(int i=0;i<data_chunk.length;i++){
            int key = indexKey(data_chunk[i]);
            if(bucketsInMem[key]==null){
                bucketsInMem[key] = new LinkedList();
            }
            bucketsInMem[key].add(data_chunk[i]);
        }
        //索引
        Pt index_in_mem=new Pt();
        Pt p = index_in_mem;
        for(int i=0;i<bucketsInMem.length;i++){
            if(bucketsInMem[i]!=null){
                p.count[i] = bucketsInMem[i].size();
            }
        }
        for(int i=1;i<index_in_mem.count.length;i++){
            p.start[i]=p.start[i-1]+p.count[i-1];
        }
        IntBuffer ib = indexMbb.asIntBuffer();
        for(int i=0;i<index_in_mem.count.length;i++){
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

        if(indexMbbs[k]==null){
            File f = new File(index_path+k);
            if(!f.getParentFile().exists()){
                f.getParentFile().mkdir();
            }
            if(!f.exists()){
                f.createNewFile();
            }
            indexMbbs[k] = new RandomAccessFile(f,"rw").getChannel();
        }
        return indexMbbs[k].map(FileChannel.MapMode.READ_WRITE,0,index_item_num*8);
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


    public int find(final long v) throws Exception{
        if(!this.isIndexed()){
            this.build();
        }
        List<Future<Integer>> results = new ArrayList<Future<Integer>>(data_partition);
        for(int i=0;i<data_partition;i++){
            final int p = i;
            final int key = indexKey(v);
            Future<Integer>  result = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int result = 0;
                    MappedByteBuffer indexMbb = getIndexMBB(p);
                    if( !indexMbb.isLoaded()){
                        indexMbb.load();
                    }
                    IntBuffer intBuffer = indexMbb.asIntBuffer();
                    int start = intBuffer.get(key*2);
                    int count = intBuffer.get(key*2+1);
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



}


