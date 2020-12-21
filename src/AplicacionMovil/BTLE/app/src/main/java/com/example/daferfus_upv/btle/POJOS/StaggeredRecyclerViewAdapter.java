package com.example.daferfus_upv.btle.POJOS;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.daferfus_upv.btle.R;

import java.util.ArrayList;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";

    private final ArrayList<String> mNamesConsejos;
    private final ArrayList<String> mImageUrls;
    private ArrayList<String> mRedireccionUrl = new ArrayList<>();
    private final Context mContext;

    public StaggeredRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<String> redireccionUrl) {
        mNamesConsejos = names;
        mImageUrls = imageUrls;
        mContext = context;
        mRedireccionUrl = redireccionUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mImageUrls.get(position))
                .apply(requestOptions)
                .into(holder.image);

        holder.name.setText(mNamesConsejos.get(position));

        holder.image.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked on: " + mRedireccionUrl.get(position));
            //Toast.makeText(mContext, mNamesConsejos.get(position), Toast.LENGTH_SHORT).show();
            String url = mRedireccionUrl.get(position);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.imageview_widget);
            this.name = itemView.findViewById(R.id.name_widget);
        }
    }
}
