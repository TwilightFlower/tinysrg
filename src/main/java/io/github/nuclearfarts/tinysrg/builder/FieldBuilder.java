package io.github.nuclearfarts.tinysrg.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.FieldDef;

public class FieldBuilder extends DescriptoredBuilder<FieldBuilder, FieldDef> {

	FieldBuilder(String descriptor, DescriptorMapper descMapper, ToIntFunction<String> namespaceIndexer, String[] namespaces) {
		super(descriptor, descMapper, namespaceIndexer, namespaces);
	}

	@Override
	protected FieldBuilder getSelf() {
		return this;
	}

	@Override
	FieldDef build() {
		return new FieldDefImpl(getNamesArray(), namespaceIndexer, comment, descriptor, descMapper);
	}

}
