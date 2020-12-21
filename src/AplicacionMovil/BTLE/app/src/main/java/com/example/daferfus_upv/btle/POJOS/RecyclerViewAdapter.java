package com.example.daferfus_upv.btle.POJOS;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.daferfus_upv.btle.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private final ArrayList<String> mNames;
    private final ArrayList<String> mDescriptions;
    private final ArrayList<Integer> mImageUrls;
    private final Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<Integer> imageUrls, ArrayList<String> descriptions) {
        mNames = names;
        mImageUrls = imageUrls;
        mDescriptions = descriptions;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked on an image: " + mDescriptions.get(position));
            Toast.makeText(mContext, mDescriptions.get(position), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;
        ProgressBar increase_progress;
        Button increase_button;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            increase_progress=itemView.findViewById(R.id.progress_increase);
            increase_button=itemView.findViewById(R.id.increase_value);

            increase_button.setOnClickListener(v -> {
                if(increase_progress.getProgress()!=100){
                    increase_progress.setProgress(increase_progress.getProgress()+10);
                }
            });
        }
    }

}
