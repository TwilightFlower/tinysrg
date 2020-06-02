package io.github.nuclearfarts.tinyutil.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.Mapped;

public abstract class MappedBuilder<Self extends MappedBuilder<Self, B>, B extends Mapped> {
	protected final Map<String, String> names = new HashMap<>();
	protected final ToIntFunction<String> namespaceIndexer;
	protected final String[] namespaces;
	protected String comment = null;
	
	MappedBuilder(ToIntFunction<String> namespaceIndexer, String[] namespaces) {
		this.namespaceIndexer = namespaceIndexer;
		this.namespaces = namespaces;
	}
	
	public Self name(String namespace, String name) {
		names.put(namespace, name);
		return getSelf();
	}
	
	public Self comment(String comment) {
		this.comment = comment;
		return getSelf();
	}
	
	protected String[] getNamesArray() {
		String[] names = new String[namespaces.length];
		for(Map.Entry<String, String> e : this.names.entrySet()) {
			names[namespaceIndexer.applyAsInt(e.getKey())] = e.getValue();
		}
		return names;
	}
	
	protected abstract Self getSelf();
	abstract B build();
}
