package com.moaapps.mathquiz.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moaapps.mathquiz.R;

import java.util.ArrayList;

public class LevelsAdapter extends RecyclerView.Adapter<LevelsAdapter.Holder> {
    private ArrayList<Integer> list;
    private Context context;

    private OnClickListener mListener;

    public interface OnClickListener{
        void OnClick(int position);
    }

    public void setOnCleckListener(OnClickListener listener){
        mListener = listener;
    }


    public LevelsAdapter(ArrayList<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.level_item , parent , false) , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        int lastLevel = context.getSharedPreferences("PRE" , Context.MODE_PRIVATE).getInt("last_level" ,1);
        Log.e("LAST LEVEL" , ""+lastLevel);
        if (list.get(position) <= lastLevel){
            holder.lockedLevel.setVisibility(View.GONE);
            holder.levelText.setText(""+list.get(position));
        }else {
            holder.lockedLevel.setVisibility(View.VISIBLE);
            holder.levelText.setText(""+list.get(position));
        }

//        here on BindViewHolder Ux Code


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView levelText;
        RelativeLayout lockedLevel;

        public Holder(@NonNull View itemView , final OnClickListener listener) {
            super(itemView);

            levelText = itemView.findViewById(R.id.level_number);
            lockedLevel = itemView.findViewById(R.id.locked_level);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnClick(position);
                        }
                    }
                }
            });
        }
    }
}
