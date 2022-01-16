package com.iotph.paa.ui.home;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mTextBday;
    private MutableLiveData<String> mProfileImg;
    private MutableLiveData<Bitmap> mQRImg;

    FirebaseAuth auth;
    FirebaseFirestore db;

    public HomeViewModel() {

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mText = new MutableLiveData<>();
        mTextBday = new MutableLiveData<>();
        mProfileImg = new MutableLiveData<>();
        mQRImg = new MutableLiveData<>();


        FirebaseUser firebaseUser = auth.getCurrentUser();
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    /*mText.setValue(documentSnapshot.getString("fullName"));
                    mTextBday.setValue(documentSnapshot.getString("birthday"));

                    if(documentSnapshot.getString("profilePic") != "") {

                        mProfileImg.setValue(documentSnapshot.getString("profilePic"));
                    }

                    String qrCodeContent = documentSnapshot.getString("mobileNumber");
                    MultiFormatWriter qrWriter = new MultiFormatWriter();

                    try {
                        BitMatrix matrix = qrWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 100, 100);
                        BarcodeEncoder encoder = new BarcodeEncoder();

                        Bitmap bitmap = encoder.createBitmap(matrix);
                        mQRImg.setValue(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }*/

                }
            }
        });








    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getTextBday() {
        return mTextBday;
    }
    public LiveData<String> getmProfileImg() {
        return mProfileImg;
    }
    public LiveData<Bitmap> getmQRImg() {
        return mQRImg;
    }
}