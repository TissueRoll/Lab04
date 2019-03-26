package generators;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

import annotations.Proxiable;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

public class ProxyGenerator {
	public ProxyGenerator() throws Exception {
		ScanResult results = new FastClasspathScanner("objects").scan();
		List<String> allResults = results.getNamesOfClassesWithAnnotation(Proxiable.class);
		String template = FileUtils.readFileToString(new File("template/ProxyMakerBase.txt"), Charset.defaultCharset());
		StringBuilder methods = new StringBuilder();
		for (String s : allResults) {
			Class c = Class.forName(s);
			String templateE = FileUtils.readFileToString(new File("template/ProxyMakerForClasses.txt"), Charset.defaultCharset());
			templateE = templateE.replaceAll("<CLASS_NAME>", c.getSimpleName());
			methods.append(templateE);
			methods.append("\n");
		}
		template = template.replaceAll("<CREATE_PROXY>", methods.toString());
		FileUtils.write(new File("../Lab04Output/src/proxy/ProxyMaker.java"), template, Charset.defaultCharset());
	}
}
