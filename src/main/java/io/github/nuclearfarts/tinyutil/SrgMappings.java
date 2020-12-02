package io.github.nuclearfarts.tinyutil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.github.nuclearfarts.tinyutil.util.Pair;
import io.github.nuclearfarts.tinyutil.util.Triple;

/**
 * Immutable basic representation of SRG-family mappings.
 */
public class SrgMappings {
	private static final Pattern CLASS_DESC = Pattern.compile("L[^.;\\[]+;");

	private final Map<String, String> classes;
	private final Map<Triple<String, String, String>, String> methods; // (class, name, desc)
	private final Map<Pair<String, String>, String> fields; // (class, name)
	
	public SrgMappings(Map<String, String> classes, Map<Triple<String, String, String>, String> methods, Map<Pair<String, String>, String> fields) {
		this.classes = new HashMap<>();
		this.methods = new HashMap<>();
		this.fields = new HashMap<>();
		this.classes.putAll(classes);
		this.methods.putAll(methods);
		this.fields.putAll(fields);
	}
	
	//no-copy ctor for internal use
	private SrgMappings(Map<String, String> classes, Map<Triple<String, String, String>, String> methods, Map<Pair<String, String>, String> fields, boolean differentiatingParameter) {
		this.classes = classes;
		this.methods = methods;
		this.fields = fields;
	}
	
	public static SrgMappings fromTsrg(Path tsrg) throws IOException {
		Map<String, String> classes = new HashMap<>();
		Map<Pair<String, String>, String> fields = new HashMap<>();
		Map<Triple<String, String, String>, String> methods = new HashMap<>();
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
		return new SrgMappings(classes, methods, fields, false);
	}
	
	public static SrgMappings fromCsrg(Path cl, String classPrefix, Path members) throws IOException {
		// bet you didn't know about this syntax
		// basically a bimap but with weird prefixes
		class ClassStore {
			final Map<String, String> main = new HashMap<>();
			final BiMap<String, String> reverse = HashBiMap.create();
			final String prefix;
			
			ClassStore(String prefix) {
				this.prefix = prefix;
			}
			
			void put(String obf, String deobf) {
				if(reverse.containsKey(deobf)) {
					throw new IllegalArgumentException(String.format("Attempted to map both %s and %s to %s", obf, reverse.get(deobf), deobf));
				} else {
					String prefixed = prefix + "/" + deobf;
					main.put(obf, prefixed);
					reverse.inverse().put(obf, deobf);
				}
			}
		}
		
		ClassStore classes = new ClassStore(classPrefix);
		Map<Pair<String, String>, String> fields = new HashMap<>();
		Map<Triple<String, String, String>, String> methods = new HashMap<>();
		try(Scanner scan = new Scanner(cl)) {
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				if(!line.startsWith("#")) {
					String[] split = line.split(" ");
					classes.put(split[0], split[1]);
				}
			}
		}
		
		try(Scanner scan = new Scanner(members)) {
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				if(!line.startsWith("#")) {
					String[] split = line.split(" ");
					String clazz = classes.reverse.getOrDefault(split[0], split[0]);
					if(split.length == 4) {
						String desc = demapMethodDesc(split[2], classes.reverse);
						methods.put(new Triple<>(clazz, split[1], desc), split[3]);
					} else {
						fields.put(new Pair<>(clazz, split[1]), split[2]);
					}
				}
			}
		}
		
		return new SrgMappings(classes.main, methods, fields, false);
	}
	
	private static String demapMethodDesc(String desc, Map<String, String> classDemapper) {
		StringBuffer sb = new StringBuffer();
		Matcher m = CLASS_DESC.matcher(desc);
		while(m.find()) {
			String d = m.group();
			String clazz = d.substring(1, d.length() - 1);
			m.appendReplacement(sb, String.format("L%s;", classDemapper.getOrDefault(clazz, clazz).replace("\\", "\\\\").replace("$", "\\$"))); // don't you just love java
		}
		m.appendTail(sb);
		return sb.toString();
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
