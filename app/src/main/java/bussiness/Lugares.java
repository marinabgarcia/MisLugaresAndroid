package bussiness;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marinagarcia on 31/5/17.
 */

public class Lugares {
    public static List<Lugar> vectorLugares = ejemploLugares();

    public Lugares(){
        vectorLugares = ejemploLugares();
    }

    public static Lugar elemento(int id){
        return vectorLugares.get(id);
    }

    public static void add(Lugar lugar){
        vectorLugares.add(lugar);
    }

    public static int nuevo(){
        Lugar lugar = new Lugar();
        vectorLugares.add(lugar);
        return vectorLugares.size()-1;
    }

    public static void borrar(int id){
        vectorLugares.remove(id);
    }

    public static int size(){
        return vectorLugares.size();
    }

    public static ArrayList<Lugar> ejemploLugares(){
        ArrayList<Lugar> lugares = new ArrayList<Lugar>();
        lugares.add(new Lugar("Escuela Politécnica Superior de Gandía", "C/ Paranimf, 1 46730 Gandia (SPAIN)", -0.166093, 38.995656, TipoLugar.EDUCACION, 96284930, "http://www.epsg.upv.es", "Uno de los mejores lugares para formarse.", 3));
        return lugares;
    }
}
