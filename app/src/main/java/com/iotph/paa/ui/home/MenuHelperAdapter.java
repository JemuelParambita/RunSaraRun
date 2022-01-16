package com.iotph.paa.ui.home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.iotph.paa.R;
import com.iotph.paa.ui.event_schedule.EventScheduleFragment;
import com.iotph.paa.ui.form_survey.FormSurveyFragment;
import com.iotph.paa.ui.member_report.MemberReportFragment;

import java.util.ArrayList;

public class MenuHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayList, arrayListName, arrayListDes;

    public MenuHelperAdapter(Context context, ArrayList arraylist, ArrayList arrayListName, ArrayList arrayListDes){
        this.context  = context;
        this.arrayList = arraylist;
        this.arrayListName = arrayListName;
        this.arrayListDes = arrayListDes;

    }
    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_menu, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        viewHolderClass.imageView.setImageResource((MenuData.home_menu_images[position]));
        //viewHolderClass.textView.setText(MenuData.home_menu_names[position]);
       // viewHolderClass.textViewDescription.setText(MenuData.home_menu_description[position]);
        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if(position == 0) {
                    MemberReportFragment memberReportFragment = new MemberReportFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, memberReportFragment).commit();
                }
                else if(position == 1) {
                    FormSurveyFragment formSurveyFragment = new FormSurveyFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, formSurveyFragment).commit();
                }
                else if(position == 2) {
                    EventScheduleFragment eventScheduleFragment = new EventScheduleFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.popBackStackImmediate();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment_content_main, eventScheduleFragment).commit();

                }
                else{*/
                    Toast.makeText(context, "Coming Soon " + MenuData.home_menu_names[position], Toast.LENGTH_SHORT).show();

               // }
            }
        });

    }
    @Override
    public int getItemCount() {
        return arrayListName.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{
    //    TextView textViewDescription;
     //   TextView textView;
        ImageView imageView;
        public ViewHolderClass(@NonNull  View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
//            textView = (TextView) itemView.findViewById(R.id.textView);
        //    textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}
