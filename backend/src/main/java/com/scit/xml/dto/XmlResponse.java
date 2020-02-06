package com.scit.xml.dto;

import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class XmlResponse extends HashMap<String, Object> {

    public XmlResponse(String key, Object value) {
        put(key, value);
    }
}
