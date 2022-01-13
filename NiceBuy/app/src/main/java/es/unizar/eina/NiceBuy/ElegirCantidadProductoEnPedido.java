package es.unizar.eina.NiceBuy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class ElegirCantidadProductoEnPedido extends AppCompatActivity {

    // Ver los pedidos
    private static final int VER_PEDIDO = Menu.FIRST;

    int selectedProduct;
    ProductDbAdapter.OrdenarPor order;
    boolean asc;
    private ProductDbAdapter mDbHelper;
    private Long pedidoId;
    private Long productoId;
    private Long mRowId;
    private EditText numProductos;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("editar pedido");
        super.onCreate(savedInstanceState);
        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.elegir_cantidad);
        setTitle(R.string.quantity_product_pedido);

        numProductos = (EditText) findViewById(R.id.cantidad);
     
        // por defecto la ordenacion es en base al nomre
        order = ProductDbAdapter.OrdenarPor.na;
        asc = true;


        Bundle extras = getIntent().getExtras();
        pedidoId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(ProductDbAdapter.PERT_PEDIDO);
        if (pedidoId== null) {
            pedidoId = (extras != null) ?
                    extras.getLong(ProductDbAdapter.PERT_PEDIDO) : null;
        }

        productoId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(ProductDbAdapter.PERT_PRODUCTO);
        if (productoId== null) {
            productoId = (extras != null) ?
                    extras.getLong(ProductDbAdapter.PERT_PRODUCTO) : null;
        }


        populateField();
        //SortedList<String> sortedList = new SortedList(mList);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_OK);
                finish();
            }

        });

    }


    private void populateField() {

        Cursor productoEnPedido = mDbHelper.fetchProductEnPedido(productoId,pedidoId);
        if(productoEnPedido != null && productoEnPedido.getCount() > 0) {
            //noinspection deprecation
            startManagingCursor(productoEnPedido);

            numProductos.setText(productoEnPedido.getString(
                    productoEnPedido.getColumnIndexOrThrow(ProductDbAdapter.PERT_CANTIDAD)));
        }
    }


// Funcion para añadir las opciones del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, VER_PEDIDO, Menu.NONE, "Ver los pedidos");
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case VER_PEDIDO:
                System.out.println("aquihara");
                Intent i = new Intent(this, PedidoEdit.class);
                i.putExtra(ProductDbAdapter.KEY_ROWID_PEDIDOS, pedidoId);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ProductDbAdapter.PERT_PRODUCTO, productoId);
        outState.putSerializable(ProductDbAdapter.PERT_PEDIDO, pedidoId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateField();
    }

    private void saveState() {
        Integer cantidad = Integer.parseInt(numProductos.getText().toString());
        if(!mDbHelper.updateProductoEnPedido(productoId,pedidoId,cantidad)){
            mDbHelper.anyadirProductoAPedido(productoId,pedidoId,cantidad);
        }
        Double nuevoPrecio = mDbHelper.precioTotalPedido(pedidoId);
        Double nuevoPeso = mDbHelper.pesoTotalPedido(pedidoId);
        System.out.println("peso pedido"+nuevoPeso.toString());
        mDbHelper.pedidoUpdatePrecioPeso(nuevoPrecio,nuevoPeso,pedidoId);
    }



}
