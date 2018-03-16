package de.seelab.codegenerator;

import de.seelab.codegenerator.annotations.CodeGenerator;
import groovy.lang.Closure;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CodeGeneratorPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		CodeGeneratorConfiguration codeGenerator = project.getExtensions().create("codeGenerator", CodeGeneratorConfiguration.class);

		project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().all(s -> {
			String outputDir = project.getBuildDir() + "/generated-src/generator/" + s.getName();
			File outputDirFile = new File(outputDir);
			s.getJava().srcDir(outputDirFile);

			String taskName = s.getTaskName("generate", "Code");
			project.getTasks().create(taskName, t -> {
				t.doLast(t2 -> {
					URL[] urls = Arrays.stream(codeGenerator.getGeneratorJars()).map(c -> {
						try {
							return new URL(c.getAbsolutePath());
						} catch (MalformedURLException e) {
							throw new RuntimeException(e);
						}
					}).collect(Collectors.toList()).toArray(new URL[codeGenerator.getGeneratorJars().length]);
					ClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
					Reflections reflections = new Reflections(new ConfigurationBuilder().addClassLoader(loader)
							.addScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false), new ResourcesScanner()));
					Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CodeGenerator.class);
					
				});
			});
		});
	}
}
