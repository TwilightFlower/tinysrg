package io.github.nuclearfarts.tinysrg.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.fabricmc.mapping.reader.v2.TinyMetadata;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.TinyTree;

class TinyTreeImpl implements TinyTree {
	private static final Pattern TYPE_MATCHER = Pattern.compile("L[^;]+?;"); // match type names in a descriptor
	
	private final List<String> namespaces;
	private final Collection<ClassDef> classes;
	private final Map<String, String> properties;
	private final TinyMetadata meta;
	private final Map<String, ClassDef> defaultMap = new HashMap<>();
	
	public TinyTreeImpl(List<String> namespaces, MethodRefHolder refs, Collection<ClassDef> classes, Map<String, String> properties, int majorVersion, int minorVersion) {
		this.classes = classes;
		this.namespaces = namespaces;
		this.properties = properties;
		meta = new TinyMetadataImpl(majorVersion, minorVersion);
		String defaultNs = namespaces.get(0);
		for(ClassDef c : classes) {
			defaultMap.put(c.getRawName(defaultNs), c);
		}
		refs.fieldDescMapper = this::mapFieldDesc;
		refs.methodDescMapper = this::mapMethodDesc;
		refs.namespaceIndexer = meta::index;
	}

	@Override
	public Collection<ClassDef> getClasses() {
		return classes;
	}

	@Override
	public Map<String, ClassDef> getDefaultNamespaceClassMap() {
		return defaultMap;
	}

	@Override
	public TinyMetadata getMetadata() {
		return meta;
	}
	
	private String mapFieldDesc(String fieldDesc, String namespace) {
		if(fieldDesc.length() <= 1) {
			return fieldDesc; // primitive type
		}
		ClassDef def;
		String stripped = fieldDesc.substring(1, fieldDesc.length() - 1); // strip L off the front and ; off the end
		if((def = defaultMap.get(stripped)) != null) {
			return "L" + def.getName(namespace) + ";";
		} else {
			return fieldDesc;
		}
	}
	
	private String mapMethodDesc(String methodDesc, String namespace) {
		Matcher m = TYPE_MATCHER.matcher(methodDesc);
		StringBuffer sb = new StringBuffer();
		while(m.find()) {
		    m.appendReplacement(sb, "");
		    sb.append(mapFieldDesc(m.group(), namespace));
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	private class TinyMetadataImpl implements TinyMetadata {
		private final int major, minor;
		TinyMetadataImpl(int major, int minor) {
			this.major = major;
			this.minor = minor;
		}
		
		@Override
		public int getMajorVersion() {
			return major;
		}

		@Override
		public int getMinorVersion() {
			return minor;
		}

		@Override
		public List<String> getNamespaces() {
			return namespaces;
		}

		@Override
		public Map<String, String> getProperties() {
			return properties;
		}

		@Override
		public int index(String namespace) throws IllegalArgumentException {
			return namespaces.indexOf(namespace);
		}
	}

	static class MethodRefHolder {
		ToIntFunction<String> namespaceIndexer;
		private DescriptorMapper fieldDescMapper, methodDescMapper;
		
		int indexNamespace(String namespace) {
			return namespaceIndexer.applyAsInt(namespace);
		}
		
		String mapFieldDesc(String desc, String targetNamespace) {
			return fieldDescMapper.map(desc, targetNamespace);
		}
		
		String mapMethodDesc(String desc, String targetNamespace) {
			return methodDescMapper.map(desc, targetNamespace);
		}
	}
}
