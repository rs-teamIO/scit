package com.scit.xml.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.common.api.RestApiConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiError {

    @JsonProperty(RestApiConstants.RESPONSE_CODE)
    private Integer responseCode;

    @JsonProperty(RestApiConstants.MESSAGE)
    private String message;

    @JsonProperty(RestApiConstants.BODY)
    private Map<String, String> body = new HashMap<>();

    public static RestApiError fromBadRequestException(BadRequestException e) {
        RestApiError restApiError = new RestApiError();
        restApiError.setResponseCode(e.getResponseCode().getCode());
        restApiError.setMessage(e.getResponseCode().getMessage());
        restApiError.getBody().putIfAbsent(RestApiConstants.MESSAGE, e.getMessage());
        return restApiError;
    }

    public static RestApiError fromNotFoundException(NotFoundException e) {
        RestApiError restApiError = new RestApiError();
        restApiError.getBody().putIfAbsent(RestApiConstants.MESSAGE, e.getMessage());
        return restApiError;
    }

    public static RestApiError fromInsufficientPrivilegesException() {
        return RestApiError.builder()
                .responseCode(ResponseCode.INSUFFICIENT_PRIVILEGES.getCode())
                .message(ResponseCode.INSUFFICIENT_PRIVILEGES.getMessage())
                .build();
    }
}
