package math;

import org.junit.Test;

/**
 * Created by quanchengyun on 2018/7/18.
 */
public class Compare {


    @Test
    public void longcompare(){

        for(long i=-1L;i>-1000000L;i--){

            if(i<=0){

            }else{
                System.out.println("exception detected.");
                break;
            }


        }

    }
}
