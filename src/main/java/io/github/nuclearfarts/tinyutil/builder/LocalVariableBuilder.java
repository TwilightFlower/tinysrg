package io.github.nuclearfarts.tinyutil.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.LocalVariableDef;

public class LocalVariableBuilder extends MappedBuilder<LocalVariableBuilder, LocalVariableDef> {
	private final int index, offset;
	private int lvtIndex = -1;
	
	public LocalVariableBuilder(ToIntFunction<String> namespaceIndexer, String[] namespaces, int index, int offset) {
		super(namespaceIndexer, namespaces);
		this.index = index;
		this.offset = offset;
	}
	
	public LocalVariableBuilder lvtIndex(int index) {
		lvtIndex = index;
		return this;
	}

	@Override
	protected LocalVariableBuilder getSelf() {
		return this;
	}

	@Override
	LocalVariableDef build() {
		return new LocalVariableDefImpl(getNamesArray(), namespaceIndexer, comment, index, offset, lvtIndex);
	}

}
