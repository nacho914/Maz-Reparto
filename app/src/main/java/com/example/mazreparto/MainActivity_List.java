package com.example.mazreparto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import Modelos.Pedidos;

public class MainActivity_List extends AppCompatActivity {

    List<list_element> elements;
    String keyTrabajador;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Pedidos");
    private ProgressDialog progressDialog;
    Spinner dropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Maz Reparto");
        progressDialog.setMessage("Verificando Datos");
        progressDialog.show();

        keyTrabajador = getIntent().getStringExtra("KeyTrabajador");

        cargarSpinner();

    }


    public void cargarSpinner()
    {
        dropdown = findViewById(R.id.spEstatus);
        String[] items = new String[]{"Activos", "Pendientes", "Finalizados"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextSize(20);

                switch (position) {
                    case 0:
                        cargarDatosActivos();
                        break;
                    case 1:
                        cargarDatosRepartidor();
                        break;
                    case 2:
                        Toast.makeText(parent.getContext(), "Spinner item 3!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }

    public void cargarDatosActivos()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                elements = new ArrayList<>();

                for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {

                    Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                    if(pedido.TrabajadorKey.isEmpty()) {

                        /*LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(pedido.getTimestampCreatedLong()),
                                TimeZone.getDefault().toZoneId()).plusMinutes(pedido.TiempoPedido);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String formatDateTime = triggerTime.format(formatter);*/

                        elements.add(new list_element(pedido.NombreNegocio, PedidoSnapshot.getKey(), "$ " + String.valueOf(pedido.Precio),keyTrabajador,pedido.getTimestampCreatedLong(),pedido.TiempoPedido));
                    }
                }
                cargarDatosLista();
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }


    public void cargarDatosRepartidor()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                elements = new ArrayList<>();

                for (DataSnapshot PedidoSnapshot: dataSnapshot.getChildren()) {
                    Pedidos pedido = PedidoSnapshot.getValue(Pedidos.class);

                    if(pedido.TrabajadorKey.equals(keyTrabajador)) {
                        /*LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(pedido.getTimestampCreatedLong()),
                                TimeZone.getDefault().toZoneId()).plusMinutes(pedido.TiempoPedido);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String formatDateTime = triggerTime.format(formatter);*/

                        elements.add(new list_element(pedido.NombreNegocio, PedidoSnapshot.getKey(), "$ " + String.valueOf(pedido.Precio), keyTrabajador,pedido.getTimestampCreatedLong(),pedido.TiempoPedido));
                    }
                }
                cargarDatosLista();
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }



    public void cargarDatosLista()
    {

        ListAdapter listAdapter = new ListAdapter(elements,this);
        RecyclerView recyclerView = findViewById(R.id.ListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

    }

}

