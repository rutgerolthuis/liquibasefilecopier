import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class LiquibaseFileMerger
{	
	public static void main (String[] args ) {
		
		List<String>[] xmlArray = new ArrayList[3];
		LiquibaseFileMerger fileMerger = new LiquibaseFileMerger();
		String[] xmlFileNameArray = new String[3];
		xmlFileNameArray[0] = "C:/dev/CurrentFundation/Database/src/liquibase/data/load_base_data.xml";
		xmlFileNameArray[1] = "C:/dev/CurrentFundation/Database/src/liquibase/data/load_topicus_data.xml";
		xmlFileNameArray[2] = "C:/dev/CurrentFundation/Database/src/liquibase/data/load_trx_data.xml";
		xmlArray[0] = fileMerger.getSqlFileNamesFromXml(xmlFileNameArray[0]);
		xmlArray[1] = fileMerger.getSqlFileNamesFromXml(xmlFileNameArray[1]);
		xmlArray[2] = fileMerger.getSqlFileNamesFromXml(xmlFileNameArray[2]);

		List<String> sqlFilesFromDir = fileMerger.getFilesFromDirectory("C:/dev/CurrentFundation/Database/src/oracle/data/base_data");
		
		SqlFileMatcher fileMatcher = new SqlFileMatcher();
		
		List<String>[] dividedSqlFilesListArray = fileMatcher.go(sqlFilesFromDir,xmlArray,xmlFileNameArray);
		
		LiquibaseFileMover fileMover = new LiquibaseFileMover();
		try
		{
			// skip the first element (0) as those files can remain in the directory 
			fileMover.moveFiles(dividedSqlFilesListArray[1], "C:\\dev\\CurrentFundation\\Database\\src\\oracle\\data\\topicus_data\\");
			fileMover.moveFiles(dividedSqlFilesListArray[2], "C:\\dev\\CurrentFundation\\Database\\src\\oracle\\data\\trx_data\\");
			fileMover.moveFiles(dividedSqlFilesListArray[3], "C:\\dev\\CurrentFundation\\Database\\src\\oracle\\data\\quarantaine\\");
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	private List<String> getSqlFileNamesFromXml(String xmlFileName)
	{	
		List<String> sqlFilesNamesFromXml = null;
		try
		{

			LiquibaseSaxParser handler = new LiquibaseSaxParser();
			
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			
			parser.parse(new File(xmlFileName), handler);
			sqlFilesNamesFromXml = handler.getSqlFilesFromXml();
			
		}
		
		catch (ParserConfigurationException|IOException|SAXException e)
		{
			e.printStackTrace();
		}
		
		return sqlFilesNamesFromXml;
	}

	private List<String> getFilesFromDirectory(String directoryName)
	{
		List<String> fileList = null;
		try (Stream<Path> paths = Files.walk(Paths.get(directoryName), 1))
		{
			fileList = paths.filter(Files::isRegularFile)
					.map(fileName -> fileName.toString())
					.map(filename -> filename.replace('\\','/'))
					.sorted()
					.collect(Collectors.toList());
		}	
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return fileList;
		
	}

	private void checkFileExists(String xmlFileName)
	{

		try (Stream<Path> paths = Files.walk(Paths.get(xmlFileName), 1))
		{
			paths.filter(Files::isRegularFile).forEach(System.out::println);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
