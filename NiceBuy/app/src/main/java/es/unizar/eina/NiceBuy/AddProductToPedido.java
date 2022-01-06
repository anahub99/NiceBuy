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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ListIterator;

@SuppressWarnings("ALL")
public class AddProductToPedido extends AppCompatActivity {

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int VER_PEDIDOS = Menu.FIRST + 2;

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
        setContentView(R.layout.add_product);

        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        // por defecto la ordenacion es en base al nomre
        order = ProductDbAdapter.OrdenarPor.na;
        asc = true;
        fillData(order,asc);
        //SortedList<String> sortedList = new SortedList(mList);

        Bundle extras = getIntent().getExtras();
        pedidoId = (extras != null) ?
                extras.getLong(ProductDbAdapter.KEY_ROWID_PEDIDOS) : null;

    }


    private void fillData (ProductDbAdapter.OrdenarPor order, boolean asc) {
        Cursor c = mDbHelper.fetchAllProducts(order, asc);
        startManagingCursor(c); // deprecated method, but still works
        String[] from = new String[] {
                ProductDbAdapter.KEY_TITLE,
                ProductDbAdapter.KEY_PESO,
                ProductDbAdapter.KEY_PRECIO,
                ProductDbAdapter.KEY_ROWID
        };

        // Revisar esto para meter mas text
        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3, R.id.button };
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.add_product_row, c, from, to); // deprecated, but works
        mList.setAdapter(notes);


    }


// Funcion para añadir las opciones del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, VER_PEDIDOS, Menu.NONE, "Ver los pedidos");

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createProduct();
                return true;
            case VER_PEDIDOS:
                System.out.println("aquihara");
                startActivity(new Intent(AddProductToPedido.this, PedidoPad.class));
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void createProduct() {
        selectedProduct = mList.getCount();
        Intent i = new Intent(this, ProductEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editProduct(int position, long id) {

        Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(ProductDbAdapter.KEY_ROWID, id);

        //noinspection deprecation
        startActivityForResult(i, ACTIVITY_EDIT);
    }



    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent intent) {
        super.onActivityResult(requestCode , resultCode , intent);
        fillData (order,asc);
        mList.setSelection(selectedProduct);
    }

    public void addProduct(View view) {
        // Do something in response to button click
        Button b = (Button) view;
        //System.out.println("text "+((String) b.getText()));
        Long productId = Long.parseLong((String) b.getText());
        System.out.println("pedidoId "+pedidoId);
        System.out.println("productId "+productId);
        mDbHelper.anyadirProductoAPedido(productId,pedidoId,1);
        System.out.println("text "+b.getText());

    }



}
