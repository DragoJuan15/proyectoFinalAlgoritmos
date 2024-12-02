package org.example.proyectofinal;

import java.util.*;

public class OrdenTopologico<T> {
    private final GrafoDirigidoAciclico<T> grafo;
    private final List<T> ordenTopologico;

    public OrdenTopologico(GrafoDirigidoAciclico<T> grafo) {
        this.grafo = grafo;
        this.ordenTopologico = new ArrayList<>();
        if (!grafo.esAciclico()) {
            System.out.println("El grafo no es acíclico.");
            return;
        }
        ordenar();
    }
    // este metodo revisa el grado de los vertices, y los acomoda en una pila
    // y es con pilas ya que el orden topologico se apoya del Deep first search, alias DFS
    // el cual se apoya del usp de pilas
    private void ordenar() {
        Set<Vertice<T>> visitados = new HashSet<>();
        Stack<Vertice<T>> stack = new Stack<>();
        //ahora repito varias veces el acomodo de los verices
        for (Vertice<T> vertice : grafo.obtenerVertices()) {
            if (!visitados.contains(vertice)) {
                ordenarUtil(vertice, visitados, stack);
            }
        }
        //finalmente, el objeto de la clase agrega ya ordenado los elementos
        while (!stack.isEmpty()) {
            ordenTopologico.add(stack.pop().getId());
        }
    }
    //este metodo guarda en la pila en caso de no ser un vertice visitado
    private void ordenarUtil(Vertice<T> vertice, Set<Vertice<T>> visitados, Stack<Vertice<T>> stack) {
        visitados.add(vertice);
        for (Arista<T> arista : vertice.getAristasSalientes()) {
            Vertice<T> destino = arista.getDestino();
            if (!visitados.contains(destino)) {
                ordenarUtil(destino, visitados, stack);
            }
        }

        stack.push(vertice);
    }

    //finalmente, regreso el orden topologico con un stringBuilder
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ordenTopologico.size(); i++) {
            sb.append(ordenTopologico.get(i));
            if (i < ordenTopologico.size() - 1) {
                sb.append(" - ");
            }
        }
        return sb.toString();
    }

    // este metodo es para obtener el ordenamiento topologico como lista
    public List<T> obtenerOrdenTopologico() {
        return ordenTopologico;
    }
}
