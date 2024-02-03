package com.hg.bethunger.exception;

public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String paramName) {
        super("Отсутствует параметр " + paramName);
    }
}
