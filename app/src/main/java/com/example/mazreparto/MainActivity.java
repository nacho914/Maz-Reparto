package com.example.mazreparto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Modelos.Pedidos;

public class MainActivity extends AppCompatActivity {

    public  String keyTrabajador;
    public TextView mPedidos;
    public TextView mCantidadPedidosFin;
    int iTotales=0;
    private ProgressDialog progressDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Pedidos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyTrabajador = getIntent().getStringExtra("KeyTrabajador");


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Cargando Datos");
        progressDialog.show();

        mPedidos=findViewById(R.id.mCantidadPedidos);
        mCantidadPedidosFin= findViewById(R.id.mCantidadPedidosFin);
        CargaTotales();

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

    public void CargaTotales()
    {
        ref.child("Activos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {

                        Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                        assert pedido != null;
                        if(pedido.TrabajadorKey.isEmpty() || pedido.TrabajadorKey.equals(keyTrabajador))
                            iTotales++;
                    }

                    mPedidos.setText(String.valueOf(iTotales));
                    iTotales=0;

                }
                else{
                    mPedidos.setText("0");
                }
                cargarFinalizados();
                //progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void cargarFinalizados()
    {
        ref.child("Finalizados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {

                        Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                        assert pedido != null;
                        if( pedido.TrabajadorKey.equals(keyTrabajador))
                            iTotales++;
                    }
                    mCantidadPedidosFin.setText(String.valueOf(iTotales));
                    iTotales=0;

                }
                else{
                    mCantidadPedidosFin.setText("0");
                }

                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    /*
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
    }*/
}