package math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quanchengyun on 2018/5/9.
 */
public class BasicMath {

    public static void main(String[] args){
        System.out.println(1999/1000*1000);
        t();

    }


    public static void t(){
        List l  = new ArrayList<>();
        l.add(new Date());
        l.add(1);
        l.add("hello");
        System.out.println(l);
    }

    @Test
    public void  testBigdecimal(){
        BigDecimal guaranteeFee=new BigDecimal(90).multiply(new BigDecimal(0.008))
                .multiply(new BigDecimal(3000)).divide(new BigDecimal(365),2, RoundingMode.HALF_UP);
        System.out.println(guaranteeFee);

        BigDecimal guaranteePeriod = guaranteeFee.divide(new BigDecimal(3),2,RoundingMode.HALF_UP);
        System.out.println(guaranteePeriod);
    }



}
