package io.github.nuclearfarts.tinyutil.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.LocalVariableDef;

class LocalVariableDefImpl extends MappedImpl implements LocalVariableDef {
	private final int index, offset, lvtIndex;
	
	public LocalVariableDefImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment, int index, int offset, int lvtIndex) {
		super(names, namespaceGetter, comment);
		this.index = index;
		this.offset = offset;
		this.lvtIndex = lvtIndex;
	}

	@Override
	public int getLocalVariableIndex() {
		return index;
	}

	@Override
	public int getLocalVariableStartOffset() {
		return offset;
	}

	@Override
	public int getLocalVariableTableIndex() {
		return lvtIndex;
	}

}
