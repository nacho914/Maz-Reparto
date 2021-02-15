package com.example.mazreparto;

public class list_element {
    public String Color;
    public String Titulo;
    public String Hora;
    public String Dinero;
    public String PedidoKey;

    public list_element(String color, String titulo, String pedidoKey,String dinero,String hora) {
        Color = color;
        Titulo = titulo;
        PedidoKey = pedidoKey;
        Dinero=dinero;
        Hora=hora;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getPedidoKey() {
        return PedidoKey;
    }

    public void setPedidoKey(String pedidoKey) {
        PedidoKey = pedidoKey;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getDinero() {
        return Dinero;
    }

    public void setDinero(String dinero) {
        Dinero = dinero;
    }
}
