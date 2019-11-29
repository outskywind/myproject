package programme;

public interface HiInterface {

    default void hello(){
        System.out.println("hello");
    }

    default String  doXX(){
        return "";
    }

    String anotherMethod();
}
