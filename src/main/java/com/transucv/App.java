package com.transucv;

import com.transucv.views.LoginView;
import com.transucv.services.AuthServicio;
import com.transucv.repositories.UsuarioRepository;

public class App 
{
    public static void main( String[] args )
    {
        try {
            UsuarioRepository usuarioRepository = new UsuarioRepository();
            AuthServicio authServicio = new AuthServicio(usuarioRepository);
            LoginView loginView = new LoginView(authServicio);
            loginView.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
        }
    }
}
