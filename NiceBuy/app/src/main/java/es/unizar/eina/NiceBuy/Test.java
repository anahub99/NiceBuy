package es.unizar.eina.NiceBuy;

/* Esta clase no tiene funcionalidad alguna dentro del sistema NiceBuy,
* Su uso está limitiado unica y exclusivamente al desarollo de la apllicación.
* */

import android.util.Log;

public class Test {
    static public void createProductTest(ProductDbAdapter p){
        try{
            Log.d("PRUEBAS DE EQUIVALENCIA","createProduct()");
            long resultado;
            // Caso 1
            resultado = p.createProduct("pr1", "primero", "10", "20");
            Log.d("Resultado - 1: ", String.valueOf(resultado));

            // Caso 2
            resultado = p.createProduct("", "primero", "10", "20");
            Log.d("Resultado - 2: ", String.valueOf(resultado));

            // Caso 3
            resultado = p.createProduct(null, "primero", "10", "20");
            Log.d("Resultado - 3: ", String.valueOf(resultado));

            // Caso 4
            resultado = p.createProduct("pr1", "primero", "10", "20");
            Log.d("Resultado - 4: ", String.valueOf(resultado));

            // Caso 5
            resultado = p.createProduct("pr2", "segundo", "10", "0");
            Log.d("Resultado - 5: ", String.valueOf(resultado));

            // Caso 6
            resultado = p.createProduct("pr2", "segundo", "0", "20");
            Log.d("Resultado - 6: ", String.valueOf(resultado));

            // Caso 7
            resultado = p.createProduct("pr2", null, "10", "20");
            Log.d("Resultado - 7: ", String.valueOf(resultado));


        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion. ");
        }
    }

    static public void updateProductTest(ProductDbAdapter p){
        try{
            Log.d("PRUEBAS DE EQUIVALENCIA","updateProduct()");
            long resultado;
            // Caso 1
            resultado = p.createProduct("pr1", "primero", "10", "20");
            Log.d("Resultado - 1: ", String.valueOf(resultado));

            // Caso 1
            resultado = p.createProduct("pr1", "primero", "10", "20");
            Log.d("Resultado - 1: ", String.valueOf(resultado));
        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion. ");
        }
    }

    static public void deleteProductTest(){
        try{

        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion. ");
        }
    }

    static public void volumeProductTest(){
        try{

        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion. ");
        }
    }

    static public void overloadProductTest(){
        try{

        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion. ");
        }
    }
}
