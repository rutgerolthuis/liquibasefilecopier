import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class SqlFileMatcher
{
	private int totalFilesNotInXml = 0;
	private int[] foundInXmlFile = new int[3];
	private List<String>[] dataListArray = new ArrayList[4];
	
	List<String>[] go(List<String> sqlFileListFromDir, List<String>[] filesFromXmlArray, String[] xmlFileNames)
	{
		sqlFileListFromDir.forEach(fileFromDir -> determineFileOrigin(fileFromDir, filesFromXmlArray));
		for (int i=0; i < xmlFileNames.length;i++) {

			int finalI = i;
			dataListArray[i].forEach(fileName -> System.out.println("File : " + fileName + " found in " + xmlFileNames[finalI]));			
			
		}
		
		System.out.println( sqlFileListFromDir.size() + " number of files checked.");
		for (int i=0;i < xmlFileNames.length;i++) {
			System.out.println( foundInXmlFile[i] + " number of files in " + xmlFileNames[i]);
		}
		System.out.println( totalFilesNotInXml + " number of files not in xml.");
		
		return dataListArray;
	}

	private void determineFileOrigin(final String file, final List<String>[] fileListFromXML)
	{
		Boolean foundInXml = false;
		for (int i = 0; i < fileListFromXML.length; i++) {
			List<String> list = new ArrayList<String>(fileListFromXML[i]);
			List<String> results = list.stream()
					.filter(a -> Objects.equals(a, file))
					.collect(Collectors.toList());
			if (results.size() != 0) {
				if (dataListArray[i] == null ) dataListArray[i] = new ArrayList<String>(); 
				dataListArray[i].add(file);
				foundInXml = true;
				foundInXmlFile[i]++;
			}
			if (results.size() == 0 && i == 2 && !foundInXml) {
				if (dataListArray[3] == null ) dataListArray[3] = new ArrayList<String>();
				dataListArray[3].add(file);
				totalFilesNotInXml ++;
			}
		}
	}
	
}