package com.example.mazreparto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Nono";
    public  String keyTrabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyTrabajador = getIntent().getStringExtra("KeyTrabajador");

        //mostrarDialogo("nana",keyTrabajador);

    }

    public void llamarActividad(View view) throws IOException {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
        @Override
        public void onComplete(@NonNull Task<String> task) {
            if (!task.isSuccessful()) {
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            String token = task.getResult();

            // Log and toast
            //String msg = getString(R.string.msg_token_fmt, token);
            Log.d(TAG, token);
            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
        }
    });

        /*double latitude=0;
        double longitude=0;
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        addresses = geocoder.getFromLocationName("Luis Donaldo Colosio #569 infonavit Jabalies, Mazatlán, Sinaloa, Mexico", 1);
        if(addresses.size() > 0) {
             latitude= addresses.get(0).getLatitude();
             longitude= addresses.get(0).getLongitude();
        }*/

        //Log.w("nana", "Coordenadas: "+String.valueOf(latitude)+" "+String.valueOf(longitude));
        //Intent intent = new Intent(this, EjemploActivity2.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }

    public void enviarLista(View view)
    {
        Intent intent = new Intent(this, MainActivity_List.class);
        intent.putExtra("KeyTrabajador", keyTrabajador);
        startActivity(intent);
    }

    public void cerrarSesion(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivityLogin.class);
        startActivity(intent);

    }

    public  void mostrarDialogo(String sTitulo, String sMensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sTitulo);
        builder.setMessage(sMensaje);
        //builder.setPositiveButton("OK", null);
        builder.setNeutralButton("Entendido",null);
        builder.setInverseBackgroundForced(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}