package de.seelab.codegenerator.test;

import de.seelab.codegenerator.Generator;
import de.seelab.codegenerator.ProjectContext;
import de.seelab.codegenerator.annotations.CodeGenerator;

@CodeGenerator
public class TestCodeGenerator implements Generator {
	@Override
	public void generate(ProjectContext context) throws Exception {
		context.writeOutputFile("de.seelab.codegenerator.test", "TestClass.java",
				"package de.seelab.codegenerator.test;\n" +
						"\n" +
						"public class TestClass {\n" +
						"   private String test;\n" +
						"   public String getTest() { return this.test; }\n" +
						"}");
	}
}
