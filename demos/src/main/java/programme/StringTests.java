package programme;

import org.junit.Test;

/**
 * Created by quanchengyun on 2018/3/6.
 */
public class StringTests {


    @Test
    public void test(){
        String str = "file:/opt/server/sevend/invitecenter-consumer/invitecenter-consumer-1.0-SNAPSHOT.jar!/BOOT-INF/lib/flume-logs-1.0.0-SNAPSHOT.jar!/";

        str =  str.substring(0,str.indexOf(".jar"));
        str = str.substring(str.lastIndexOf("/")+1);
        str = str.substring(0,str.lastIndexOf("-"));
        str = str.substring(0,str.lastIndexOf("-"));
        System.out.println(str);
    }


    @Test
    public void testReplace(){
        String str = "select *\n" +
                "\t\tfrom t_bank_card\n" +
                "\t\twhere dafy_pay_id = ?\n" +
                "\t\t\tand state = 1 ";

        String str2 = "\t\n" +
                "select *\n" +
                "\t\tfrom t_bank_card\n" +
                "\t\twhere dafy_pay_id = ?\n" +
                "\t\t\tand state = 1";


        System.out.println(repalce(str));
    }

    public static String repalce(String src){
        boolean blank_detect = false;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<src.length();i++){
            char c = src.charAt(i);
            if(c =='\n'){
                continue;
            }
            if (c == '\t' || c ==' '){
                if (!blank_detect) sb.append(' ');
                blank_detect = true;
            }
            else{
                // other character
                blank_detect =false;
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
