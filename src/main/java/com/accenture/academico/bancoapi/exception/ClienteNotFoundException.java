package com.accenture.academico.bancoapi.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String msg) {
        super(msg);
    }

}
