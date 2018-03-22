package de.fhdo.seelab.codegenerator.test;

import de.fhdo.seelab.codegenerator.CodeGeneratorPlugin;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CodeGeneratorPluginTests extends AbstractPluginTest {

	private Project test_project;

	@Before
	public void createProject() {
		test_project = ProjectBuilder.builder().withName("Test Project").build();
	}

	@Test
	public void taskApplyTest() {
		test_project.getPlugins().apply(JavaPlugin.class);
		test_project.getPlugins().apply(CodeGeneratorPlugin.class);

		TaskContainer tasks = test_project.getTasks();
		assertThat(tasks.parallelStream().anyMatch(t -> t.getName().equals("generateCode")), is(equalTo(true)));
	}

	@Test
	public void testBuild() {
		File file = createFile("build.gradle");

		try {
			File resource = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("test-generator.jar")).toURI());
			FileUtils.copyFile(resource, new File(file.getParentFile(), "test-generator.jar"));
			String fileContent = "plugins {\n" +
					"   id 'java'\n" +
					"   id 'de.seelab.CodeGenerator'\n" +
					"}\n" +
					"codeGenerator {\n" +
					"   generatorJar 'test-generator.jar'\n" +
					"}\n";
			FileUtils.write(file, fileContent, "UTF-8");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		BuildResult myCoolTask = GradleRunner.create()
				.withProjectDir(file.getParentFile())
				.withPluginClasspath()
				.withDebug(true)
				.withArguments("generateCode", "--stacktrace").build();

		System.out.println(myCoolTask.getOutput());
		System.out.println(file.getAbsolutePath());
		assertThat(myCoolTask.task(":generateCode").getOutcome(), is(equalTo(TaskOutcome.SUCCESS)));
		assertThat(new File(file.getParentFile(), "build/generated-src/generator/main/de/seelab/codegenerator/test/TestClass.java").exists(), is(equalTo(true)));
	}
}
