package com.humblefool.myallowancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class UpdateExpense extends AppCompatActivity {
    private Integer mCurrentAllowance;
    private String mUpdatedAllowance;

    private void updateAllowanceToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Allowance.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private Integer readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("Allowance.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("MyAllowance", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("MyAllowance", "Can not read file: " + e.toString());
        }

        StringTokenizer st = new StringTokenizer(ret);
        Integer allw = Integer.parseInt(st.nextToken());
        return allw;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Calling superclass onCreate method
        super.onCreate(savedInstanceState);

        //Setting view
        setContentView(R.layout.activity_update_expense);

        //Initializing variables for input controls
        final TextView mCurrentAllowanceValueView = findViewById(R.id.value_current_balance);
        final EditText mAmountSpentView = findViewById(R.id.value_amount_spent);
        Button mConfirmButton = findViewById(R.id.button_confirm);

        //Retrieving value from local file
        mCurrentAllowance = readFromFile(this);
        Log.d("MyAllowance","Retrieved value "+mCurrentAllowance.toString());

        //Setting value to UI
        mCurrentAllowanceValueView.setText(mCurrentAllowance.toString());

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting value in java variable from UI
                String mAmountSpent  = mAmountSpentView.getText().toString();
                if(!mAmountSpent.isEmpty()) {
                    //Conversion from string to integer
                   int mAmountSpentInt = Integer.parseInt(mAmountSpent);

                   //Validating input
                   Boolean IsValid = CheckAmounts(mCurrentAllowance, mAmountSpentInt, mAmountSpentView);

                   if (IsValid) {

                        //Updating value and saving value to local file
                        mCurrentAllowance = mCurrentAllowance - mAmountSpentInt;
                        mCurrentAllowanceValueView.setText(mCurrentAllowance.toString());
                        mUpdatedAllowance = mCurrentAllowance + " as of " + new java.util.Date().toString();
                        updateAllowanceToFile(mUpdatedAllowance.toString(), UpdateExpense.this);
                        Log.d("MyAllowance", "Updated value " + mUpdatedAllowance.toString());

                        //Setting and calling MainActivity intent
                        Intent mIntent = new Intent(UpdateExpense.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                    }
                }

            }
        });
    }
        //Function to check whether inputs follow specific criteria
        private Boolean CheckAmounts(int currentAllowance, int amountSpent, EditText amountSpentView){
        //Criteria to be checked-expense is non-zero and less than current balance
            if(currentAllowance<amountSpent){
                amountSpentView.setError("Amount exceeds current balance!");
                return false;
            }
            if(amountSpent==0){
                amountSpentView.setError("Enter non-zero amount!");
                return false;
            }
            return true;
        }
}
