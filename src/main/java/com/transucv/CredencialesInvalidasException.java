package main.java.com.transucv;

public class CredencialesInvalidasException extends Exception 
{
    public CredencialesInvalidasException() 
    {
        super("Credenciales inválidas, por favor intente nuevamente");
    }
}
