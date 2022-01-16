package com.iotph.paa.ui.member_report;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import com.iotph.rsr.databinding.FragmentMemberReportBinding;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class MemberReportFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList description, address, filename;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_member_report, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.menu_member_report);

        recyclerView = view.findViewById(R.id.recyclerView_member_report);


        view.findViewById(R.id.fab_add_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddMemberReportActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    public void onStart (){
        super.onStart();
        loadData();

    }
    private void loadData(){

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getContext().getResources().getString((R.string.menu_member_report)));
        progressDialog.setMessage("Loading "+ getContext().getResources().getString(R.string.menu_member_report) + ".");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Query query = FirebaseFirestore.getInstance().collection("reports").orderBy("datetime", DESCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<MemberReport> report = snapshot.toObjects(MemberReport.class);
                description = new ArrayList();
                address = new ArrayList();
                //datetime = new ArrayList();
                filename =  new ArrayList();
                //title = new ArrayList();

                for(MemberReport item : report) {
                    description.add(item.getDescription());
                    address.add(item.getAddress());
                    //datetime.add(item.getDatetime());
                    filename.add(item.getFilename());
                    //title.add(item.getTitle());


                }
                // Update UI
                MemberReportHelperAdapter helperAdapter = new MemberReportHelperAdapter(getContext(), description, address, filename);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(helperAdapter);

            }

        });


        progressDialog.hide();

    }

}