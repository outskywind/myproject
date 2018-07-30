package algorithm.sort;

import java.util.Random;

/**
 * Created by quanchengyun on 2018/7/5.
 * 原址排序，非稳定
 */
public class QuickSort {

    public static void main(String[] args){
        int[] array = new int[]{3,1,6,5,4,7,9,11,8,12,15,2};
        sort(array,0,array.length-1);
        for(int i=0;i<array.length;i++){
            // / 是结果取商
            System.out.print(array[i]+",");
        }
    }

    //随机选择划分值以达到期望时间复杂度 NlgN
    private static void random_sort(int[] array,int start,int end){
        if(start<end){
            int p = random_partition(array,start,end);
            sort(array,start,p-1);
            sort(array,p+1,end);
        }
    }

    //随机抽样法
    private static int random_partition(int[] array,int start,int end){
        int random_p = new Random(System.currentTimeMillis()).nextInt(end-start);
        swap(array, start+random_p,end);
        return partition(array,start,end);
    }

    //可是使用递归有个问题，栈使用的内存比较多
    public static void sort(int[] array,int start,int end){
        if(start<end){
            int p = partition(array,start,end);
            sort(array,start,p-1);
            sort(array,p+1,end);
        }
    }

    private static int partition(int[] array, int start, int end){
        int i=start-1;
        for(int j=start;j<end;j++){
            if(array[j]<array[end]){
                i++;
                swap(array,i,j);
            }
        }
        swap(array,i+1,end);
        return i+1;
    }

    private static void swap(int[] array, int src,int dest){
        int tmp = array[src];
        array[src] = array[dest];
        array[dest] = tmp;
    }

}
