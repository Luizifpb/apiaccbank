package com.accenture.academico.bancoapi.exception;

public class ContaCorrenteNotFoundException extends RuntimeException{
    public ContaCorrenteNotFoundException(String msg){
        super(msg);
    }
}
