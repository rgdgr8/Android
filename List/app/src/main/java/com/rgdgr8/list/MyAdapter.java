package com.rgdgr8.list;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    @NonNull

    String[] s1;
    int[] i1;
    Context cx;

    public MyAdapter(Context c, String[] s, int[] i) {

        cx = c;
        s1 = s;
        i1 = i;

    }

    @Override
    public MyAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(cx);
        View v = inflater.inflate(R.layout.row, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyHolder holder, int position) {

        holder.t1.setText(s1[position]);
        holder.m1.setImageResource(i1[position]);

        if (holder.t1.getText().toString().equals("DOG")) {
            holder.layout.setVisibility(View.GONE);
            holder.layout.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }else {
            holder.layout.setVisibility(View.VISIBLE);
            holder.layout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    @Override
    public int getItemCount() {
        return s1.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView t1;
        ImageView m1;
        ConstraintLayout layout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            t1 = itemView.findViewById(R.id.textView1);
            layout = itemView.findViewById(R.id.layout);
            m1 = itemView.findViewById(R.id.imageView);
            t1.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            final EditText editText = new EditText(cx);

            new AlertDialog.Builder(cx)
                    .setMessage("Change Content?")
                    .setCustomTitle(editText)
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            t1.setText(editText.getText().toString());
                        }
                    })
                    .setNegativeButton("CANCEL", null).show();

        }
    }
}
