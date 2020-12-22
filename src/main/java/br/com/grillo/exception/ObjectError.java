package br.com.grillo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ObjectError {

    private String message;
    private String field;
    private Object parameter;
}