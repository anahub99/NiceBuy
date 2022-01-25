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
            // Caso 1 - correcto
            resultado = p.createProduct("pr1", "primero", 10.0, 20.0);
            Log.d("Resultado - 1: ", String.valueOf(resultado));

            // Caso 2 - fallo vacio
            resultado = p.createProduct("", "primero", 10.0, 20.0);
            Log.d("Resultado - 2: ", String.valueOf(resultado));

            // Caso 3 - fallo null
            resultado = p.createProduct(null, "primero", 10.0, 20.0);
            Log.d("Resultado - 3: ", String.valueOf(resultado));

            // Caso 4 - fallo existe producto
            resultado = p.createProduct("pr1", "primero", 10.0, 20.0);
            Log.d("Resultado - 4: ", String.valueOf(resultado));

            // Caso 5 - fallo precio 0
            resultado = p.createProduct("pr2", "segundo", 10.0, 0.0);
            Log.d("Resultado - 5: ", String.valueOf(resultado));

            // Caso 6 - fallo peso 0
            resultado = p.createProduct("pr2", "segundo", 0.0, 20.0);
            Log.d("Resultado - 6: ", String.valueOf(resultado));

            // Caso 7 - fallo descripcion nula
            resultado = p.createProduct("pr2", null, 10.0, 20.0);
            Log.d("Resultado - 7: ", String.valueOf(resultado));


        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion creando producto. ");
        }
    }

    static public void updateProductTest(ProductDbAdapter p){
        try{
            Log.d("PRUEBAS DE EQUIVALENCIA","updateProduct()");
            boolean resultado;
            // Caso 1 - correcto
            resultado = p.updateProduct(2, "newNam", "newp2", 100.0, 200.0);
            Log.d("Resultado - 1: ", String.valueOf(resultado));

            // Caso 2 - fallo null
            resultado = p.updateProduct(2, null, "newp2", 100.0, 200.0);
            Log.d("Resultado - 2: ", String.valueOf(resultado));

            // Caso 3 - fallo vacio
            resultado = p.updateProduct(2, "", "newp2", 100.0, 200.0);
            Log.d("Resultado - 3: ", String.valueOf(resultado));

            // Caso 4 - fallo peso
            resultado = p.updateProduct(2, "newNam2", "newp2", 0.0, 200.0);
            Log.d("Resultado - 4: ", String.valueOf(resultado));

            // Caso 5 - fallo precop
            resultado = p.updateProduct(2, "newNam2", "newp2", 100.0, 0.0);
            Log.d("Resultado - 5: ", String.valueOf(resultado));

            // Caso 6 - fallo descripcion
            resultado = p.updateProduct(2, "newNam2", null, 100.0, 200.0);
            Log.d("Resultado - 6: ", String.valueOf(resultado));

            // Caso 7 - fallo nombre  existe
            resultado = p.updateProduct(2, "newNam", "newp2", 100.0, 200.0);
            Log.d("Resultado - 7: ", String.valueOf(resultado));

            // Caso 8- fallo no existe rowId
            resultado = p.updateProduct(5, "newNam2", "newp2", 100.0, 200.0);
            Log.d("Resultado - 8: ", String.valueOf(resultado));
        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion actualizando producto. ");
        }
    }

    static public void deleteProductTest(ProductDbAdapter p){
        try{
            Log.d("PRUEBAS DE EQUIVALENCIA","deleteProduct()");
            boolean resultado;
            // Caso 1 - correcto
            resultado = p.deleteProduct(2);
            Log.d("Resultado - 1: ", String.valueOf(resultado));

            // Caso 2 - Fallo no existe roducto
            resultado = p.deleteProduct(100);
            Log.d("Resultado - 2: ", String.valueOf(resultado));

        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion borrando producto. ");
        }
    }

    static public void volumeProductTest(ProductDbAdapter pr){
        boolean res;
        try{
            // Funcion que crea muchos productos
            Log.d("PRUEBA DE VOLUMEN", "Volumen");
            res = pr.manyProductTest(1000);
            Log.d("RESULTADO PRUEBA DE VOLUMEN", String.valueOf(res));
        }catch(Throwable e){
            System.out.println("Se ha producido una excepcion durante la prueba de volumen. ");
        }
    }

    static public void overloadProductTest(ProductDbAdapter p){
        String cadena = "estosondie";
        long rowid = 0;
        int limite = 50000000;
        try {
            while (cadena.length() < limite) {
                cadena += cadena;
                rowid = p.createProduct(cadena, "cadena", 100.0,200.0);
                Log.d("PRUEBA SOBRECARGA ", "Los caracteres añadidos son: " + cadena.length());
                if (rowid == -1) {
                    Log.d("PRUEBA SOBRECARGA", "Fallo, limite " + cadena.length());
                }

            }
        }catch(Throwable e){
            Log.d("PRUEBA SOBRECARGA", "Fallo " + cadena.length());
            Log.d("PRUEBA SOBRECARGA", "Expecion " + e.toString());
        }
    }


}
