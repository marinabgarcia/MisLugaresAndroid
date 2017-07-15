package mislugares.example.com.mislugares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;

import bussiness.Lugar;

/**
 * Created by marinagarcia on 31/5/17.
 */


public class VistaLugarActivity extends AppCompatActivity {
    private long id;
    private Lugar lugar;
    private ImageView imageView;
    private Uri uriFoto;
    /*
    Desde la actividad VistaLugar llamamos a diferentes actividades y algunas de ellas nos tienen que devolver información.
    En estos casos llamamos a la actividad con startActivityForResult() pasándole un código que identifica la llamada.
    Cuando esta actividad termine, se llamará al método onActivityResult(),
    que nos indicará el mismo código usado en la llamada. Como vamos a hacerlo con tres actividades diferentes, hemos creado tres constantes, con los respectivos códigos de respuesta. Actuando de esta forma conseguimos un código más legible.
     */
    final static int RESULTADO_EDITAR = 1;
    final static int RESULTADO_GALERIA = 2;
    final static int RESULTADO_FOTO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        //Obtengo id que viene de la actividad principal por defecto -1
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        imageView = (ImageView) findViewById(R.id.foto);
        actualizarVistas();
    }

    /**
     * Método que actualiza todas las vistas según la información del lugar
     */
    private void actualizarVistas() {
        //Obtengo elemento correspondiente a ese id
        lugar = MainActivity.lugares.elemento((int) id);

        //Nombre
        TextView nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());

        //Logo
        ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());

        //Tipo
        TextView tipo = (TextView) findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());

        //Dirección
        if (lugar.getDireccion() != null && lugar.getDireccion().isEmpty()) {
            findViewById(R.id.direccion).setVisibility(View.GONE);
        } else {
            TextView direccion = (TextView) findViewById(R.id.direccion);
            direccion.setText(lugar.getDireccion());
        }

        //Telefono
        if (lugar.getTelefono() == 0) {
            findViewById(R.id.telefono).setVisibility(View.GONE);
        } else {
            TextView telefono = (TextView) findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }

        //Url
        if (lugar.getUrl() != null && lugar.getUrl().isEmpty()) {
            findViewById(R.id.url).setVisibility(View.GONE);
        } else {
            TextView url = (TextView) findViewById(R.id.url);
            url.setText(lugar.getUrl());
        }

        //Comentario
        if (lugar.getComentario() != null && lugar.getComentario().isEmpty()) {
            findViewById(R.id.comentario).setVisibility(View.GONE);
        } else {
            TextView comentario = (TextView) findViewById(R.id.comentario);
            comentario.setText(lugar.getComentario());
        }

        //Fecha
        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));

        //Hora
        TextView hora = (TextView) findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));

        //Rating
        RatingBar valoracion = (RatingBar) findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float valor, boolean fromUser) {
                        lugar.setValoracion(valor);
                        MainActivity.lugares.actualiza((int) id, lugar);
                    }
                });
        //Foto
        ponerFoto(imageView, lugar.getFoto());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
    }

    /**
     * Maneja los clicks de la AppBar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_compartir:
                //Intención implicita para compartir lugar
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, lugar.getNombre() + " - " + lugar.getUrl());
                startActivity(intent);
                return true;
            case R.id.accion_llegar:
                verMapa(null);
                return true;
            case R.id.accion_editar:
                //Intención explicita para lanzar la actividad EdicionLugarActivity
                Intent intentE = new Intent(this, EdicionLugarActivity.class);
                //Pongo información extra, el id del lugar
                intentE.putExtra("id", id);
                //Lanzo actividad esperando resultado, ese resultado vendrá con el código
                //RESULTADO_EDITAR, es para saber de que actividad vuelvo
                startActivityForResult(intentE, RESULTADO_EDITAR);
                return true;
            case R.id.accion_borrar:
                //Muestro cuadro de dialogo para confirmación
                new AlertDialog.Builder(this)
                        .setTitle("Borrado de lugar")
                        .setMessage("Esta seguro que desea borrar el lugar?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                MainActivity.lugares.borrar((int) id);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Indica que pasa cuando la actividad lanzada termina
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        //Pregunto de que actividad se esta volviendo, en este caso es obligatorio porque lanzo varias actividades
        //Vuelve de EdicionLugar
        if (requestCode == RESULTADO_EDITAR) {
            actualizarVistas();
            //Repintar
            findViewById(R.id.scrollView1).invalidate();
            //Vuelve de la galeria
        } else if (requestCode == RESULTADO_GALERIA && resultCode == Activity.RESULT_OK) {
            lugar.setFoto(data.getDataString());
            MainActivity.lugares.actualiza((int) id, lugar);
            ponerFoto(imageView, lugar.getFoto());
            //Vuelve de tomar un foto
        } else if (requestCode == RESULTADO_FOTO && resultCode == Activity.RESULT_OK
                && lugar != null && uriFoto != null) {
            lugar.setFoto(uriFoto.toString());
            MainActivity.lugares.actualiza((int) id, lugar);
            ponerFoto(imageView, lugar.getFoto());
        }
    }

    /**
     * Este método crea una intención indicando que queremos obtener contenido, que pueda ser abierto y que además sea de tipo imagen y de cualquier subtipo. Típicamente se abrirá la aplicación galería de fotos (u otra similar).
     * Como necesitamos una respuesta, usamos startActivityForResult() con el código adecuado.
     *
     * @param view
     */
    public void galeria(View view) {
        //Intención implicita para tomar contenido de la galeria
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //El tipo que necesito es una imagen
        intent.setType("image/*");
        //Lanzo intención esperando resultado
        startActivityForResult(intent, RESULTADO_GALERIA);
    }

    /**
     * Este método comienza verificando que nos han pasado algún archivo válido en URI. Si es así, lo asigna a imageView.
     * En caso contrario, se le asigna un Bitmap igual a null, que es equivalente a que no se represente ninguna imagen.
     *
     * @param imageView
     * @param uri
     */
    protected void ponerFoto(ImageView imageView, String uri) {
        if (uri != null) {
            imageView.setImageURI(Uri.parse(uri));
            //imageView.setImageBitmap(reduceBitmap(this, uri, 1024,      1024));
        } else {
            imageView.setImageBitmap(null);
        }
    }

    /**
     * Este método crea una intención indicando que queremos capturar una imagen desde el dispositivo.
     * Típicamente se abrirá la aplicación cámara de fotos.
     * A esta intención vamos a añadirle un extra con una URI al fichero donde queremos que se almacene la fotografía.
     * Para crear este fichero, se parte del directorio de almacenamiento usado por defecto para las fotografías
     * (en muchos dispositivos, “/sdcard/Pictures”), seguido del separador de carpetas (“/”),
     * luego “img_” y se añade un valor numérico. El método currentTimeMillis()
     * nos da el número de milisegundos transcurridos desde 1.970. Al dividir entre 1.000, tenemos el número de segundos.
     * El objetivo que se persigue es que, al crear un nuevo fichero, su nombre nunca coincida con uno anterior.
     * Finalmente se añade la extensión del fichero. En la última línea llamamos a startActivityForResult()
     * con el código adecuado.
     *
     * @param view
     */
    public void tomarFoto(View view) {
        //Intención implicita para tomar foto
        Intent intent = new Intent("MediaStore.ACTION_IMAGE_CAPTURE");
        uriFoto = Uri.fromFile(new File(Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "img_" + (System.currentTimeMillis() / 1000) + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
        startActivityForResult(intent, RESULTADO_FOTO);
    }


    /**
     * Cargar fotografías de forma eficiente
     *
     * @param contexto
     * @param uri
     * @param maxAncho
     * @param maxAlto
     * @return
     */
    public static Bitmap reduceBitmap(Context contexto, String uri,
                                      int maxAncho, int maxAlto) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(contexto.getContentResolver().openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(contexto.getContentResolver().openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso no encontrado", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;

        }
    }

    /**
     * Método para mostrar mapa
     *
     * @param view
     */
    public void verMapa(View view) {
        Uri uri;
        //Obtengo latitud y longitud del lugar
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if (lat != 0 || lon != 0) {
            //Si tengo latitud y longitud uso la uri correspondiente
            uri = Uri.parse("geo:" + lat + "," + lon);
        } else {
            //Si no tengo latitud y longitud uso la dirección usando la uri correspondiente
            uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }
        //Lanzo intención implicita
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Método para lanzar intención implicita para marcar número
     *
     * @param view
     */
    public void llamadaTelefono(View view) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lugar.getTelefono())));
    }

    /**
     * Método para lanzar intención implicita para visualizar página web
     *
     * @param view
     */
    public void pgWeb(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
    }
}
