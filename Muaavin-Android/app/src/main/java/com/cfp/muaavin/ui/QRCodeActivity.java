package com.cfp.muaavin.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cfp.muaavin.helper.PrefManager;

public class QRCodeActivity extends ActionBarActivity {

    EditText etCode;
    Button btnVerify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.appTheme)/*Color.parseColor("#3b5998")*/);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        etCode = (EditText)findViewById(R.id.editText);
        btnVerify = (Button)findViewById(R.id.btnVerify);

        if(PrefManager.getInstance(getApplicationContext()).isUserAuthenticated())
        {
            Intent intent = new Intent(QRCodeActivity.this,FacebookLoginActivity.class);
            startActivity(intent);
        }

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString();
                if(code!=null&&(!code.equals("")))
                {
                    if(code.equalsIgnoreCase("mmfd.muaavin"))
                    {

                        PrefManager.getInstance(getApplicationContext()).setUserAuthentication(true);
                        Intent intent = new Intent(QRCodeActivity.this,FacebookLoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        etCode.setError("Invalid verification code. Please rescan and enter valid code." );
                    }
                }
                else
                    etCode.setError("Please enter verification code.");
            }
        });

    }

}
