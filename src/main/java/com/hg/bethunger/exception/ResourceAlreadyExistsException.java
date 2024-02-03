package com.hg.bethunger.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resourceName, Long id) {
        super(resourceName + " с id " + id + " уже существует");
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
