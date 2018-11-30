package algorithm.countsearch;

public class TestRun {

    public static void main(String[] args){
        DataGenerator dataGenerator = new DataGenerator();
        HybridIndex index = new HybridIndex();
        if(!index.isDataGerated()){
            dataGenerator.generate();
        }
        //System.out.print(64-Long.numberOfLeadingZeros(1L<<50));
        long  start = System.nanoTime();
        try{
            boolean pass = true;
            for(DataGenerator.KCounter k: dataGenerator.testNums){
                if(index.find(k.number)!=k.count){
                    pass=false;
                }
            }
            System.out.println("test is passed : "+pass);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.print("构建桶并查找耗时："+ (System.nanoTime() - start)/1000000+"ms");
    }

}
