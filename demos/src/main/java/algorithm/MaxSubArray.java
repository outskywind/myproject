package algorithm;

public class MaxSubArray {


    public static class Item{
        int maxLeft=0;
        int maxRight=0;
        int maxSum=0;

        @Override
        public String toString() {
            return "Item{" +
                    "maxLeft=" + maxLeft +
                    ", maxRight=" + maxRight +
                    ", maxSum=" + maxSum +
                    '}';
        }
    }

    /**
     * 最大子数组问题
     *
     * 问题模型： 对于连续的股市波动变化，在某一天买入，某一天卖出，每天的交易关闭后操作
     *           各只操作一次，获取收益最大化，提前预知每天的价格波动
     *
     * 模型抽象：问题转换为，选择某个点的值作为基础值，经过一系列变化之后寻找相对于这个点值的最大值
     * 建模： 将每一天相对于前一天的变化作为数组值，然后寻找一个连续的子数组，使得改子数组的和最大。
     *
     *
     * 算法说明： 对于A[1...j] 已知最大子数组 A[m...n], Sum(m...n) , 以及存在 i, 1<=i<=j 使得 A[i...j] 的子数组之和最大 Sum(i,j)
     *           那么对于 A[1...j+1] 的数组的最大子数组 ，为 A[m...n] 与 A[i...j+1]  以及 A[j+1] 3者的较大者
     *
     * 此算法时间复杂度为 O(n) ; 若采用分治递归策略，将为 O(nlgn)
     */


    public static void main(String[] args){

        int[] array = new int[]{-1,2,-3,4,5,6,-1,-6,7,8,-9,10};
        Item result = compute(array);
        System.out.println(result);

    }


    public static Item compute(int[] array){
        Item result = new Item();
        if(array.length==1){
            result.maxSum=array[0];
            return result;
        }
        int maxLeft=0;
        int maxRight=0;
        int maxSum=array[0];
        int reverseSubMaxKey=0;
        int reverseSubMaxSum=array[0];
        for(int i=1;i<array.length;i++){
            //计算新的reverseSubMaxKey
            if(reverseSubMaxSum<=0){
                reverseSubMaxKey = i;
                reverseSubMaxSum = array[i];
            }
            else{
                reverseSubMaxSum += array[i];
            }
            if(reverseSubMaxSum>maxSum){
                maxLeft = reverseSubMaxKey;
                maxRight = i;
                maxSum = reverseSubMaxSum;
            }
        }
        result.maxSum =maxSum;
        result.maxLeft = maxLeft;
        result.maxRight = maxRight;
        return result;
    }



}
