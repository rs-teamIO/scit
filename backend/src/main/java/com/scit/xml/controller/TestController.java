package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController
@RequestMapping(RestApiEndpoints.TEST)
@RequiredArgsConstructor
public class TestController {

    @PreAuthorize("hasAuthority('author')")
    @GetMapping(value = RestApiEndpoints.TEST_AUTHOR, produces = TEXT_PLAIN_VALUE)
    public String testAuthor() {
        return "Author role test successful.";
    }

    @PreAuthorize("hasAuthority('editor')")
    @GetMapping(RestApiEndpoints.TEST_EDITOR)
    public String testEditor() {
        return "Editor role test successful.";
    }
}