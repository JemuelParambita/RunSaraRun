package com.iotph.paa.ui.form_survey;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iotph.paa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FormSurveyHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayListLabel, arrayListLink, arrayListImg;

    public FormSurveyHelperAdapter(Context context, ArrayList arraylist, ArrayList arrayListLabel, ArrayList arrayListLink){
        this.context  = context;
        this.arrayListImg = arraylist;
        this.arrayListLabel = arrayListLabel;
        this.arrayListLink = arrayListLink;

    }
    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_form_survey, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        StorageReference storage =  FirebaseStorage.getInstance().getReference().child("public/forms/");
        final StorageReference ref = storage.child(arrayListImg.get(position).toString());
        ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downUri = task.getResult();
                    Picasso.get().load(downUri.toString()).into(viewHolderClass.imageView);
                }else{
                }
            }
        });

        viewHolderClass.textView.setText(arrayListLabel.get(position).toString());
        viewHolderClass.clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("link",arrayListLink.get(position).toString());
                bundle.putString("label",arrayListLabel.get(position).toString());

                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                fm.popBackStackImmediate();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                FormSurveyDetailFragment formSurveyDetailFragment = new FormSurveyDetailFragment();
                formSurveyDetailFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, formSurveyDetailFragment).commit();

            }
        });

    }
    @Override
    public int getItemCount() {
        return arrayListLabel.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        TextView clickHere;
        public ViewHolderClass(@NonNull  View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.survey_image);
            textView = (TextView) itemView.findViewById(R.id.survey_title);
            clickHere = (TextView) itemView.findViewById(R.id.click_here);
        }
    }
}
