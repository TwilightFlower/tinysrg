package io.github.nuclearfarts.tinysrg.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.Descriptored;

public abstract class DescriptoredBuilder<Self extends DescriptoredBuilder<Self, B>, B extends Descriptored> extends MappedBuilder<Self, B> {
	protected final String descriptor;
	protected final DescriptorMapper descMapper;
	
	DescriptoredBuilder(String descriptor, DescriptorMapper descMapper, ToIntFunction<String> namespaceIndexer, String[] namespaces) {
		super(namespaceIndexer, namespaces);
		this.descriptor = descriptor;
		this.descMapper = descMapper;
	}
}
