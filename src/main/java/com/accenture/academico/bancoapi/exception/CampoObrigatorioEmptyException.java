package com.accenture.academico.bancoapi.exception;

public class CampoObrigatorioEmptyException extends RuntimeException{

    public CampoObrigatorioEmptyException(String msg){
        super(msg);
    }

}
