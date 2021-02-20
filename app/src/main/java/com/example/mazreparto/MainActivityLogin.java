package com.example.mazreparto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import Modelos.Pedidos;
import Modelos.Usuarios;

public class MainActivityLogin extends AppCompatActivity {


    EditText usuario;
    EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Usuarios");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        usuario =findViewById(R.id.m_username);
        password =findViewById(R.id.m_password);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            obtenerKeyTrabajador(currentUser.getEmail());
        }
        else
        {
            progressDialog.dismiss();
        }

    }

    public void LoginUser(View view)
    {

        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        if(!usuario.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        verificarUsuario();

                    }
                    else {

                        progressDialog.dismiss();
                        mostrarDialogo("Autenticación","Favor de verificar sus datos");
                    }

                }
            });
        }
        else
        {
            progressDialog.dismiss();
            mostrarDialogo("Autenticación","Favor de ingresar sus datos");
        }

    }

    public  void verificarUsuario()
    {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean bCerrarSesion=true;
                for (DataSnapshot UsuarioSnapshot: dataSnapshot.getChildren()) {

                    Usuarios user = UsuarioSnapshot.getValue(Usuarios.class);

                    if(user.Correo.equals(usuario.getText().toString()) && user.IdTipoUsuario==0)
                    {
                        actualizarKeyCelular(UsuarioSnapshot.getKey());
                        bCerrarSesion=false;
                        break;
                    }
                }
                if(bCerrarSesion)
                {
                    progressDialog.dismiss();
                    mostrarDialogo("Autenticación","Favor de verificar que la aplicación instalada sea la correcta");
                    FirebaseAuth.getInstance().signOut();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                mostrarDialogo("Autenticación","Ocurrio el siguiente problema: "+error.toException());
                FirebaseAuth.getInstance().signOut();
            }
        });
    }


    public  void obtenerKeyTrabajador(String sCorreo)
    {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot UsuarioSnapshot: dataSnapshot.getChildren()) {

                    Usuarios user = UsuarioSnapshot.getValue(Usuarios.class);

                    if(user.Correo.equals(sCorreo) && user.IdTipoUsuario==0)
                    {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("KeyTrabajador", UsuarioSnapshot.getKey());
                        startActivity(intent);
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.dismiss();
                mostrarDialogo("Autenticación","Ocurrio el siguiente problema: "+error.toException());
                FirebaseAuth.getInstance().signOut();
            }
        });
    }

    public void actualizarKeyCelular(String key)
    {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    mostrarDialogo("Autenticación","Ocurrio el siguiente problema al registrar el token: "+task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                ref.child(key).child("keyNotificaciones").setValue(token);
                suscribirNotificaciones(key);

            }
        });

    }

    public void suscribirNotificaciones(String key)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("NotificacionesPedidos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Suscrito";
                if (!task.isSuccessful()) {
                    msg = "Fallamos";
                }

                progressDialog.dismiss();
                Intent intent = new Intent(MainActivityLogin.this, MainActivity.class);
                intent.putExtra("KeyTrabajador", key);
                startActivity(intent);
            }
        });;
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