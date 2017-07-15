package datos;

import bussiness.Lugar;

/**
 * Created by marinagarcia on 14/7/17.
 */

public interface LugaresInterface {
    Lugar elemento(int id);
    Lugar elementoPorIndex(int posicion);
    int nuevo();
    void borrar(int id);
    int size();
    void actualiza(int id, Lugar lugar);
}
