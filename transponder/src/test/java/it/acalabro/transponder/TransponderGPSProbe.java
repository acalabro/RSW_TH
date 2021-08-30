package it.acalabro.transponder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;

import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.event.ConcernGpsEvent;
import it.acalabro.transponder.event.GpsType;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TransponderGPSProbe {

	static Session session;
	static MessageProducer producer;
	static String macAddress;
	static float receivedDb;
	static String device = "/dev/ttyACM0";

	public static void main(String[] args) throws InterruptedException {
		String brokerUrl = "tcp://0.0.0.0:61616";
		//String username = "vera";
		//String password = "griselda";
		String topicName = "DROOLS-InstanceOne";
		//args 0 = username, args 1 = password, args 2 = brokerUrl, args 3 = topicName, args 4 = device
		
		printHello();
		try {		
	//		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(args[0], args[1], args[2]);
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
			Connection connection = connectionFactory.createConnection();
			
	        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	        
	        Topic topic = session.createTopic(topicName);
	        //Topic topic = session.createTopic(args[3]);
	        
	        producer = session.createProducer(topic); 
	        
	        loopThread();
 
		} catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}   
	}	
		
	private static void loopThread() {
		//final Process p = Runtime.getRuntime().exec("tcpdump -i "+ args[4] + " -e type mgt");
		try {	
			//Process p = Runtime.getRuntime().exec("cat " + args[4]);
			Process p = Runtime.getRuntime().exec("cat " + device);
		new Thread(new Runnable() {
		    public void run() {
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line = null;
		        String[] results;
		        try {
		            while ((line = input.readLine()) != null)
		            	
		            	if (line != null && line.startsWith("$GPGLL")) //  || line.contains("signal:"))
	                	{
		            		results = line.split(",");
		            		if (results[6].compareTo("A") == 0) { //gps signal is valid
		            			
		            		}	                	
		            	sendMessage(session, new ConcernGpsEvent<String>(
		            			System.currentTimeMillis(),
		            			"GPS-Probe",
		            			"Monitor",
		            			this.getClass().getName()+"1",
		            			"0",
		            			"GPSPosition",
		            			"Satellite OK",
		            			CepType.DROOLS,
		            			results[1]+","+results[3],GpsType.GLONASS)
		            			,producer);
	                	}
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}).start();

			p.waitFor();
		} catch (InterruptedException | IOException e1) {
			e1.printStackTrace();
		}		
	}

	private static void sendMessage(Session session, ConcernGpsEvent<String> event, MessageProducer producer) {
		try {
			ObjectMessage msg = session.createObjectMessage();
			msg.setObject(event);
		producer.send(msg);
		System.out.println("Message Sent");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private static void printHello() {
System.out.println(" _____ ______  _____  ______          _          \n"
		+ "|  __ \\| ___ \\/  ___| | ___ \\        | |         \n"
		+ "| |  \\/| |_/ /\\ `--.  | |_/ / __ ___ | |__   ___ \n"
		+ "| | __ |  __/  `--. \\ |  __/ '__/ _ \\| '_ \\ / _ \\\n"
		+ "| |_\\ \\| |    /\\__/ / | |  | | | (_) | |_) |  __/\n"
		+ " \\____/\\_|    \\____/  \\_|  |_|  \\___/|_.__/ \\___|\n"
		+ "                                                 \n");
	}
}
