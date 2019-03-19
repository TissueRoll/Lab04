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

public class InterfaceGenerator {
	
	public InterfaceGenerator() throws Exception {
		ScanResult results = new FastClasspathScanner("objects").scan();
		List<String> allResults = results.getNamesOfClassesWithAnnotation(Proxiable.class);
		for (String s : allResults) {
			Class c = Class.forName(s);
			String template = FileUtils.readFileToString(new File("template/ObjectProxyTemplate.txt"), Charset.defaultCharset());
			StringBuilder getters = new StringBuilder();
			StringBuilder setters = new StringBuilder();
			for (Field f : c.getDeclaredFields()) {
				String fN = Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1);
				getters.append("public " + f.getType().getSimpleName() + " get" + fN + "();\n");
				setters.append("public void set" + fN + "(" + f.getType().getSimpleName() + " " + f.getName() + ");\n");
			}
			template = template.replaceAll("<OBJECT>", c.getSimpleName());
			template = template.replaceAll("<GET_FIELDS>",  getters.toString());
			template = template.replaceAll("<SET_FIELDS>", setters.toString());
			FileUtils.write(new File("../Lab04Output/src/interfaces/" + c.getSimpleName() + "Proxy.java"), template, Charset.defaultCharset());
		}
	
	}
	
}
