package com.emergencias.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EmergencyEvent {
    // formato para que la fecha salga bien
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String tipoEmergencia;
    private String ubicacion;
    private UserData datosUsuario; // aqui guardamos al usuario dentro del evento
    private LocalDateTime timestamp;

    public EmergencyEvent(String tipoEmergencia, String ubicacion, UserData datosUsuario) {
        // comprobamos que todo este relleno
        if (tipoEmergencia == null || ubicacion == null || datosUsuario == null) {
            throw new IllegalArgumentException("Faltan datos para crear el evento");
        }
        this.tipoEmergencia = tipoEmergencia;
        this.ubicacion = ubicacion;
        this.datosUsuario = datosUsuario;
        this.timestamp = LocalDateTime.now(); // pilla la hora actual
    }

    // getters normales
    public String getTipoEmergencia() { return tipoEmergencia; }
    public String getUbicacion() { return ubicacion; }
    public UserData getDatosUsuario() { return datosUsuario; }

    public String getTimestamp() {
        return timestamp.format(FORMATTER);
    }
    
    @Override
    public String toString() {
        // devolvemos un string con todo formateado para el log
        return "[ALERTA] " + getTimestamp() + " | Tipo: " + tipoEmergencia + 
               " | Lugar: " + ubicacion + " | " + datosUsuario.toString();
    }
}