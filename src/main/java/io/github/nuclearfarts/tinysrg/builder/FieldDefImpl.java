package io.github.nuclearfarts.tinysrg.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.FieldDef;

class FieldDefImpl extends DescriptoredImpl implements FieldDef {

	public FieldDefImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment, String descriptor, DescriptorMapper descMapper) {
		super(names, namespaceGetter, comment, descriptor, descMapper);
	}

}
