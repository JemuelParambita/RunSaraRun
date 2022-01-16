package com.iotph.paa.ui.event_schedule;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iotph.paa.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventScheduleHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arrayListDate, arrayListTime, arrayListTitle;
    long epoch;
    private String filename = "events.txt";
    String fileData;

    public EventScheduleHelperAdapter(Context context, ArrayList arrayListDate, ArrayList arrayListTime, ArrayList arrayListTitle){
        this.context  = context;
        this.arrayListDate = arrayListDate;
        this.arrayListTime = arrayListTime;
        this.arrayListTitle = arrayListTitle;

    }
    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_events_schedule, viewGroup, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        try {
            fileData = readFromFileInputStream();
            Log.d("EVENTS_DATA", fileData);
        }catch (Exception e){

            Log.d("EVENTS_DATA", "NO FILE");
        }
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;

        String[] myArr = fileData.split("<br>");
        for(String item : myArr) {
            String[] myArrData = item.split("<>");
            if(arrayListTitle.get(position).toString().equals(myArrData[0])){
                if(myArrData[1].equals("0")){//decline
                    viewHolderClass.textviewAccept.setVisibility(View.GONE);
                    viewHolderClass.textviewDecline.setText("Declined");
                    viewHolderClass.textviewDecline.setClickable(false);
                    viewHolderClass.textviewDecline.setEnabled(false);
                }
                else{
                    viewHolderClass.textviewDecline.setVisibility(View.GONE);
                    viewHolderClass.textviewAccept.setText("Accepted");
                    viewHolderClass.textviewAccept.setClickable(false);
                    viewHolderClass.textviewAccept.setEnabled(false);
                    viewHolderClass.textviewAccept.setBackgroundTintList(context.getColorStateList(R.color.ic_launcher_background));
                }
            }

        }

        viewHolderClass.textViewTitle.setText(arrayListTitle.get(position).toString());
        viewHolderClass.textViewDate.setText("Date: "+ arrayListDate.get(position).toString());
        viewHolderClass.textViewTime.setText("Time: "+arrayListTime.get(position).toString());

        //TODO: show if accepted or decline here

        viewHolderClass.textviewAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, arrayListTitle.get(position).toString());

                try {
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                    Date date = df.parse(arrayListDate.get(position).toString() + " " + arrayListTime.get(position).toString());
                    epoch = date.getTime();
                    } catch (ParseException e) {
                    e.printStackTrace();
                }
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, epoch);
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,  epoch);
                intent.putExtra(CalendarContract.Events.ALL_DAY, true);// periodicity
                intent.putExtra(CalendarContract.Events.DESCRIPTION,arrayListTitle.get(position).toString());
                context.startActivity( intent );
                writeDataToFile( arrayListTitle.get(position).toString()+"<>"+1+"<br>");

            }
        });

        viewHolderClass.textviewDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: remove from list
                writeDataToFile(arrayListTitle.get(position).toString()+"<>"+0+"<br>");
                Toast.makeText(v.getContext(), arrayListTitle.get(position).toString()+" is Declined.", Toast.LENGTH_LONG).show();
                //notifyDataSetChanged();
                EventScheduleFragment eventScheduleFragment = new EventScheduleFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_main, eventScheduleFragment).commit();
            }
        });

    }


    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }

    // This method will write data to FileOutputStream.
    private void writeDataToFile( String data)
    {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
           // fos.write(string.getBytes());
            //fos.close();
           // FileOutputStream fileOutputStream = new FileOutputStream(new File("filename"),true);//context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(fileData +"<br>"+data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();

        }catch(FileNotFoundException ex)
        {
            Log.e("EVENTS_WRITE", ex.getMessage(), ex);
        }catch(IOException ex)
        {
            Log.e("EVENTS_WRITE", ex.getMessage(), ex);
        }
    }
    // This method will read data from FileInputStream.
    private String readFromFileInputStream()
    {
        StringBuffer retBuf = new StringBuffer();
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        }catch(IOException ex)
        {
            Log.e("EVENTS_READ", ex.getMessage(), ex);
        }finally
        {
            return retBuf.toString();
        }
    }
    @Override
    public int getItemCount() {
        return arrayListTitle.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewDate;
        TextView textViewTime;


        Button textviewAccept;
        Button textviewDecline;

        public ViewHolderClass(@NonNull  View itemView) {
            super(itemView);

            textViewDate = (TextView) itemView.findViewById(R.id.event_date);
            textViewTime = (TextView) itemView.findViewById(R.id.event_time);
            textViewTitle = (TextView) itemView.findViewById(R.id.event_title);

            textviewAccept = (Button) itemView.findViewById(R.id.event_accept);
            textviewDecline = (Button) itemView.findViewById(R.id.event_decline);
        }
    }
}
