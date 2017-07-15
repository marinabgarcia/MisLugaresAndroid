package datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bussiness.GeoPunto;
import bussiness.Lugar;
import bussiness.TipoLugar;

public class LugaresBD extends SQLiteOpenHelper implements LugaresInterface {

    Context contexto;

    /**
     * El constructor de la clase se limita a llamar al constructor heredado con el perfil
     *
     * @param contexto
     */
    public LugaresBD(Context contexto) {
        /*
        contexto: Contexto usado para abrir o crear la base de datos.
        nombre: Nombre de la base de datos que se creará. En nuestro caso, “puntuaciones”.
        cursor: Se utiliza para crear un objeto de tipo cursor. En nuestro caso no lo necesitamos.
        version: Número de versión de la base de datos empezando desde 1. En el caso de que la base de datos actual tenga una versión más antigua se llamará a onUpgrade() para que actualice la base de datos.
         */
        super(contexto, "lugares", null, 1);
        Log.w("Crea BD", "Constructor BD");
        this.contexto = contexto;
    }

    /**
     * El método onCreate() se invocará cuando sea necesario crear la base de datos.
     * Como parámetro se nos pasa una instancia de la base de datos que se acaba de crear.
     * Este es el momento de crear las tablas que contendrán información.
     *
     * @param bd
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        /*
        En nuestra aplicación necesitamos solo la tabla lugares, que es creada por medio del comando SQL CREATE TABLE lugares…
        La primera columna tiene por nombre _id y será un entero usado como clave principal.
        Su valor será introducido automáticamente por el sistema, de forma que dos filas no tengan nunca el mismo valor de _id.
         */
        Log.w("Crea BD", "Crea BD");

        bd.execSQL("CREATE TABLE lugares (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "direccion TEXT, " +
                "longitud REAL, " +
                "latitud REAL, " +
                "tipo INTEGER, " +
                "foto TEXT, " +
                "telefono INTEGER, " +
                "url TEXT, " +
                "comentario TEXT, " +
                "fecha BIGINT, " +
                "valoracion REAL)");
        /*
        Las siguientes líneas introducen nuevas filas en la tabla utilizando el
        comando SQL INSERT INTO lugares VALUES ( , , … ).
         */
        bd.execSQL("INSERT INTO lugares VALUES (null, " +
                "'Escuela Politécnica Superior de Gandía', " +
                "'C/ Paranimf, 1 46730 Gandia (SPAIN)', -0.166093, 38.995656, " +
                TipoLugar.EDUCACION.ordinal() + ", '', 962849300, " +
                "'http://www.epsg.upv.es', " +
                "'Uno de los mejores lugares para formarse.', " +
                System.currentTimeMillis() + ", 3.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null, 'Al de siempre', " +
                "'P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)', " +
                " -0.190642, 38.925857, " + TipoLugar.BAR.ordinal() + ", '', " +
                "636472405, '', " + "'No te pierdas el arroz en calabaza.', " +
                System.currentTimeMillis() + ", 3.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null, 'androidcurso.com', " +
                "'ciberespacio', 0.0, 0.0," + TipoLugar.EDUCACION.ordinal() + ", '', " +
                "962849300, 'http://androidcurso.com', " +
                "'Amplia tus conocimientos sobre Android.', " +
                System.currentTimeMillis() + ", 5.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null,'Barranco del Infierno'," +
                "'Vía Verde del río Serpis. Villalonga (Valencia)', -0.295058, " +
                "38.867180, " + TipoLugar.NATURALEZA.ordinal() + ", '', 0, " +
                "'http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-" +
                "rio.html', 'Espectacular ruta para bici o andar', " +
                System.currentTimeMillis() + ", 4.0)");
        bd.execSQL("INSERT INTO lugares VALUES (null, 'La Vital', " +
                "'Avda. La Vital,0 46701 Gandia (Valencia)',-0.1720092,38.9705949," +
                TipoLugar.COMPRAS.ordinal() + ", '', 962881070, " +
                "'http://www.lavital.es', 'El típico centro comercial', " +
                System.currentTimeMillis() + ", 2.0)");
    }

    /**
     * Si más adelante, en una segunda versión de Mis LugaresRAM,
     * decidiéramos crear una nueva estructura para la base de datos, tendríamos que indicar
     * un número de versión superior, por ejemplo la 2. Cuando se ejecute el código sobre un sistema
     * que disponga de una base de datos con la versión 1, se invocará el método onUpgrade().
     * En él tendremos que escribir los comandos necesarios para transformar la antigua base de datos en la nueva,
     * tratando de conservar la información de la versión anterior.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }

    /**
     * Su finalidad es buscar el lugar correspondiente a un id y devolverlo
     *
     * @param id
     * @return
     */
    @Override
    public Lugar elemento(int id) {
        //para que corresponda con el valor devuelto en caso de no encontrarse el id
        Lugar lugar = null;
        //Este objeto nos permitirá hacer consultas en la base de datos
        SQLiteDatabase bd = getReadableDatabase();
        //consulta en la tabla lugares
        Cursor cursor = bd.rawQuery("SELECT * FROM lugares WHERE _id = " + id, null);
        //Cursor pase a la siguiente fila encontrada. Devuelve true si lo encuentra y false si no
        if (cursor.moveToNext()) {
            lugar = extraeLugar(cursor);
        }
        //Es importante cerrar lo antes posibles los cursores y bases de datos por tener mucho consumo de memoria
        cursor.close();
        bd.close();
        //El método termina devolviendo el lugar
        return lugar;
    }

    public Lugar elementoPorIndex(int posicion) {
        //para que corresponda con el valor devuelto en caso de no encontrarse el id
        Lugar lugar = null;
        //Este objeto nos permitirá hacer consultas en la base de datos
        SQLiteDatabase bd = getReadableDatabase();
        //consulta en la tabla lugares
        Cursor cursor = bd.rawQuery("SELECT * FROM lugares LIMIT 1 OFFSET " + posicion, null);
        //Cursor pase a la siguiente fila encontrada. Devuelve true si lo encuentra y false si no
        if (cursor.moveToNext()) {
            Log.w("LugaresBD", "cursor");
            lugar = extraeLugar(cursor);
        }
        //Es importante cerrar lo antes posibles los cursores y bases de datos por tener mucho consumo de memoria
        cursor.close();
        bd.close();
        //El método termina devolviendo el lugar
        return lugar;
    }

    /**
     * Su finalidad es reemplazar el lugar correspondiente al id indicado por un nuevo lugar.
     *
     * @param id
     * @param lugar
     */
    public void actualiza(int id, Lugar lugar) {
        SQLiteDatabase bd = getWritableDatabase();
        //Consulta a BD
        bd.execSQL("UPDATE lugares SET nombre = '" + lugar.getNombre() +
                "', direccion = '" + lugar.getDireccion() +
                "', longitud = " + lugar.getPosicion().getLongitud() +
                " , latitud = " + lugar.getPosicion().getLatitud() +
                " , tipo = " + lugar.getTipo().ordinal() +
                " , foto = '" + lugar.getFoto() +
                "', telefono = " + lugar.getTelefono() +
                " , url = '" + lugar.getUrl() +
                "', comentario = '" + lugar.getComentario() +
                "', fecha = " + lugar.getFecha() +
                " , valoracion = " + lugar.getValoracion() +
                " WHERE _id = " + id);
        bd.close();
    }

    @Override
    public int nuevo() {
        //Comenzamos inicializando el valor del _id a devolver a -1. De esta manera, si hay algún problema este será el valor devuelto.
        int _id = -1;
        //Luego se crea un nuevo objeto Lugar.
        // Si consultas el constructor de la clase, observarás que solo se inicializan posicion, tipo y fecha.
        // El resto de los valores serán una cadena vacía para String y 0 para valores numéricos.
        Lugar lugar = new Lugar();
        SQLiteDatabase bd = getWritableDatabase();
        bd.execSQL("INSERT INTO lugares (longitud, latitud, tipo, fecha) "+
                "VALUES ( " + lugar.getPosicion().getLongitud()+","+
                lugar.getPosicion().getLatitud()+", "+
                lugar.getTipo().ordinal()+", "+lugar.getFecha()+")");
        Cursor c = bd.rawQuery("SELECT _id FROM lugares WHERE fecha = " +
                lugar.getFecha(), null);
        if (c.moveToNext()){
            _id = c.getInt(0);
        }
        c.close();
        bd.close();
        return _id;
    }

    /**
     * Su finalidad es eliminar el lugar correspondiente al id indicado
     * @param id
     */
    public void borrar(int id) {
        SQLiteDatabase bd = getWritableDatabase();
        bd.execSQL("DELETE FROM lugares WHERE _id = " + id );
        bd.close();
    }

    /**
     * Método que crea un nuevo lugar con los datos de la posición actual de un Cursor
     *
     * @param cursor
     * @return
     */
    public static Lugar extraeLugar(Cursor cursor) {
        Lugar lugar = new Lugar();
        lugar.setId(cursor.getInt(0));
        lugar.setNombre(cursor.getString(1));
        lugar.setDireccion(cursor.getString(2));
        lugar.setPosicion(new GeoPunto(cursor.getDouble(3),
                cursor.getDouble(4)));
        lugar.setTipo(TipoLugar.values()[cursor.getInt(5)]);
        lugar.setFoto(cursor.getString(6));
        lugar.setTelefono(cursor.getInt(7));
        lugar.setUrl(cursor.getString(8));
        lugar.setComentario(cursor.getString(9));
        lugar.setFecha(cursor.getLong(10));
        lugar.setValoracion(cursor.getFloat(11));
        return lugar;
    }

    /**
     * Devuelve un cursor que contiene todos los datos de la tabla
     *
     * @return
     */
    public Cursor extraeCursor() {
        String consulta = "SELECT * FROM lugares";
        SQLiteDatabase bd = getReadableDatabase();
        return bd.rawQuery(consulta, null);
    }

    public int size() {
        String consulta = "SELECT count(*) FROM lugares";
        SQLiteDatabase bd = getReadableDatabase();
        Cursor cursor = bd.rawQuery(consulta, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }


}