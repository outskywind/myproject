package algorithm.countsearch;

public class TestRun {

    public static void main(String[] args) throws Exception{
        DataGenerator dataGenerator = new DataGenerator();
        HybridIndex index = new HybridIndex();
        if(!index.isDataGerated()){
            dataGenerator.generate();
        }
        //System.out.print(64-Long.numberOfLeadingZeros(1L<<50));
        long  start = System.nanoTime();
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
        System.out.print("查找耗时："+ (System.nanoTime() - start)/1000000+"ms");
    }

}
