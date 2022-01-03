package es.unizar.eina.NiceBuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


@SuppressWarnings("ALL")
public class PedidoPad extends AppCompatActivity{
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    // Ordenar nombre ascendente
    private static final int PE_O_N_A = Menu.FIRST + 3;
    // Ordenar nombre descendente
    private static final int PE_O_N_A_D = Menu.FIRST + 4;
    // Ordenar weight ascendente
    private static final int PE_O_W_A = Menu.FIRST + 5;
    // Ordenar weight descendente
    private static final int PE_O_W_A_D = Menu.FIRST + 6;
    // Ordenar precio ascendente
    private static final int PE_O_P_A = Menu.FIRST + 7;
    // Ordenar precio descendente
    private static final int PE_O_P_A_D = Menu.FIRST + 8;
    // Ver los productos
    private static final int VER_PRODUCTOS = Menu.FIRST + 9;

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
        System.out.println("Oncreate, antesFilldata");
        fillData(order,asc);
        //SortedList<String> sortedList = new SortedList(mList);
        registerForContextMenu(mList);
        System.out.println("Oncreate, fin");

    }

    private void fillData (ProductDbAdapter.OrdenarPor order, boolean asc) {
        Cursor c = mDbHelper.FetchAllPedidos(order, asc);
        System.out.println("fillDaa, finFetchAllPedidos");
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
        mList.setAdapter(notes); }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            boolean result = super.onCreateOptionsMenu(menu);
            menu.add(Menu.NONE, INSERT_ID, Menu.NONE, "Add order");
            menu.add(Menu.NONE, PE_O_N_A, Menu.NONE, "Order by Name Asc.");
            menu.add(Menu.NONE, PE_O_N_A_D, Menu.NONE, "Order by Name Desc.");
            menu.add(Menu.NONE, PE_O_P_A, Menu.NONE, "Order by Price Asc.");
            menu.add(Menu.NONE, PE_O_P_A_D, Menu.NONE, "Order by Price Desc.");
            menu.add(Menu.NONE, PE_O_W_A, Menu.NONE, "Order by Weight Asc.");
            menu.add(Menu.NONE, PE_O_W_A_D, Menu.NONE, "Order by Weight Desc.");
            menu.add(Menu.NONE, VER_PRODUCTOS, Menu.NONE, "Ver los productos");


            return result;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case INSERT_ID:
                    createProduct();
                    return true;
                case PE_O_N_A:
                    order = ProductDbAdapter.OrdenarPor.na;
                    asc = true;
                    fillData(order,true);
                    return true;
                case PE_O_N_A_D:
                    order = ProductDbAdapter.OrdenarPor.na;
                    asc = false;
                    fillData(order,false);
                    return true;
                case PE_O_P_A:
                    order = ProductDbAdapter.OrdenarPor.pa;
                    asc = true;
                    fillData(order,true);
                    return true;
                case PE_O_P_A_D:
                    order = ProductDbAdapter.OrdenarPor.pa;
                    asc = false;
                    fillData(order,false);
                    return true;
                case PE_O_W_A:
                    order = ProductDbAdapter.OrdenarPor.wa;
                    asc = true;
                    fillData(order,true);
                    return true;
                case PE_O_W_A_D:
                    order = ProductDbAdapter.OrdenarPor.wa;
                    asc = false;
                    fillData(order,false);
                    return true;
                case VER_PRODUCTOS:
                    startActivity(new Intent(PedidoPad.this, ProductPad.class));
                    finish();
                    return true;


            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Eliminar Pedido");
            menu.add(Menu.NONE, EDIT_ID, Menu.NONE, "Editar pedido");
            // menu.add(Menu.NONE, EMAIL_ID, Menu.NONE, R.string.menu_email);
            // menu.add(Menu.NONE, SMS_ID, Menu.NONE, R.string.menu_sms);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            selectedProduct = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
            switch(item.getItemId()) {
                case DELETE_ID:
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    mDbHelper.deleteProduct(info.id);
                    fillData(order,asc);
                    return true;
                case EDIT_ID:
                    info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    editProduct(info.position, info.id);
                    return true;

            }
            return super.onContextItemSelected(item);
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


    }
