package com.emergencias.data;

import com.emergencias.model.CentroSalud;
import com.emergencias.model.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestorDatos {

    private static final String ARCHIVO_JSON = "CentrosdeSalud.json";
    private static final String ARCHIVO_RUTAS = "rutas.txt"; // UT06 - Fichero de rutas

    public List<CentroSalud> cargarCentros() {
        List<CentroSalud> lista = new ArrayList<>();
        Gson gson = new Gson();

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(ARCHIVO_JSON);
            if (is == null) {
                System.out.println("No se encuentra el archivo: " + ARCHIVO_JSON);
                return lista;
            }
            Reader reader = new InputStreamReader(is);
            Type tipoLista = new TypeToken<ArrayList<CentroSalud>>(){}.getType();
            lista = gson.fromJson(reader, tipoLista);
            System.out.println("Cargados " + lista.size() + " centros médicos.");
        } catch (Exception e) {
            System.out.println("Error leyendo JSON de centros.");
        }
        return lista;
    }


    public List<Route> cargarRutas() {
        List<Route> rutas = new ArrayList<>();
        File archivo = new File(ARCHIVO_RUTAS);


        if (!archivo.exists()) {
            System.out.println("Creando base de datos de rutas por defecto...");
            rutas.add(new Route("Ruta del Cares", "Media"));
            rutas.add(new Route("Pico Mulhacén", "Alta"));
            rutas.add(new Route("Sendero de los Sentidos", "Baja"));
            guardarRutas(rutas);
            return rutas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 4) {
                    rutas.add(new Route(datos[0], datos[1], Double.parseDouble(datos[2]), Integer.parseInt(datos[3])));
                }
            }
            System.out.println("Cargadas " + rutas.size() + " rutas de montaña.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de rutas: " + e.getMessage());
        }
        return rutas;
    }


    public void guardarRutas(List<Route> rutas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_RUTAS))) {
            for (Route ruta : rutas) {
                pw.println(ruta.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar las rutas: " + e.getMessage());
        }
    }
}