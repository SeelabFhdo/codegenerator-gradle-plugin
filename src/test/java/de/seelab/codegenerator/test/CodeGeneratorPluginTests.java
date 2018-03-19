package de.seelab.codegenerator.test;

import de.seelab.codegenerator.CodeGeneratorPlugin;
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
import java.net.URL;

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
			URL resource = Thread.currentThread().getContextClassLoader().getResource("test-code-gen-1.0-SNAPSHOT.jar");
			if(resource == null) throw new Exception("File not found!");
			FileUtils.writeByteArrayToFile(new File(file.getParent(), "test-code-gen-1.0-SNAPSHOT.jar"), FileUtils.readFileToByteArray(new File(resource.toURI())));
			String fileContent = "plugins {\n" +
					"   id 'java'\n" +
					"   id 'de.seelab.CodeGenerator'\n" +
					"}\n" +
					"\n" +
					"codeGenerator {\n" +
					"   generatorJar 'test-code-gen-1.0-SNAPSHOT.jar'\n" +
					"}";
			FileUtils.write(file, fileContent, "UTF-8");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		BuildResult myCoolTask = GradleRunner.create()
				.withProjectDir(file.getParentFile())
				.withPluginClasspath()
				.withDebug(true)
				.withArguments("generateCode", "--stacktrace").build();

		assertThat(myCoolTask.task(":generateCode").getOutcome(), is(equalTo(TaskOutcome.SUCCESS)));
		assertThat(new File(file.getParent(), "build/generated-src/generator/main/io/freefair/test/TestClass").exists(), is(equalTo(true)));
	}
}
