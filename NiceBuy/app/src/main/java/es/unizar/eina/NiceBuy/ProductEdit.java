package es.unizar.eina.NiceBuy;

import android.widget.EditText;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


public class ProductEdit extends AppCompatActivity{
    private EditText titulo;
    private EditText descripcion;
    private EditText peso;
    private EditText precio;
    private TextView id;

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
        id = (TextView) findViewById(R.id.id);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(ProductDbAdapter.KEY_ROWID);
        if (mRowId== null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ?
                    extras.getLong(ProductDbAdapter.KEY_ROWID) : null;
        }
        populateFields();

        if(id.getText().toString() == ""){
            id.setText("***");
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) { //setResult(RESULT_OK);
                saveState();
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
            id.setText(mRowId.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProductDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = titulo.getText().toString();
        String body = descripcion.getText().toString();
        String pesoString = peso.getText().toString();
        String precioString = precio.getText().toString();
        Double w, p;

        try {
            w = Double.valueOf(pesoString);
            p = Double.valueOf(precioString);
        } catch (NumberFormatException nfe){
            Toast.makeText(this, "Hay campos no rellenados o inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!mDbHelper.checkProduct(title,body,w,p)){
            Toast.makeText(this, "Hay campos no rellenados o inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mDbHelper.sameName("productos", ProductDbAdapter.KEY_TITLE, title) && (mRowId == null)){
            Toast.makeText(this, "Ya existe un producto con el mismo nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mRowId == null) {
            long id = mDbHelper.createProduct(title, body, w, p);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateProduct(mRowId, title, body, w, p);
        }

        finish ();
    }

}
