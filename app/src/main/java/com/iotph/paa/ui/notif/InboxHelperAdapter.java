package com.iotph.paa.ui.notif;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotph.paa.R;

import java.util.ArrayList;

public class InboxHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayListTitle, arraylistType, arraylistDateTime;

        public InboxHelperAdapter(Context context, ArrayList arrayListTitle, ArrayList arraylistType, ArrayList arraylistDateTime) {
        this.context = context;
        this.arraylistDateTime = arraylistDateTime;
        this.arrayListTitle = arrayListTitle;
        this.arraylistType = arraylistType;
    }
    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_inbox, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;

        //Picasso.get().load(arrayListImg.get(position).toString()).into(viewHolderClass.imageView);
        viewHolderClass.textviewTitle.setText(arrayListTitle.get(position).toString());
        viewHolderClass.textviewDatetime.setText(DateFormat.format("dd MMM yyyy hh:mm a", Long.parseLong(arraylistDateTime.get(position).toString())));
       // viewHolderClass.textviewDatetime.setText(arraylistDateTime.get(position).toString());
        if(arraylistType.get(position).toString().equals("reports")){
        viewHolderClass.imageViewType.setImageResource(R.drawable.ic_menu_report);
        }else{
            viewHolderClass.imageViewType.setImageResource(R.drawable.ic_mail);
        }


    }
    @Override
    public int getItemCount() {
        return arrayListTitle.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView textviewTitle, textviewDatetime;
        ImageView imageViewType;
        public ViewHolderClass(@NonNull  View itemView) {
            super(itemView);
            imageViewType = (ImageView)itemView.findViewById(R.id.inbox_type);
            textviewTitle = (TextView) itemView.findViewById(R.id.inbox_title);
            textviewDatetime = (TextView) itemView.findViewById(R.id.inbox_dateTime);
        }
    }
}