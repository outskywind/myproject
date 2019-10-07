package org.lotus.algorithm.strings;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by quanchengyun on 2019/10/7.
 */
public class Strings {


    public static char[]  str= {'a','b','c','d','e','f','g','h'};

    private static  char[] getStr(){
        return Arrays.copyOf(str,str.length);
    }
    //1.start-----------------------------------------------------------
    /**
     * 在原字符串中把字符串尾部的m个字符移动到字符串的头部，
     * 要求：长度为n的字符串操作时间复杂度为O(n)，空间复杂度为O(1)。
     * 例如，原字符串为”Ilovebaofeng”，m=7，输出结果为：”baofengIlove”。
     *
     * ： (x~y~)~=y~~x~~=yx
     */
    public  static void  revertChar(char[] str , int m){
            //0,n-1
            //x= str[0],str[n-1-m] , y = n-1-m+1 , n-1
            if(m>=str.length){
                System.out.print("invalid param m");
            }
           revert(str,0,str.length-1-m);
           revert(str,str.length-m,str.length-1);
           revert(str,0,str.length-1);
           System.out.println(str);
    }

    private static void  revert(char[] str , int start ,int end){
        int j=start;
        int k=end;
        while(j < k){
            char v = str[j];
            str[j]=str[k];
            str[k]= v;
            j++;
            k--;
        }
    }

    @Test
    public void testRevert(){
        revertChar(getStr(),3);
        revertChar(getStr(),4);
        revertChar(getStr(),5);
    }

    /**
     *单词翻转。输入一个英文句子，翻转句子中单词的顺序，但单词内字符的顺序不变，句子中单词以空格符隔开。
     * 为简单起见，标点符号和普通字母一样处理。例如，输入“I am a student.”，则输出“student. a am I”
     *  (x~_y~_z~)~ = z~~_y~~_x~~ = z_y_x
     */
    private static void revertWord(){
        //略
    }
    //1.end-----------------------------------------------------------



}
