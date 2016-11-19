package registro.purisimanes.org.registropurisima;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Scanner;
import java.util.jar.Manifest;

public class RegistroPurisima extends AppCompatActivity {

    private static final String RUTA = Environment.getExternalStorageDirectory().getAbsolutePath()+""+File.separator+"Android"+File.separator+"data"+File.separator+"alarms.xml";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200;

    EditText etNombre;
    EditText etApellidos;
    EditText etEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_purisima);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final RegistroPurisimaFragment fragment = (RegistroPurisimaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        this.etNombre = ((EditText)fragment.getView().findViewById(R.id.etNombre));
        this.etApellidos = ((EditText)fragment.getView().findViewById(R.id.etApellidos));
        this.etEmail = ((EditText)fragment.getView().findViewById(R.id.etEmail));

        String datos[]=null;
        if((datos=leeDatosXML())!=null){
            this.etNombre.setText(datos[0]);
            this.etApellidos.setText(datos[1]);
            this.etEmail.setText(datos[2]);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(fragment!=null){

                    String nombre = etNombre.getText().toString();
                    String apellidos = etApellidos.getText().toString();
                    String email = etEmail.getText().toString();

                    if(comprobarDatos(nombre, apellidos, email)){
                        //Escribir XML
                        if(escribeXML(nombre, apellidos, email)){
                            Date fecha = new Date();

                            ((TextView)fragment.getView().findViewById(R.id.tvResult)).setText("Datos guardados correctamente. "+fecha.toString());
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Fragmento es null", Toast.LENGTH_LONG).show();
                }
            }
        });

        requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE); // Habilitar permiso escritura a partir Android 6
    }



    public boolean comprobarDatos(String nombre, String apellidos, String email){
        boolean result = false;
        if(!nombre.trim().equals("")){
            if(!apellidos.trim().equals("")){
                if(!email.trim().equals("") && email.contains("@alu.lapurisimavalencia.com")){
                   result=true;
                }else{
                    Toast.makeText(getApplicationContext(),"ERROR: Email no puede ser vacío", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"ERROR: Apellidos no puede ser vacío", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"ERROR: Nombre no puede ser vacío", Toast.LENGTH_LONG).show();
        }
        return  result;
    }

    public boolean escribeXMLAnterior(String nombre, String apellidos, String email){
        boolean result = true;

        XmlSerializer serialiser = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serialiser.setOutput(writer);
            serialiser.startDocument(null, null);
            serialiser.startTag(null, "alumno");
            serialiser.attribute(null, "nombre", nombre);
            serialiser.attribute(null, "apellidos", apellidos);
            serialiser.attribute(null, "email", email);
            serialiser.endTag(null, "alumno");
            serialiser.endDocument();
            //String ruta = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SOTI/alumno.xml";
            String ruta = Environment.getExternalStorageDirectory().getAbsolutePath()+""+ File.separator+"alumno.xml";
            //Toast.makeText(getApplicationContext(),"Ruta: "+ruta, Toast.LENGTH_LONG).show();
            FileOutputStream fos = new FileOutputStream(new File(ruta));
            //FileOutputStream fos = openFileOutput("alumno.xml",Context.MODE_PRIVATE);
            fos.write(writer.toString().getBytes());
            fos.flush();
            fos.close();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"ERROR: Al escribir el XML. "+e, Toast.LENGTH_LONG).show();
            System.out.println("ERROR: " + e);
            result = false;
        }
        return result;
    }

    public boolean escribeXML(String nombre, String apellidos, String email){
        boolean result = true;

        StringWriter writer = new StringWriter();
        try {
            writer.append("<?xml version='1.0' ?>");
            writer.append("<alumno>");
            writer.append("<datos> ");
            writer.append("<nombre> "+nombre+" </nombre> ");
            writer.append("<apellidos> "+apellidos+" </apellidos> ");
            writer.append("<email> "+email+" </email>");
            writer.append("</datos>");
            writer.append("</alumno>");
            //String ruta = "/data/data/net.soti.mobicontrol.plus/conf.xml";
            //FileOutputStream fos = openFileOutput("alumno.xml",Context.MODE_PRIVATE);
            //String ruta = Environment.getExternalStorageDirectory().getAbsolutePath()+""+File.separator+"Android"+File.separator+"data"+File.separator+"alarms.xml";
            System.out.println(RUTA);
            FileOutputStream fos = new FileOutputStream(new File(RUTA));
            fos.write(writer.toString().getBytes());
            fos.flush();
            fos.close();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"ERROR: Al escribir el XML. "+e, Toast.LENGTH_LONG).show();
            System.out.println("ERROR: " + e);
            result = false;
        }
        return result;
    }

    public String[] leeDatosXML(){
        String datos[] = new String[3];
        File file = new File(RUTA);

        if(!file.exists() || !file.canRead())
            return null;

        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return null;
        }

        String token;

        while(sc.hasNext()){
            token = sc.next();
            if(token.equals("<nombre>")){
                datos[0]=sc.next();
            }else if(token.equals("<apellidos>")){
                datos[1]=sc.next();
                token = sc.next();
                if(!token.equals("</apellidos>")){
                    datos[1]=""+datos[1]+" "+token;
                }
            }else if(token.equals("<email>")){
                datos[2] = sc.next();
            }
        }

        sc.close();
        return datos;
    }

    // Habilitar permiso escritura a partir Android 6
    public void requestPermissions (Activity activity, String[] permissions, int requestCode) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(RegistroPurisima.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistroPurisima.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(RegistroPurisima.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        // Habilitar permiso escritura a partir Android 6
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
