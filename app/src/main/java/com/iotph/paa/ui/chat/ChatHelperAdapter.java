package com.iotph.paa.ui.chat;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotph.paa.R;

import java.util.ArrayList;

public class ChatHelperAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList arraylistName, arraylistMessage, arraylistDateTime, arraylistMessageType;
    LinearLayout linearLayout;

    public static final int MESSAGE_TYPE_IN = 1;
    public static final int MESSAGE_TYPE_OUT = 2;

    //fullname, message, datetime)
    public ChatHelperAdapter(Context context, ArrayList arraylistName, ArrayList arraylistMessage, ArrayList arraylistDateTime, ArrayList arraylistMessageType) {
        this.context = context;
        this.arraylistDateTime = arraylistDateTime;
        this.arraylistMessage = arraylistMessage;
        this.arraylistName = arraylistName;
        this.arraylistMessageType = arraylistMessageType;

    }

    private class MessageInViewHolder extends RecyclerView.ViewHolder {

        TextView message_name, message_txt, message_date;

        MessageInViewHolder(final View itemView) {
            super(itemView);
            message_date = itemView.findViewById(R.id.message_date);
            message_txt = itemView.findViewById(R.id.message_text);
            message_name = itemView.findViewById(R.id.message_name);
        }

        void bind(int position) {
            message_name.setText(arraylistName.get(position).toString());
            message_date.setText(DateFormat.format("dd MMM yyyy hh:mm a", Long.parseLong(arraylistDateTime.get(position).toString())));
            message_txt.setText(arraylistMessage.get(position).toString());

        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {

        TextView message_name, message_txt, message_date;

        MessageOutViewHolder(final View itemView) {
            super(itemView);
            message_date = itemView.findViewById(R.id.message_date);
            message_txt = itemView.findViewById(R.id.message_text);
            message_name = itemView.findViewById(R.id.message_name);
        }

        void bind(int position) {
            message_name.setText(arraylistName.get(position).toString() +" (You)");
            message_date.setText(DateFormat.format("dd MMM yyyy hh:mm a", Long.parseLong(arraylistDateTime.get(position).toString())));
            message_txt.setText(arraylistMessage.get(position).toString());

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_IN) {
            return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.items_messages_in, parent, false));
        }
        return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.items_messages_out, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (arraylistMessageType.get(position).equals(MESSAGE_TYPE_IN)) {
            ((MessageInViewHolder) holder).bind(position);
        } else {
            ((MessageOutViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return arraylistName.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (int) arraylistMessageType.get(position);
    }
}