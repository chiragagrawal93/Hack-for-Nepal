package com.explora;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    String REGISTRATION_URL = "http://192.168.2.3:8000/exploraapp/register/";
    EditText name;
    EditText phoneNumber;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }


    void initialize()
    {
        App.currentActivity = this;
        name = (EditText)findViewById(R.id.name);
        phoneNumber = (EditText)findViewById(R.id.phone_number);
        register = (Button)findViewById(R.id.button);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button)
        {

            String nameString = name.getText().toString();
            String phoneString = phoneNumber.getText().toString();

            if(!(nameString.equals("")))
            {
                if((phoneString.length()==10))
                {
                    REGISTRATION_URL += nameString + "/" + phoneString;
                    Log.e("url",REGISTRATION_URL);
                    final ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);
                    asyncDialog.setMessage("Registering. Please wait...");
                    asyncDialog.setCanceledOnTouchOutside(false);
                    //show dialog
                    asyncDialog.show();
                    Utils.fetchDataFromUrl(REGISTRATION_URL, new Utils.HttpListener() {
                        @Override
                        public void success(String data) {
                            App.currentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(App.currentActivity, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            });

                            asyncDialog.dismiss();
                        }

                        @Override
                        public void failed(String reason) {
                            Log.e("Registration","failed");
                            asyncDialog.dismiss();
                        }
                    });
                }
            }
        }
    }
}
