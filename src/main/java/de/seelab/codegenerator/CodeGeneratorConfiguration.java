package de.seelab.codegenerator;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter(AccessLevel.PACKAGE)
public class CodeGeneratorConfiguration {
	private List<File> generatorJars = new ArrayList<>();

	/**
	 * Add files to generator jars
	 * @param file File to add to generator jars
	 */
	public void generatorJar(File file) {
		generatorJars.add(file);
	}

	/**
	 * Add file to generator jars
	 * @param filename Filename to add to generator jars
	 */
	public void generatorJar(String filename) {
		generatorJars.add(new File(filename));
	}
}
