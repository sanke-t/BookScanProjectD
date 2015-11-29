package com.example.sanket.d;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.V> {
    private ArrayList<Book>l =new ArrayList<>();
    private LayoutInflater layout;
    static class V extends RecyclerView.ViewHolder {
        TextView t,b,p;
        public V(View itemView) {
            super(itemView);
            t=(TextView)itemView.findViewById(R.id.title);
            b=(TextView)itemView.findViewById(R.id.by);
            p=(TextView)itemView.findViewById(R.id.publisher);
        }
    }

    public CustomAdapter(Context context,ArrayList<Book>l)
    {
        layout=LayoutInflater.from(context);
        this.l=l;
        notifyItemRangeChanged(0,l.size());
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layout.inflate(R.layout.book,parent,false);
        V viewHolder=new V(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        Book current =l.get(position);
        holder.t.setText(current.getTitle());
        holder.b.setText(current.getAuthor());
        holder.p.setText(current.getPublisher());

    }

    @Override
    public int getItemCount() {
        return l.size();
    }
}

