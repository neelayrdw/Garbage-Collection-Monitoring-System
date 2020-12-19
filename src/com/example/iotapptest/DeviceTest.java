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
import java.util.Properties;

import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.content.Context;
import android.util.Log;

public class DeviceTest {

	private int count = 0;
	private int totalcount = 0;
	private MqttHandler handler = null;


	/**
	 * Run the device
	 */
	public void doDevice(Context context) {
		//Read properties from the conf file
		Properties props = MqttUtil.readProperties("/sdcard/device.conf");

		String org = props.getProperty("org");
		String id = props.getProperty("deviceid");
		String authmethod = "use-token-auth";
		String authtoken = props.getProperty("token");
		//isSSL property
		String sslStr = props.getProperty("isSSL");
		boolean isSSL = false;
		if (sslStr.equals("T")) {
			isSSL = true;
		}

		String tag = "IOTTestApp";
		Log.v(tag,"org: " + org);
		Log.v(tag,"id: " + id);
		Log.v(tag,"authmethod: " + authmethod);
		Log.v(tag,"authtoken: " + authtoken);
		Log.v(tag,"isSSL: " + isSSL);

		String serverHost = org + MqttUtil.SERVER_SUFFIX;

		//Format: d:<orgid>:<type-id>:<divice-id>
		String clientId = "d:" + org + ":" + MqttUtil.DEFAULT_DEVICE_TYPE + ":"
				+ id;
		handler = new DeviceMqttHandler(context);
		handler.connect(serverHost, clientId, authmethod, authtoken, isSSL);

		//Subscribe the Command events
		//iot-2/cmd/<cmd-type>/fmt/<format-id>
		handler.subscribe("iot-2/cmd/" + MqttUtil.DEFAULT_CMD_ID + "/fmt/json",
				0);

		while (totalcount < 20) {
			
			//Format the Json String
			JSONObject contObj = new JSONObject();
			JSONObject jsonObj = new JSONObject();
			try {
				contObj.put("Rice", count);
				contObj.put("Sugar", count);
				contObj.put("Wheat", count);
				//contObj.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						//.format(new Date()));
				jsonObj.put("d", contObj);
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			Log.v(tag,"Send count as " + count);
			Log.v(tag,"Payload is "+jsonObj.toString());
			//Publish device events to the app
			//iot-2/evt/<event-id>/fmt/<format> 
			handler.publish("iot-2/evt/" + MqttUtil.DEFAULT_EVENT_ID
					+ "/fmt/json", jsonObj.toString(), false, 0);

			count++;
			totalcount++;

			try {
				Thread.sleep(15*1000 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Log.v(tag,"Max Count reached, try to disconnect");
		//handler.disconnect();
	}

	/**
	 * This class implements as the device MqttHandler
	 *
	 */
	private class DeviceMqttHandler extends MqttHandler {
		
		public DeviceMqttHandler(Context context)
		{
			super(context);
			Log.v("IOTTestApp"," Constructor AppMqttHandler");
			
		}

		@Override
		public void messageArrived(String topic, MqttMessage mqttMessage)
				throws Exception {
			super.messageArrived(topic, mqttMessage);
			
			//Check whether the event is a command event from app
			if (topic.equals("iot-2/cmd/" + MqttUtil.DEFAULT_CMD_ID
					+ "/fmt/json")) {
				String payload = new String(mqttMessage.getPayload());
				JSONObject jsonObject = new JSONObject(payload);
				String cmd = jsonObject.getString("cmd");
				//Reset the count
				if (cmd != null && cmd.equals("reset")) {
					int resetcount = jsonObject.getInt("count");
					count = resetcount;
					System.out.println("Count is reset to " + resetcount);
				}
			}
		}
	}

}
