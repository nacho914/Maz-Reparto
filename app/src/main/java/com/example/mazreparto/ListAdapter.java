package com.example.mazreparto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{
    private List<list_element> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<list_element> mData, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    @Override
    public int getItemCount()
    { return mData.size();}

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
    {
        View view = mInflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull final ListAdapter.ViewHolder holder, final int position)
    {
        holder.binData(mData.get(position));
    }

    public void setItems(List<list_element> items)
    {mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView titulo;
        TextView dinero;
        TextView hora;

        ViewHolder(View itemView)
        {
            super(itemView);
            titulo= itemView.findViewById(R.id.m_Titulo);
            dinero= itemView.findViewById(R.id.m_dinero);
            hora= itemView.findViewById(R.id.m_hora);

        }

        void binData(@org.jetbrains.annotations.NotNull final list_element item)
        {
            titulo.setText(item.getTitulo());
            dinero.setText(item.getDinero());
            hora.setText(item.getHora());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, MainActivity_pedido.class);
                    intent.putExtra("keyPedido", item.PedidoKey);
                    context.startActivity(intent);
                }
            });
        }
    }



}
