package com.emergencias.model;

/**
 * Clase para guardar los datos del usuario que tiene la emergencia
 */
public class UserData {
    private String nombre;
    private String telefono;
    private String infoMedica; 

    public UserData(String nombre, String telefono, String infoMedica) {
        // validamos que no vengan vacios los campos importantes
        if (nombre == null || nombre.trim().isEmpty() || telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre y el telefono son obligatorios. revisa el user_data.txt");
        }
        this.nombre = nombre;
        this.telefono = telefono;
        this.infoMedica = infoMedica;
    }

    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getInfoMedica() { return infoMedica; }

    @Override
    public String toString() {
        return "Usuario: " + nombre + " (" + telefono + ") - Info: " + infoMedica;
    }
}