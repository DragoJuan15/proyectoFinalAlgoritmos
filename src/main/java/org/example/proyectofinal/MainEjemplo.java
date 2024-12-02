package org.example.proyectofinal;

public class MainEjemplo {
    public static void main(String[] args) {
        // Grafo 1: Acíclico
        GrafoDirigidoAciclico<String> grafo1 = new GrafoDirigidoAciclico<>();
        grafo1.agregarArista("A", "B");
        grafo1.agregarArista("A", "C");
        grafo1.agregarArista("B", "D");

        System.out.println("Grafo 1:");
        grafo1.imprimirGrafo();
        System.out.println("Orden Topológico Grafo 1:");
        try {
            System.out.println(grafo1.topologicalSort());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Grafo 2: Acíclico
        GrafoDirigidoAciclico<Integer> grafo2 = new GrafoDirigidoAciclico<>();
        grafo2.agregarArista(1, 2);
        grafo2.agregarArista(1, 3);
        grafo2.agregarArista(3, 4);
        grafo2.agregarArista(2, 4);

        System.out.println("Grafo 2:");
        grafo2.imprimirGrafo();
        System.out.println("Orden Topológico Grafo 2:");
        try {
            System.out.println(grafo2.topologicalSort());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Grafo 3: Acíclico
        GrafoDirigidoAciclico<Character> grafo3 = new GrafoDirigidoAciclico<>();
        grafo3.agregarArista('X', 'Y');
        grafo3.agregarArista('Y', 'Z');
        grafo3.agregarArista('X', 'Z');

        System.out.println("Grafo 3:");
        grafo3.imprimirGrafo();
        System.out.println("Orden Topológico Grafo 3:");
        try {
            System.out.println(grafo3.topologicalSort());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // Grafo 4: Cíclico
        GrafoDirigidoAciclico<String> grafo4 = new GrafoDirigidoAciclico<>();
        grafo4.agregarArista("P", "Q");
        grafo4.agregarArista("Q", "R");
        grafo4.agregarArista("R", "P"); // Ciclo: P → Q → R → P

        System.out.println("Grafo 4:");
        grafo4.imprimirGrafo();
        System.out.println("Orden Topológico Grafo 4:");
        try {
            System.out.println(grafo4.topologicalSort());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
