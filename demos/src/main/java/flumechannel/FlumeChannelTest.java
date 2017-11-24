package flumechannel;

import nio.FileChannelTest;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.Transaction;
import org.apache.flume.channel.SpillableMemoryChannel;
import org.apache.flume.channel.file.FileChannelConfiguration;
import org.apache.flume.event.JSONEvent;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import javax.annotation.Resource;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quanchengyun on 2017/11/22.
 */
public class FlumeChannelTest {

    @Test
    public void testXX() throws Exception{
        SpillableMemoryChannel channel  = new SpillableMemoryChannel();
        Context context = new Context();

        context.put(SpillableMemoryChannel.MEMORY_CAPACITY,"2");
        String homePath = System.getProperty("user.home").replace('\\', '/');

        //context.put(FileChannelConfiguration.CHECKPOINT_DIR,"");
        channel.configure(context);
        channel.setName("audit");
        channel.start();
        ObjectMapper mapper = new ObjectMapper();
        Transaction tx = channel.getTransaction();
        tx.begin();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss.SSS");
        String str = sdf.format(new Date());
        for(int i=0;i<10;i++){
            JSONEvent e =  new JSONEvent();
            Map<String,String> mp = new HashMap<>();
            mp.put("time",str+"_"+i);
            byte[] body = mapper.writeValueAsBytes(mp);
            e.setBody(body);
            channel.put(e);
        }
        tx.commit();
        tx.close();
        //after spilled ,we get take it again;
        Event e = null;
        tx = channel.getTransaction();
        tx.begin();
        while((e=channel.take())!=null){
            //Event e = channel.take();
            System.out.println(mapper.readValue(e.getBody(),Map.class));
        }
        tx.commit();
        tx.close();
        channel.stop();
        System.currentTimeMillis();
    }

    @Test
    public void test2(){
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(".").getPath());
        System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }






}
