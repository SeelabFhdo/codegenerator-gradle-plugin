package de.seelab.codegenerator.test;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public abstract class AbstractPluginTest {

	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder();

	protected File createFile(String file) {
		try {
			return testProjectDir.newFile(file);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
