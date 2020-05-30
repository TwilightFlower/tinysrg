package io.github.nuclearfarts.tinysrg.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.Mapped;

abstract class MappedImpl implements Mapped {
	private final String[] names;
	private final ToIntFunction<String> namespaceGetter;
	private final String comment;
	
	public MappedImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment) {
		this.names = names;
		this.namespaceGetter = namespaceGetter;
		this.comment = comment;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public String getName(String namespace) {
		for(int i = namespaceGetter.applyAsInt(namespace); i >= 0; i--) {
			String name;
			if((name = names[i]) != null) {
				return name;
			}
		}
		return names[0];
	}

	@Override
	public String getRawName(String namespace) {
		String name;
		return (name = names[namespaceGetter.applyAsInt(namespace)]) != null ? name : "";
	}

}
