package org.example.proyectofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HelloController {

    private GrafoDirigidoAciclico<String> grafo;
    private final ArchivoGrafos archivoGrafos = new ArchivoGrafos("grafos.txt");

    @FXML
    private Pane canvasPane;
    @FXML
    private TextArea matrizAdyacencia, listaAdyacencia;
    @FXML
    private Label mensajeLabel;
    @FXML
    private TabPane tabPane; // Referencia al TabPane principal

    public HelloController() {
        grafo = new GrafoDirigidoAciclico<>();
    }

    @FXML
    public void agregarVertice() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Vértice");
        dialog.setHeaderText("Ingrese el identificador del vértice:");
        dialog.setContentText("Vértice:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> {
            grafo.agregarVertice(id);
            actualizarVista();
        });
    }

    @FXML
    public void agregarArista() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Agregar Arista");
        dialog.setHeaderText("Ingrese los vértices de origen y destino:");
        ButtonType okButton = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        TextField origenField = new TextField();
        TextField destinoField = new TextField();
        grid.addRow(0, new Label("Origen:"), origenField);
        grid.addRow(1, new Label("Destino:"), destinoField);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(button -> {
            if (button == okButton) {
                return new Pair<>(origenField.getText(), destinoField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            grafo.agregarArista(pair.getKey(), pair.getValue());
            actualizarVista();
        });
    }

    @FXML
    public void crearGrafoAleatorio() {
        grafo = new GrafoDirigidoAciclico<>();
        Random rand = new Random();
        String[] vertices = {"A", "B", "C", "D", "E"};
        for (String vertice : vertices) {
            grafo.agregarVertice(vertice);
        }
        for (int i = 0; i < 6; i++) {
            String origen = vertices[rand.nextInt(vertices.length)];
            String destino = vertices[rand.nextInt(vertices.length)];
            if (!origen.equals(destino)) {
                grafo.agregarArista(origen, destino);
            }
        }
        actualizarVista();
    }

    @FXML
    public void ordenarTopologicamente() {
        try {
            String orden = grafo.topologicalSort();
            mensajeLabel.setText("Orden Topológico: " + orden);
        } catch (IllegalStateException e) {
            mensajeLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void guardarGrafo() {
        if (archivoGrafos.guardarGrafo(grafo)) {
            mensajeLabel.setText("Grafo guardado exitosamente.");
        } else {
            mensajeLabel.setText("El grafo ya existe en el archivo.");
        }
    }

    @FXML
    public void cargarGrafos() {
        List<GrafoDirigidoAciclico<String>> grafos = archivoGrafos.cargarGrafos();
        mensajeLabel.setText("Se cargaron " + grafos.size() + " grafos.");

        // Crear una nueva pestaña para mostrar el historial de grafos
        Tab historialTab = new Tab("Historial de Grafos");
        TextArea historialArea = new TextArea();
        historialArea.setEditable(false);
        historialArea.setText(archivoGrafos.mostrarTodoElTexto());
        historialTab.setContent(historialArea);

        // Añadir la nueva pestaña al TabPane
        tabPane.getTabs().add(historialTab);
        tabPane.getSelectionModel().select(historialTab); // Seleccionar la pestaña nueva
    }

    @FXML
    public void borrarHistorial() {
        try (FileWriter writer = new FileWriter("grafos.txt", false)) {
            writer.write(""); // Sobrescribir el archivo con un contenido vacío
            mensajeLabel.setText("Historial borrado exitosamente.");
        } catch (IOException e) {
            mensajeLabel.setText("Error al borrar el historial.");
            e.printStackTrace();
        }

        // Remover la pestaña historial si existe
        tabPane.getTabs().removeIf(tab -> tab.getText().equals("Historial de Grafos"));
    }

    @FXML
    public void borrarGrafoActual() {
        grafo = new GrafoDirigidoAciclico<>();
        actualizarVista();
        mensajeLabel.setText("Grafo actual borrado.");
    }

    //este metodo fue para largo, pero su objetivo es dibujar el grafo como bien lo pidio
    private void actualizarVista() {
        matrizAdyacencia.setText(generarMatrizAdyacencia());
        listaAdyacencia.setText(grafo.mostrarEstructura());

        // limpio la pantalla antes de dibujar el grafo
        canvasPane.getChildren().clear();

        // creo un map para guardar los vertices, junto con sus posiciones
        Map<String, double[]> posiciones = new HashMap<>();
        int radio = 200; // radio del circulo del vertice
        int centroX = 300, centroY = 300; // hubicacion del canvas
        int numVertices = grafo.obtenerVertices().size();
        int index = 0;

        // ahora muevo los vertices graficos de tal forma que pueda hacer que las las lineas no se crucen
        // sobre un vertice que no es su destino
        for (Vertice<String> vertice : grafo.obtenerVertices()) {
            double angulo = 2 * Math.PI * index / numVertices;
            double x = centroX + radio * Math.cos(angulo);
            double y = centroY + radio * Math.sin(angulo);
            posiciones.put(vertice.getId(), new double[]{x, y});

            // ahora que tengo una buena posicion, creo el circulo
            Circle circulo = new Circle(x, y, 20);
            // le pongo un color al circulo y al texto
            circulo.setStyle("-fx-fill: lightblue; -fx-stroke: black;");

            Label etiqueta = new Label(vertice.getId());
            etiqueta.setLayoutX(x - 5); // con este otro metodo ajusto el vertice
            etiqueta.setLayoutY(y - 10);

            canvasPane.getChildren().addAll(circulo, etiqueta);
            index++;
        }

        // ahora tocan las aristas
        for (Arista<String> arista : grafo.obtenerAristas()) {
            // pongo las coordenadas del destino y origen
            double[] origen = posiciones.get(arista.getOrigen().getId());
            double[] destino = posiciones.get(arista.getDestino().getId());

            // creo y dibujo la linea
            Line linea = new Line(origen[0], origen[1], destino[0], destino[1]);
            linea.setStyle("-fx-stroke: black;");

            // y para indicar el destino, pongo una flecha
            double dx = destino[0] - origen[0];
            double dy = destino[1] - origen[1];
            double angulo = Math.atan2(dy, dx);
            double flechaX1 = destino[0] - 10 * Math.cos(angulo - Math.PI / 6);
            double flechaY1 = destino[1] - 10 * Math.sin(angulo - Math.PI / 6);
            double flechaX2 = destino[0] - 10 * Math.cos(angulo + Math.PI / 6);
            double flechaY2 = destino[1] - 10 * Math.sin(angulo + Math.PI / 6);

            Line flecha1 = new Line(destino[0], destino[1], flechaX1, flechaY1);
            Line flecha2 = new Line(destino[0], destino[1], flechaX2, flechaY2);

            canvasPane.getChildren().addAll(linea, flecha1, flecha2);
        }
    }

    // y para la matriz de adyacencia, uso un generador
    // para ello creo una matriz y un hashmap que me permita
    // no repetir
    private String generarMatrizAdyacencia() {
        //creo un arraylist de los vertices del grafo
        List<String> vertices = new ArrayList<>(grafo.obtenerVertices().stream().map(Vertice::getId).toList());
        //los acomodo en orden con el collections sort
        Collections.sort(vertices);
        //creo la matriz
        int n = vertices.size();
        int[][] matriz = new int[n][n];
        Map<String, Integer> indiceVertices = new HashMap<>();
        //pongo en cada posicion los vertices para la tabla
        for (int i = 0; i < n; i++) {
            indiceVertices.put(vertices.get(i), i);
        }
        // ahora cuento las aristas para meterlas a la matriz
        for (Arista<String> arista : grafo.obtenerAristas()) {
            int i = indiceVertices.get(arista.getOrigen().getId());
            int j = indiceVertices.get(arista.getDestino().getId());
            matriz[i][j] = 1;
        }
        // y ahora creo el texto basado en la matriz
        StringBuilder matrizAImprimir = new StringBuilder("   ");
        for (String vertice : vertices) {
            matrizAImprimir.append(vertice).append(" ");
        }
        matrizAImprimir.append("\n");
        for (int i = 0; i < n; i++) {
            matrizAImprimir.append(vertices.get(i)).append(": ");
            for (int j = 0; j < n; j++) {
                matrizAImprimir.append(matriz[i][j]).append(" ");
            }
            matrizAImprimir.append("\n");
        }
        return matrizAImprimir.toString();
    }
}
