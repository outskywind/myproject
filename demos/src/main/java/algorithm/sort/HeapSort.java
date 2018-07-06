package algorithm.sort;

/**
 * Created by quanchengyun on 2018/7/3.
 */
public class HeapSort {

    public static void main(String[] args){
        int[] array = new int[]{3,1,6,5,4,7,9,11,8,12,15,2};
        heap_sort(array);
        for(int i=0;i<array.length;i++){
            // / 是结果取商
            System.out.print(array[i]+",");
        }
    }

    //这里的时间复杂度为 子树的高度
    private static void max_heapify(int[] array , int root , int heap_size){
        //假设 左右子树已经是各自的最大堆，那么它的时间就是lgN
        int left = left(root);
        int right = right(root);
        int max = root;
        if(left <=heap_size-1 && array[left]>array[max]){
            max = left;
        }
        if(right <=heap_size-1 && array[right]>array[max]){
            max = right;
        }
        //发生交换
        if(max!=root){
            int tmp = array[root];
            array[root] = array[max];
            array[max] = tmp;
            //交换之后的新的子树需要再次调整
            max_heapify(array, max, heap_size);
        }
    }

    //build-max 的时间复杂度为 O(n)
    //证明对于height  h 的节点数，因为 N(h) <= n/2^h+1
    //累加时间复杂度 ∑n/2^(h+1)*O(h) =n∑h/2^h+1,累加和极限是一个常量收敛，所以是O(n)
    private static void build_max(int[] array){
        //对于构建一个最大堆，从所有的非叶子节点开始,-1 是因为下标从0开始
        int tail = (int)Math.floor(array.length/2.0)-1;
        for(int i=tail;i>=0;i--){
            max_heapify(array,i,array.length);
        }
    }

    //heap_sort 观察：对于根节点，要迭代N/2次lgN的max_heapify
    //所以总时间复杂度为 O(NlgN)
    private static void heap_sort(int[] array ){
        //first build max heap
        build_max(array);
        int heap_size = array.length;
        for(int i=array.length-1;i>0;i--){
            //最大的数交换到堆尾
            int tmp = array[0];
            array[0] = array[i];
            array[i] = tmp;
            //重建最大堆
            max_heapify(array,0,i);
        }
        //结果是升序排列
    }

    private static int left(int root){
        //因为java中下标从0开始
        int left = (root<<1)+1;//优先级问题 + 号优先级比 << 高
        return left;
    }

    private static int right(int root){
        int right = (root<<1)+2;
        return right;
    }

}
