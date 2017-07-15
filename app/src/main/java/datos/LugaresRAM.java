package datos;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bussiness.Lugar;
import bussiness.TipoLugar;

/**
 * Created by marinagarcia on 31/5/17.
 */

public class LugaresRAM implements LugaresInterface{
    public static List<Lugar> vectorLugares = ejemploLugares();

    public LugaresRAM(){
        vectorLugares = ejemploLugares();
    }

    public Lugar elemento(int id){
        return vectorLugares.get(id);
    }

    public Lugar elementoPorIndex(int posicion){
        return vectorLugares.get(posicion);
    }

    public int nuevo(){
        Lugar lugar = new Lugar();
        vectorLugares.add(lugar);
        int id = vectorLugares.size()-1;
        lugar.setId(id);
        return id;
    }

    public void actualiza(int id, Lugar lugar) {
    }

    public void borrar(int id){
        vectorLugares.remove(id);
    }

    public int size(){
        return vectorLugares.size();
    }

    public static ArrayList<Lugar> ejemploLugares(){
        ArrayList<Lugar> lugares = new ArrayList<Lugar>();
        Lugar lugar = new Lugar("Escuela Politécnica Superior de Gandía", "C/ Paranimf, 1 46730 Gandia (SPAIN)", -0.166093, 38.995656, TipoLugar.EDUCACION, 96284930, "http://www.epsg.upv.es", "Uno de los mejores lugares para formarse.", 3);
        lugar.setId(0);
        lugares.add(lugar);
        return lugares;
    }
}
