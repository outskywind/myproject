package nio.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by quanchengyun on 2018/7/26.
 */
public class ProcotolCodec {


    public ByteBuffer encode(Object obj){
        if(obj instanceof ProtocolRequest){
            //

        }
        return ByteBuffer.wrap(((String)obj).getBytes(Charset.forName("utf-8")));
    }

    public Object decode(ByteBuffer message){
            return null;
    }


}
