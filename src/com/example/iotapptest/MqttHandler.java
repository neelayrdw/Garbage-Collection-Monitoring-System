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
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.example.iotapptest.ActionListener.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

public class MqttHandler implements MqttCallback {
	private final static String DEFAULT_TCP_PORT = "1883";
	private final static String DEFAULT_SSL_PORT = "8883";
	//private  MqttHandler m_handler;
	private Context m_context; 
	//private MqttClient client = null;
	private MqttAndroidClient client = null;
	
	
	public MqttHandler(Context context) {
		Log.v(MqttUtil.LOGTAG," Constructor MqttHandler");
		m_context = context;
		//m_handler = this;
	}
	public MqttHandler() {

	}

	@Override
	public void connectionLost(Throwable throwable) {
		if (throwable != null) {
			throwable.printStackTrace();
		}
		Log.v(MqttUtil.LOGTAG," connectionLost MqttHandler");
		reconnect(MqttUtil.SERVER_HOST, MqttUtil.CLIENTID, MqttUtil.AUTHMETHOD,  MqttUtil.AUTHTOKEN, MqttUtil.ISSSL);
	}

	/**
	 * One message is successfully published
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		System.out.println(".deliveryComplete() entered");
		Log.v(MqttUtil.LOGTAG," delivery Complete MqttHandler");
	}

	/**
	 * Received one subscribed message
	 */
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage)
			throws Exception {
		String payload = new String(mqttMessage.getPayload());
		Log.v(MqttUtil.LOGTAG,".messageArrived in Handler - Message received on topic "
				+ topic + ": message is " + payload);
	}
	
	public void reconnect(String serverHost, String clientId, String authmethod,
			String authtoken, boolean isSSL) {
	
			String connectionUri = null;
			Log.v(MqttUtil.LOGTAG,"inside isMqttConnected");
			//tcp://<org-id>.messaging.internetofthings.ibmcloud.com:1883
			//ssl://<org-id>.messaging.internetofthings.ibmcloud.com:8883
			if (isSSL) {
				connectionUri = "ssl://" + serverHost + ":" + DEFAULT_SSL_PORT;
			} else {
				connectionUri = "tcp://" + serverHost + ":" + DEFAULT_TCP_PORT;
			}
			Log.v(MqttUtil.LOGTAG,connectionUri);
			if (client != null) {
				/*try {
					client.disconnect();
				} catch (MqttException e) {
					e.printStackTrace();
				}*/
				client = null;
			}		
			
				client = new MqttAndroidClient(m_context, connectionUri, clientId);

				client.setCallback(this);	
			

			// create MqttConnectOptions and set the clean session flag
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setConnectionTimeout(1000);
			options.setKeepAliveInterval(10);

			options.setUserName(authmethod);
			options.setPassword(authtoken.toCharArray());
			
			
			
			

			//If SSL is used, do not forget to use TLSv1.2
			if (isSSL) {
				java.util.Properties sslClientProps = new java.util.Properties();
				sslClientProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
				options.setSSLProperties(sslClientProps);
			}

			final String url = connectionUri;
			Log.v(MqttUtil.LOGTAG,"Before calling Connect");
			
			try {
				client.connect(options,m_context, new ActionListener(m_context, Action.CONNECT, connectionUri,this));
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public void connect(String serverHost, String clientId, String authmethod,
			String authtoken, boolean isSSL) {
		// check if client is already connected
		if (!isMqttConnected()) {
			String connectionUri = null;
			Log.v(MqttUtil.LOGTAG,"inside isMqttConnected");
			//tcp://<org-id>.messaging.internetofthings.ibmcloud.com:1883
			//ssl://<org-id>.messaging.internetofthings.ibmcloud.com:8883
			if (isSSL) {
				connectionUri = "ssl://" + serverHost + ":" + DEFAULT_SSL_PORT;
			} else {
				connectionUri = "tcp://" + serverHost + ":" + DEFAULT_TCP_PORT;
			}
			Log.v(MqttUtil.LOGTAG,connectionUri);
			if (client != null) {
				try {
					client.disconnect();
				} catch (MqttException e) {
					e.printStackTrace();
				}
				client = null;
			}		
			
				client = new MqttAndroidClient(m_context, connectionUri, clientId);

				client.setCallback(this);	
			

			// create MqttConnectOptions and set the clean session flag
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setConnectionTimeout(1000);
			options.setKeepAliveInterval(10);

			options.setUserName(authmethod);
			options.setPassword(authtoken.toCharArray());
			
			
			
			

			//If SSL is used, do not forget to use TLSv1.2
			if (isSSL) {
				java.util.Properties sslClientProps = new java.util.Properties();
				sslClientProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
				options.setSSLProperties(sslClientProps);
			}

			final String url = connectionUri;
			Log.v(MqttUtil.LOGTAG,"Before calling Connect");
			
			try {
				client.connect(options,m_context, new ActionListener(m_context, Action.CONNECT, connectionUri,this));
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Disconnect MqttClient from the MQTT server
	 */
	public void disconnect() {

		// check if client is actually connected
		if (isMqttConnected()) {
			try {
				// disconnect
				client.disconnect(m_context, new ActionListener(m_context, Action.DISCONNECT, null,this));
				Log.v("IOTTestApp", "in disconnect client disconnected");
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Subscribe MqttClient to a topic
	 * 
	 * @param topic
	 *            to subscribe to
	 * @param qos
	 *            to subscribe with
	 */
	public void subscribe(String topic, int qos) {

		// check if client is connected
		if (isMqttConnected()) {
			try {
				client.subscribe(topic, qos,m_context,new ActionListener(m_context, Action.SUBSCRIBE, null,this));
				Log.v(MqttUtil.LOGTAG,"Subscribed: " + topic);

			} catch (MqttException e) {
				e.printStackTrace();
				Log.v(MqttUtil.LOGTAG,"Exception in Subscribe");
			}
		} else {
			connectionLost(null);
			Log.v(MqttUtil.LOGTAG,"Connection Lost in Subscribe");
		}
	}

	/**
	 * Unsubscribe MqttClient from a topic
	 * 
	 * @param topic
	 *            to unsubscribe from
	 */
	public void unsubscribe(String topic) {
		// check if client is connected
		if (isMqttConnected()) {
			try {

				client.unsubscribe(topic);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(null);
		}
	}

	/**
	 * Publish message to a topic
	 * 
	 * @param topic
	 *            to publish the message to
	 * @param message
	 *            JSON object representation as a string
	 * @param retained
	 *            true if retained flag is requred
	 * @param qos
	 *            quality of service (0, 1, 2)
	 */
	public void publish(String topic, String message, boolean retained, int qos) {
		// check if client is connected
		System.out.println("topic published"+topic);
		System.out.println("Message published"+message);
		if (isMqttConnected()) {
			// create a new MqttMessage from the message string
			MqttMessage mqttMsg = new MqttMessage(message.getBytes());
			// set retained flag
			mqttMsg.setRetained(retained);
			// set quality of service
			mqttMsg.setQos(qos);
			try {
				client.publish(topic, mqttMsg);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		} else {
			connectionLost(null);
		}
	}

	/**
	 * Checks if the MQTT client has an active connection
	 * 
	 * @return True if client is connected, false if not.
	 */
	private boolean isMqttConnected() {
		boolean connected = false;
		try {
			if ((client != null) && (client.isConnected())) {
				connected = true;
			}
		} catch (Exception e) {
			// swallowing the exception as it means the client is not connected
		}
		return connected;
	}

}
