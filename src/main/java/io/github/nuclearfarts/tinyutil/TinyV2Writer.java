package io.github.nuclearfarts.tinyutil;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import net.fabricmc.mapping.reader.v2.TinyMetadata;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.mapping.tree.ParameterDef;
import net.fabricmc.mapping.tree.TinyTree;

public class TinyV2Writer {
	public static void write(TinyTree tiny, OutputStream outStream) {
		PrintStream out = new PrintStream(outStream);
		TinyMetadata meta = tiny.getMetadata();
		List<String> namespaces = meta.getNamespaces();
		int numNamespaces = namespaces.size();
		out.printf("tiny\t%d\t%d\t%s\n", meta.getMajorVersion(), meta.getMinorVersion(), tabSeparate(namespaces.toArray(new String[numNamespaces])));
		String[] names = new String[numNamespaces];
		for(ClassDef clazz : tiny.getClasses()) {
			for(int i = 0; i < numNamespaces; i++) {
				names[i] = clazz.getRawName(namespaces.get(i));
			}
			out.printf("c\t%s\n", tabSeparate(names));
			for(MethodDef method : clazz.getMethods()) {
				for(int i = 0; i < numNamespaces; i++) {
					names[i] = method.getRawName(namespaces.get(i));
				}
				out.printf("\tm\t%s\t%s\n", method.getDescriptor(namespaces.get(0)), tabSeparate(names));
				for(ParameterDef param : method.getParameters()) {
					for(int i = 0; i < numNamespaces; i++) {
						names[i] = param.getRawName(namespaces.get(i));
					}
					out.printf("\t\tp\t%d\t%s\n", param.getLocalVariableIndex(), tabSeparate(names));
				}
			}
			for(FieldDef field : clazz.getFields()) {
				for(int i = 0; i < numNamespaces; i++) {
					names[i] = field.getRawName(namespaces.get(i));
				}
				out.printf("\tf\t%s\t%s\n", field.getDescriptor(namespaces.get(0)), tabSeparate(names));
			}
		}
		out.flush();
	}
	
	private static String tabSeparate(String... strings) {
		StringBuilder sb = new StringBuilder(strings[0]);
		for(int i = 1; i < strings.length; i++) {
			sb.append('\t').append(strings[i]);
		}
		return sb.toString();
	}
}
