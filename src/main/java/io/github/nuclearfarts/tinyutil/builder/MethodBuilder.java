package io.github.nuclearfarts.tinyutil.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.LocalVariableDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.mapping.tree.ParameterDef;

public class MethodBuilder extends DescriptoredBuilder<MethodBuilder, MethodDef> {
	private final Collection<ParameterDef> params = new LinkedList<>();
	private final Collection<LocalVariableDef> locals = new LinkedList<>();
	
	MethodBuilder(String descriptor, DescriptorMapper descMapper, ToIntFunction<String> namespaceIndexer, String[] namespaces) {
		super(descriptor, descMapper, namespaceIndexer, namespaces);
	}

	public MethodBuilder parameter(int localIndex, Consumer<ParameterBuilder> consumer) {
		ParameterBuilder builder = new ParameterBuilder(namespaceIndexer, namespaces, localIndex);
		consumer.accept(builder);
		params.add(builder.build());
		return this;
	}
	
	public MethodBuilder local(int localIndex, int offset, Consumer<LocalVariableBuilder> consumer) {
		LocalVariableBuilder builder = new LocalVariableBuilder(namespaceIndexer, namespaces, localIndex, offset);
		consumer.accept(builder);
		locals.add(builder.build());
		return this;
	}
	
	@Override
	protected MethodBuilder getSelf() {
		return this;
	}

	@Override
	MethodDef build() {
		Collection<LocalVariableDef> lCopy = new LinkedList<>();
		Collection<ParameterDef> pCopy = new LinkedList<>();
		for(LocalVariableDef l : locals) {
			lCopy.add(l);
		}
		for(ParameterDef p : params) {
			pCopy.add(p);
		}
		return new MethodDefImpl(getNamesArray(), namespaceIndexer, comment, descriptor, descMapper, lCopy, pCopy);
	}

}
