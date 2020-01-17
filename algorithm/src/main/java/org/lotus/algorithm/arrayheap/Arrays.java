package org.lotus.algorithm.arrayheap;

import org.lotus.algorithm.sort.SortUtil;

public class Arrays {


    /**
     * 有两个序列A和B,A=(a1,a2,...,ak),B=(b1,b2,...,bk)，A和B都按升序排列。对于1<=i,j<=k，求k个最小的（ai+bj）
     * 分析：对于已经有序的序列查找，已知最好的时间性能是 logN ，二分查找，但是这个似乎用不上二分查找
     * 那么其次的性能是 N:
     * 将a序列 和 b序列 比较，然后输出k个数 ,O(k) ，k比较小的时候，比较好
     */
    public  static  void  findKminSum(int[] a , int[] b, int k){
            int count=0;
            //a,b数组已扫描的下标
            int imax=0,jmax=0;
            if (k==1) {
                System.out.println(imax+","+jmax);
                return;
            }else
                System.out.println(imax+","+jmax);
            while(count<k-1){
                if(a[imax+1]<=b[jmax+1]){
                    imax++;
                    for (int j=0;j<=jmax;j++){
                        System.out.println(imax+","+j);
                        count++;
                        if (count==k-1){
                            return;
                        }
                    }
                }else{
                    jmax++;
                    for (int i=0;i<=imax;i++){
                        System.out.println(i+","+jmax);
                        count++;
                        if (count==k-1){
                            return;
                        }
                    }
                }
            }
            //k次比较
    }

    /**
     * 从 海量文本中寻找最小的k个数
     * 假设 10亿个数 long
     * 10*4*1000*1000*100 = 4G
     * 设计：假如一批读取256M,每一批k最小的【原址选择】
     * 然后再针对这些进行选择算法。
     *
     * 此方法为单个数组选择k-min
     */
    public static void  findKMin(int[] arr,int k){
        int pivot = -1;
        //pivot 会划分到大于k，或者小于k的部分
        int start = 0;
        int end = arr.length-1;
        while((pivot= quickSelect(arr,start,end,k))!=k-1){
            if (pivot>k-1){
                start = pivot+1;
            }else{
                end = pivot-1;
            }
        }
    }

    /**
     * 按照算法导论 9.3章 实现
     * 最坏时间为 o(N)的select 算法 .
     * 每组5个，划分成n/5 组，对每组插入排序，找到每组的中位数
     * 然后再对n/5 个中位数 再次运行此算法找出 中位数的中位数 x ,
     * 此时这个数组是完成了选择交换的， 然后按照x 进行select
     *
     * @param arr
     * @param k
     * @param start
     * @param end
     * @return
     */
    private  static int  quickSelect(int[] arr, int k,int start,int end){
        //
        int num = (int)Math.ceil((end-start+1)/5);
        int[] pivots = new int[num];
        int m=0;
        int i=0;
        for(;i+5<=arr.length;i+=5){
            SortUtil.insertSort(arr,i,i+5);
            pivots[m++]=arr[i+2];
        }
        SortUtil.insertSort(arr,i,arr.length);
        pivots[m]=arr[(int)Math.ceil((i+arr.length)/2)];

        //int ppivot = quickSelect();
    }


}
