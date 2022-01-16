package com.iotph.paa.ui.member_profile;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;
import com.iotph.paa.databinding.FragmentHomeBinding;
import com.iotph.paa.ui.home.HomeViewModel;
import com.iotph.paa.ui.home.MenuData;
import com.iotph.paa.ui.home.MenuHelperAdapter;
import com.iotph.paa.ui.info_board.InfoBoardHelperAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MemberProfileFragment extends Fragment {
    private HomeViewModel homeViewModel;
   // private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    ArrayList images,names, description;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member_profile, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.menu_info_board);

        recyclerView = view.findViewById(R.id.recyclerView_memberP);



        images = new ArrayList();
        names = new ArrayList();
        description = new ArrayList();

        for(int i = 0; i< MenuData.home_menu_names.length; i++){
            images.add(MenuData.home_menu_images);

            Log.e("TEST", String.valueOf(MenuData.home_menu_names));
            names.add(MenuData.home_menu_names);
            description.add(MenuData.home_menu_description);

        }



        MenuHelperAdapter helperAdapter = new MenuHelperAdapter(getContext(), images, names, description);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(helperAdapter);


        return view;
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