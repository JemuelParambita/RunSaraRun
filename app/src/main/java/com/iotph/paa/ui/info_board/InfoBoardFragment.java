package com.iotph.paa.ui.info_board;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;
import com.iotph.paa.databinding.FragmentFormSurveyBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InfoBoardFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList images, title, desc, address, datetime;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_form_survey, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.menu_info_board);
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
    }