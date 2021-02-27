package Modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class UsuariosRepartidores {
    public String Nombre;
    public String FechaLicencia;
    public String Domicilio;
    public long Telefono;
    public String TipoSangre;
    public long TeleEmergencia;
    public String Correo;
    public  String Password;
    public  String NombreSeguro;
    public  String FechaSeguro;
    public  String keyNotificaciones;
    HashMap<String, Object> timestampCreated;

    public UsuariosRepartidores()
    {}

    public UsuariosRepartidores(String sNombre,String sFechaLicencia,String sDomicilio,long lTele,String sTipoSangre,long lTeleEmer, String sCorreo,
                                String sPassword,String sNombreSeguro,String sFechaSeguro,String sKey)
    {

        Nombre = sNombre;
        FechaLicencia = sFechaLicencia;
        Domicilio= sDomicilio;
        Telefono=lTele;
        TipoSangre=sTipoSangre;
        TeleEmergencia=lTeleEmer;
        Correo=sCorreo;
        Password=sPassword;
        NombreSeguro=sNombreSeguro;
        FechaSeguro=sFechaSeguro;
        keyNotificaciones=sKey;

        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestampCreated = timestampNow;
    }

    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }

    @Exclude
    public long getTimestampCreatedLong(){
        return (long)timestampCreated.get("timestamp");
    }

}
