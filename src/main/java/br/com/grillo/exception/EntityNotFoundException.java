package br.com.grillo.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1027266129938562526L;

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
