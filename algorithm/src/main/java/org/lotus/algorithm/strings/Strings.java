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



    //3. 字符串匹配   hash-------------------------------------

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
                //这里如果相等，严谨来说还需要一个个比较
                //基于hash 映射的 字符串匹配算法，效率取决于hash的效率
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

    /**
     * 字符串匹配  Boyer-Moore-Galil
     * yes- u made it 花了1周的时间？？理解
     */
    public int[] BMPattern(char[] target, char[] pattern){
        //计算前后缀move
        //表示当前字符为结尾的
        /**
         *  pattern  + + + + + + + + + [i + + + ] + + + [+ + + +]
         *           + + + + + + + + + [+ + + + ] + + + [+ + + +]
         *                             length=4 ,以及最长匹配的 k ,m
         */
        //当前字符失配时，好后缀规则移动的距离
        int[] good_move= good_move(pattern);
        int[] bad_move= bad_move(pattern,65535);
        int ord = get_min_ord(pattern);
        System.out.println(String.copyValueOf(target));
        //System.out.println(String.copyValueOf(pattern));
        //System.out.println("ord="+ord);

        //start
        int k=pattern.length-1;    //text
        int j=pattern.length-1;   //pattern
        int last=0;               //pattern end

        int[] match= new int[pattern.length-1];
        int mi=0;
        while(k<target.length){
            //打印
            for(int off=k-pattern.length;off>=0;off--){
                System.out.print(" ");
            }
            System.out.println(String.valueOf(pattern));

            for (;j>=last && target[k]==pattern[j] ;k--,j--){
            }
            //失配
            if(j>=last){
                k += Math.max(bad_move[target[k]], good_move[j]);
            }
            //完全匹配,计算最小的周期串
            else{
                System.out.println("bingo");
                match[mi++]=k+1;
                if (ord>0) {
                    k+=(ord+pattern.length);
                    last=pattern.length-ord;
                }else
                {
                    k+=(pattern.length+good_move[j]);
                    last=0;
                }
            }
            j = pattern.length-1;
        }


        /*for (int i=0;i<pattern.length;i++){
            System.out.print(pattern[i]+" ");
        }
        System.out.println();
        for (int i=0;i<move.length;i++){
            System.out.print(move[i]+" ");
        }
        System.out.println();*/
        return match;
    }

    private int get_min_ord(char[] pattern) {
        //计算最小的正周期，必须大于等于1
        int ord=1;
        if (pattern.length<2*ord) return 0;
        int i=ord;
        while (i<pattern.length){
            if(pattern[i]==pattern[i-ord]){
                i++;
            }else {
                ord++;
                if (ord>pattern.length/2){
                    return 0;
                }
                i=ord;
            }
        }
        return ord;
    }


    //需要构建一个所有字符的字典
    private int[] bad_move(char[] pattern,int charset_size) {
        int[] move = new int[charset_size];
        for (int i=0;i<charset_size;i++){
            move[i]= pattern.length;
        }
        for (int i=0;i<pattern.length;i++){
            move[pattern[i]]= pattern.length -1-i;
        }
        return move;
    }


    private  int[] good_move(char[]  pattern){
        int[] move = new int[pattern.length];
        //i-tail段，在文中出现的最右的匹配位置距离，也就是最小距离
        int[] suffix = new int[pattern.length];
        int j=0;//当前字符为首的后缀,长度
        int k=0;// 已找到的最大的匹配的后缀的首字符
        int l=0;// 已找到的最大的匹配的后缀的长度
        //
        int tail= pattern.length-1;
        for(int i=tail;i>=0;i--){
            if(i==tail){
                continue;
            }
            int old_l=l;
            j= (pattern[i]==pattern[tail-j])?j+1:(pattern[i]==pattern[tail]?1:0);
            if (l<=0){
                //与tail比较
                if (pattern[i]==pattern[tail]){
                    k=i;
                    l=l+1;
                }
            }
            else {
                //计算当前包含当前字符的子串 长度是否 > 当前最大子串
                if(j>l){
                    k=i;
                    l=j;
                }
            }
            //记录最右的后缀距离
            if(l>old_l){
                suffix[tail-l+1]=tail-l+1-k;
            }
            //System.out.println("j,k,l="+j+","+k+","+l);
        }


       /* for(int i=0;i<=tail;i++){
            System.out.print(suffix[i]+" ");
        }
        System.out.println();*/

        //计算move
        for(int i=0;i<=tail-1;i++){
            if (suffix[i+1]>0)
                move[i]= suffix[i+1]+tail-i;
            else
                //对于没有匹配到的后缀，查找最长的前缀
                move[i]=  tail+1-2*j+tail-i;
        }
        move[tail]=1;
        return move;
    }

    @Test
    public void testBMPattern(){
        char[] target  = ("afhkcvaofaaamvakidcbaebacbacbasdlkczovoaababcababcababl" +
                "ogapipahnfjadnvcxbxdiazdlkfdfeeigdbjvqweqiutfafpvpzvbadbahufu").toCharArray();
        //char[]  pattern = "ababcababcabab".toCharArray();

        //char[]  pattern = "ababcababcabab".toCharArray();

        char[]  pattern = "ababcababcabab".toCharArray();

        int[]  pos = BMPattern(target,pattern);
        for (int i:pos){
            if (i>0){
                System.out.print(i+" ");
            }
        }

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
