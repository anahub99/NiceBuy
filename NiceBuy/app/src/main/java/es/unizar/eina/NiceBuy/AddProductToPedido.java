package es.unizar.eina.NiceBuy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class AddProductToPedido extends AppCompatActivity {

    private static final int ACTIVITY_ADD_PRODUCT=0;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int VER_PEDIDO = Menu.FIRST + 2;

    int selectedProduct;
    ProductDbAdapter.OrdenarPor order;
    boolean asc;
    private ProductDbAdapter mDbHelper;
    private ListView mList;
    private Long pedidoId;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.add_product);
        setTitle(R.string.add_product_pedido);

        mList = (ListView)findViewById(R.id.list);
        // por defecto la ordenacion es en base al nomre
        order = ProductDbAdapter.OrdenarPor.na;
        asc = true;
        fillData();

        Bundle extras = getIntent().getExtras();
        pedidoId = (extras != null) ?
                extras.getLong(ProductDbAdapter.KEY_ROWID_PEDIDOS) : null;

        Button backButton = (Button) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { setResult(RESULT_OK);
                finish ();
            }
        });

    }


    private void fillData () {
        Cursor c = mDbHelper.fetchAllProducts(order, asc);
        startManagingCursor(c);
        String[] from = new String[] {
                ProductDbAdapter.KEY_TITLE,
                ProductDbAdapter.KEY_PESO,
                ProductDbAdapter.KEY_PRECIO,
                ProductDbAdapter.KEY_ROWID
        };

        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.button };
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.add_product_row, c, from, to); // deprecated, but works
        mList.setAdapter(notes);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, VER_PEDIDO, Menu.NONE, "Ver pedido");
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case VER_PEDIDO:
                Intent i = new Intent(this, PedidoEdit.class);
                i.putExtra(ProductDbAdapter.KEY_ROWID_PEDIDOS, pedidoId);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent intent) {
        super.onActivityResult(requestCode , resultCode , intent);
        fillData ();
        mList.setSelection(selectedProduct);
    }

    public void addProduct(View view) {
        // Hacer al pulsar un botón
        Button b = (Button) view;
        Long productId = Long.parseLong((String) b.getText());

        Intent i = new Intent(this, ElegirCantidadProductoEnPedido.class);
        i.putExtra(ProductDbAdapter.PERT_PEDIDO, pedidoId);
        i.putExtra(ProductDbAdapter.PERT_PRODUCTO, productId);

        startActivityForResult(i, ACTIVITY_ADD_PRODUCT);


    }



}
