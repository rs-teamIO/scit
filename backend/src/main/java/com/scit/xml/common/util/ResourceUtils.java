package com.scit.xml.common.util;

import com.google.common.io.ByteStreams;
import com.scit.xml.exception.InternalServerException;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {

    private ResourceUtils() { }

    public static byte[] convertResourceToByteArray(Resource resource) {
        try {
            return ByteStreams.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }

    public static String convertResourceToString(Resource resource) {
        try {
            Reader htmlResourceReader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(htmlResourceReader);
        } catch (IOException e) {
            throw new InternalServerException(e);
        }
    }
}
