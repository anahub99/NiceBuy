package es.unizar.eina.NiceBuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import es.unizar.eina.send.SendAbstractionImpl;

@SuppressWarnings("ALL")
public class ProductPad extends AppCompatActivity {

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    // Ordenar nombre ascendente
    private static final int O_N_A = Menu.FIRST + 3;
    // Ordenar nombre descendente
    private static final int O_N_A_D = Menu.FIRST + 4;
    // Ordenar weight ascendente
    private static final int O_W_A = Menu.FIRST + 5;
    // Ordenar weight descendente
    private static final int O_W_A_D = Menu.FIRST + 6;
    // Ordenar precio ascendente
    private static final int O_P_A = Menu.FIRST + 7;
    // Ordenar precio descendente
    private static final int O_P_A_D = Menu.FIRST + 8;
    // Ver los pedidos
    private static final int VER_PEDIDOS = Menu.FIRST + 9;
    // Pruebas
    private static final int TESTS = Menu.FIRST + 10;

    int selectedProduct;
    ProductDbAdapter.OrdenarPor order;
    boolean asc;
    private ProductDbAdapter mDbHelper;
    private ListView mList;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_pad);

        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        // por defecto la ordenacion es en base al nomre
        order = ProductDbAdapter.OrdenarPor.na;
        asc = true;
        fillData();
        registerForContextMenu(mList);

    }

    private void createProduct() {
        selectedProduct = mList.getCount();
        Intent intento = new Intent(this, ProductEdit.class);
        startActivityForResult(intento, ACTIVITY_CREATE);
    }


    private void fillData () {
        Cursor c = mDbHelper.fetchAllProducts(order, asc);
        startManagingCursor(c); 
        String[] from = new String[] {
                ProductDbAdapter.KEY_TITLE,
                ProductDbAdapter.KEY_PESO,
                ProductDbAdapter.KEY_PRECIO
        };

        int[] to = new int[] { R.id.text1, R.id.text2, R.id.text3 };
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.product_row, c, from, to); 
        mList.setAdapter(notes); }


// Funcion para a??adir las opciones del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.product_insert);
        menu.add(Menu.NONE, O_N_A, Menu.NONE, R.string.order_name_asc);
        menu.add(Menu.NONE, O_N_A_D, Menu.NONE, R.string.order_name_desc);
        menu.add(Menu.NONE, O_P_A, Menu.NONE, R.string.order_price_asc);
        menu.add(Menu.NONE, O_P_A_D, Menu.NONE, R.string.order_price_desc);
        menu.add(Menu.NONE, O_W_A, Menu.NONE, R.string.order_weight_asc);
        menu.add(Menu.NONE, O_W_A_D, Menu.NONE, R.string.order_weight_desc);
        menu.add(Menu.NONE, TESTS, Menu.NONE, R.string.tests);
        menu.add(Menu.NONE, VER_PEDIDOS, Menu.NONE, R.string.ver_pedidos);

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createProduct();
                return true;
            case O_N_A:
                order = ProductDbAdapter.OrdenarPor.na;
                asc = true;
                fillData();
                return true;
            case O_N_A_D:
                order = ProductDbAdapter.OrdenarPor.na;
                asc = false;
                fillData();
                return true;
            case O_P_A:
                order = ProductDbAdapter.OrdenarPor.pa;
                asc = true;
                fillData();
                return true;
            case O_P_A_D:
                order = ProductDbAdapter.OrdenarPor.pa;
                asc = false;
                fillData();
                return true;
            case O_W_A:
                order = ProductDbAdapter.OrdenarPor.wa;
                asc = true;
                fillData();
                return true;
            case O_W_A_D:
                order = ProductDbAdapter.OrdenarPor.wa;
                asc = false;
                fillData();
                return true;
            case VER_PEDIDOS:
                startActivity(new Intent(ProductPad.this, PedidoPad.class));
                finish();
                return true;
            case TESTS:
                startActivity(new Intent(ProductPad.this, Pruebas.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    public boolean onContextItemSelected(MenuItem item) {
        selectedProduct = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteProduct(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editProduct(info.position, info.id);
                return true;

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);

        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit);
    }


    @Override
    protected void onActivityResult(int codigoSolicitud , int codigoResultado , Intent intento) {
        super.onActivityResult(codigoSolicitud , codigoResultado , intento);
        fillData ();
        mList.setSelection(selectedProduct);
    }

    protected void editProduct(int position, long identificador) {

        Intent intento = new Intent(this, ProductEdit.class);
        intento.putExtra(ProductDbAdapter.KEY_ROWID, identificador);

        startActivityForResult(intento, ACTIVITY_EDIT);
    }





}
