package com.emergencias.main;

import com.emergencias.controller.EmergencyManager;

public class Main {
    public static void main(String[] args) {
        // creamos el manager y le damos caña
        EmergencyManager sistema = new EmergencyManager();
        sistema.startSystem();
    }
}