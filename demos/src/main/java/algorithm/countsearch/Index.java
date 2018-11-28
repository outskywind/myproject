package algorithm.countsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.LongBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by quanchengyun on 2018/11/27.
 */
public class Index {

    static String  default_index_path = "d:/demos/";
    static int  kbit =  (1<<16)-1;
    static int  level1mask = kbit << (16*3);
    static int  level2mask = kbit << (16*2);
    static int  level3mask = kbit << 16;
    static int  level4mask = kbit;
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
        try {
            RandomAccessFile file = new RandomAccessFile(DataGenerator.dataFile,"r");
            MappedByteBuffer buffer;
            for(int j=0;j<DataGenerator.trip;j++){
                //each trip indexing 1M data
                long[] data_chunk = new long[DataGenerator.chunk_size];
                long position = j*DataGenerator.chunk_size*8;
                buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY,position,DataGenerator.chunk_size*8);
                LongBuffer lb = buffer.load().asLongBuffer().asReadOnlyBuffer();
                lb.get(data_chunk);
                //
                indexing(data_chunk);
                //
                buffer.force();
                buffer.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
     }

    /**
     * find first 22bit ,22bit ,22bit , 4bit
     * @param data_chunk
     */
    private void indexing(long[] data_chunk) {

    }

    //16
    private void index_first_level(long[] data_chunk){
        for(int i=0;i<data_chunk.length;i++){
            long level1bits = data_chunk[i] & level1mask ;


        }
    }







     //




}
