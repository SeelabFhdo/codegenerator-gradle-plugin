package de.seelab.codegenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor
public class ProjectContext {
	private File rootDir;
	private File inputDir;
	private File outputDir;

	public String getNamespaceFromFile(File file) {
		String absolutePath = file.getAbsolutePath();
		if(absolutePath.startsWith(inputDir.getAbsolutePath()))
			absolutePath = absolutePath.replace(inputDir.getAbsolutePath(), "");
		else if (absolutePath.startsWith(outputDir.getAbsolutePath()))
			absolutePath = absolutePath.replace(outputDir.getAbsolutePath(), "");
		return absolutePath.substring(1).replace("/", ".");
	}

	public void writeOutputFile(String namespace, String filename, String content) throws Exception {
		writeOutputFile(namespace, filename, content.getBytes());
	}

	public void writeOutputFile(String namespace, String filename, byte[] content) throws Exception {
		File file = new File(new File(outputDir.getAbsolutePath(), namespace.replace(".", "/")).getAbsoluteFile(), filename);
		boolean mkdirs = file.getParentFile().mkdirs();
		if(!mkdirs) throw new IOException("Cannot create directory " + file.getParent());
		FileUtils.writeByteArrayToFile(file, content);
	}
}
