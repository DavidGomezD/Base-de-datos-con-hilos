package com.example.basededatosconhilos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basededatosconhilos.utilidades.utlidades;

public class RegistroUsuariosActivity extends AppCompatActivity {

    private EditText campoId, campoNombre, campoTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);

        campoId = (EditText) findViewById(R.id.editText);
        campoNombre = (EditText) findViewById(R.id.editText2);
        campoTelefono = (EditText) findViewById(R.id.editText3);
    }

    public void onClick(View view){
        registrarUsuarios();
    }

    public void onClick2(View view){
        Toast.makeText(getApplicationContext(),"Buscando...",Toast.LENGTH_SHORT).show();
        hilos();
    }

    private void registrarUsuarios() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios",null,1);

        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(utlidades.CAMPO_ID, campoId.getText().toString());
        values.put(utlidades.CAMPO_NOMBRE, campoNombre.getText().toString());
        values.put(utlidades.CAMPO_TELEFONO, campoTelefono.getText().toString());

        Long idResultante = db.insert(utlidades.TABLA_USUARIO, utlidades.CAMPO_ID, values);

        Toast.makeText(getApplicationContext(),"Id registro: "+idResultante,Toast.LENGTH_SHORT).show();

        db.close();
    }

    private void consultar(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_usuarios",null,1);

        SQLiteDatabase db = conn.getReadableDatabase();

        String[] parametros = {campoId.getText().toString()};
        String[] campos ={utlidades.CAMPO_NOMBRE, utlidades.CAMPO_TELEFONO};

        try {

            Cursor cursor = db.query(utlidades.TABLA_USUARIO,campos,utlidades.CAMPO_ID+"=?",parametros,null,null,null);
            cursor.moveToFirst();
            campoNombre.setText(cursor.getString(0));
            campoTelefono.setText(cursor.getString(1));
            cursor.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No hay datos",Toast.LENGTH_SHORT).show();
            limpiar();
        }

    }

    private void limpiar(){
        campoNombre.setText("");
        campoTelefono.setText("");
    }

    //Hilo principal
    private void hilos(){

        new Thread(new Runnable() { //creamos una nueva instancia hilo
            @Override
            public void run() {

                //proceso lento a realizar
                try {
                    //tiempo de hilo en milisegundos
                    Thread.sleep(3000);
                }catch (InterruptedException e){

                }

                //Se utiliza para ejecutar tareas en el hilo
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        consultar();
                    }
                });

            }
        }).start();//ejecuta el hilo
    }

}
