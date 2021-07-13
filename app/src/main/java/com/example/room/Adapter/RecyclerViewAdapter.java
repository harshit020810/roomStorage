package com.example.room.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.room.Model.Repo;
import com.example.room.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Repo> arrayList;

    public RecyclerViewAdapter(Context context, List<Repo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Repo repo = arrayList.get(position);
        holder.name.setText("Name: " + repo.getName());
        holder.capital.setText("Capital: " + repo.getCapital());
        holder.region.setText("Region: " + repo.getRegion());
        holder.subRegion.setText("Sub Region: " + repo.getSubRegion());
        holder.population.setText("Population: " + String.valueOf(repo.getPopulation()));
        holder.borders.setText("Borders: " + repo.getBorders());
        holder.languages.setText("Languages: " + repo.getLanguages());

        Glide.with(context)
                .load(Uri.parse(repo.getFlag()))
                .placeholder(R.drawable.ic_baseline_flag_24)
                .into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, capital, region, subRegion, population, borders, languages;
        public ImageView flag;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            capital = itemView.findViewById(R.id.capital);
            region = itemView.findViewById(R.id.region);
            subRegion = itemView.findViewById(R.id.subRegion);
            population = itemView.findViewById(R.id.population);
            borders = itemView.findViewById(R.id.borders);
            languages = itemView.findViewById(R.id.languages);
            flag = itemView.findViewById(R.id.flag);
        }
    }
}
