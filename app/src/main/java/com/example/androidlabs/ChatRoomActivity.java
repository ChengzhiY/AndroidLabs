package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ChatRoomActivity extends AppCompatActivity {
    private ArrayList<MessageHandler> listMessages = new ArrayList<>();
    private MyListAdapter  myAdapter = new MyListAdapter();

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);
        Button btSend = findViewById(R.id.send);
        Button btReceive = findViewById(R.id.receive);
        EditText textMessage = findViewById(R.id.message_to_send);

        loadDataFromDatabase();

        ContentValues newRowValues = new ContentValues();
        btSend.setOnClickListener(e -> {
            String message = textMessage.getText().toString();
            newRowValues.put(MyOpener.COL_MESSAGE, message);
            newRowValues.put(MyOpener.COL_SENT, 1);
            long newIdSend = db.insert(MyOpener.TABLE_NAME, "NullColumnName", newRowValues);
            MessageHandler messageSend = new MessageHandler(message, true,newIdSend);
            listMessages.add(messageSend);
            myAdapter.notifyDataSetChanged();
            textMessage.setText("");
             });
        btReceive.setOnClickListener(e -> {
            String message = textMessage.getText().toString();
            newRowValues.put(MyOpener.COL_MESSAGE, message);
            newRowValues.put(MyOpener.COL_SENT, 0);
            long newIdReceive = db.insert(MyOpener.TABLE_NAME, "NullColumnName", newRowValues);
            MessageHandler messageReceive = new MessageHandler(message, false, newIdReceive);
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
                        deleteMessage(myAdapter.getItem(position));
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
       public MessageHandler getItem(int position) {
           return listMessages.get(position);
       }

       @Override
       public long getItemId(int position) {
           return getItem(position).getId();
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
    private void loadDataFromDatabase() {
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_SENT};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int messageColumnIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int isSendColumnIndex = results.getColumnIndex(MyOpener.COL_SENT);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        while(results.moveToNext()) {
            String message = results.getString(messageColumnIndex);
            boolean isSend = false;
            if(results.getInt(isSendColumnIndex)== 1)
                isSend = true;
            else
                isSend = false;
            long id = results.getLong(idColIndex);
            listMessages.add(new MessageHandler(message, isSend, id));

        }
    }
    protected void updateMessage(MessageHandler message) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MyOpener.COL_MESSAGE, message.getMessage());
        updatedValues.put(MyOpener.COL_SENT, message.isSend());

        db.update(MyOpener.TABLE_NAME, updatedValues, MyOpener.COL_ID + "= ?", new String[] {Long.toString(message.getId())});
    }

    protected void deleteMessage(MessageHandler message) {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(message.getId())});
    }
    public void printCursor( Cursor c, int version){


    }

}