package es.unizar.eina.NiceBuy;

import android.widget.EditText;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import es.unizar.eina.NiceBuy.ProductDbAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class PedidoEdit extends AppCompatActivity{

    private EditText nombrCliente;
    private EditText telefonoCliente;
    private ListView mList;
    ArrayList<String> productosListados = new ArrayList<String>(1000); //Lista que contiene los productos existentes del usuario
    int nProductos = 0;
    EditText ed;
    List<EditText> allEds = new ArrayList<EditText>();
    public int cantidades [] = new int [1000];

    // identificador fila
    private Long mRowId;

    // gestion bbdd
    private ProductDbAdapter mDbHelper;


    private void obtenerProductos(){
        // Comprobar aqui lo del segundo parametro de fetchAllProduct si pasarle el true asi
        Cursor notas = mDbHelper.fetchAllProducts(ProductDbAdapter.OrdenarPor.na, true);
        startManagingCursor(notas);
        // CAmpos a mostrar
        String[] camposSHow = new String[]{ProductDbAdapter.KEY_TITLE};
        //comprobar
        int[] hacia = new int[] {10000};

/*
        Cursor c = mDbHelper.fetchAllProducts(order, asc);
        startManagingCursor(c); // deprecated method, but still works
        String[] from = new String[] {
                ProductDbAdapter.KEY_TITLE,
                ProductDbAdapter.KEY_PESO,
                ProductDbAdapter.KEY_PRECIO
        };

        // Revisar esto para meter mas text
        int[] to = new int[] { R.id.text1 };
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to); // deprecated, but works
        mList.setAdapter(notes);

        */

    }

}
