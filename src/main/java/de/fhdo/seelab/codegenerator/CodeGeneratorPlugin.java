package de.fhdo.seelab.codegenerator;

import de.fhdo.seelab.codegenerator.annotations.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CodeGeneratorPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		CodeGeneratorConfiguration codeGenerator = project.getExtensions().create("codeGenerator", CodeGeneratorConfiguration.class);

		project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().all(s -> {
			String outputDir = project.getBuildDir() + "/generated-src/generator/" + s.getName();
			File outputDirFile = new File(outputDir);
			s.getJava().srcDir(outputDirFile);

			if(log.isDebugEnabled()) {
				log.debug("Using output dir " + outputDir);
			}

			File inputDir = new File(project.getProjectDir() + "/src/code-generator/" + s.getName());
			s.getJava().srcDir(inputDir);

			if(log.isDebugEnabled()) {
				log.debug("Using input dir " + inputDir);
			}

			String taskName = s.getTaskName("generate", "Code");
			project.getTasks().create(taskName, t -> {
				project.getTasks().getByName(s.getCompileJavaTaskName()).dependsOn(t.getPath());
				t.doLast(t2 -> {
					URL[] urls = codeGenerator.getGeneratorJars().stream().map(c -> {
						try {
							File file;
							if (!c.getPath().startsWith("/"))
								file = new File(project.getProjectDir().getAbsolutePath(), c.getPath());
							else
								file = c;
							if (file.exists())
								return file.toURI().toURL();
							return null;
						} catch (MalformedURLException e) {
							throw new RuntimeException(e);
						}
					}).filter(Objects::nonNull).toArray(URL[]::new);

					if(log.isDebugEnabled()) {
						log.debug("Found " + urls.length + " urls to scan");
					}

					ClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
					Thread.currentThread().setContextClassLoader(loader);
					Collection<URL> reflectionUrls = new ArrayList<>(ClasspathHelper.forClassLoader(loader));
					reflectionUrls.addAll(ClasspathHelper.forClassLoader(Thread.currentThread().getContextClassLoader()));
					ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
					configurationBuilder.setClassLoaders(new ClassLoader[] { loader, Thread.currentThread().getContextClassLoader() });
					configurationBuilder.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(),
							new ResourcesScanner());
					configurationBuilder.setUrls(reflectionUrls);
					Reflections reflections = new Reflections(configurationBuilder);
					Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CodeGenerator.class);

					if(log.isDebugEnabled()) {
						log.debug("Found " + typesAnnotatedWith.size() + " with code generator annotation: ");
						log.debug(typesAnnotatedWith.stream().map(Class::getCanonicalName).collect(Collectors.joining(",")));
					}

					ProjectContext context = new ProjectContext(project.getProjectDir(), inputDir, outputDirFile, codeGenerator.getConfigurationValues());

					typesAnnotatedWith.forEach(c -> {
						try {
							log.debug("Executing " + c.getCanonicalName() + " ...");
							new CodeGeneratorExecutor(c).execute(context);
							log.debug("... Success");
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					});
				});
			});
		});
	}
}
