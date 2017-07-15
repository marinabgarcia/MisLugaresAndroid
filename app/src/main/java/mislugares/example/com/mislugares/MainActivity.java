package mislugares.example.com.mislugares;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import datos.LugaresBD;
import datos.LugaresRAM;

/**
 * Actividad principal, desde aca inicio app
 */
public class MainActivity extends AppCompatActivity {
    //public static LugaresRAM lugares = new LugaresRAM();
    public static LugaresBD lugares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Esta inicialización ya no puede hacerse en la declaración porque necesitamos conocer
        // el contexto que se pasa como parámetro.
        lugares = new LugaresBD(this);

        //Toolbar correspondiente a la AppBar de MaterialDesign
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Oculto título
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Oculto menús y titulo
        //getSupportActionBar().hide();

        //Botón flotante de Material Design
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                //Comenzamos creando un nuevo lugar en la base e datos cuyo identificaor va a ser _id.
                long _id = lugares.nuevo();
                //Lanzar la actividad EdicionLugarActivity para que el usuario rellene los datos del lugar
                Log.w("Creo un nuevo lugar", String.valueOf(_id));
                Intent i = new Intent(MainActivity.this,EdicionLugarActivity.class);
                i.putExtra("id", _id);
                startActivity(i);
            }
        });

        //Forma de indicarle a un botón que pasa ante un click usando método setOnClickListener
        //La otra forma es mediante el atributo onClick del xml
        //Acordarse que se esta utilizando clase interna anónima (new View.OnClickListener() {...})
        Button btnAcercaDe = (Button) findViewById(R.id.btnAcercaDe);
        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarAcercaDe(null);
            }
        });

        //Lo mismo para el botón salir
        Button btnSalir = (Button) findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla menú, agrega los items si la appbar está presente
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Maneja los clicks de la AppBar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Obtengo id del menú del botón clickeado
        int id = item.getItemId();

        //Lo analizo y en base a eso decido que hay que hacer
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.acercaDe) {
            lanzarAcercaDe(null);
            return true;
        }

        if (id == R.id.menu_buscar) {
            lanzarVistaLugar(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Lanza actividad AcercaDe
     * @param view
     */
    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }

    /**
     * Lanza actividad ListaLugares (la que tiene el ListView)
     * @param view
     */
    public void lanzarMostrarLugares(View view) {
        Intent i = new Intent(this, ListaLugares.class);
        startActivity(i);
    }

    /**
     * Muestra cuadro de diálogo y en base a lo ingresado lanza actividad VistaLugarActivity
     * (solo a fines de aprendizaje, podría llamarse también a ListaLugares (la que tiene el ListView))
     * @param view
     */
    public void lanzarVistaLugar(View view) {
        //Creo un nuevo campo de texto para visualizar en el cuadro de dialogo
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        //Creo un nuevo cuadro de dialogo
        new AlertDialog.Builder(this)
                .setTitle("Selección de lugar")
                .setMessage("indica su id:")
                .setView(entrada)
                //Indico que pasa cuando hago click en el botón OK
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        long id = Long.parseLong(entrada.getText().toString());
                        Intent i = new Intent(MainActivity.this,
                                VistaLugarActivity.class);
                        i.putExtra("id", id);
                        startActivity(i);
                    }
                })
                //Indico que pasa cuando hago click en el botón Cancelar
                .setNegativeButton("Cancelar", null)
                .show();

    }
}


