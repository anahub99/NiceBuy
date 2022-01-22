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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("ALL")
public class PedidoEdit extends AppCompatActivity {



    private static final int ADD_PRODUCT=0;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    //private static final int EDIT_ID = Menu.FIRST + 2;

    // Ver los pedidos
    private static final int VER_PEDIDOS = Menu.FIRST + 3;

    int selectedProduct;
    ProductDbAdapter.OrdenarPor order;
    boolean asc;
    private ProductDbAdapter mDbHelper;
    private ListView mList;
    private Long pedidoId;

    private EditText nombre;
    private EditText telefono;
    private EditText fecha;
    private TextView pesoPedido;
    private TextView precioPedido;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mDbHelper = new ProductDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.pedido_edit);
        setTitle(R.string.order_edit);

        nombre = (EditText) findViewById(R.id.title);
        telefono = (EditText) findViewById(R.id.telephone);
        fecha = (EditText) findViewById(R.id.date);
        pesoPedido = (TextView) findViewById(R.id.weight);
        precioPedido = (TextView) findViewById(R.id.price);


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
        populateFields();

        if(pedidoId == null) telefono.setText("34");

        //SortedList<String> sortedList = new SortedList(mList);
        registerForContextMenu(mList);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //setResult(RESULT_OK);
                //finish();
                saveState();
            }

        });

    }


    private void fillData () {
        if(pedidoId != null) {
            Cursor c = mDbHelper.fetchProductosDePedido(pedidoId, order, asc);
            startManagingCursor(c); // deprecated method, but still works
            String[] from = new String[]{
                    ProductDbAdapter.KEY_TITLE,
                    ProductDbAdapter.PERT_CANTIDAD,
                    ProductDbAdapter.KEY_PESO,
                    ProductDbAdapter.KEY_PRECIO
            };

            // Revisar esto para meter mas text
            int[] to = new int[]{ R.id.text1, R.id.text2, R.id.text3, R.id.text4 };
            SimpleCursorAdapter notes =
                    new SimpleCursorAdapter(this, R.layout.product_in_pedido, c, from, to); // deprecated, but works
            mList.setAdapter(notes);
        }
    }


    private void populateFields() {

        if(pedidoId != null) {
            Cursor pedido = mDbHelper.fetchPedido(pedidoId);
            if (pedido != null && pedido.getCount() > 0) {
                //noinspection deprecation
                startManagingCursor(pedido);

                nombre.setText(pedido.getString(
                        pedido.getColumnIndexOrThrow(ProductDbAdapter.PE_KEY_TITLE)));
                telefono.setText(pedido.getString(
                        pedido.getColumnIndexOrThrow(ProductDbAdapter.PE_KEY_TEL)));
                fecha.setText(pedido.getString(
                        pedido.getColumnIndexOrThrow(ProductDbAdapter.PE_KEY_DATE)));
                pesoPedido.setText(pedido.getString(
                        pedido.getColumnIndexOrThrow(ProductDbAdapter.PE_KEY_WEIGHT)));
                precioPedido.setText(pedido.getString(
                        pedido.getColumnIndexOrThrow(ProductDbAdapter.PE_KEY_PRICE)));
            }
        }

    }


// Funcion para añadir las opciones del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        if(pedidoId != null) menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.add_product_pedido);
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

                startActivity(new Intent(PedidoEdit.this, PedidoPad.class));
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
        //menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.change_quantity);
       // menu.add(Menu.NONE, EMAIL_ID, Menu.NONE, R.string.menu_email);
       // menu.add(Menu.NONE, SMS_ID, Menu.NONE, R.string.menu_sms);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        selectedProduct = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteProductFromPedido(info.id, pedidoId);
                fillData();
                return true;
            /*case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editProduct(info.position, info.id);
                return true;*/

        }
        return super.onContextItemSelected(item);
    }

    private void addProduct() {
        selectedProduct = mList.getCount();
        Intent i = new Intent(this, AddProductToPedido.class);
        i.putExtra(ProductDbAdapter.KEY_ROWID_PEDIDOS, pedidoId);
        startActivityForResult(i, ADD_PRODUCT);
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
        //saveState();
        outState.putSerializable(ProductDbAdapter.KEY_ROWID_PEDIDOS, pedidoId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
        fillData();
    }


    private void saveState() {
        String nameString = nombre.getText().toString();
        String telefonoString = telefono.getText().toString();
        String fechaString = fecha.getText().toString();

        if(!mDbHelper.checkPedido(nameString,telefonoString,fechaString)){
            Toast.makeText(this, "Hay campos no rellenados", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Integer tel1 = Integer.parseInt(telefonoString.substring(0,(telefonoString.length()/2)));
            Integer tel2 = Integer.parseInt(telefonoString.substring(telefonoString.length()/2));
        } catch (NumberFormatException nfe){
            Toast.makeText(this, "Número de teléfono incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        Date date;
        try {
            date = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            //fecha mal escrita
            e.printStackTrace();
            Toast.makeText(this, "Fecha mal escrita", Toast.LENGTH_SHORT).show();
            return;
        }

        if(date != null) System.out.println(date.toString());
        else System.out.println("date es null");
        if (pedidoId == null) {
            long id = mDbHelper.crearPedido(nameString, telefonoString, fechaString);
            if (id > 0) {
                pedidoId = id;
            }
        } else {
            mDbHelper.updatePedido(nameString, telefonoString, fechaString, pedidoId);
        }

        finish();


    }

    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent intent) {
        super.onActivityResult(requestCode , resultCode , intent);
        fillData();
        mList.setSelection(selectedProduct);
    }

}
