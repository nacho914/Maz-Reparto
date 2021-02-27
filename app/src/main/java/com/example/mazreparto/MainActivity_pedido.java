package com.example.mazreparto;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import Modelos.Pedidos;

public class MainActivity_pedido extends AppCompatActivity {

    String key;
    String keyTrabajador;
    TextView mNombreNego;
    TextView mNombreCliente;
    TextView mDireccion;
    TextView mPrecio;
    TextView mTelefono;
    TextView mTiempo;
    Button mActualiza;
    private ProgressDialog progressDialog;
    boolean bEsApartado = false;
    int iTipoPedido;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Pedidos");
    //DatabaseReference refFinalizados = database.getReference("Pedidos/Finalizados");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedido);

        Bundle extras = getIntent().getExtras();
        key = extras.getString("keyPedido");
        keyTrabajador = extras.getString("keyTrabajador");
        iTipoPedido =Integer.parseInt(extras.getString("tipoPedido"));

        mNombreNego=findViewById(R.id.mNombreNegocio);
        mNombreCliente=findViewById(R.id.mNombreCliente);
        mDireccion=findViewById(R.id.mDirección);
        mPrecio=findViewById(R.id.mPrecio);
        mTelefono=findViewById(R.id.mTelefono);
        mTiempo=findViewById(R.id.mTiempo);
        mActualiza=findViewById(R.id.mBtnApartarPedido);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        if(iTipoPedido==0)
        {
            ref.child("Activos").child(key).addValueEventListener(returnListener());
        }
        if(iTipoPedido == 1)
        {
            mActualiza.setText("Finalizar Pedido");
            bEsApartado=true;
            ref.child("Activos").child(key).addValueEventListener(returnListener());
        }
        if(iTipoPedido == 2) {
            mActualiza.setEnabled(false);
            mActualiza.setText("Pedido Finalizado");
            ref.child("Finalizados").child(key).addValueEventListener(returnListener());
        }


    }

    public ValueEventListener returnListener()
    {
        return new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    Pedidos pedido = dataSnapshot.getValue(Pedidos.class);
                    assert pedido != null;
                    mNombreNego.setText(pedido.NombreNegocio);
                    mNombreCliente.setText(pedido.NombreCliente);
                    mDireccion.setText(pedido.Direccion);
                    mPrecio.setText("$ " + pedido.Precio);
                    mTelefono.setText(String.valueOf(pedido.Telefono));
                    mTiempo.setText(HoraPedido(pedido));
                    progressDialog.dismiss();
                }
                else {
                    progressDialog.dismiss();
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        };
    }
    public void apartarPedido(View view)
    {
        if(bEsApartado)
        {
            ref.child("Activos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference NewUserPush = ref.child("Finalizados").push();
                Pedidos pedidos = snapshot.getValue(Pedidos.class);
                NewUserPush.setValue(pedidos);

                snapshot.getRef().removeValue();
                mActualiza.setEnabled(false);
                mActualiza.setText("Pedido Finalizado");
                mostrarDialogo("Finalizado","Haz marcado este pedido como concluido",true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }});
        }
        else {
            ref.child("Activos").child(key).child("TrabajadorKey").setValue(keyTrabajador);
            mActualiza.setEnabled(false);
            mostrarDialogo("Apartado","Haz apartado este pedido",false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String HoraPedido(Pedidos pedido)
    {
        LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(pedido.getTimestampCreatedLong()),
                TimeZone.getDefault().toZoneId()).plusMinutes(pedido.TiempoPedido);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return triggerTime.format(formatter);
    }

    public  void mostrarDialogo(String sTitulo, String sMensaje,Boolean bFinaliza)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(sTitulo);
        builder.setMessage(sMensaje);
        //builder.setPositiveButton("OK", null);
        if(bFinaliza)
        {
            builder.setNeutralButton("Entendido", (dialog, which) -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity_List.class);
                intent.putExtra("KeyTrabajador", keyTrabajador);
                startActivity(intent); });
        }
        else
        {
            builder.setNeutralButton("Entendido",null);
        }

        builder.setInverseBackgroundForced(true);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}