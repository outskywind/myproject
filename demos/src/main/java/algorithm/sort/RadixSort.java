package algorithm.sort;

/**
 * Created by quanchengyun on 2018/7/30.
 * 基数排序,非原址，稳定
 */
public class RadixSort {



    public static void main(String[] args){
        int[] array = new int[]{3,1,6,4,7,9,11,8,12,15,2,33,89,100,90,34};
        array = sort(array);
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]+",");
        }
    }

    static int find_max(int[] array){
        int max=0;
        for(int i=0;i<array.length;i++){
            max=array[i]>max?array[i]:max;
        }
        return max;
    }

    public static int[] sort(int[] array){
        //
        if(array.length<=1){
            return array;
        }
        //r = log(length)
        int r =0;
        long length = array.length;
        while((length=length>>1)!=0){
            r++;
        }
        int max = find_max(array);
        int bits = 1;
        while((max=max>>1)!=0){
            bits++;
        }
        //r= log[array.length]
        //int rounds = (int)Math.ceil(bits/r);
        int rounds = 1;
        while((bits=bits-r)>0){
            rounds++;
        }
        int mask = (1<<r)-1;
        for(;rounds>0;rounds--){
            //from low bits start each time get r bits
            int[] c = new int[mask+1];
            for(int i=0;i<array.length;i++){
                c[array[i] & mask] = c[array[i] & mask]+1;
            }
            for(int i=1;i<c.length;i++){
                c[i]= c[i]+c[i-1];
            }
            //get the new round array
            int[] sorted = new int[array.length];
            for(int i=array.length-1; i>=0;i--){
                sorted[c[array[i] & mask]-1]=array[i];
                c[array[i] & mask]--;
            }
            array = sorted;
            mask = mask << r;
        }
        return array;
    }


}
