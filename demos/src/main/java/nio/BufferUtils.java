package nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class BufferUtils {
	
	/**
	 * The transfered byte sequence will be start at the current position p,end with limit l;
	 * @param bf
	 * @return null if the bf is null.otherwise  the content as String in unicode by default; 
	 */
	
	
	public static  String  asString(final ByteBuffer bf){
		
		if(bf==null){
			return null;
		}
		
		//字节流的编码是个问题呢
		//return "";
		
	}

}
