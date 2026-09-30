package main.java.com.transucv;

// Atributos para el registro y autenticacion 
public class Usuario 
{
        private String id;
        private String nombre;
        private String correo;
        private String contra;
        private Rol rol;

        public Usuario(String id, String nombre, String correo, String contra, Rol rol)
        {
            this.id = id;
            this.nombre = nombre;
            this.correo = (correo != null) ? correo.trim().toLowerCase() : ""; //Obtenemos correo, y le quitamos espacios y ponemos todo en minuscula.
            this.contra = contra;
            this.rol = rol;
        }

        public String getId() {return id;}
        public String getNombre() {return nombre;}
        public String getCorreo() {return correo;}
        public String getContra() {return contra;}
        public Rol getRol() {return rol;}
}