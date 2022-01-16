package com.iotph.paa.ui.member_report;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotph.paa.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MemberReportHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayListDescription, arrayListAddress, arrayListFilename;

    public MemberReportHelperAdapter(Context context, ArrayList arrayListDescription, ArrayList arrayListAddress,  ArrayList arrayListFilename){
        this.context  = context;
        this.arrayListDescription = arrayListDescription;
        this.arrayListAddress = arrayListAddress;
        this.arrayListFilename = arrayListFilename;
    }
    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_member_report, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        boolean noImage = false;
        Picasso.get()
                .load(arrayListFilename.get(position).toString())
                .into(viewHolderClass.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.get().load(arrayListFilename.get(position).toString()).into(viewHolderClass.imageView);
                        viewHolderClass.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO: onclick image fullscreen
                                //fullscreen
                                Bundle bundle = new Bundle();
                                bundle.putString("img",arrayListFilename.get(position).toString());
                                Intent intent = new Intent(context, ImageFullscreenActivity.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("REPORT", "error");
                        viewHolderClass.imageView.setImageResource(R.drawable.ic_noimage);
                    }
                });

        viewHolderClass.textviewAdd.setText(arrayListAddress.get(position).toString());
        viewHolderClass.textviewDesc.setText(arrayListDescription.get(position).toString());



    }
    @Override
    public int getItemCount() {
        return arrayListDescription.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView textviewDesc, textviewAdd;
        ImageView imageView;
        public ViewHolderClass(@NonNull  View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.member_report_image);
            textviewDesc = (TextView) itemView.findViewById(R.id.member_report_description);
            textviewAdd = (TextView) itemView.findViewById(R.id.member_report_address);
        }
    }
}
