package com.scit.xml.common.util;

import com.scit.xml.exception.InternalServerException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility methods for {@link ResourceSet} class.
 */
public class ResourceSetUtils {

    private ResourceSetUtils() { }

    /**
     * Converts a {@link ResourceSet} instance to XML string
     * @param resourceSet {@link ResourceSet} instance to be transformed
     * @return result XML string
     */
    public static String toXml(ResourceSet resourceSet) {
        StringBuilder sb = new StringBuilder();
        try {
            ResourceIterator i = resourceSet.getIterator();
            Resource res;
            while (true) {
                if (!i.hasMoreResources())
                    break;
                res = i.nextResource();
                sb.append(res.getContent());
            }
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
        return sb.toString();
    }


    public static List<String> toList(ResourceSet resourceSet) {
        List<String> resources = new ArrayList<>();
        try {
            ResourceIterator i = resourceSet.getIterator();
            Resource res;
            while (true) {
                if (!i.hasMoreResources())
                    break;
                res = i.nextResource();
                resources.add((String) res.getContent());
            }
        } catch (Exception e) {
            throw new InternalServerException(e);
        }
        return resources;
    }
}
