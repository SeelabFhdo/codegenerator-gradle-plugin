package de.seelab.codegenerator.test;

import de.seelab.codegenerator.CodeGeneratorPlugin;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CodeGeneratorPluginTests extends AbstractPluginTest {

	private Project test_project;

	@Before
	public void createProject() {
		test_project = ProjectBuilder.builder().withName("Test Project").build();
	}

	@Test
	public void taskApplyTest() {
		test_project.getPlugins().apply(CodeGeneratorPlugin.class);

		assertThat(test_project.getTasks().parallelStream().anyMatch(t -> t.getName().equals("MyCoolTask")), is(equalTo(true)));
	}

	@Test
	public void testBuild() {
		File file = createFile("build.gradle");

		try {
			FileUtils.write(file, "plugins {\n" +
					"   id 'CodeGenerator'\n" +
					"}", "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<File> collect = Arrays.stream(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs()).map(u -> {
			try {
				return new File(u.toURI());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
		BuildResult myCoolTask = GradleRunner.create()
				.withProjectDir(testProjectDir.getRoot())
				.withPluginClasspath(collect)
				.withArguments("MyCoolTask").build();

		System.out.println(myCoolTask.getOutput());
	}
}
