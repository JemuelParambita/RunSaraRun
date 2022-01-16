package com.iotph.paa.ui.notif;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.firestore.Query.Direction.DESCENDING;

public class InboxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList   title, type, dateTime;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        getSupportActionBar().setSubtitle(R.string.notif);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView_inbox);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData(){

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString((R.string.notif)));
        progressDialog.setMessage("Loading "+ getResources().getString(R.string.notif) + ".");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Query query = FirebaseFirestore.getInstance().collection("notification_logs").orderBy("timestamp", DESCENDING);

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
                List<Inbox> inbox = snapshot.toObjects(Inbox.class);
                title = new ArrayList();
                type = new ArrayList();
                dateTime = new ArrayList();


                for(Inbox item : inbox) {

                    type.add(item.getType());
                    title.add(item.getTitle());
                    dateTime.add(item.getTimestamp());
                    Log.d("INBOX", item.getTitle());

                }
                // Update UI
                InboxHelperAdapter helperAdapter = new InboxHelperAdapter(getApplicationContext(), title, type, dateTime);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(helperAdapter);

            }

        });


        progressDialog.hide();
    }
}