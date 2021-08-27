package com.cardinity.taskmanager.common;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Accessors(chain = true)
public class RestApiResponse {
	
	private int status;
    private String message = "";
    private boolean valid;
    private Object response;
    
    public static final RestApiResponse OK = new RestApiResponse(Boolean.TRUE, HttpStatus.OK.value(),
            HttpStatus.OK.getReasonPhrase());
    public static final RestApiResponse SUCCESS = new RestApiResponse(Boolean.TRUE, HttpStatus.OK.value(),
            HttpStatus.OK.getReasonPhrase()).setResponse(null);
     public static final RestApiResponse ERROR = new RestApiResponse(Boolean.FALSE,
            HttpStatus.EXPECTATION_FAILED.value(), HttpStatus.EXPECTATION_FAILED.getReasonPhrase()).setResponse(null);
    
    public RestApiResponse(boolean valid, int status, String message) {
        this.valid = valid;
        this.status = status;
        this.message = message;
    }

    public RestApiResponse(RestApiResponse sharedResponse, Object response) {
        this.valid = sharedResponse.isValid();
        this.status = sharedResponse.getStatus();
        this.message = sharedResponse.getMessage();
        this.response = response;
    }

    public RestApiResponse() {
    }

}
