package mislugares.example.com.mislugares;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

/**
 * Created by marinagarcia on 23/6/17.
 */

public class ListaLugares extends ListActivity{
    public AdaptadorLugares adaptador;
    final static int RESULTADO_EDITAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        //Instancio el adaptador para la list view
        adaptador= new AdaptadorLugares(this);
        //Seteo el adaptador que voy a usar
        setListAdapter(adaptador);
    }

    /**
     * Método que indica que pasa cuando se hace click en un elemento de la lista
     * @param listView
     * @param vista
     * @param position
     * @param id
     */
    @Override
    protected void onListItemClick(ListView listView, View vista, int
            position, long id) {
        super.onListItemClick(listView, vista, position, id);
        //Lanzo actividad VistaLugarActivity
        Intent i = new Intent(this, VistaLugarActivity.class);
        i.putExtra("id",id);
        //Indico que espero un resultado
        startActivityForResult(i,RESULTADO_EDITAR);
    }

    /**
     * Método que indica que pasa cuando la actividad iniciada términa
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        //Me fijo de que actividad volvió, es este caso solo lanzo una actividad podría no ser necesario
        if (requestCode == RESULTADO_EDITAR) {
           adaptador.notifyDataSetChanged();
        }
    }
}
