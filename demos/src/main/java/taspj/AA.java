package taspj;

/**
 * Created by quanchengyun on 2017/11/2.
 */
public class AA extends A {

    public void m(int i){
        System.out.println("AA.m()");
    }

    @Override
    public void m(){
        System.out.println("AA.m()");
    }


}
