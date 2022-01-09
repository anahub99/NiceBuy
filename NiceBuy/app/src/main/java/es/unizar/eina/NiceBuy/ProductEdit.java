package es.unizar.eina.NiceBuy;

import android.widget.EditText;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class ProductEdit extends AppCompatActivity{
    private EditText titulo;
    private EditText descripcion;
    private EditText peso;
    private EditText precio;

    // identificador fila
    private Long mRowId;

    // gestion bbdd
    private ProductDbAdapter mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.product_edit);
        setTitle(R.string.menu_edit);



        titulo = (EditText) findViewById(R.id.title);
        descripcion = (EditText) findViewById(R.id.descripcion);
        peso = (EditText) findViewById(R.id.peso);
        precio = (EditText) findViewById(R.id.precio);


        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(ProductDbAdapter.KEY_ROWID);
        if (mRowId== null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ?
                    extras.getLong(ProductDbAdapter.KEY_ROWID) : null;
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) { setResult(RESULT_OK);
                finish ();
            }

        });
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchProduct(mRowId);
            //noinspection deprecation
            startManagingCursor(note);

            titulo.setText(note.getString(
                    note.getColumnIndexOrThrow(ProductDbAdapter.KEY_TITLE)));
            descripcion.setText(note.getString(
                    note.getColumnIndexOrThrow(ProductDbAdapter.KEY_DESCRIPCION)));
            peso.setText(note.getString(
                    note.getColumnIndexOrThrow(ProductDbAdapter.KEY_PESO)));
            precio.setText(note.getString(
                    note.getColumnIndexOrThrow(ProductDbAdapter.KEY_PRECIO)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ProductDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = titulo.getText().toString();
        String body = descripcion.getText().toString();
        Double w = Double.valueOf(peso.getText().toString());
        Double p = Double.valueOf(precio.getText().toString());
        if (mRowId == null) {
            long id = mDbHelper.createProduct(title, body, w, p);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateProduct(mRowId, title, body, w, p);
        }
    }

}
