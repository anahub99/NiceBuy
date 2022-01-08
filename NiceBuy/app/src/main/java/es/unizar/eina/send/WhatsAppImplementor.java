package es.unizar.eina.send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

/** Concrete implementor utilizando aplicacion por defecto de Android para gestionar mail. No funciona en el emulador si no se ha configurado previamente el mail */
public class WhatsAppImplementor implements SendImplementor{

   /** actividad desde la cual se abrira la actividad de gestión de correo */
   private Activity sourceActivity;
   private Context c;

   /** Constructor
    * @param source actividad desde la cual se abrira la actividad de gestion de correo
    */
   public WhatsAppImplementor(Activity source, Context ctx){
	   setSourceActivity(source);
	   c = ctx;
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
   public void send (String subject, String body) {
	// Por implementar
    Intent intent = new Intent(Intent.ACTION_SEND);

    intent.setType("text/plain");
    intent.setPackage("com.whatsapp");

    intent.putExtra(Intent.EXTRA_TEXT, body);

    if (intent.resolveActivity(c.getPackageManager()) == null) {
        Toast.makeText(c, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
        return;
    }

    getSourceActivity().startActivity(intent);
   }

}
