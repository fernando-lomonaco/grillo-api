package br.com.grillo.exception;

public class PasswordException extends Exception {

    private static final long serialVersionUID = -4678364511821627582L;

    public PasswordException(String msg) {
        super(msg);
    }
}
