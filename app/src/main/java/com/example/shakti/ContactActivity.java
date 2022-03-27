package com.example.shakti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {
    private static final String TAG = "";
    DataBaseHandler mDbHelper;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ListView contactList = (ListView) findViewById(R.id.contact_list);
        ArrayList<String> phoneNumberList = new ArrayList<>();

        FloatingActionButton addContact = (FloatingActionButton) findViewById(R.id.add_contact_fab);
        FloatingActionButton deleteContact = (FloatingActionButton) findViewById(R.id.delete_contact_fab);
        EditText phoneNumberEdit = (EditText) findViewById(R.id.phone_number);



        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEdit.getText().toString();
                if(phoneNumber.length()==10)
                {
                    if (mDbHelper.insertPhoneNumber(phoneNumber)) {
                        Toast.makeText(ContactActivity.this, getString(R.string.phone_number_added_successfully), Toast.LENGTH_SHORT).show();
                        phoneNumberEdit.setText(" ");
                    } else
                        Toast.makeText(ContactActivity.this, getString(R.string.addition_unsuccessful), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(ContactActivity.this, getString(R.string.phone_number_incorrect), Toast.LENGTH_SHORT).show();
            }
        });

        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContactActivity.this, getString(R.string.data_deleted), Toast.LENGTH_SHORT).show();
                String phoneNumber = phoneNumberEdit.getText().toString();
                if(mDbHelper.deletePhoneNumber(phoneNumber)){
                    Toast.makeText(ContactActivity.this, getString(R.string.number_deleted_successfully), Toast.LENGTH_SHORT).show();
                    phoneNumberEdit.setText(" ");
                }
                else Toast.makeText(ContactActivity.this, getString(R.string.delete_unsuccessful), Toast.LENGTH_SHORT).show();
            }
        });

        mDbHelper = new DataBaseHandler(this);
        phoneNumberList = mDbHelper.phoneNumberList();
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, phoneNumberList);
        contactList.setAdapter(adapter);
    }
}