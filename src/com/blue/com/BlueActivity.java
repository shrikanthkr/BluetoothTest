package com.blue.com;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class BlueActivity extends Activity {
	public BlueActivity(){
		super();
	}
	
	Button client,server;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        client=(Button)findViewById(R.id.client_button);
        server=(Button)findViewById(R.id.server_button);
        Log.i("TAG","button_created");
        client.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent x=new Intent(BlueActivity.this,SendRequestActivity.class);
				startActivity(x);
			}
		});
        server.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent y=new Intent(BlueActivity.this,ListenerActivity.class);
				startActivity(y);
			}
		});
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_blue, menu);
        return true;
    }*/
}
