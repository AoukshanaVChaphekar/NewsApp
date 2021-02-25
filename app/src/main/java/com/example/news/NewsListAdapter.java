package com.example.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    ArrayList<NewsData> items=new ArrayList<>();
    NewsItemClicked listener;
    public NewsListAdapter(NewsItemClicked listener)
    {
        this.listener=listener;
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        final NewsViewHolder newsViewHolder=new NewsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnItemClicked(items.get(newsViewHolder.getAdapterPosition()));
            }
        });
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsData currentItem=items.get(position);
        holder.title.setText(currentItem.title);
        holder.author.setText(currentItem.author);
        Glide.with(holder.itemView.getContext()).load(currentItem.imageUrl).into(holder.newImage);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void updateNews(ArrayList<NewsData> updatedItems)
    {
        items.clear();
        items.addAll(updatedItems);

        notifyDataSetChanged();
    }
}
class NewsViewHolder extends RecyclerView.ViewHolder{
    TextView title;
    ImageView newImage;
    TextView author;
    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.title);
        author=itemView.findViewById(R.id.author);
        newImage=itemView.findViewById(R.id.newsImage);

    }
}
interface NewsItemClicked
{
    void OnItemClicked(NewsData item);
}
