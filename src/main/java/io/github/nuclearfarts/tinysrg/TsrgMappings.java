package io.github.nuclearfarts.tinysrg;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import io.github.nuclearfarts.tinysrg.util.Pair;
import io.github.nuclearfarts.tinysrg.util.Triple;

public class TsrgMappings {

	private final Map<String, String> classes = new HashMap<>();
	private final Map<Triple<String, String, String>, String> methods = new HashMap<>();
	private final Map<Pair<String, String>, String> fields = new HashMap<>();
	
	public TsrgMappings(Path tsrg) throws IOException {
		try(Scanner scan = new Scanner(tsrg)) {
			String currentClass = "";
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				if(!line.startsWith("\t")) {
					String[] split = line.split(" ");
					classes.put(split[0], split[1]);
					currentClass = split[0];
				} else {
					String[] split = line.trim().split(" ");
					if(split.length == 3) {
						methods.put(new Triple<>(currentClass, split[0], split[1]), split[2]);
					} else {
						fields.put(new Pair<>(currentClass, split[0]), split[1]);
					}
				}
			}
		}
	}
	
	public String mapClass(String clazz) {
		return classes.getOrDefault(clazz, "");
	}

	public String mapMethod(Triple<String, String, String> method) {
		return methods.getOrDefault(method, "");
	}
	
	public String mapField(Pair<String, String> field) {
		return fields.getOrDefault(field, "");
	}
}
