package com.emergencias.model;

public class Route {
    private String nombre;
    private String dificultad;
    private double puntuacionTotal;
    private int numeroVotos;


    public Route(String nombre, String dificultad) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.puntuacionTotal = 0.0;
        this.numeroVotos = 0;
    }


    public Route(String nombre, String dificultad, double puntuacionTotal, int numeroVotos) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.puntuacionTotal = puntuacionTotal;
        this.numeroVotos = numeroVotos;
    }

    public void addPuntuacion(double puntos) {
        if (puntos >= 0 && puntos <= 5) {
            this.puntuacionTotal += puntos;
            this.numeroVotos++;
        }
    }

    public double getNotaMedia() {
        if (numeroVotos == 0) return 0.0;
        return puntuacionTotal / numeroVotos;
    }

    public String getNombre() {
        return nombre;
    }

    // UT.06 - Método para preparar los datos antes de guardarlos en el .txt
    public String toFileString() {
        return nombre + ";" + dificultad + ";" + puntuacionTotal + ";" + numeroVotos;
    }

    @Override
    public String toString() {
        return nombre + " (Dificultad: " + dificultad + ") - Nota Media: " + String.format("%.1f", getNotaMedia()) + " ⭐ (" + numeroVotos + " votos)";
    }
}