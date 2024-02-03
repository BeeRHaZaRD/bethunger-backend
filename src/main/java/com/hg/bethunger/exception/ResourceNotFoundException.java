package com.hg.bethunger.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super("Ресурс не найден: " + resourceName + " с id " + id);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("Ресурс не найден: " + resourceName + " с " + fieldName + " = " + fieldValue.toString());
    }
}
