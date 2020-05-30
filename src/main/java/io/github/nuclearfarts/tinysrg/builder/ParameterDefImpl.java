package io.github.nuclearfarts.tinysrg.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.ParameterDef;

class ParameterDefImpl extends MappedImpl implements ParameterDef {
	private final int localIndex;
	
	public ParameterDefImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment, int localIndex) {
		super(names, namespaceGetter, comment);
		this.localIndex = localIndex;
	}

	@Override
	public int getLocalVariableIndex() {
		return localIndex;
	}

}
