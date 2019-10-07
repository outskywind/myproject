package jvm;

/**
 * Created by quanchengyun on 2019/7/22.
 */
public class NewInstanceTest {

    static class  A implements IA {
        private int id;
        private String name;

        public void m(String name){
            this.name=name;
        }

        @Override
        public void call(String v) {
            System.out.println("call...");
        }
    }

    public static void main(String[] args){
            A _a= new A();
            _a.m("hello");
            _a.call("hello");
            IA  ia = _a;
            ia.call("hello");
    }

}
