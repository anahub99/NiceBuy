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
    private static final int EMAIL_ID = Menu.FIRST + 3;
    private static final int SMS_ID = Menu.FIRST + 4;
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
        fillData();
        //SortedList<String> sortedList = new SortedList(mList);
        registerForContextMenu(mList);

    }


    private void fillData () {
        Cursor c = mDbHelper.fetchAllProducts();
        startManagingCursor(c); // deprecated method, but still works
        String[] from = new String[] { ProductDbAdapter.KEY_TITLE };

        int[] to = new int[] { R.id.text1 };
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to); // deprecated, but works
        mList.setAdapter(notes); }


// Funcion para añadir las opciones del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createProduct();
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
        menu.add(Menu.NONE, EMAIL_ID, Menu.NONE, R.string.menu_email);
        menu.add(Menu.NONE, SMS_ID, Menu.NONE, R.string.menu_sms);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
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
            case EMAIL_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                send(info.id, EMAIL_ID);
                return true;
            case SMS_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                send(info.id, SMS_ID);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createProduct() {
        Intent i = new Intent(this, ProductEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editProduct(int position, long id) {

        Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(ProductDbAdapter.KEY_ROWID, id);

        //noinspection deprecation
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private void send(long mRowId, int type) {

        Cursor note = mDbHelper.fetchNote(mRowId);
        startManagingCursor(note);

        String mTitleText = (note.getString(
                note.getColumnIndexOrThrow(ProductDbAdapter.KEY_TITLE)));
        String mBodyText = (note.getString(
                note.getColumnIndexOrThrow(ProductDbAdapter.KEY_DESCRIPCION)));

        SendAbstractionImpl a;
        if (type == SMS_ID)
            a = new SendAbstractionImpl(this, "SMS");
        else
            a = new SendAbstractionImpl(this, "");

        a.send(mTitleText, mBodyText);

    }


    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent intent) {
        super.onActivityResult(requestCode , resultCode , intent);
        fillData ();
    }

}
