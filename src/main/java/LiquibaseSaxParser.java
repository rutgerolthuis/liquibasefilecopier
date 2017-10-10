import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LiquibaseSaxParser extends DefaultHandler
{
	
	private List<String> sqlFilesFromXml = new ArrayList<String>();
	private String sqlFileName;

	public List<String> getSqlFilesFromXml()
	{
		return sqlFilesFromXml;
	}

	public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException
	{	
		if (qName == "sqlFile")
			sqlFileName = attributes.getValue("path").replace("${sqlRoot}","C:/dev/CurrentFundation/Database/src");
		
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if (sqlFileName != null) sqlFilesFromXml.add(sqlFileName);
		
	}
}
