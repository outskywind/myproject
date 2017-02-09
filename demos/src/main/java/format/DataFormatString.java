package format;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class DataFormatString {
	
	
	
	@Test
	public void testFormat() {
		//最多保留几位小数，就用几个#，最少位就用0来确定
		//DecimalFormat df = new DecimalFormat(".0####");
		String s=new DecimalFormat("#0.0####").format(0.4141286556);
		System.out.println(s);
		NumberFormat f = NumberFormat.getInstance();
		 if (f instanceof DecimalFormat) {
		      ((DecimalFormat) f).setDecimalSeparatorAlwaysShown(false);
		  }
		
	}

}
