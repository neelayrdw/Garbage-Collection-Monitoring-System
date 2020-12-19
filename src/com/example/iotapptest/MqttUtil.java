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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MqttUtil {
	//Default Mqtt Server Suffix
	public final static String SERVER_SUFFIX = ".messaging.internetofthings.ibmcloud.com";
	
	public final static String DEFAULT_EVENT_ID = "eid";
	public final static String DEFAULT_CMD_ID = "cid";
	public final static String LOGTAG = "IOTTESTAPP";
	public final static String DEFAULT_DEVICE_TYPE = "truckSensor";	
	public final static String SUBTOPIC1 = "iot-2/type/" + DEFAULT_DEVICE_TYPE + "/id/+/mon";
	public final static String SUBTOPIC2 = "iot-2/type/" + MqttUtil.DEFAULT_DEVICE_TYPE	+ "/id/+/evt/" + MqttUtil.DEFAULT_EVENT_ID + "/fmt/json";
	
	/*
	 * 
	 * 
	 * #The org field is the same org field as the Device side
org=mfv4dh

# A unique id you choose it by yourself, maybe, abcdefg123456
appid=smartcityplugin

# The key field from App Keys info you copied previously
key=a-mfv4dh-zuljxofmwb

# The Auth Token field from App Keys info you copied previously
token=jhcjfa668hm3mp1@@z

#T or F, T means using SSL, while F means not
isSSL=F
	 * */

	
	public  static  String ORG = "zmeszr";
	public  static String ID = "001122334455";
	public  static String AUTHMETHOD = "a-zmeszr-8s67udg8ja";//API-KEY
	public  static String AUTHTOKEN = "xn5fQWSUAFrIeEzfE6";
	public  static String CLIENTID = "";
	public  static String SERVER_HOST = "";
	public  static boolean ISSSL = false;
	//isSSL property
	public  static String SSLSTR = "F";
	public  static int pushcount = 0;

	

	/**
	 * This method reads the properties from the config file
	 * @param filePath
	 * @return
	 */
	public static Properties readProperties(String filePath) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					filePath));
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}
}








