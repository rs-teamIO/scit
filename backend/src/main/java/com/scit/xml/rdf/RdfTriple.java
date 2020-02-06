package com.scit.xml.rdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a RDF Triple.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RdfTriple {

    private String subject;
    private String predicate;
    private String object;

    @Override
    public String toString() {

        return String.format("%s %s %s.\n",
                RdfExtractor.wrap(this.subject), this.predicate, RdfExtractor.wrap(this.object));
    }
}
