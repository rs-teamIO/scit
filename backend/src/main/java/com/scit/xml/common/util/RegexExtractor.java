package com.scit.xml.common.util;

import com.scit.xml.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the RegexExtractor class.
 */
public class RegexExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegexExtractor.class);

    private Pattern pattern;

    /**
     * Initializes a new instance of the {@link RegexExtractor} class.
     */
    public RegexExtractor(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    /**
     * Extracts a value from XML input by a pre-set regex.
     *
     * @param text input to be extracted from
     * @return extracted value
     */
    public String extract(String text) {
        LOGGER.debug("Extracting value from XML input.");

        String extractedValue = null;
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
            extractedValue = matcher.group(1);

            LOGGER.debug(String.format("Extracted value: %s", extractedValue));
            return extractedValue;
        }
        else {
            throw new InternalServerException();
        }
    }
}
