package org.example.proyectofinal;

import java.util.*;

public class GrafoDirigidoAciclico<T> {
    private final Map<T, Vertice<T>> vertices; // Mapa de los vertices
    private final Set<Arista<T>> aristas;    // Set de aristas del grafo

    public GrafoDirigidoAciclico() {
        this.vertices = new HashMap<>();
        this.aristas = new HashSet<>();
    }

    // con este metodo agrego un vertice al grafo
    public void agregarVertice(T id) {
        if (!vertices.containsKey(id)) {
            vertices.put(id, new Vertice<>(id));
        }
    }

    // y con este otro una arista al grafo
    public void agregarArista(T origenId, T destinoId) {
        // en caso de no existir algun vertice, los creo
        // y no hay problema ya que no permite repetidos
        // y T O D O gracias a que los guardo en un HASHMAP
        agregarVertice(origenId);
        agregarVertice(destinoId);

        // los verices los paso a otros vertices tipo T
        Vertice<T> origen = vertices.get(origenId);
        Vertice<T> destino = vertices.get(destinoId);

        // creo la arista
        Arista<T> arista = new Arista<>(origen, destino);
        aristas.add(arista);

        // Finalmente agrego la arista a ambos vertices
        origen.addAristaSaliente(arista);
        destino.addAristaEntrante(arista);
    }

    // ahora devuelvo un vertice por medio de su identificador
    public Vertice<T> obtenerVertice(T id) {
        return vertices.get(id);
    }
    // el regresador de aristas no existe ya que no tiene identificador
    // ademas de que solo sirven para unir los vertices
    // DX

    // con este metodo devuelvo todos los vertices del grafo
    public Collection<Vertice<T>> obtenerVertices() {
        return vertices.values();
    }

    // y con este todas las aristas del grafo
    public Set<Arista<T>> obtenerAristas() {
        return aristas;
    }

    // ahora imprimo el grafo
    public void imprimirGrafo() {
        for (Vertice<T> vertice : vertices.values()) {
            System.out.print(vertice.getId() + " → ");
            Set<Arista<T>> salientes = vertice.getAristasSalientes();
            for (Arista<T> arista : salientes) {
                System.out.print(arista.getDestino().getId() + " ");
            }
            System.out.println();
        }
    }

    // este metodo revisa si tiene ciclos
    public boolean esAciclico() {
        return !tieneCiclos();
    }

    // este metodo tambien, la razon por la que son dos es porque
    // me pedian un metodo llamado es aciclico, y si se pregunta
    // porque no puse el metodo para medir el grado
    // del vertice, fue porque no lei bien los metodos, pero aun
    // con ese inconveniente, pude hacer bien esto
    public boolean tieneCiclos() {
        Set<Vertice<T>> visitados = new HashSet<>();
        Stack<Vertice<T>> pila = new Stack<>();

        for (Vertice<T> vertice : vertices.values()) {
            if (!visitados.contains(vertice) && tieneCiclo(vertice, visitados, pila)) {
                return true;
            }
        }
        return false;
    }

    // este metodo apoya al metodo tieneCiclos, ya que en una pila guardo
    // los vertices, y en caso de ver que el vertice se repite o la arista
    // lleva al punto de inicio, significa que tiene ciclos, lol
    private boolean tieneCiclo(Vertice<T> vertice, Set<Vertice<T>> visitados, Stack<Vertice<T>> pila) {
        if (pila.contains(vertice)) {
            return true; // hay un ciclo
        }
        if (visitados.contains(vertice)) {
            return false; // ya se proceso este vértice
        }

        visitados.add(vertice);
        pila.push(vertice);

        for (Arista<T> arista : vertice.getAristasSalientes()) {
            if (tieneCiclo(arista.getDestino(), visitados, pila)) {
                return true;
            }
        }

        pila.pop();
        return false;
    }

    // metodo para realizar el ordenamiento topologico
    public String topologicalSort() {
        if (tieneCiclos()) {
            System.out.println("El grafo tiene ciclos, ni modo");
            return "El grafo tiene ciclos, ni modo";
        }

        List<T> ordenTopologico = new ArrayList<>();
        Set<Vertice<T>> visitados = new HashSet<>();
        Stack<Vertice<T>> stack = new Stack<>();

        // ahora recorro los vertices y uso el metodo de ordenamiento deep first search, ya que con el
        // puedo ordenar con pilas, y en este caso estoy usando pilas
        for (Vertice<T> vertice : vertices.values()) {
            if (!visitados.contains(vertice)) {
                topologicalSortUtil(vertice, visitados, stack);
            }
        }

        // ahora voy a extraer los vertices de la pila y voy a añadirlos a la lista
        while (!stack.isEmpty()) {
            ordenTopologico.add(stack.pop().getId());
        }

        //  finalmente, guardo los vertices que se ordenaron
        List<String> ordenComoString = new ArrayList<>();
        for (T elemento : ordenTopologico) {
            ordenComoString.add(elemento.toString());
        }

        return String.join(" - ", ordenComoString);
    }

    //con este metodo hago el deep first search, o DFS, para trabajar con el ordenamiento
    private void topologicalSortUtil(Vertice<T> vertice, Set<Vertice<T>> visitados, Stack<Vertice<T>> stack) {
        visitados.add(vertice);
        for (Arista<T> arista : vertice.getAristasSalientes()) {
            Vertice<T> destino = arista.getDestino();
            if (!visitados.contains(destino)) {
                topologicalSortUtil(destino, visitados, stack);
            }
        }
        stack.push(vertice);
    }

    // este metodo es para mostrar la estructura del grafo en forma de matriz
    public String mostrarEstructura() {
        StringBuilder sb = new StringBuilder();
        for (Vertice<T> vertice : vertices.values()) {
            sb.append(vertice.getId()).append(": ");
            for (Arista<T> arista : vertice.getAristasSalientes()) {
                sb.append(arista.getDestino().getId()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
