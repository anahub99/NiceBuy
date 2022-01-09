package es.unizar.eina.NiceBuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public static final String KEY_ROWID_PEDIDOS = "_id";
    public static final String PE_KEY_TITLE = "nombre";
    public static final String PE_KEY_TEL = "telefono";
    public static final String PE_KEY_DATE = "fechaLimite";
    public static final String PE_KEY_PRICE = "precioPedido";
    public static final String PE_KEY_WEIGHT = "pesoPedido";

    public static final String PE_KEY_CANTIDAD = "cantidad";

    public static final String PERT_PRODUCTO = "_idProducto";
    public static final String PERT_PEDIDO = "_idPedidos";
    public static final String PERT_CANTIDAD = "cantidad";



    public enum OrdenarPor {
        na, pa, wa
    }


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_TABLE = "productos";
    private static final String DATABASE_TABLE_PEDIDOS = "pedidos";
    private static final String DATABASE_TABLE_PERTENENCIA = "pertenece";

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
    public long createProduct(String title, String descripcion, Double peso, Double precio) {
        if (!checkProduct(title, descripcion, peso, precio)){
            System.out.println("Error en los datos del producto");
            return -1;
        }
        else{
            if(totalProducts() < 1000){
                ContentValues initialValues = new ContentValues();
                initialValues.put(KEY_TITLE, title);
                initialValues.put(KEY_PESO, peso);
                initialValues.put(KEY_PRECIO, precio);
                initialValues.put(KEY_DESCRIPCION, descripcion);
                return mDb.insert(DATABASE_TABLE, null, initialValues);
            }
            else{
                System.out.println("Limte de productos alcanzado");
                return -1;
            }
        }
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
    public Cursor fetchPedido(long rowId) throws SQLException{

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_PEDIDOS, null,
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
    public boolean updateProduct(long rowId, String title, String descripcion, Double peso, Double precio) {
       if(!checkProduct(title, descripcion, peso, precio)){
           return false;
       }
       else{
           ContentValues args = new ContentValues();
           args.put(KEY_TITLE, title);
           args.put(KEY_PESO, peso);
           args.put(KEY_PRECIO, precio);
           args.put(KEY_DESCRIPCION, descripcion);


           return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
       }

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
        return mDb.query(DATABASE_TABLE_PEDIDOS, null
                , null, null, null, null, parametroAux);
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

    // Devuelve un entero correspondiente al numero total de productos
    public int totalProducts(){
        Cursor notesCursor = fetchAllProducts(ProductDbAdapter.OrdenarPor.na, true);
        int totalP = 0;
        for(notesCursor.moveToFirst(); !notesCursor.isAfterLast(); notesCursor.moveToNext()) {
            totalP++;
        }
        return totalP;
    }

    // Devuelve un entero correspondiente al numero total de pedidos
    public int totalOrders(){
        Cursor notesCursor = FetchAllPedidos(ProductDbAdapter.OrdenarPor.na, true);
        int cuenta = 0;
        for(notesCursor.moveToFirst(); !notesCursor.isAfterLast(); notesCursor.moveToNext()) {
            cuenta++;
        }
        return cuenta;
    }

    // Devuelve TRUE si y solo si los parametros de un producto son correctos y no vacios
    public boolean checkProduct(String title, String descripcion, Double peso, Double precio){
        boolean correcto = true;
        if (title == null || title.length() <= 0 ||
                descripcion == null || descripcion.length() <= 0 ||
                peso < 0.0 ||
                precio < 0.0) correcto = false;
        return correcto;

    }



    // Devuelve TRUE si y solo si los parametros de un pedido son correctos
    public boolean checkPedido(String nombre, String telefono, String fechaLimite){
        boolean correcto = true;
        if (nombre == null || nombre.length() <= 0 ||
                telefono == null || telefono.length() <= 0 ||
                fechaLimite == null || fechaLimite.length() <= 0) correcto = false;
        return correcto;
    }

    public long createOrder(String tituloPedido, ArrayList<String> productos, int cuantos[]) {
        if (totalOrders() > 99) {
            return -1;
        }

        if (tituloPedido == null || tituloPedido.length() == 0) {
            return -1;
        }

        ContentValues valores = new ContentValues();
        int identificador = 0;
        int precio = 0;
        int peso = 0;

        Cursor apuntador;




        valores.put(PE_KEY_TITLE, tituloPedido);
        valores.put(PE_KEY_WEIGHT, peso);
        valores.put(PE_KEY_PRICE, precio);
        long idRowOrder = mDb.insert(DATABASE_TABLE_PEDIDOS, null, valores);

        if(productos != null){
            // bucle para recorrer todos los productos
            for(int i = 0; i< productos.size(); i++){
                
            }


        }
        else{
            return idRowOrder;
        }

    return 10;

    }

    public long crearPedido(String nombre, String telefono, String fechaLimite){

        if (!checkPedido(nombre, telefono, fechaLimite)){
            System.out.println("Error en los datos del pedido");
            return -1;
        }
        else {
            if (totalOrders() < 200) {
                ContentValues initialValues = new ContentValues();
                initialValues.put(PE_KEY_TITLE, nombre);
                initialValues.put(PE_KEY_TEL, telefono);
                initialValues.put(PE_KEY_DATE, fechaLimite);
                initialValues.put(PE_KEY_PRICE, 0.0);
                initialValues.put(PE_KEY_WEIGHT, 0.0);
                return mDb.insert(DATABASE_TABLE_PEDIDOS, null, initialValues);
            } else {
                System.out.println("Limte de pedidos alcanzado");
                return -1;
            }
        }
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePedido(long rowId) {

        return ((mDb.delete(DATABASE_TABLE_PERTENENCIA, PERT_PEDIDO + "=" + rowId, null) > 0)
                & (mDb.delete(DATABASE_TABLE_PEDIDOS, KEY_ROWID + "=" + rowId, null) > 0));

    }


    public boolean updatePedido(String nombre, String telefono, String fechaLimite, long pedidoId){
        if (!checkPedido(nombre, telefono, fechaLimite)){
            return false;
        }
        else{
            ContentValues args = new ContentValues();
            args.put(PE_KEY_TITLE, nombre);
            args.put(PE_KEY_TEL, telefono);
            args.put(PE_KEY_DATE, fechaLimite);

            return mDb.update(DATABASE_TABLE_PEDIDOS, args, KEY_ROWID + "=" + pedidoId, null) > 0;
        }
    }


    private boolean checkPesoPrecio(Double precio, Double peso){
        if(precio == null || precio < 0 || peso == null || peso < 0) return false;
        else return true;
    }

    public boolean pedidoUpdatePrecioPeso(Double precio, Double peso, long pedidoId){
        if (!checkPesoPrecio(precio, peso)){
            return false;
        }
        else{
            ContentValues args = new ContentValues();
            args.put(PE_KEY_PRICE, precio);
            args.put(PE_KEY_WEIGHT, peso);
            return mDb.update(DATABASE_TABLE_PEDIDOS, args, KEY_ROWID + "=" + pedidoId, null) > 0;
        }
    }


    public long anyadirProductoAPedido(Long idProducto, Long idPedido, Integer cantidad) {
        if (cantidad == null || idProducto == null || idPedido == null || cantidad < 0){
            return -1;
        }else {
            System.out.println("param long " + idProducto);
            ContentValues initialValues = new ContentValues();
            initialValues.put(PERT_PRODUCTO, idProducto);
            initialValues.put(PERT_PEDIDO, idPedido);
            initialValues.put(PERT_CANTIDAD, cantidad);
            return mDb.insert(DATABASE_TABLE_PERTENENCIA, null, initialValues);
        }
    }

    public boolean updateProductoEnPedido(Long idProducto, Long idPedido, Integer cantidad) {
        if (cantidad == null || idProducto == null || idPedido == null || cantidad < 0){
            return false;
        }else{
            ContentValues args = new ContentValues();
            args.put(PERT_CANTIDAD, cantidad);
            boolean b  = mDb.update(DATABASE_TABLE_PERTENENCIA, args, PERT_PRODUCTO + "=? AND " + PERT_PEDIDO + "=?",
                    new String[]{String.valueOf(idProducto), String.valueOf(idPedido)}) > 0;
            System.out.print("update ");
            System.out.println(b);
            return b;

        }
    }

    public boolean deleteProductFromPedido(Long idProducto, Long idPedido){
        return (mDb.delete(DATABASE_TABLE_PERTENENCIA, PERT_PRODUCTO + "=? AND " + PERT_PEDIDO + "=?",
                new String[]{String.valueOf(idProducto), String.valueOf(idPedido)}) > 0);
    }

    public Cursor fetchProductosDePedido(Long idPedido, OrdenarPor parametro, boolean asc) {

        if(idPedido == null) return null;

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

        String seleccionarProductos = "SELECT * FROM pertenece, productos WHERE"
                + " productos._id=_idProducto AND _idPedidos=?"
                + " ORDER BY ?";
        System.out.println("seleccionar productos");
        return mDb.rawQuery(seleccionarProductos, new String[]{String.valueOf(idPedido), parametroAux});
    }


    public Cursor fetchProductEnPedido(Long idProducto, Long idPedido) {
        System.out.println("pedidoId "+String.valueOf(idPedido));
        System.out.println("productId "+String.valueOf(idProducto));
        Cursor mCursor = mDb.query(true, DATABASE_TABLE_PERTENENCIA, new String[]{PERT_CANTIDAD},
                        PERT_PRODUCTO + "=? AND " + PERT_PEDIDO + "=?", new String[]{String.valueOf(idProducto),
                                String.valueOf(idPedido)},
                        null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

        }
        return mCursor;
    }


    public Double precioTotalPedido(Long idPedido){

        //Cursor mCursor = mDb.rawQuery("select sum(totalprice) as total from " + TABLE_Users + ";", null);

        String query = "select sum(cantidad * precio) as total from productos, pertenece "
                + "where _idPedidos = ? AND _idProducto = productos._id";

        Cursor mCursor = mDb.rawQuery(query, new String[]{String.valueOf(idPedido)});
        if (mCursor != null) {
            mCursor.moveToFirst();
            return mCursor.getDouble(0);
        }
        return null;
    }

    public Double pesoTotalPedido(Long idPedido){

        //Cursor mCursor = mDb.rawQuery("select sum(totalprice) as total from " + TABLE_Users + ";", null);

        String query = "select sum(cantidad * peso) as total from productos, pertenece "
                + "where _idPedidos = ? AND _idProducto = productos._id";

        Cursor mCursor = mDb.rawQuery(query, new String[]{String.valueOf(idPedido)});
        if (mCursor != null) {
            mCursor.moveToFirst();
            return mCursor.getDouble(0);
        }
        return null;
    }

    // Funcion que crea numberProduct-1 productos
    // Devuelve TRUE si han sido todos creados correctamente, FALSE en caso contrario
    boolean manyProductTest(int numberProdcts){
        boolean resultadoFinal = true;
        long resultadoCreate = -1;

        int i = 1;
        while(i < numberProdcts){
            resultadoCreate = createProduct("Producto" + i, "Descripcion numero" + i, "10", "20");
            if(resultadoCreate == -1) resultadoFinal = false; break;
        }
        return resultadoFinal;
    }



}









