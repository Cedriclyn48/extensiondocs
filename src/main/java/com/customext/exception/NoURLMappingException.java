package com.customext.exception;

public class NoURLMappingException extends Exception{
    public NoURLMappingException() {
        super("No URL mapping for your RestAssured api path");
    }
}
