package com.scit.xml.controller;

import com.scit.xml.common.api.RestApiEndpoints;
import com.scit.xml.common.api.RestApiRequestParameters;
import com.scit.xml.common.util.XmlResponseUtils;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(RestApiEndpoints.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET api/v1/user/
     * ACCESS LEVEL: Anyone
     *
     * Returns user data of user with given ID.
     * @param id unique identifier of the User
     */
    @GetMapping(params = { RestApiRequestParameters.USER_ID },
                produces = APPLICATION_XML_VALUE)
    public ResponseEntity<String> getUserById(@RequestParam(RestApiRequestParameters.USER_ID) String id) {
        String userXml = this.userService.findById(id);
        String responseBody = XmlResponseUtils.convertToUserXmlResponse(userXml);
        return ResponseEntity.ok(responseBody);
    }

    /**
     * GET api/v1/user/me
     * ACCESS LEVEL: Only if authenticated
     *
     * Returns the current user's data.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = RestApiEndpoints.CURRENT_USER,
                produces = { APPLICATION_XML_VALUE })
    public ResponseEntity<String> getCurrentUser() {
        String userXml = this.userService.findById(JwtTokenDetailsUtil.getCurrentUserId());
        String responseBody = XmlResponseUtils.convertToUserXmlResponse(userXml);
        return ResponseEntity.ok(responseBody);
    }
}
