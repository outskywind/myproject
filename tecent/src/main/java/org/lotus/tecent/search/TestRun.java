package org.lotus.tecent.search;

/**
 * 清理数据文件同时，需要清理test文件 和 索引文件
 * 构建索引数据文件之后，统计100个数据 17000ms -Xms256M -Xmx256M
 * 为了节省内存增加了磁盘io
 */
public class TestRun {


    public static void main(String[] args) throws Exception{
        DataGenerator dataGenerator = new DataGenerator();
        if(!dataGenerator.isDataGerated()){
            dataGenerator.generate();
        }
        long  start = System.nanoTime();
        HybridIndex index = new HybridIndex();
        try{
            boolean pass = true;
            for(DataGenerator.KCounter k: dataGenerator.getTestNums()){
                int fc = index.find(k.number);
                if(fc!=k.count){
                    pass=false;
                    break;
                }
            }
            System.out.println("test is passed : "+pass);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.print("统计100个数据耗时："+ (System.nanoTime() - start)/1000000+"ms");
    }

}
