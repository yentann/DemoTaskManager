package com.example.demotaskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
        int piReqCode = 12;
        Button btnAdd, btnCancel;
        EditText etName, etDescription, etSeconds;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);

            etName = (EditText) findViewById(R.id.etName);
            etDescription = (EditText) findViewById(R.id.etDescription);
            etSeconds = (EditText) findViewById(R.id.etTime);

            btnAdd = (Button) findViewById(R.id.btnAddOK);
            btnCancel = (Button) findViewById(R.id.btnAddCancel);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int seconds = Integer.valueOf(etSeconds.getText().toString());
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.SECOND, seconds);

                    String name = etName.getText().toString();
                    String desc = etDescription.getText().toString();
                    DBHelper dbh = new DBHelper(AddActivity.this);
                    int id = (int) dbh.insertTask(name, desc);
                    dbh.close();

                    //Create a new PendingIntent and add it .to the AlarmManager
                    Intent iReminder = new Intent(AddActivity.this, TaskReminder.class);

                    iReminder.putExtra("id", id);
                    iReminder.putExtra("name", name);
                    iReminder.putExtra("desc", desc);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, piReqCode, iReminder, PendingIntent.FLAG_CANCEL_CURRENT);

                    AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                    setResult(RESULT_OK);
                    finish();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        }
}
