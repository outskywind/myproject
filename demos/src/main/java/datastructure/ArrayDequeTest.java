package datastructure;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by quanchengyun on 2018/2/28.
 */
public class ArrayDequeTest {

    @Test
    public void  testxx(){
        //deque是无界队列
        ArrayDeque<String> t = new ArrayDeque<>(5);


        for(int i=0;i<10;i++){
            t.offer("q");
            t.addLast("kk");
        }

        for(String s:t){
            System.out.println(s);
        }

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(5);
        ArrayBlockingQueue<String> arrayBlockingQueue2 = new ArrayBlockingQueue<String>(5);

        //offer full时返回失败 推荐使用offer
        for(int i=0;i<10;i++){
            arrayBlockingQueue.offer("q");
        }

        //add full时会抛异常
        /*for(int i=0;i<10;i++){
            arrayBlockingQueue2.add("q");
        }*/
        //会阻塞
        try{
            //删除队列元素 空时阻塞
            String e = arrayBlockingQueue.take();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //不会删除队列元素
        String e = arrayBlockingQueue.peek();
        //删除队列元素 空时返回null
        e = arrayBlockingQueue.poll();



        for(String s:arrayBlockingQueue){
            System.out.println(s);
        }


    }



}
