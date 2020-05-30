package io.github.nuclearfarts.tinysrg.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;

public class ClassBuilder extends MappedBuilder<ClassBuilder, ClassDef> {
	private final Collection<MethodDef> methods = new LinkedList<>();
	private final Collection<FieldDef> fields = new LinkedList<>();
	private final DescriptorMapper fieldDescMapper, methodDescMapper;
	
	ClassBuilder(DescriptorMapper fieldMapper, DescriptorMapper methodMapper, ToIntFunction<String> namespaceIndexer, String[] namespaces) {
		super(namespaceIndexer, namespaces);
		fieldDescMapper = fieldMapper;
		methodDescMapper = methodMapper;
	}

	@Override
	protected ClassBuilder getSelf() {
		return this;
	}

	public ClassBuilder field(String descriptor, Consumer<? super FieldBuilder> consumer) {
		FieldBuilder builder = new FieldBuilder(descriptor, fieldDescMapper, namespaceIndexer, namespaces);
		consumer.accept(builder);
		fields.add(builder.build());
		return this;
	}
	
	public ClassBuilder method(String descriptor, Consumer<? super MethodBuilder> consumer) {
		MethodBuilder builder = new MethodBuilder(descriptor, methodDescMapper, namespaceIndexer, namespaces);
		consumer.accept(builder);
		methods.add(builder.build());
		return this;
	}
	
	@Override
	ClassDef build() {
		Collection<FieldDef> fCopy = new LinkedList<>();
		Collection<MethodDef> mCopy = new LinkedList<>();
		for(MethodDef m : methods) {
			mCopy.add(m);
		}
		for(FieldDef f : fields) {
			fCopy.add(f);
		}
		return new ClassDefImpl(getNamesArray(), namespaceIndexer, comment, fCopy, mCopy);
	}
}
