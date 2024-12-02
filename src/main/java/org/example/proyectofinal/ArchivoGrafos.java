package org.example.proyectofinal;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ArchivoGrafos<T> {
    private final String archivoRuta;

    public ArchivoGrafos(String archivoRuta) {
        this.archivoRuta = archivoRuta;
    }

    // este es el metodo mas importante, ya que aqui se guarda un grafo en el archivo luas luas
    public boolean guardarGrafo(GrafoDirigidoAciclico<T> grafo) {
        try {
            List<String> grafosExistentes = leerGrafosCompletos();
            String grafoSerializado = serializarGrafo(grafo);

            if (grafosExistentes.contains(grafoSerializado)) {
                return false; // El grafo ya existe en el archivo
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoRuta, true))) {
                writer.write(grafoSerializado + "\n");
                writer.write("Fecha y hora: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
                writer.write("---\n");
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ahora voy a cargar todos los grafos desde el archivo de texto txt, juas juas
    public List<GrafoDirigidoAciclico<T>> cargarGrafos() {
        List<GrafoDirigidoAciclico<T>> grafos = new ArrayList<>();
        try {
            List<String> contenido = leerArchivo();
            GrafoDirigidoAciclico<T> grafoActual = null;

            for (String linea : contenido) {
                if (linea.startsWith("---")) {
                    if (grafoActual != null) {
                        grafos.add(grafoActual);
                    }
                    grafoActual = null;
                } else if (!linea.startsWith("Fecha y hora:")) {
                    if (grafoActual == null) {
                        grafoActual = new GrafoDirigidoAciclico<>();
                    }
                    String[] partes = linea.split(" -> ");
                    T origen = (T) partes[0];
                    if (partes.length > 1 && !partes[1].isEmpty()) {
                        String[] destinos = partes[1].split(", ");

                        for (String destino : destinos) {
                            grafoActual.agregarArista(origen, (T) destino);
                        }
                    }
                }
            }
            if (grafoActual != null) {
                grafos.add(grafoActual);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grafos;
    }

    // con este metodo leo el contenido del archivo linea por linea
    private List<String> leerArchivo() throws IOException {
        List<String> contenido = new ArrayList<>();
        File archivo = new File(archivoRuta);
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoRuta))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    contenido.add(linea);
                }
            }
        }
        return contenido;
    }

    // con este metodo leo los grafos completos del archivo
    private List<String> leerGrafosCompletos() throws IOException {
        List<String> grafosCompletos = new ArrayList<>();
        List<String> contenido = leerArchivo();
        StringBuilder grafo = new StringBuilder();
        for (String linea : contenido) {
            if (linea.startsWith("---")) {
                grafosCompletos.add(grafo.toString().trim());
                grafo.setLength(0);
            } else if (!linea.startsWith("Fecha y hora:")) {
                grafo.append(linea).append("\n");
            }
        }
        if (!grafo.isEmpty()) {
            grafosCompletos.add(grafo.toString().trim());
        }
        return grafosCompletos;
    }

    // ahora voy a serializar un grafo en formato legible
    private String serializarGrafo(GrafoDirigidoAciclico<T> grafo) {
        StringBuilder sb = new StringBuilder();
        for (Vertice<T> vertice : grafo.obtenerVertices()) {
            sb.append(vertice.getId()).append(" -> ");
            List<String> destinos = new ArrayList<>();
            for (Arista<T> arista : vertice.getAristasSalientes()) {
                destinos.add(arista.getDestino().getId().toString()); // Convertir a String
            }
            sb.append(String.join(", ", destinos)).append("\n");
        }
        return sb.toString().trim();
    }





    // metodo para devolver todos los grafos guardados, aunque no muy bien que digamos
    // ya que no me dio tiempo para hacer que los lea como un grafo, sino como
    // un texto
    public String mostrarTodoElTexto() {
        StringBuilder sb = new StringBuilder();
        try {
            List<String> contenido = leerArchivo();
            for (String linea : contenido) {
                sb.append(linea).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
