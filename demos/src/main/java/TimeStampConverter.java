import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by quanchengyun on 2018/1/9.
 */
public class TimeStampConverter {

    @Test
    public  void test(){
        System.out.println(System.currentTimeMillis());
        System.out.println(System.nanoTime());

        //long timstamp = 1515483415053080L;
        long timestamp13 = 1538179199998L;

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(timestamp13);
        Date d = cl.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


        System.out.println(sdf.format(d));
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date d3 = new Date(1541628159715L);
        System.out.println(sdf.format(d3));
        System.out.println(System.currentTimeMillis());

    }
}
