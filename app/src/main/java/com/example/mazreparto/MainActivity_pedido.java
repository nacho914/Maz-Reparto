package com.example.mazreparto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import Modelos.Pedidos;

import static java.sql.Types.TIMESTAMP;

public class MainActivity_pedido extends AppCompatActivity {

    String key;
    TextView mKey;
    TextView mNombreNego;
    TextView mNombreCliente;
    TextView mDireccion;
    TextView mPrecio;
    TextView mTelefono;
    TextView mTiempo;
    Button mActualiza;
    private ProgressDialog progressDialog;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Pedidos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedido);

        key = getIntent().getStringExtra("keyPedido");
        mNombreNego=(TextView)findViewById(R.id.mNombreNegocio);
        mNombreCliente=(TextView)findViewById(R.id.mNombreCliente);
        mDireccion=(TextView)findViewById(R.id.mDirección);
        mPrecio=(TextView)findViewById(R.id.mPrecio);
        mTelefono=(TextView)findViewById(R.id.mTelefono);
        mTiempo=(TextView)findViewById(R.id.mTiempo);
        mActualiza=(Button)findViewById(R.id.mBtnApartarPedido);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                Pedidos pedido = dataSnapshot.child(key).getValue(Pedidos.class);
                mNombreNego.setText(pedido.NombreNegocio);
                mNombreCliente.setText(pedido.NombreCliente);
                mDireccion.setText(pedido.Direccion);
                mPrecio.setText("$ "+String.valueOf(pedido.Precio));
                mTelefono.setText(String.valueOf(pedido.Telefono));
                mTiempo.setText(HoraPedido(pedido));

                if(!pedido.TrabajadorKey.isEmpty())
                    mActualiza.setEnabled(false);
                else
                    mActualiza.setEnabled(true);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }

    public void apartarPedido(View view)
    {

        ref.child(key).child("TrabajadorKey").setValue("nana");
        Toast toast = Toast.makeText(getApplicationContext(),ServerValue.TIMESTAMP.toString(),Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public String HoraPedido(Pedidos pedido)
    {
        LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(pedido.getTimestampCreatedLong()),
                TimeZone.getDefault().toZoneId()).plusMinutes(pedido.TiempoPedido);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formatDateTime = triggerTime.format(formatter);

        return  formatDateTime;
    }
}