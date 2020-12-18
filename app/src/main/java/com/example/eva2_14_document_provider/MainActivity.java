package com.example.eva2_14_document_provider;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText datos;
    final int ABRIR_ARCHIVO = 100, GUARDAR_ARCHIVO = 200;
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datos = findViewById(R.id.datos);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ABRIR_ARCHIVO:
                if(resultCode == Activity.RESULT_OK) {
                    uri = data.getData();
                    Log.wtf("URI", uri.toString());
                    String sCade;
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);

                        while ((sCade = br.readLine()) != null) {
                            datos.append(sCade);
                            datos.append(("\n"));
                        }
                        br.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case GUARDAR_ARCHIVO:
                if(resultCode == Activity.RESULT_OK){
                    uri = data.getData();
                    Log.wtf("URI", uri.toString());

                    try{
                        String [] aCade = datos.getText().toString().split("\n");
                        OutputStream os = getContentResolver().openOutputStream(uri);
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        BufferedWriter bw = new BufferedWriter(osw);

                        for(int i = 0 ; i < aCade.length ; i++){
                            bw.append(aCade[i]);
                            bw.append("\n");
                        }
                        bw.close();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void abrir(View v){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "prueba.txt");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityForResult(intent, ABRIR_ARCHIVO);

    }

    public void guardar(View v){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "prueba.txt");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityForResult(intent, GUARDAR_ARCHIVO);
    }
}