package generators;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import annotations.Proxiable;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class ClassGenerator {
	
	public ClassGenerator() throws Exception {
		ScanResult results = new FastClasspathScanner("objects").scan();
		List<String> allResults = results.getNamesOfClassesWithAnnotation(Proxiable.class);
		for (String s : allResults) {
			Class c = Class.forName(s);
			String template = FileUtils.readFileToString(new File("template/ObjectClassTemplate.txt"), Charset.defaultCharset());
			StringBuilder declarers = new StringBuilder();
			StringBuilder getters = new StringBuilder();
			StringBuilder setters = new StringBuilder();
			for (Field f : c.getDeclaredFields()) {
				declarers.append("private " + f.getType().getSimpleName() + " " + f.getName() + ";\n");
				String fN = Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1);
				getters.append("public " + f.getType().getSimpleName() + " get" + fN + "() {return " +f.getName() + ";}\n");
				setters.append("public void set" + fN + "(" + f.getType().getSimpleName() + " " + f.getName() + "){this." + f.getName() + " = " + f.getName() + ";}\n");
			}
			template = template.replaceAll("<OBJECT>", c.getSimpleName());
			template = template.replaceAll("<DECLARE_FIELD>", declarers.toString());
			template = template.replaceAll("<GET_METHOD>",  getters.toString());
			template = template.replaceAll("<SET_METHOD>", setters.toString());
			FileUtils.write(new File("../Lab04Output/src/classes/" + c.getSimpleName() + ".java"), template, Charset.defaultCharset());
		}
	
	}
	
}
