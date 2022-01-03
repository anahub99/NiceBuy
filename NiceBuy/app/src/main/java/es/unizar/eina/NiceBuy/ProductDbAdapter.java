package es.unizar.eina.NiceBuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import es.unizar.eina.NiceBuy.DatabaseHelper;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class ProductDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_PRECIO = "precio";
    public static final String KEY_PESO = "peso";
    public static final String KEY_ROWID = "_id";

    public static final String PE_KEY_TITLE = "nombrePedido";
    public static final String PE_KEY_PRICE = "precioPedido";
    public static final String PE_KEY_WEIGHT = "precioLista";

    public static final String PE_KEY_CANTIDAD = "cantidad";
    private static final String KEY_ROWID_PEDIDOS = "_id";

    public enum OrdenarPor {
        na, pa, wa
    }


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_TABLE = "productos";
    private static final String DATABASE_TABLE_PEDIDOS = "pedidos";
    private static final String DATABASE_TABLE_PERTENENCIA = "pertenece;";

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public ProductDbAdapter(Context ctx) {

        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public ProductDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param title the title of the note
     * @param descripcion the body of the note
     * @return rowId or -1 if failed
     */
    public long createProduct(String title, String descripcion, String peso, String precio) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_PESO, peso);
        initialValues.put(KEY_PRECIO, precio);
        initialValues.put(KEY_DESCRIPCION, descripcion);


        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }


   // public long createPedido()

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteProduct(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllProducts(OrdenarPor parametro, boolean asc) {
    String parametroAux = null;
    switch(parametro){
        //nombre ascendente
        case na:
            parametroAux = KEY_TITLE;
            if(asc) parametroAux = parametroAux+" ASC";
            else parametroAux = parametroAux+" DESC";
            break;
            //precio ascendente
        case pa:
            parametroAux = KEY_PRECIO;
            if(asc) parametroAux = parametroAux+" ASC";
            else parametroAux = parametroAux+" DESC";
            break;
            //peso ascendente
        case wa:
            parametroAux = KEY_PESO;
            if(asc) parametroAux = parametroAux+" ASC";
            else parametroAux = parametroAux+" DESC";
    }

        return mDb.query(DATABASE_TABLE, new String[] {
                KEY_ROWID,
                KEY_TITLE,
                KEY_DESCRIPCION,
                KEY_PESO,
                KEY_PRECIO,
        }, null, null, null, null, parametroAux);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchProduct(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID,
                                KEY_TITLE,
                                KEY_DESCRIPCION,
                        KEY_PRECIO,
                        KEY_PESO}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchPedidos(long rowId) throws SQLException{

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE_PEDIDOS, new String[]{KEY_ROWID_PEDIDOS,
                                PE_KEY_TITLE,
                                PE_KEY_PRICE,
                                PE_KEY_WEIGHT},
                        KEY_ROWID_PEDIDOS + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param descripcion value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateProduct(long rowId, String title, String descripcion, String peso, String precio) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_PESO, peso);
        args.put(KEY_PRECIO, precio);
        args.put(KEY_DESCRIPCION, descripcion);


        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }




    public Cursor FetchAllPedidos(OrdenarPor parametro, boolean asc) {
        System.out.println("hola entre");
        String parametroAux = null;
        switch(parametro){
            //nombre ascendente
            case na:
                parametroAux = PE_KEY_TITLE;
                if(asc) parametroAux = parametroAux+" ASC";
                else parametroAux = parametroAux+" DESC";
                System.out.println("caso na");
                break;
            //precio ascendente
            case pa:
                parametroAux = PE_KEY_PRICE;
                if(asc) parametroAux = parametroAux+" ASC";
                else parametroAux = parametroAux+" DESC";
                break;
            //peso ascendente
            case wa:
                parametroAux = PE_KEY_WEIGHT;
                if(asc) parametroAux = parametroAux+" ASC";
                else parametroAux = parametroAux+" DESC";
                break;
        }
        System.out.println("al final");
        return mDb.query(DATABASE_TABLE_PEDIDOS, new String[] {
                KEY_ROWID_PEDIDOS,
                PE_KEY_TITLE,
                PE_KEY_WEIGHT,
                PE_KEY_PRICE,
        }, null, null, null, null, parametroAux);
    }

    private int producCounter(int p[]){
        int counter = 0;
        for (int i = 0; i<p.length; i++){
            if (p[i] != 0){
                counter++;
            }
        }
        return counter;
    }



}