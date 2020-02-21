package programme;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

public class Atomics {


    @Test
    public void  testAtomic(){
        //默认是0
        AtomicLong clock = new AtomicLong();

        System.out.println(clock.get());

        //System.out.println(i);

    }
}
