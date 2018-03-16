package de.seelab.codegenerator;

import lombok.Data;

import java.io.File;

@Data
public class CodeGeneratorConfiguration {
	private File[] generatorJars;
}
