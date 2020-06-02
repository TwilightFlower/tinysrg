package io.github.nuclearfarts.tinyutil.builder;

import java.util.Collection;
import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;

class ClassDefImpl extends MappedImpl implements ClassDef {
	private final Collection<FieldDef> fields;
	private final Collection<MethodDef> methods;
	
	public ClassDefImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment, Collection<FieldDef> fields, Collection<MethodDef> methods) {
		super(names, namespaceGetter, comment);
		this.fields = fields;
		this.methods = methods;
	}

	@Override
	public Collection<FieldDef> getFields() {
		return fields;
	}

	@Override
	public Collection<MethodDef> getMethods() {
		return methods;
	}

}
