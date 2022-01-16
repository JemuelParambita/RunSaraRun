package com.iotph.paa.ui.home;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;
import com.iotph.paa.databinding.FragmentHomeBinding;
import com.iotph.paa.ui.info_board.InfoBoardHelperAdapter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList images, title, desc, address, datetime;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.menu_info_board);
        recyclerView = view.findViewById(R.id.recyclerView_form_survey);

        return view;
    }
    public void onStart (){
        super.onStart();
        loadData();

    }
    private void loadData() {

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getContext().getResources().getString((R.string.menu_info_board)));
        progressDialog.setMessage("Loading "+ getContext().getResources().getString(R.string.menu_info_board) + ".");
        progressDialog.setCancelable(false);
        progressDialog.show();

        images = new ArrayList();
        title = new ArrayList();
        desc = new ArrayList();
        address = new ArrayList();
        datetime = new ArrayList();


        db.collection("Blog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();

                            for (QueryDocumentSnapshot info_board : task.getResult()) {

                                images.add(info_board.get("IMAGE"));
                                title.add(info_board.get("Title"));
                                desc.add(info_board.get("DESCRIPTION"));
                                address.add(info_board.get("address"));
                                datetime.add(info_board.get("dateTime"));

                                //Log.d("INFO_BOARD", info_board.get("IMAGE").toString());
                            }
                        }

                        InfoBoardHelperAdapter helperAdapter = new InfoBoardHelperAdapter(getContext(), images, title, desc, address, datetime);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(helperAdapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }

    /*private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    ArrayList images,names, description;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       z final TextView textView = binding.textName;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        final TextView textViewBday = binding.textBday;
        homeViewModel.getTextBday().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewBday.setText(s);
            }
        });


        final CircleImageView profilePic = binding.updateProfilePic;
        homeViewModel.getmProfileImg().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // Log.d("PROFILE",s);
                if(s.length() > 5) Picasso.get().load(s).noFade().into(profilePic);

            }
        });

        final ImageView QrPic = binding.imgQR;
        homeViewModel.getmQRImg().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap s) {
                QrPic.setImageBitmap(s);
            }
        });





        images = new ArrayList();
        names = new ArrayList();
        description = new ArrayList();

        for(int i = 0; i< MenuData.home_menu_names.length; i++){
            images.add(MenuData.home_menu_images);
            names.add(MenuData.home_menu_names);
            description.add(MenuData.home_menu_description);

        }


        recyclerView = binding.recyclerView;
        MenuHelperAdapter helperAdapter = new MenuHelperAdapter(getContext(), images, names, description);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(helperAdapter);

        return root;
    }*/
}
