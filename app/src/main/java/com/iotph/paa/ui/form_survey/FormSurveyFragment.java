package com.iotph.paa.ui.form_survey;


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


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FormSurveyFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList images, label, link;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_survey, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.menu_survey);
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
        progressDialog.setTitle(getContext().getResources().getString((R.string.menu_survey)));
        progressDialog.setMessage("Loading "+ getContext().getResources().getString(R.string.menu_survey) + ".");
        progressDialog.setCancelable(false);
        progressDialog.show();

        images = new ArrayList();
        label = new ArrayList();
        link = new ArrayList();

        db.collection("forms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();

                            for (QueryDocumentSnapshot surveys : task.getResult()) {
                                images.add(surveys.get("filename"));
                                link.add(surveys.get("link"));
                                label.add(surveys.get("label"));
                            }
                        }

                        FormSurveyHelperAdapter helperAdapter = new FormSurveyHelperAdapter(getContext(), images, label, link);
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