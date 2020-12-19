/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.iotapptest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.helper.DbHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class AppTest {

	private MqttHandler handler;	
	public LinearLayout m_tableLayout;
	Context m_context;
	Boolean flag=false;
	int countRows;
	Boolean firstTimeFlag=false;
	public AppTest()
	{
		
		
	}
	/**
	 * @param args
	 */
	

	/**
	 * Run the app
	 */
	public void doApp(Context context,LinearLayout tLayout) {
	
		
		m_tableLayout = tLayout;
		m_context = context;
		
		//Read properties from the conf file
		/*Properties props = MqttUtil.readProperties("/sdcard/app_SmartcityPlugin.conf");

		MqttUtil.ORG = props.getProperty("org");
		MqttUtil.ID  = props.getProperty("appid");
		MqttUtil.AUTHMETHOD  = props.getProperty("key");
		MqttUtil.AUTHTOKEN = props.getProperty("token");
		//isSSL property
		MqttUtil.SSLSTR = props.getProperty("isSSL");*/
	
		boolean isSSL = false;
		if (MqttUtil.SSLSTR.equals("T")) {
			MqttUtil.ISSSL = true;
		}
		
		Log.v(MqttUtil.LOGTAG,"org: " + MqttUtil.ORG);
		Log.v(MqttUtil.LOGTAG,"id: " + MqttUtil.ID);
		Log.v(MqttUtil.LOGTAG,"authmethod: " + MqttUtil.AUTHMETHOD);
		Log.v(MqttUtil.LOGTAG,"authtoken" + MqttUtil.AUTHTOKEN);
		Log.v(MqttUtil.LOGTAG,"isSSL: " + isSSL);
		
		//Format: a:<orgid>:<app-id>
		MqttUtil.CLIENTID = "a:" +  MqttUtil.ORG + ":" + MqttUtil.ID;
		MqttUtil.SERVER_HOST = MqttUtil.ORG + MqttUtil.SERVER_SUFFIX;

		handler = new AppMqttHandler(context);
		handler.connect(MqttUtil.SERVER_HOST, MqttUtil.CLIENTID, MqttUtil.AUTHMETHOD,  MqttUtil.AUTHTOKEN, MqttUtil.ISSSL);
		
		/*Button button = (Button)m_tableLayout.findViewById(R.id.Refresh_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.reconnect(MqttUtil.SERVER_HOST, MqttUtil.CLIENTID, MqttUtil.AUTHMETHOD,  MqttUtil.AUTHTOKEN, MqttUtil.ISSSL);		
				
			}
		});*/
	}

	/**
	 * This class implements as the application MqttHandler
	 *
	 */
	public class AppMqttHandler extends MqttHandler {
		
		//Pattern to check whether the events comes from a device for an event
		Pattern pattern = Pattern.compile("iot-2/type/"
				+ MqttUtil.DEFAULT_DEVICE_TYPE + "/id/(.+)/evt/"
				+ MqttUtil.DEFAULT_EVENT_ID + "/fmt/json");
		
		public AppMqttHandler(Context context)
		{
			super(context);
			Log.v("IOTTestApp"," Constructor AppMqttHandler");
			
		}
		
		/**
		 * Once a subscribed message is received
		 */
		@Override
		public void messageArrived(String topic, MqttMessage mqttMessage)
				throws Exception {
			super.messageArrived(topic, mqttMessage);

			Matcher matcher = pattern.matcher(topic);
			if (matcher.matches()) {
				String deviceid = matcher.group(1);
				String payload = new String(mqttMessage.getPayload());
				Log.v(MqttUtil.LOGTAG, "in function msg arvd payload is .........."+payload);
				//Parse the payload in Json Format
				JSONObject jsonObject = new JSONObject(payload);
				JSONObject contObj = jsonObject.getJSONObject("d");
				
				 contObj.keys();
				 Iterator<String> keys = contObj.keys();
				 int val_count = 1; //ID of value of item
				 int consumed =100; //ID of value of item consumed
				 Log.v("IOTTestApp", "lenght of contOBJ is .................."+contObj.length());
				 
				 if(firstTimeFlag==false)
				 {
					 
					 m_tableLayout.removeViews(3, 3);
					 firstTimeFlag = true;
				 }
				 
				 if (flag==true && countRows != contObj.length())
				 {
					 
					 flag = false;
					 m_tableLayout.removeViews(3, countRows);
					 //countRows = contObj.length();
				 }
				 if (flag == false)
				 while( keys.hasNext() ) {
					 
				     String key = (String)keys.next();
				     Log.v("IOTTestApp", "JSON object key is "+ key + "value is "+ contObj.getInt(key));
				     
				     
	
				    	 LinearLayout layout2 = new LinearLayout(m_context);
					     
					     layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
					     layout2.setOrientation(LinearLayout.HORIZONTAL);
	
					     
					     
					     TextView tv = (TextView) View.inflate(m_context, R.layout.firsttextview, null);
					     tv.setText(key);
					    /* Button tv = new Button(m_context);
					     tv.setText(key);
					     tv.setTextColor(Color.WHITE);
					     tv.setBackgroundColor(Color.TRANSPARENT);
					     
					     
					     */
					     layout2.addView(tv);
					     TextView tv1 = (TextView) View.inflate(m_context, R.layout.firsttextview, null);
					     tv1.setText("100%");
					     tv1.setId(val_count);
					     layout2.addView(tv1);
					     
					     TextView tv2 = (TextView) View.inflate(m_context, R.layout.firsttextview, null);
					     tv2.setText("0%");
					     tv2.setId(consumed);
					     //tv2.setTextColor(Color.GREEN);
					     //tv2.setBackgroundResource(R.drawable.rounded_border);
					     layout2.addView(tv2);
					     
					     layout2.setClickable(true);
					     
					     layout2.setOnClickListener(new OnClickListener() {
					      
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Log.v("IOTTestApp", "layout clicked ..........................");
							}
						});
					     m_tableLayout.addView(layout2, 3);
					     //m_tableLayout.addView(layout2);
					     val_count = val_count + 1;
					     consumed = consumed + 1;
					 
				 }
				 countRows = contObj.length();
				flag = true;
				val_count = 1;
				consumed = 100;
				keys = contObj.keys();
				 while( keys.hasNext() ) {//check for all possible key of json eg rice, wheat
				     String key = (String)keys.next();		//contains current key
				     //Log.v("IOTTestApp", "JSON object key is "+ key + "value is "+ contObj.getInt(key));
				     
				     
				     TextView rice = (TextView) m_tableLayout.findViewById(val_count);
						String strRice = rice.getText().toString();
						
						//int countRiceInit = rice.getText().
						//rice.setText(contObj.getInt(key));
						rice.setText(contObj.getString(key));
						
						
						
						TextView RiceDiff = (TextView) m_tableLayout.findViewById(consumed);
						int Diff = 100 - contObj.getInt(key);
						RiceDiff.setText("");
						//if (Diff>=50)
						//	RiceDiff.setTextColor(Color.RED);
						//else
						//	RiceDiff.setTextColor(Color.GREEN);
						//Inserting the data in items database
						//String sql="Create table if not exists items(name text,value text,consumed text,date text)";
						/*DbHelper db=DbHelper.getInstance(m_context);
					 	 db.open();
					 	String name= key;
						String value = ""+contObj.getInt(key);
						Date date = new Date();
						date.toString();
						String sql1="insert into items(name,value,consumed,date) values(\""+ name + "\",\"" + value + "\",\""+ Diff + "\",\""+ date.toString() + "\")";
						db.execSQl(sql1, null);*/
						
						
						val_count = val_count + 1;
						consumed = consumed + 1;
				     
				     
				     
				     
				 }
				/*int count = contObj.getInt("Rice");
				int sugarCount = contObj.getInt("Sugar");
				int wheatCount = contObj.getInt("Wheat");
				
				Log.v("IOTTestApp","Received rice " + count + " kg from device "
						+ deviceid);
				Log.v("IOTTestApp","Received Sugar " + sugarCount + " kg from device "
						+ deviceid);
				Log.v("IOTTestApp","Received Wheat " + wheatCount + " kg from device "
						+ deviceid);
				
				TextView rice = (TextView) m_tableLayout.findViewById(R.id.RiceValue);
				String strRice = rice.getText().toString();
				
				//int countRiceInit = rice.getText().
				rice.setText(count+" gm");
				
				TextView Sugar = (TextView) m_tableLayout.findViewById(R.id.SugarValue);
				String strSugar = Sugar.getText().toString();
				Sugar.setText(count+" gm");
				
				TextView Wheat = (TextView) m_tableLayout.findViewById(R.id.WheatValue);
				String strWheat = Wheat.getText().toString();
				Wheat.setText(count+" gm");
				
				TextView RiceDiff = (TextView) m_tableLayout.findViewById(R.id.Rice_diff);
				int Diff = 1000 - count;
				RiceDiff.setText(Diff+" gm");
				if (Diff>=500)
					RiceDiff.setTextColor(Color.RED);
					
				
				TextView SugarDiff = (TextView) m_tableLayout.findViewById(R.id.Sugar_diff);
				Diff = 1000 - sugarCount;
				SugarDiff.setText(Diff+" gm");
				if (Diff>=500)
					SugarDiff.setTextColor(Color.RED);
				
				
				TextView WheatDiff = (TextView) m_tableLayout.findViewById(R.id.Wheat_diff);
				
				Diff = 1000 - wheatCount;
				WheatDiff.setText(Diff+" gm");
				if (Diff>=500)
					WheatDiff.setTextColor(Color.RED);*/
			
				/*if (count >= 4) {
					new ResetCountThread(deviceid, 0).start();
				}*/
			}
		}
	}

	/**
	 * A thread to reset the count
	 *
	 */
	private class ResetCountThread extends Thread {
		private String deviceid = null;
		private int count = 0;

		public ResetCountThread(String deviceid, int count) {
			this.deviceid = deviceid;
			this.count = count;
		}

		public void run() {
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("cmd", "reset");
				jsonObj.put("count", count);
				jsonObj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println("Reset count for device " + deviceid);
			
			//Publish command to one specific device
			//iot-2/type/<type-id>/id/<device-id>/cmd/<cmd-id>/fmt/<format-id>
			handler.publish("iot-2/type/" + MqttUtil.DEFAULT_DEVICE_TYPE
					+ "/id/" + deviceid + "/cmd/" + MqttUtil.DEFAULT_CMD_ID
					+ "/fmt/json", jsonObj.toString(), false, 0);
		}
	}

}
