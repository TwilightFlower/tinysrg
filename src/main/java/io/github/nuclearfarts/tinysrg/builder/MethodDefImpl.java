package io.github.nuclearfarts.tinysrg.builder;

import java.util.Collection;
import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.LocalVariableDef;
import net.fabricmc.mapping.tree.MethodDef;
import net.fabricmc.mapping.tree.ParameterDef;

class MethodDefImpl extends DescriptoredImpl implements MethodDef {
	private final Collection<LocalVariableDef> locals;
	private final Collection<ParameterDef> params;
	
	public MethodDefImpl(String[] names, ToIntFunction<String> namespaceGetter, String comment, String descriptor, DescriptorMapper descMapper, Collection<LocalVariableDef> locals, Collection<ParameterDef> params) {
		super(names, namespaceGetter, comment, descriptor, descMapper);
		this.locals = locals;
		this.params = params;
	}

	@Override
	public Collection<LocalVariableDef> getLocalVariables() {
		return locals;
	}

	@Override
	public Collection<ParameterDef> getParameters() {
		return params;
	}

}
