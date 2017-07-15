package mislugares.example.com.mislugares;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import bussiness.Lugar;

/**
 * Adaptador para ListView
 */

public class AdaptadorLugares extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre, direccion;
    ImageView foto;
    RatingBar valoracion;

    public AdaptadorLugares(Context contexto) {
        //Un  inflater es una herramienta que nos permite crear un objeto Java a partir de un fichero XML que lo describe.
        // En el ejemplo creamos un inflater para layouts.
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Construir un nuevo objeto View que será visualizado en la  posición position
     * @param posicion
     * @param vistaReciclada
     * @param padre
     * @return
     */
    public View getView(int posicion, View vistaReciclada,
                        ViewGroup padre) {

        //Obtenemos el lugar para esa posición
        //Lugar lugar = MainActivity.lugares.elemento(posicion);
        Lugar lugar = MainActivity.lugares.elementoPorIndex(posicion);

        //Para la primera llamada a getView(), este parámetro será nulo y tendremos que crear
        // una nueva vista e inflarla con el inflater desde un XML (este proceso puede ser algo lento).
        //Pero para las siguientes llamadas, este parámetro contendrá la vista devuelta por nosotros en la llamada anterior,
        // para esta posición. De esta forma ya no será necesario crearla desde cero y solo tendremos que modificar sus características y devolverla.
        // El resto del método se utiliza para actualizar cada campo, según el lugar a representar.
        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.elemento_lista, null);
        }

        //Obtenemos todas las vistas en objetos
        nombre = (TextView) vistaReciclada.findViewById(R.id.nombre);
        direccion = (TextView) vistaReciclada.findViewById(R.id.direccion);
        foto = (ImageView) vistaReciclada.findViewById(R.id.foto);
        valoracion = (RatingBar) vistaReciclada.findViewById(R.id.valoracion);
        //Seteamos nombre
        nombre.setText(lugar.getNombre());
        //Seteamos dirección
        direccion.setText(lugar.getDireccion());
        int id = R.drawable.otros;
        //Vemos que dibujo mostrar en base al tipo de lugar
        switch (lugar.getTipo()) {
            case RESTAURANTE:
                id = R.drawable.restaurante;
                break;
            case BAR:
                id = R.drawable.bar;
                break;
            case COPAS:
                id = R.drawable.copas;
                break;
            case ESPECTACULO:
                id = R.drawable.espectaculos;
                break;
            case HOTEL:
                id = R.drawable.hotel;
                break;
            case COMPRAS:
                id = R.drawable.compras;
                break;
            case EDUCACION:
                id = R.drawable.educacion;
                break;
            case DEPORTE:
                id = R.drawable.deporte;
                break;
            case NATURALEZA:
                id = R.drawable.naturaleza;
                break;
            case GASOLINERA:
                id = R.drawable.gasolinera;
                break;
        }
        //Seteamos imagen
        foto.setImageResource(id);
        foto.setScaleType(ImageView.ScaleType.FIT_END);
        //Seteamos rating
        valoracion.setRating(lugar.getValoracion());
        return vistaReciclada;
    }

    /**
     * Devuelve el número de elementos de la lista.
     * @return
     */
    public int getCount() {
        //return LugaresRAM.size();
        return MainActivity.lugares.size();
    }

    /**
     * Devuelve el elemento en una determinada posición de la lista.
     * @param posicion
     * @return
     */
    public Object getItem(int posicion) {
        //return LugaresRAM.elemento(posicion);
        return MainActivity.lugares.elementoPorIndex(posicion);
    }

    /**
     * Devuelve el identificador de fila de una determinada posición de la lista.
     * @param posicion
     * @return
     */
    public long getItemId(int posicion) {
        return MainActivity.lugares.elementoPorIndex(posicion).getId();
    }
}
