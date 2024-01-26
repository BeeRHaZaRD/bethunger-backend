package com.hg.bethunger.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resourceName, Long id) {
        super(resourceName + " with id " + id + " already exists");
    }
}
