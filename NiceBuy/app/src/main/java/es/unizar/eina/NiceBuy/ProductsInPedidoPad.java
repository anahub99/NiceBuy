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
public class ProductsInPedidoPad extends AppCompatActivity {

    private EditText nombrePedido;

    private static final int ADD_PRODUCT=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    // Ver los pedidos
    private static final int VER_PEDIDOS = Menu.FIRST + 3;

    int selectedProduct;
    ProductDbAdapter.OrdenarPor order;
    boolean asc;
    private ProductDbAdapter mDbHelper;
    private ListView mList;
    private Long pedidoId;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("editar pedido");
        super.onCreate(savedInstanceState);
        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.pedido_edit);
        setTitle("Edit pedido");

        nombrePedido = (EditText) findViewById(R.id.title);
        mList = (ListView)findViewById(R.id.list);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        // por defecto la ordenacion es en base al nomre
        order = ProductDbAdapter.OrdenarPor.na;
        asc = true;
        pedidoId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(ProductDbAdapter.KEY_ROWID_PEDIDOS);
        if (pedidoId == null) {
            Bundle extras = getIntent().getExtras();
            pedidoId = (extras != null) ?
                    extras.getLong(ProductDbAdapter.KEY_ROWID_PEDIDOS) : null;
        }
        fillData();
        //SortedList<String> sortedList = new SortedList(mList);
        registerForContextMenu(mList);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_OK);
                finish();
            }

        });

    }


    private void fillData () {
        if(pedidoId != null) {
            Cursor c = mDbHelper.fetchProductosDePedido(pedidoId, order, asc);
            startManagingCursor(c); // deprecated method, but still works
            String[] from = new String[]{
                    ProductDbAdapter.KEY_TITLE,
                    ProductDbAdapter.KEY_PESO,
                    ProductDbAdapter.KEY_PRECIO
            };

            // Revisar esto para meter mas text
            int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3};
            SimpleCursorAdapter notes =
                    new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to); // deprecated, but works
            mList.setAdapter(notes);
        }
    }


// Funcion para añadir las opciones del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.add_product_pedido);
        menu.add(Menu.NONE, VER_PEDIDOS, Menu.NONE, "Ver los pedidos");


        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                addProduct();
                return true;
            case VER_PEDIDOS:
                System.out.println("aquihara");
                startActivity(new Intent(ProductsInPedidoPad.this, PedidoPad.class));
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit);
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
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editProduct(info.position, info.id);
                return true;

        }
        return super.onContextItemSelected(item);
    }

    private void addProduct() {
        selectedProduct = mList.getCount();
        Intent i = new Intent(this, AddProductToPedido.class);
        i.putExtra(ProductDbAdapter.KEY_ROWID_PEDIDOS, pedidoId);
        startActivityForResult(i, ADD_PRODUCT);
    }


    protected void editProduct(int position, long id) {

        Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(ProductDbAdapter.KEY_ROWID, id);

        //noinspection deprecation
        startActivityForResult(i, ACTIVITY_EDIT);
    }



    /*@Override
    protected void onActivityResult(int requestCode , int resultCode , Intent intent) {
        super.onActivityResult(requestCode , resultCode , intent);
        fillData ();
        mList.setSelection(selectedProduct);
    }*/


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(ProductDbAdapter.KEY_ROWID_PEDIDOS, pedidoId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pedidoId != null) {
            Cursor pedido = mDbHelper.fetchPedido(pedidoId);
            //noinspection deprecation
            startManagingCursor(pedido);

            nombrePedido.setText(pedido.getString(
                    pedido.getColumnIndexOrThrow(ProductDbAdapter.PE_KEY_TITLE)));

            fillData();
        }
    }

    private void saveState() {
        String name = nombrePedido.getText().toString();
        if (pedidoId == null) {
            long id = mDbHelper.crearPedido(name);
            if (id > 0) {
                pedidoId = id;
            }
        } else {
            mDbHelper.updatePedido(name, pedidoId);
        }
    }



}
