package algorithm.sort;

/**
 * Created by quanchengyun on 2018/7/5.
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

    //可是使用递归有个问题，栈使用的内存比较多
    private static void sort(int[] array,int start,int end){
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

    public static void swap(int[] array, int src,int dest){
        int tmp = array[src];
        array[src] = array[dest];
        array[dest] = tmp;
    }

}
