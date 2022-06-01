package com.android.sqlitedatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final Context context;
    ArrayList id,title,desc,date,time;
    public Adapter(Context context,
                   ArrayList id,
                   ArrayList title,
                   ArrayList desc,
                   ArrayList date,
                   ArrayList time){
        this.context = context;
        this.id = id;
        this.title = title;
        this.desc=desc;
        this.date = date;
        this.time = time;
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, @SuppressLint("RecyclerView")  int position) {
        holder.number_id.setText(String.valueOf(id.get(position)));
        holder.note_title.setText(String.valueOf(title.get(position)));
        holder.note_desc.setText(String.valueOf(desc.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number_id,note_title,note_desc;
        ImageView image_id,image_view;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number_id = itemView.findViewById(R.id.number_id);
            note_title = itemView.findViewById(R.id.note_title);
            note_desc = itemView.findViewById(R.id.note_desc);
            image_id = itemView.findViewById(R.id.image_id);
            linearLayout = itemView.findViewById(R.id.linearLayoutBorder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context,ShowDataActivity.class);
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
//                            Pair.create(number_id,ViewCompat.getTransitionName(number_id)),
//                            Pair.create(note_title,ViewCompat.getTransitionName(note_title)),
//                            Pair.create(note_desc,ViewCompat.getTransitionName(note_desc))
//                    );
                    intent.putExtra("id",String.valueOf(id.get(position)));
                    intent.putExtra("title",String.valueOf(title.get(position)));
                    intent.putExtra("desc",String.valueOf(desc.get(position)));
                    intent.putExtra("date",String.valueOf(date.get(position)));
                    intent.putExtra("time",String.valueOf(time.get(position)));
                    context.startActivity(intent);
                }
            });

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    linearLayout.setBackgroundResource(R.drawable.blue_custom_border);
//                    return true;
//                }
//            });

        }
    }
}
