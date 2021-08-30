package it.acalabro.transponder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;

import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.event.ConcernWiFiEvent;
import it.acalabro.transponder.event.PacketType;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TransponderWiFiProbe {

	static Session session;
	static MessageProducer producer;
	static String macAddress;
	static String receivedDb;
	static String device;
	
	public static void main(String[] args) throws InterruptedException {
		String username; 
		String password; 
		String brokerUrl;
		String topicName;

		if (args.length<4) {
			System.out.println(""
					+ "USAGE: java -jar TranspoderWifiProbe username"
					+ "password brokerurl topicName device\n"
					+ "Example: java -jar TranspoderWifiProbe vera griselda"
					+ "tcp://0.0.0.0:61616 DROOLS-InstanceOne wlx984827c6f74a");
			System.exit(1);
		}

		username = args[0];
		password = args[1];
		brokerUrl = args[2];
		topicName = args[3];
		device = args[4];
		
		printHello();
		try {		
			ConnectionFactory connectionFactory =
					new ActiveMQConnectionFactory(username, password, brokerUrl);
			Connection connection = connectionFactory.createConnection();
			
	        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	        
	        Topic topic = session.createTopic(topicName);
     
	        producer = session.createProducer(topic); 
	        
	        loopThread();
 
		} catch (JMSException e) {
		e.printStackTrace();
		}   
	}	
		
	private static void loopThread() {
		try {	
			Process p = Runtime.getRuntime().exec(
					"tcpdump -i " + device + " -e type mgt");
		new Thread(new Runnable() {
		    public void run() {
		        BufferedReader input = new BufferedReader(
		        		new InputStreamReader(p.getInputStream()));
		        String line = null;
				String[] results;
				System.out.print("Scan running...");
		        try {
		            while ((line = input.readLine()) != null) {
		            	System.out.print(".");
		            	if (line != null && (
		            			line.contains("Probe Request") || 
		            			line.contains("Beacon") ||
		            			line.contains("Probe Response")))
	                	{
		            		results = line.split(" ");
		            				            		
		            		macAddress = results[16];
		            		receivedDb = results[10];
		            		PacketType packetType = checkPacketType(line);
		            		
	                	
		            	sendMessage(session, new ConcernWiFiEvent<String>(
		            			System.currentTimeMillis(),
		            			"Wi-Fi-Probe", 
		            			"Monitor", 
		            			this.getClass().getName()+"-Session1", 
		            			"",
		            			"tracking","Wi-Fi-trace",
		            			CepType.DROOLS,
		            			macAddress,
		            			packetType,
		            			receivedDb),producer);
	                	}
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }

			private PacketType checkPacketType(String line) {
				if (line.contains("Probe Request")) {
        			return PacketType.PROBE_REQUEST;
        		}
				if (line.contains("Probe Response")) {
        			return PacketType.PROBE_RESPONSE;
        		}
				if (line.contains("Beacon")) {
        			return PacketType.BEACON;
        		}
				return null;
			}
		}).start();

			p.waitFor();
		} catch (InterruptedException | IOException e1) {
			e1.printStackTrace();
		}		
	}

	private static void sendMessage(Session session,
			ConcernWiFiEvent<String> event, 
			MessageProducer producer) {
		try {
			ObjectMessage msg = session.createObjectMessage();
			msg.setObject(event);
		producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
//	private static void testJson() {
//	ConcernWiFiEvent<String> event = new ConcernWiFiEvent<String>(System.currentTimeMillis(),
//			"WiFi-Probe", "monitoring", "emergencyDataSession", "none", 
//			"device found", "2412Mhz", CepType.DROOLS, "8c:8d:ab:10:40:bd",
//			PacketType.PROBE_REQUEST,"-38f");
//	JSONObject jsonObj = new JSONObject( event );
//    System.out.println( jsonObj );		
//}

	private static void printHello() {
		
		System.out.println(" _    _ _       ______ _  ______          _\n"          
		 + "| |  | (_)      |  ___(_) | ___ \\        | |\n"
		 + "| |  | |_ ______| |_   _  | |_/ / __ ___ | |__   ___\n"
		 + "| |/\\| | |______|  _| | | |  __/ '__/ _ \\| '_ \\ / _ \\\n"
		 + "\\  /\\  / |      | |   | | | |  | | | (_) | |_) |  __/\n"
		 + " \\/  \\/|_|      \\_|   |_| \\_|  |_|  \\___/|_.__/ \\___|\n");	                                                      
	}
}
