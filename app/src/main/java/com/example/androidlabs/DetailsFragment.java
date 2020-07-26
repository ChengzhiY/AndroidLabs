package com.example.androidlabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class DetailsFragment extends Fragment {

    private AppCompatActivity parentActivity;
    public DetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle dataFromActivity = getArguments();

        View result = inflater.inflate(R.layout.fragment_details, container,false);
        TextView item = (TextView) result.findViewById(R.id.message_here);
        TextView idView = (TextView) result.findViewById(R.id.id);
        CheckBox checkBox = (CheckBox)result.findViewById(R.id.checkbox_is_a_send);

        item.setText("Message: " + dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));
        idView.setText("ID: " + dataFromActivity.getLong(ChatRoomActivity.ITEM_ID));
        checkBox.setChecked(dataFromActivity.getBoolean(ChatRoomActivity.IS_SEND));

        Button finishButton = (Button)result.findViewById(R.id.bt_hide);
        finishButton.setOnClickListener( clk -> {
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}