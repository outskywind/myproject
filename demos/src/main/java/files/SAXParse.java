package files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

public class SAXParse {
	
	
	@Test
	public void testMain(String[] args) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		
		
		//EntityResolver entityResolver = new ResourceEntityResolver();
		
		
		//DocumentBuilder builder = createDocumentBuilder(factory, null, null);
	}
	
	
	
	
	protected DocumentBuilder createDocumentBuilder(
			DocumentBuilderFactory factory, EntityResolver entityResolver, ErrorHandler errorHandler)
			throws ParserConfigurationException {

		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		if (entityResolver != null) {
			docBuilder.setEntityResolver(entityResolver);
		}
		if (errorHandler != null) {
			docBuilder.setErrorHandler(errorHandler);
		}
		return docBuilder;
	}

}
