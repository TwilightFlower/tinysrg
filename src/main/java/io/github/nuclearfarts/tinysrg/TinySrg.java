package io.github.nuclearfarts.tinysrg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.LocalVariableDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.mapping.tree.ParameterDef;
import net.fabricmc.mapping.tree.TinyMappingFactory;
import net.fabricmc.mapping.tree.TinyTree;

import io.github.nuclearfarts.tinysrg.builder.TinyTreeBuilder;
import io.github.nuclearfarts.tinysrg.util.Pair;
import io.github.nuclearfarts.tinysrg.util.Triple;

public class TinySrg {
	
	public static void main(String[] args) throws IOException {
		TinyTree mappings;
		try(BufferedReader r = Files.newBufferedReader(Paths.get(args[0]))) {
			mappings = TinyMappingFactory.load(r);
		}
		TsrgMappings tsrg = new TsrgMappings(Paths.get(args[1]));
		TinyTree tinySrg = addSrg(mappings, tsrg);
		try(OutputStream out = Files.newOutputStream(Paths.get(args[2]), StandardOpenOption.CREATE)) {
			TinyV2Writer.write(tinySrg, out);
		}
	}

	public static TinyTree addSrg(TinyTree tiny, TsrgMappings tsrg) {
		TinyTreeBuilder builder = new TinyTreeBuilder()
				.major(tiny.getMetadata().getMajorVersion())
				.minor(tiny.getMetadata().getMinorVersion());
		tiny.getMetadata().getProperties().forEach(builder::property);
		List<String> namespaces = tiny.getMetadata().getNamespaces();
		for(String namespace : namespaces) {
			builder.namespace(namespace);
		}
		builder.namespace("srg");
		for(ClassDef clazz : tiny.getClasses()) {
			builder.clazz(cb -> {
				for(String namespace : namespaces) {
					cb.name(namespace, clazz.getRawName(namespace));
				}
				String obfClass = clazz.getName("official");
				cb.comment(clazz.getComment());
				cb.name("srg", tsrg.mapClass(obfClass));
				for(MethodDef method : clazz.getMethods()) {
					String desc = method.getDescriptor("official");
					cb.method(desc, mb -> {
						for(String namespace : namespaces) {
							mb.name(namespace, method.getRawName(namespace));
						}
						mb.comment(method.getComment())
								.name("srg", tsrg.mapMethod(new Triple<>(obfClass, method.getName("official"), desc)));
						for(ParameterDef param : method.getParameters()) {
							mb.parameter(param.getLocalVariableIndex(), pb -> {
								for(String namespace : namespaces) {
									pb.name(namespace, param.getRawName(namespace));
								}
								pb.comment(param.getComment());
							});
						}
						for(LocalVariableDef local : method.getLocalVariables()) {
							mb.local(local.getLocalVariableIndex(), local.getLocalVariableStartOffset(), lb -> {
								for(String namespace : namespaces) {
									lb.name(namespace, local.getRawName(namespace));
								}
								lb.lvtIndex(local.getLocalVariableTableIndex())
										.comment(local.getComment());
							});
						}
					});
				}
				
				for(FieldDef field : clazz.getFields()) {
					cb.field(field.getDescriptor("official"), fb -> {
						for(String namespace : namespaces) {
							fb.name(namespace, field.getRawName(namespace));
						}
						fb.comment(field.getComment())
								.name("srg", tsrg.mapField(new Pair<>(obfClass, field.getName("official"))));
					});
				}
			});
		}
		return builder.build();
	}
}
