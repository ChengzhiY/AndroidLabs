package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
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
    private ArrayList<MessageHandler> listMessages = new ArrayList<>();
    private MyListAdapter  myAdapter = new MyListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView listView = findViewById(R.id.list_view);
        Button btSend = findViewById(R.id.send);
        Button btReceive = findViewById(R.id.receive);
        EditText textMessage = findViewById(R.id.message_to_send);
        listView.setAdapter(myAdapter);
        btSend.setOnClickListener(e -> {

            String message = textMessage.getText().toString();
            MessageHandler messageSend = new MessageHandler(message, true);
            listMessages.add(messageSend);
            myAdapter.notifyDataSetChanged();
            textMessage.setText("");
        });
        btReceive.setOnClickListener(e -> {

            String message = textMessage.getText().toString();
            MessageHandler messageReceive = new MessageHandler(message, false);
            listMessages.add(messageReceive);
            myAdapter.notifyDataSetChanged();
            textMessage.setText("");
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + (position + 1) + "\n" + "The database id is:" + id
)
                    .setPositiveButton("Yes", (click, arg) -> {
                        listMessages.remove(position);
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> { })
                    .create().show();

            return true;
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
           MessageHandler messageHandler = (MessageHandler)getItem(position);
           LayoutInflater inflater = getLayoutInflater();
          //if (view == null){
               if(messageHandler.isSend()){
                   view = inflater.inflate(R.layout.send_message,parent,false);
               }
               else{
                   view = inflater.inflate(R.layout.receive_message,parent,false);
               }
               TextView message = (TextView) view.findViewById(R.id.textMessage);
               message.setTextColor(Color.BLACK);
               message.setTextSize(46);
               message.setText(messageHandler.getMessage());
         // }
           return view;
       }
   }
    @Override
    protected void onStart() {

        super.onStart();
    }
}