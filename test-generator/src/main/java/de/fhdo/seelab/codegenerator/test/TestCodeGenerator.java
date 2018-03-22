package de.fhdo.seelab.codegenerator.test;

import de.fhdo.seelab.codegenerator.Generator;
import de.fhdo.seelab.codegenerator.ProjectContext;
import de.fhdo.seelab.codegenerator.annotations.CodeGenerator;

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
