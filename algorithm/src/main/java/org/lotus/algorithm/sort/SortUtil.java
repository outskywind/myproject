package org.lotus.algorithm.sort;

public class SortUtil {

    /**
     * 插入排序的时间复杂度是 o(N2)
     */
    public static void insertSort(int[] arr,int start,int end){
        for(int i=start;i<=end;i++){
            for (int j=i;j>start && arr[j]<arr[j-1];j--){
                int v = arr[j];
                arr[j]=arr[j-1];
                arr[j-1]=v;
            }
        }
    }
}
