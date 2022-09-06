package com.sistema.examenes.excepciones;

public class UsuarioNotFoundException extends Exception{

    public UsuarioNotFoundException(){
        super("El usuario con ese nombre no existe en la base de datos, vuelta a intentar!!");
    }

    public UsuarioNotFoundException(String mensaje){
        super(mensaje);
    }

}
