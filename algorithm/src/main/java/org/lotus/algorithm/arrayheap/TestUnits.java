package org.lotus.algorithm.arrayheap;

import org.junit.Test;

public class TestUnits {


    @Test
    public void test(){
        int[] a={1,7,30,35,66,80,90,100,122,126,133};
        int[] b={0,2,3,4,33,34,66,70,74,89,144};
        Arrays.findKminSum(a,b,11);

    }
}
