package Modelos;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Pedidos {

    public String NombreNegocio;
    public String Direccion;
    public long Telefono;
    public String NombreCliente;
    public int Precio;
    public int TiempoPedido;
    public String TrabajadorKey;
    HashMap<String, Object> timestampCreated;

    public  Pedidos()
    {

    }

    public Pedidos(String sNombreNegocio, String sDireccion, long iTelefono, String sNombre, int iPrecio, int iTiempo,String sTrabajadorKey) {

        this.NombreNegocio=sNombreNegocio;
        this.Direccion= sDireccion;
        this.Telefono= iTelefono;
        this.NombreCliente=sNombre;
        this.Precio=iPrecio;
        this.TiempoPedido=iTiempo;
        this.TrabajadorKey = sTrabajadorKey;
    }

    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }

}