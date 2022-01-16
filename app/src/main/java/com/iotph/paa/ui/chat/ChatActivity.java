package com.iotph.paa.ui.chat;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

//import com.iotph.rsr.databinding.FragmentMemberReportBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.iotph.paa.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList fullName, message, dateTime, messagetype;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    Button sendMessage;
    private EditText inputMessageEditView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(R.string.menu_chat);
        recyclerView = findViewById(R.id.recyclerView_chat);
        sendMessage = findViewById(R.id.send);
        inputMessageEditView = findViewById(R.id.input_messsage);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessageEditView.getText().toString();
                sendDocumentData(message);
            }
        });

        //loadData();
    }
    @Override
    protected void onStart() {
        super.onStart();

        loadData();
    }
    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }

    private void loadData(){

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString((R.string.menu_chat)));
        progressDialog.setMessage("Loading "+ getResources().getString(R.string.menu_chat) + ".");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Query query = FirebaseFirestore.getInstance().collection("chats").orderBy("dateTime");
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
                List<Chat> chats = snapshot.toObjects(Chat.class);
                fullName = new ArrayList();
                message = new ArrayList();
                dateTime = new ArrayList();
                messagetype = new ArrayList();

                for(Chat item : chats) {
                   Log.d("CHAT", DateFormat.format("dd-MM-yyyy (HH:mm:ss)", Long.parseLong(item.getDateTime()))+ "___"+item.getName() +"___"+ item.getMessage());
                    fullName.add(item.getName());
                    message.add(item.getMessage());
                    dateTime.add(item.getDateTime());
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(item.getName())){
                        messagetype.add(2);
                    }
                    else{messagetype.add(1);
                    }

                }
                // Update UI
               // binding.listOfMessages.setAdapter(new MessageAdapter(MainActivity.this, chats));
                ChatHelperAdapter helperAdapter = new ChatHelperAdapter(getApplicationContext(), fullName, message, dateTime, messagetype);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(helperAdapter);
                recyclerView.smoothScrollToPosition(recyclerView.getBottom());
                LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this);
                manager.setStackFromEnd(true);
               // manager.setReverseLayout(true);
                recyclerView.setLayoutManager(manager);
            }

        });


        progressDialog.hide();
    }
    private void sendDocumentData(String message) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // Log.d("CHATS", user.getDisplayName() +"___"+ user.getEmail() +"____"+ user.getUid());
        Chat chat = new Chat(user.getEmail(), message, 0);

        db.collection("chats")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                        //loadData();
                        inputMessageEditView.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error adding document", e);
                    }
                });
    }

}