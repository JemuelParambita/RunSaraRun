package com.iotph.paa.ui.info_board;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotph.paa.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InfoBoardHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayListDesc, arrayListImg, arrayListDatetime, arrayListTitle, arrayListLocation ;
    //images, title, desc, address, datetime
    public InfoBoardHelperAdapter(Context context, ArrayList arrayListImg, ArrayList arrayListTitle, ArrayList arrayListDesc, ArrayList arrayListLocation, ArrayList arrayListDatetime){
        this.context  = context;
        this.arrayListImg = arrayListImg;
        this.arrayListDatetime = arrayListDatetime;
        this.arrayListDesc = arrayListDesc;
        this.arrayListTitle = arrayListTitle;
        this.arrayListLocation = arrayListLocation;

    }
    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_info_board, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Picasso.get().load(arrayListImg.get(position).toString()).into(viewHolderClass.imageView);
        viewHolderClass.textviewTitle.setText(arrayListTitle.get(position).toString());
        viewHolderClass.textViewDescription.setText(arrayListDesc.get(position).toString());


        Date datetimethis = new Date(Long.parseLong(arrayListDatetime.get(position).toString()));
        String newstring = new SimpleDateFormat("yyyy-MM-dd").format(datetimethis);
        viewHolderClass.textviewDatetime.setText(newstring);
        viewHolderClass.textviewLocation.setText(arrayListLocation.get(position).toString());
        //viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //    }
        //});

    }
    @Override
    public int getItemCount() {
        return arrayListTitle.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView textViewDescription;
        TextView textviewDatetime;
        ImageView imageView;
        TextView textviewTitle;
        TextView textviewLocation;

        public ViewHolderClass(@NonNull  View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            textviewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textviewDatetime = (TextView) itemView.findViewById(R.id.textViewDateTime);
            textviewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
        }
    }
}
