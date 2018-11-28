package math;

import java.math.BigDecimal;

/**
 * Created by quanchengyun on 2018/5/17.
 */
public class DoubleBigDecimal {


    public static void main(String[] args){
        double d = 1.000;
        BigDecimal bd=new BigDecimal(d);
        double d1=bd.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(d1);


         d = 1.86;
         bd=new BigDecimal(d);
         d1=bd.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(d1);


        double avg_qps =div(1, 60,4)*100;
        System.out.println(avg_qps);
    }


    private static double div(long div1, long div2, int scale){
        BigDecimal bigDecimal = new BigDecimal(div1);
        BigDecimal bigDecimal2 = new BigDecimal(div2);
        return bigDecimal.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


}
