package com.emergencias.main;

import com.emergencias.controller.EmergencyManager;

public class Main {
    public static void main(String[] args) {
        // creamos el manager y deberia funcionar
        EmergencyManager sistema = new EmergencyManager();
        sistema.startSystem();
    }
}