package com.humblefool.myallowancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    //Checks if a file exists, if not, initialises with value 0
    private void checkFile(String data, Context context) {
        String mDirectory = context.getFilesDir().toString();
        mDirectory += "/Allowance.txt";
        File mFile = new File(mDirectory);
        if(!mFile.exists()) {
            try {
                Log.d("MyAllowanceManger","File does not exist in "+mDirectory);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Allowance.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        else{
            Log.d("MyAllowanceManger","File exists in "+mDirectory);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialising control views
        Button updateAllowanceButton = findViewById(R.id.button_update_allowance);
        Button updateExpenseButton = findViewById(R.id.button_update_expense);
        mContext = this;

        //Setting onClickListener for Update Allowance button
        updateAllowanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, UpdateAllowance.class);
                checkFile("0 as of " +(new java.util.Date().toString())+ "\n",mContext);
                startActivity(mIntent);
            }
        });

        //Setting onClickListener for Update Expense button
        updateExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, UpdateExpense.class);
                checkFile("0 as of "+(new java.util.Date().toString())+ "\n",mContext);
                startActivity(mIntent);
            }
        });
    }
}
