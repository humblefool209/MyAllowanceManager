package com.humblefool.myallowancemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class UpdateAllowance  extends AppCompatActivity {
    private Integer mCurrentAllowance;
    private Context mContext;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_allowance);
        final EditText mReceivedAllowanceView = findViewById(R.id.value_allowance_received);
        final TextView mCurrentAllowanceValueView = findViewById(R.id.value_current_balance);
        Button mConfirmButton = findViewById(R.id.button_confirm);

        mCurrentAllowance = readFromFile(this);
        Log.d("MyAllowance","Retrieved value "+mCurrentAllowance);

        mCurrentAllowanceValueView.setText(mCurrentAllowance.toString());

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mAllowanceReceived = mReceivedAllowanceView.getText().toString();
                if (mAllowanceReceived != "") {
                    int mAllowanceReceivedInt = Integer.parseInt(mAllowanceReceived);
                    mCurrentAllowance = mCurrentAllowance + mAllowanceReceivedInt;
                    mCurrentAllowanceValueView.setText(mCurrentAllowance.toString());
                    mUpdatedAllowance = mCurrentAllowance.toString() + " as of "+ new java.util.Date().toString();
                    updateAllowanceToFile(mUpdatedAllowance,UpdateAllowance.this);
                    Log.d("MyAllowance","Updated value "+mUpdatedAllowance);

                }

                Intent mIntent = new Intent(UpdateAllowance.this,MainActivity.class);
                startActivity(mIntent);
                finish();

            }
        });


    }
}
