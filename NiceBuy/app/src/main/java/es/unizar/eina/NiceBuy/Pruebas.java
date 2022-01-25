package es.unizar.eina.NiceBuy;
/*
*
* Pantalla de pruebas, no realiza funciones de gestion en NiceBuy
*
* */
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class Pruebas extends AppCompatActivity {
    //Prueba crear
    private static final int CREATE_TEST = Menu.FIRST;
    //Prueba borrar
    private static final int DELETE_TEST = Menu.FIRST + 2;
    //Prueba actualizar
    private static final int UPDATE_TEST = Menu.FIRST + 3;
    //Prueba volumen objetos
    private static final int VOLUME_TEST = Menu.FIRST + 4;
    //Prueba sobrecarga
    private static final int OVERLOAD_TEST = Menu.FIRST + 5;
    // Volver al menu de ver productos
    private static final int VER_PRODUCTOS = Menu.FIRST + 6;

    private ProductDbAdapter prHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        prHelper = new ProductDbAdapter(this);
        prHelper.open();
    }

    // Menu de la pantalla
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean res = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, CREATE_TEST, Menu.NONE,"CREATE TEST");
        menu.add(Menu.NONE, DELETE_TEST, Menu.NONE,"DELETE TEST");
        menu.add(Menu.NONE, UPDATE_TEST, Menu.NONE,"UPDATE TEST");
        menu.add(Menu.NONE, VOLUME_TEST, Menu.NONE,"VOLUME TEST");
        menu.add(Menu.NONE, OVERLOAD_TEST, Menu.NONE,"OVERLOAD TEST");
        menu.add(Menu.NONE, VER_PRODUCTOS, Menu.NONE, R.string.ver_productos);
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Test test = new Test();
        switch (item.getItemId()) {
            case CREATE_TEST:
                test.createProductTest(prHelper);
                return true;
            case DELETE_TEST:
                test.deleteProductTest(prHelper);
                return true;
            case UPDATE_TEST:
                test.updateProductTest(prHelper);
                return true;
            case VOLUME_TEST:
                test.volumeProductTest(prHelper);
                return true;
            case OVERLOAD_TEST:
                test.overloadProductTest(prHelper);
                return true;
            case VER_PRODUCTOS:
                startActivity(new Intent(this, ProductPad.class));
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
