package com.emergencias.model;

import com.google.gson.annotations.SerializedName;

public class CentroSalud {

    // @SerializedName le dice a Gson: "Busca 'codigo' o 'Codigo' o 'CODIGO' en el JSON"
    @SerializedName(value = "codigo", alternate = {"Codigo", "CODIGO", "id"})
    private String codigo;

    @SerializedName(value = "nombre", alternate = {"Nombre", "NOMBRE", "centro", "Centro"})
    private String nombre;

    @SerializedName(value = "direccion", alternate = {"Direccion", "DIRECCION"})
    private String direccion;

    @SerializedName(value = "latitud", alternate = {"Latitud", "LATITUD", "lat"})
    private double latitud;

    @SerializedName(value = "longitud", alternate = {"Longitud", "LONGITUD", "lon", "lng"})
    private double longitud;

    // Constructor vacío obligatorio
    public CentroSalud() {}

    public CentroSalud(String codigo, String nombre, String direccion, double latitud, double longitud) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }

    @Override
    public String toString() {
        // Si el nombre es null, mostramos aviso
        String nombreMostrar = (nombre != null) ? nombre : "SIN NOMBRE (Revisa el JSON)";
        return "🏥 " + nombreMostrar + "\n   📍 " + direccion + " (" + latitud + ", " + longitud + ")";
    }
}