package com.scit.xml.common;

import com.scit.xml.exception.InternalServerException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class XQueryBuilder {

    /**
     * Used to build a query string.
     * @param queryFile file where the query template is located
     * @param params parameters for the query template
     * @return query string
     */
    public String buildQuery(Resource queryFile, String... params) {
        try {
            String queryPath = Paths.get(queryFile.getURI()).toString();
            return String.format(this.loadQuery(queryPath), params);
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    /**
     * Convenience method for reading file contents into a string.
     * @param path path where the query template file is located at
     * @return query template string representation
     * @throws IOException Thrown in case the file is not found at the given path
     */
    private String loadQuery(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
