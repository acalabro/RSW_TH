package it.acalabro.transponder.cep;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import it.acalabro.transponder.eventListener.ConcernMqttCallBack;

public class MqttListener {

	public static void main(String[] args) {
	
	//MOSQUITOTEST
	try {
		MqttClient client2 = new MqttClient("tcp://0.0.0.0:1883", MqttClient.generateClientId());
		client2.setCallback( new ConcernMqttCallBack() );
		client2.connect();
		client2.subscribe("iot_data"); 
		System.out.println("listening");
	} catch (MqttException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
