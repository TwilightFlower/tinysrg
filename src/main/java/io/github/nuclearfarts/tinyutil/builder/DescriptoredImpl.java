package io.github.nuclearfarts.tinyutil.builder;

import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.Descriptored;

abstract class DescriptoredImpl extends MappedImpl implements Descriptored {
	private final DescriptorMapper mapper;
	private final String descriptor;
	
	public DescriptoredImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment, String descriptor, DescriptorMapper descMapper) {
		super(names, namespaceGetter, comment);
		this.mapper = descMapper;
		this.descriptor = descriptor;
	}

	@Override
	public String getDescriptor(String namespace) {
		return mapper.map(descriptor, namespace);
	}
}
