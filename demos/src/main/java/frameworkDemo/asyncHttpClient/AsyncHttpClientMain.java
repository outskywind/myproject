package frameworkDemo.asyncHttpClient;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by quanchengyun on 2018/5/30.
 */
public class AsyncHttpClientMain {

    static String host="";
    //static String scheme="";
    static String contentType="application/json";
    static String message = "ok";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AsyncHttpClient httpClient = Dsl.asyncHttpClient();
        Future<Response> response = httpClient.preparePost("http://"+host+"/druid/v2/?pretty")
                .setBody(message).setHeader("Content-Type",contentType).execute();
        Response result =    response.get();

    }




}



