package org.example.proyectofinal;

import java.util.HashSet;
import java.util.Set;

public class Vertice<T> {
    private final T id;
    private final Set<Arista<T>> aristasEntrantes;
    private final Set<Arista<T>> aristasSalientes;

    public Vertice(T id) {
        this.id = id;
        this.aristasEntrantes = new HashSet<>();
        this.aristasSalientes = new HashSet<>();
    }
    //interior del vertice
    public T getId() {
        return id;
    }
    //este metodo da todas las aristas que entran, juas juas
    public Set<Arista<T>> getAristasEntrantes() {
        return aristasEntrantes;
    }
    //este otro da aristas salientes
    public Set<Arista<T>> getAristasSalientes() {
        return aristasSalientes;
    }
    //con este le doy una arista donde el es el destino
    public void addAristaEntrante(Arista<T> arista) {
        aristasEntrantes.add(arista);
    }
    //y este otro donde la arista lo detecta como el origen
    public void addAristaSaliente(Arista<T> arista) {
        aristasSalientes.add(arista);
    }

    public int getGradoDeEntrada() {
        return aristasEntrantes.size();
    }

    public int getGradoDeSalida() {
        return aristasSalientes.size();
    }
}
