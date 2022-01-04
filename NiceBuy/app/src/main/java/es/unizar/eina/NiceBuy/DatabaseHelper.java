package es.unizar.eina.NiceBuy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 3;

    private static final String TAG = "DatabaseHelper";

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table productos (_id integer primary key autoincrement, "
                    + "title text not null, descripcion text not null, precio text not null, peso text not null );";

    private static final String DATABASE_CREATE_PEDIDOS =
            "create table pedidos (_id integer primary key autoincrement, "
                    + "nombrePedido text not null, precioPedido double, pesoPedido double);";

    private static final String DATABASE_CREATE_PERTENENCIA =
            "create table pertenece (_idProducto integer, _idPedidos integer, cantidad integer not null, "
                    + "PRIMARY KEY (_idProducto, _idPedidos),"
                    + "FOREIGN KEY (_idProducto) REFERENCES productos (_id) ON DELETE CASCADE,"
                    + "FOREIGN KEY (_idPedidos) REFERENCES pedidos (_id) ON DELETE CASCADE);";



    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE_PEDIDOS);
        db.execSQL(DATABASE_CREATE_PERTENENCIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS pedidos");
        db.execSQL("DROP TABLE IF EXISTS pertenece");
        onCreate(db);
    }

}
