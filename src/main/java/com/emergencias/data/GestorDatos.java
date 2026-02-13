package com.emergencias.data;

import com.emergencias.model.CentroSalud;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestorDatos {

    // Nombre del archivo en la carpeta resources
    private static final String ARCHIVO_JSON = "CentrosdeSalud.json";

    public List<CentroSalud> cargarCentros() {
        List<CentroSalud> lista = new ArrayList<>();
        Gson gson = new Gson();

        try {
            // Busco el archivo en resources
            InputStream is = getClass().getClassLoader().getResourceAsStream(ARCHIVO_JSON);

            if (is == null) {
                System.out.println("Error: No encuentro el archivo " + ARCHIVO_JSON);
                return lista;
            }

            Reader reader = new InputStreamReader(is);

            // Defino el tipo lista para Gson
            Type tipoLista = new TypeToken<ArrayList<CentroSalud>>(){}.getType();

            // Leo los datos y los paso a la lista
            lista = gson.fromJson(reader, tipoLista);

            System.out.println("Datos cargados: " + lista.size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static void main(String[] args) {
        GestorDatos gestor = new GestorDatos();
        List<CentroSalud> centros = gestor.cargarCentros();

        // Muestro los datos para comprobar
        for (CentroSalud c : centros) {
            System.out.println(c);
        }
    }
}