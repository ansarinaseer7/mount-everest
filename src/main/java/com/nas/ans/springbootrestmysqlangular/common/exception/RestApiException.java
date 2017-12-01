package com.nas.ans.springbootrestmysqlangular.common.exception;

public class RestApiException {
	 private String errorMessage;

	    public RestApiException(String errorMessage){
	        this.errorMessage = errorMessage;
	    }

	    public String getErrorMessage() {
	        return errorMessage;
	    }
}
