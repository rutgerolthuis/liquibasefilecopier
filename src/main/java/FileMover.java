import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileMover
{
	public void moveFiles(final List<String> fileList,final String targetDir) throws IOException
	{
		
		for (String fileName : fileList) {
			Files.copy(Paths.get(fileName), createTargetPath(targetDir,Paths.get(fileName)), REPLACE_EXISTING);
			// add the delete function later... still in beta mode ;) 
		}
		
	}
	
	private Path createTargetPath (final String targetDir, final Path originalFile) {
		
		return Paths.get(targetDir + originalFile.getFileName().toString());
		
	}
}
