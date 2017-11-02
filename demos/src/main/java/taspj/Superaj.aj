package taspj;

/**
 * Created by quanchengyun on 2017/11/2.
 */
public aspect Superaj {

        pointcut  a() : call( * taspj.A.m() );

        before() : a(){
            System.out.println("pointcut invoked");
        }

        pointcut  a2() : call( public void taspj.A.m2(..) );

        before() : a2(){
            System.out.println("pointcut invoked");
        }

        pointcut  aa() : call( * taspj.AA.m() );

        before() : aa(){
            System.out.println("pointcut invoked");
        }

}
