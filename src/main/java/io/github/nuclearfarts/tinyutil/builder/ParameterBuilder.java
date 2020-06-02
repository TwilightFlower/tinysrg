package io.github.nuclearfarts.tinyutil.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.ParameterDef;

public class ParameterBuilder extends MappedBuilder<ParameterBuilder, ParameterDef> {
	private final int localIndex;
	
	ParameterBuilder(ToIntFunction<String> namespaceIndexer, String[] namespaces, int localIndex) {
		super(namespaceIndexer, namespaces);
		this.localIndex = localIndex;
	}

	@Override
	protected ParameterBuilder getSelf() {
		return this;
	}

	@Override
	ParameterDef build() {
		return new ParameterDefImpl(getNamesArray(), namespaceIndexer, comment, localIndex);
	}

}
