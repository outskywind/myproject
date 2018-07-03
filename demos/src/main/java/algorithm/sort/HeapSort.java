package algorithm.sort;

/**
 * Created by quanchengyun on 2018/7/3.
 */
public class HeapSort {


    public static void main(String[] args){


        for(int i=0;i<10;i++){
            // / 是结果取商
            System.out.println(i/2);
        }
    }



    //这里的时间复杂度为 子树的高度
    private static void max_heapify(int[] array , int root){
        //假设 左右子树已经是各自的最大堆，那么它的时间就是lgN
        int left = left(root);
        int right = right(root);
        int max = root;
        if(left <=array.length && array[left]>array[max]){
            max = left;
        }
        if(right <=array.length && array[right]>array[max]){
            max = right;
        }
        //发生交换
        if(max!=root){
            int tmp = array[root];
            array[root] = array[max];
            array[max] = tmp;
            //交换之后的新的子树需要再次调整
            max_heapify(array, max);
        }
    }

    private static void build_max(int[] array , int root){
        //对于构建一个最大堆，从所有的非叶子节点开始,-1 是因为下标从0开始
        int tail = (int)Math.floor(array.length/2.0)-1;
        for(int i=tail;i>=0;i--){
            max_heapify(array,i);
        }
    }


    private static int left(int root){
        //因为java中下标从0开始
        int left = root<<1+1;
        return left;
    }

    private static int right(int root){
        int right = root<<1+2;
        return right;
    }


}
