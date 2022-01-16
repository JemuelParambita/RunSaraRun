package com.iotph.paa.ui.event_schedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class EventScheduleFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList date, time, title;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_member_report, container, false);
        View view = inflater.inflate(R.layout.fragment_event_schedule, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.menu_events_sched);

        recyclerView = view.findViewById(R.id.recyclerView_events);

        view.findViewById(R.id.fab_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),  AddEventActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }



    public void onStart (){
        super.onStart();
        loadData();

    }
    private void loadData() {
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getContext().getResources().getString((R.string.menu_events_sched)));
        progressDialog.setMessage("Loading "+ getContext().getResources().getString(R.string.menu_events_sched) + ".");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Query query = FirebaseFirestore.getInstance().collection("events").orderBy("timestamp", DESCENDING);

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
                List<EventSchedule> event = snapshot.toObjects(EventSchedule.class);
                date = new ArrayList();
                time = new ArrayList();
                title = new ArrayList();

                for(EventSchedule item : event) {
                    date.add(item.getEvent_date());
                    time.add(item.getEvent_time());
                    title.add(item.getEvent_title());


                }
                // Update UI
                EventScheduleHelperAdapter helperAdapter = new EventScheduleHelperAdapter(getContext(), date, time, title);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(helperAdapter);

            }

        });


        progressDialog.hide();

    }

}