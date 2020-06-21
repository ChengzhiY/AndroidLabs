package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private List<MessageHandler> listMessages = new ArrayList<>();
    private ListView listView;
    private EditText textMessage;
    private MyListAdapter  myAdapter = new MyListAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listView = findViewById(R.id.list_view);
        Button btSend = findViewById(R.id.send);
        Button btReceive = findViewById(R.id.receive);
        textMessage = findViewById(R.id.message_to_send);

        btSend.setOnClickListener(e -> {

            String message = textMessage.getText().toString();
            MessageHandler messageSend = new MessageHandler(message, true);
            listMessages.add(messageSend);
            listView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            textMessage.setText("");
        });
        btReceive.setOnClickListener(e -> {

            String message = textMessage.getText().toString();
            MessageHandler messageReceive = new MessageHandler(message, false);
            listMessages.add(messageReceive);
            listView.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            textMessage.setText("");
        });

    }

   private class MyListAdapter extends BaseAdapter {

       @Override
       public int getCount() {
           return listMessages.size();
       }

       @Override
       public Object getItem(int position) {
           return listMessages.get(position);
       }

       @Override
       public long getItemId(int position) {
           return (long) position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           View view = convertView;
           LayoutInflater inflater = getLayoutInflater();
           if (view == null){
               if(listMessages.get(position).isSend()){
                   view = inflater.inflate(R.layout.send_message,parent,false);
               }
               else{
                   view = inflater.inflate(R.layout.receive_message,parent,false);
               }
               TextView message = (TextView) view.findViewById(R.id.textMessage);
               message.setTextColor(Color.BLACK);
               message.setTextSize(46);
               message.setText(listMessages.get(position).getMessage());
           }
           return view;
       }
   }
    @Override
    protected void onStart() {

        super.onStart();
    }
}