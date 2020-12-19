package com.example.iotapptest;

import com.ibm.mobile.services.push.IBMPushNotificationListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.table_view);
		//setContentView(R.layout.listlay);
		setContentView(R.layout.runtimelayout);
		//TableLayout tLayout = (TableLayout) findViewById(R.id.table);
		LinearLayout tLayout = (LinearLayout) findViewById(R.id.runtimelayout);
		
		
		new AppTest().doApp(this,tLayout);	
		
	}
	
}