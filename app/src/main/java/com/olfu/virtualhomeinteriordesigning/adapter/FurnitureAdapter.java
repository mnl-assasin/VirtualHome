package com.olfu.virtualhomeinteriordesigning.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olfu.virtualhomeinteriordesigning.R;
import com.olfu.virtualhomeinteriordesigning.model.ImageItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mykelneds on 10/14/15.
 */
public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.MyViewHolder> {

    Context context;
    ArrayList<ImageItem> data;
    LayoutInflater inflater;

    public FurnitureAdapter(Context context, ArrayList<ImageItem> data) {
        this.context = context;
        this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.furniture_cell, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        String source = data.get(i).getImage();
        String path = "android.resource://" + context.getPackageName() + "/drawable/" + source;
        Log.d("FA", "Path: " + path);
        Picasso.with(context).load(path).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}
