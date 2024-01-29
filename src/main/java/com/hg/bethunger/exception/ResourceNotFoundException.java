package com.hg.bethunger.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super("Could not find " + resourceName + " with id " + id);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("Could not find " + resourceName + " with " + fieldName + " = " + fieldValue.toString());
    }
}
