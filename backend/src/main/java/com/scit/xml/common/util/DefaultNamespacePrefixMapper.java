package com.scit.xml.common.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link NamespacePrefixMapper} that maps the schema
 * namespaces more to readable names. Used by the JAXB {@link javax.xml.bind.Marshaller}.
 * Requires setting the property "com.sun.xml.bind.namespacePrefixMapper" to an instance of this class.
 * <p>
 * Requires dependency on JAXB implementation JARs
 * </p>
 */
@XmlTransient
public class DefaultNamespacePrefixMapper extends NamespacePrefixMapper {

    private Map<String, String> mappings = new HashMap<>();

    public DefaultNamespacePrefixMapper() {
        this.setDefaultMappings();
    }

    /**
     * Creates default mappings.
     */
    protected void setDefaultMappings() {
        this.mappings.clear();
        this.addMapping("http://www.scit.org/schema/papers", "papers");
        this.addMapping("http://www.scit.org/schema/paper", "paper");
        this.addMapping("http://www.scit.org/schema/cover_letters", "cover_letters");
        this.addMapping("http://www.scit.org/schema/cover_letter", "cover_letter");
        this.addMapping("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        this.addMapping("http://java.sun.com/xml/ns/jaxb", "jaxb");
    }

    public void addMapping(String uri, String prefix) {
        this.mappings.put(uri, prefix);
    }

    /* (non-Javadoc)
     * Returning null when not found based on spec.
     * @see com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        return this.mappings.getOrDefault(namespaceUri, suggestion);
    }
}
