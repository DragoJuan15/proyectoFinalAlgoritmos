package org.example.proyectofinal;

public class Arista<T> {
    private final Vertice<T> origen;
    private final Vertice<T> destino;

    public Arista(Vertice<T> origen, Vertice<T> destino) {
        this.origen = origen;
        this.destino = destino;
    }

    //este metodo regresa su origen
    public Vertice<T> getOrigen() {
        return origen;
    }
    //y este otro su desitno
    public Vertice<T> getDestino() {
        return destino;
    }
}
