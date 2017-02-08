package escapingAnalysis;



/**
 * -server -verbose:gc  -server模式默认开启逃逸分析
 * @author s1a
 *
 */
public class EAtest {
	
	private static class Foo {
        private int x;
        private static int counter;

        public Foo() {
            x = (++counter);
        }
    }
    public static void main(String[] args) {
        System.out.println("start");
        for (int i = 0; i < 10000000; ++i) {
            Foo foo = new Foo();
        }
        System.out.println(Foo.counter);
    }

}
