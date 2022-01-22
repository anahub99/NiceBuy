package es.unizar.eina.send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** Concrete implementor utilizando aplicacion por defecto de Android para gestionar mail. No funciona en el emulador si no se ha configurado previamente el mail */
public class WhatsAppImplementor implements SendImplementor{

   /** actividad desde la cual se abrira la actividad de gestión de correo */
   private Activity sourceActivity;

   /** Constructor
    * @param source actividad desde la cual se abrira la actividad de gestion de correo
    */
   public WhatsAppImplementor(Activity source){
	   setSourceActivity(source);
   }

   /**  Actualiza la actividad desde la cual se abrira la actividad de gestion de correo */
   public void setSourceActivity(Activity source) {
	   sourceActivity = source;
   }

   /**  Recupera la actividad desde la cual se abrira la actividad de gestion de correo */
   public Activity getSourceActivity(){
     return sourceActivity;
   }

   /**
    * Implementacion del metodo send utilizando la aplicacion de gestion de correo de Android
    * Solo se copia el asunto y el cuerpo
    * @param subject asunto
    * @param body cuerpo del mensaje
    */

   private boolean isAppInstalled(String packageName) {
       PackageManager pm = getSourceActivity().getPackageManager();
       boolean app_installed;
       try {
           pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
           app_installed = true;
       }
       catch (PackageManager.NameNotFoundException e) {
           app_installed = false;
       }
       return app_installed;
   }

   public void send (String subject, String body) {
       try {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");

            //con subject se pasa el teléfono destinatario
            String url = "https://api.whatsapp.com/send?phone=" + subject + "&text=" + URLEncoder.encode(body, "UTF-8");


           intent.setData(Uri.parse(url));
           Context c = sourceActivity;


            if (!isAppInstalled("com.whatsapp")) {
                Toast.makeText(c, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
                return;
            }

           if (intent.resolveActivity(c.getPackageManager()) != null) {
               sourceActivity.startActivity(intent);
           }else{
               Toast.makeText(c, "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show();
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
   }

}
