package mislugares.example.com.mislugares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import bussiness.Lugar;
import bussiness.TipoLugar;

/**
 * Actividad para modificar un lugar
 */

public class EdicionLugarActivity extends AppCompatActivity {
    private long id;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);

        //Obtengo id de la actividad principal
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);

        //Obtengo elemento correspondiente a ese id
        lugar = MainActivity.lugares.elemento((int) id);

        //Tipo
        tipo = (Spinner) findViewById(R.id.tipo);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        if(lugar.getTipo()!=null)
            tipo.setSelection(lugar.getTipo().ordinal());

        //Nombre
        nombre = (EditText) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());


        //Dirección
        direccion = (EditText) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());

        //Telefono
        telefono = (EditText) findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));

        //Url
        url = (EditText) findViewById(R.id.url);
        url.setText(lugar.getUrl());

        //Comentario
        comentario = (EditText) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    /**
     * Maneja los clicks de la AppBar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_guardar:
                //En el lugar guardo los cambios
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());

                //Actualizo lugar
                MainActivity.lugares.actualiza((int) id,lugar);

                //Devuelvo resultado OK a la actividad que la llamo
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;

            case R.id.accion_cancelar:
                //Verifico que algún campo este completo
                if(lugar.getDireccion()==null && lugar.getUrl()==null && lugar.getComentario()==null && lugar.getNombre()==null && lugar.getTelefono()==0)
                    MainActivity.lugares.borrar((int) id);
                //Directamente cierro la actividad y vuelve a la actividad que la llamo
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
