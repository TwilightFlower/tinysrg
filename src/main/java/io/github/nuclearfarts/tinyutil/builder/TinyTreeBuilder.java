package io.github.nuclearfarts.tinyutil.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.TinyTree;

public class TinyTreeBuilder {
	private final Collection<ClassDef> classes = new LinkedList<>();
	private final Map<String, String> properties = new HashMap<>();
	private final List<String> namespaces = new ArrayList<>();
	// mutable lambdas essentially
	private final TinyTreeImpl.MethodRefHolder refHolder = new TinyTreeImpl.MethodRefHolder();
	private final ToIntFunction<String> namespaceIndexer;
	private final DescriptorMapper methodDescMapper;
	private final DescriptorMapper fieldDescMapper;
	
	private boolean acceptingNamespaces = true;
	private String[] namespacesArr;
	private int majorVersion = -1;
	private int minorVersion = -1;
	
	public TinyTreeBuilder() {
		//more shenanigans -- this one specifically to make sure the lambdas do not retain a reference to this object
		TinyTreeImpl.MethodRefHolder holder = refHolder;
		namespaceIndexer = holder::indexNamespace;
		methodDescMapper = holder::mapMethodDesc;
		fieldDescMapper = holder::mapFieldDesc;
		holder.namespaceIndexer = namespaces::indexOf; // will get overwritten later
	}
	
	public TinyTreeBuilder namespace(String namespace) {
		if(acceptingNamespaces) {
			namespaces.add(namespace);
		} else {
			throw new IllegalStateException("Adding namespace to builder after class is not allowed!");
		}
		return this;
	}
	
	public TinyTreeBuilder clazz(Consumer<? super ClassBuilder> consumer) {
		finalizeNamespaces();
		ClassBuilder builder = new ClassBuilder(fieldDescMapper, methodDescMapper, namespaceIndexer, namespacesArr);
		consumer.accept(builder);
		classes.add(builder.build());
		return this;
	}
	
	public TinyTreeBuilder property(String k, String v) {
		properties.put(k, v);
		return this;
	}
	
	public TinyTreeBuilder major(int version) {
		majorVersion = version;
		return this;
	}
	
	public TinyTreeBuilder minor(int version) {
		minorVersion = version;
		return this;
	}
	
	public TinyTree build() {
		return new TinyTreeImpl(ImmutableList.copyOf(namespaces), refHolder, ImmutableList.copyOf(classes), ImmutableMap.copyOf(properties), majorVersion, minorVersion);
	}
	
	private void finalizeNamespaces() {
		if(acceptingNamespaces) {
			namespacesArr = namespaces.toArray(new String[namespaces.size()]);
			acceptingNamespaces = false;
		}
	}
}
