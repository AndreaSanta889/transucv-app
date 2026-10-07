package com.transucv.exceptions;

public class CredencialesInvalidasException extends Exception 
{
    public CredencialesInvalidasException() 
    {
        super("Credenciales inválidas, por favor intente nuevamente");
    }
}
