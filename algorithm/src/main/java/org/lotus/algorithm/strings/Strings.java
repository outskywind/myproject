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
    //1.翻转子字符串-----------------------------------------------------------
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
    //1.end-----------------------------------------------------------------------



    //2.最长回文子串问题------------------------------------------------------------
    /**
     * 1.manacher 算法 ，扩展字符数组填充特殊字符# ，2N-1
     * 2. 以i为中心扩展查找
     */

    private static void palindromic(char[] str){
        char[] extend = new char[str.length*2-1];
        for(int i=0,j=0;i<str.length;i++,j++){
            extend[j]=str[i];
            j++;
            if (j<extend.length)
                extend[j]='#';
        }

        int[] mp = new int[extend.length];

        int m=0,r=0;
        for(int i=0;i<extend.length;i++){
            mp[i]= i < r ? Math.min(mp[2*m-i],r-i):1;
            while(i-mp[i]>=0 && i+mp[i]<extend.length && extend[i-mp[i]]==extend[i+mp[i]]){
                mp[i]++;
            }
            //i为中心的构成回文，如果i大于等于 r ,那么扩展后 r=i,m=i
            if(i+mp[i] > r) {
                r=i+mp[i];
                m=i;
            }
        }
        //找到最大的那一个
        int max=0;
        for(int i=0;i<mp.length;i++){
            if (mp[i]>mp[max]){
                max=i;
            }
        }
        if (mp[max]>1) {
            int left = max-mp[max]+1;
            int right = max+mp[max]-1;
            left = (left+1)/2;
            right = (right-1)/2;
            for (int i=left;i<=right;i++){
                System.out.print(str[i]);
            }
        }
    }

    @Test
    public void testPalindromic(){
        char[] origin = "abcdeffeffedcab".toCharArray();
        palindromic(origin);
    }
    //2. end------------------------------



    //3. 字符串hash -------------------------------------

    /**
     *  h(s[i])= s[i]*b^i mod M
     *  h(s) = h(s[0])+...+ h(s[n])
     *  h(s[l,r]) = h(s[0,r])-h(s[0,l-1])
     *  h(s[l,r])=h(s[l-1,r-1]) - h(s[l-1]) + h(s[r]);
     * 找出在目标字符串中 target 包含的 模式子串 pattern
     *
     * O(N+M) 平均时间，极端情况下,O(N*M)
     */
    public static int  patternMatch( char[] target,char[] pattern){

        //计算字符串hash
        //前缀hash  --Rabin-Karp
        if(pattern.length>target.length){
            return -1;
        }
        //缺陷是计算的数字要在一个long 范围内
        int b = 233;
        int M = 1000000007;

        long hashPattern = 0;
        long hashTarget = 0;
        long bl = 1;

        for(int i=1;i<pattern.length;i++){
            bl = bl*b;
        }
        bl=bl%M;

        for(int i=0;i<pattern.length;i++){
            hashPattern = (hashPattern*b+pattern[i])%M;
            hashTarget = (hashTarget*b+target[i])%M;
        }

        int pos = -1;
        for(int i=0;i<target.length-pattern.length;i++){
            if (hashPattern == hashTarget){
                pos=i;
                break;
            }
            hashTarget = (hashTarget*b - (target[i]*bl*b)%M + target[i+pattern.length])%M;
        }
        return pos;
    }


    @Test
    public void testPatternMatch(){
        char[] target  = ("afhkcvaofaaamvakidwvbhadoavbiqqqqhsdlkczovoavaqafbmkginl" +
                "ogapipahnfjadnvcxbxdiazdlkfdfeeigdbjvqweqiutfafpvpzvbadbahufu").toCharArray();
        char[]  pattern = "lkfdf".toCharArray();

        int  pos = patternMatch(target,pattern);
        System.out.println(pos);
    }


    /*private  long  hash(char[] str,int dwHashType){
        long  seed1= 0x7FED7FED;
        long seed2= 0xEEEEEEEE;

        long[] cryptTable=prepareCryptTable();
        int ch;
        for (int i=0;i<str.length;i++)
        {
            ch = str[i];
            seed1 = cryptTable[(dwHashType << 8) + ch] ^ (seed1 + seed2);
            seed2 = ch + seed1 + seed2 + (seed2 << 5) + 3;
        }
        return seed1;
    }

    static long[] prepareCryptTable()
    {
        long seed = 0x00100001;
        int index2=0,index1=0, i=0;
        long[] cryptTable = new long[256];
        for(index1 = 0; index1 < 256; index1++) {
            for(index2 = index1, i = 0; i < 5; i++, index2 += 256)
            {
                long temp1, temp2;
                seed = (seed * 125 + 3) % 0x2AAAAB;
                temp1 = (seed & 0xFFFF) << 0x10;
                seed = (seed * 125 + 3) % 0x2AAAAB;
                temp2 = (seed & 0xFFFF);
                cryptTable[index2] = ( temp1 | temp2 );
            }
        }
        return cryptTable;
    }*/

    //3. end--------------------------------------------











}
