package it.acalabro.transponder;

import java.util.HashMap;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import it.acalabro.transponder.broker.ActiveMQBrokerManager;
import it.acalabro.transponder.broker.BrokerManager;
import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.cep.ComplexEventProcessorManager;
import it.acalabro.transponder.cep.DroolsComplexEventProcessorManager;
import it.acalabro.transponder.notification.NotificationManager;
import it.acalabro.transponder.register.ChannelsManagementRegistry;
import it.acalabro.transponder.requestListener.ServiceListenerManager;
import it.acalabro.transponder.storage.MySQLStorageController;

public class ConcernApp
{
	private static BrokerManager broker;
	private static ComplexEventProcessorManager cepMan;
	private static NotificationManager notificationManager;
	private static ChannelsManagementRegistry channelRegistry;
	private static MySQLStorageController storageManager;
	
	private static String brokerUrlJMS;
	private static Long maxMemoryUsage;
	private static Long maxCacheUsage;
	public static ActiveMQConnectionFactory factory;
    public static Logger logger = LogManager.getRootLogger();
	private static final boolean SHUTDOWN = false;
	public static HashMap<String, Boolean> componentStarted = new HashMap<String, Boolean>();
	private static String username;
	private static String password;
	private static boolean LOCALBROKER = true;
	private static boolean runningInJMS = true;
	private static String mqttBrokerUrl;
	private static MqttClient listenerClient;

    public static void main( String[] args ) throws InterruptedException
    {
    	username = "vera";
    	password = "griselda";
    	
    	//brokerUrl = "tcp://sedc-nethd.isti.cnr.it:49195";
    	if(runningInJMS) {
    	brokerUrlJMS = "tcp://0.0.0.0:61616";
    	maxMemoryUsage = 128000l;
    	maxCacheUsage = 128000l;
    	factory = new ActiveMQConnectionFactory(brokerUrlJMS);
    	logger.info("Starting components");
    	StartComponents(factory, brokerUrlJMS, maxMemoryUsage, maxCacheUsage);
    	}
    	else{
    		mqttBrokerUrl="tcp://localhost:1183";
    		logger.info("Starting components");
    		StartComponents(listenerClient,mqttBrokerUrl, "serotoninData");
    	}
    }

    public static void StartComponents(MqttClient listenerClient, String mqttBrokerUrl, String topic) {
		try {
			channelRegistry = new ChannelsManagementRegistry();

	    	logger.debug("Channels Management Registry created");
	    	System.out.println("PATH: " + System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl");

	    	String CEPInstanceName = "InstanceOne";
	    	listenerClient = new MqttClient("tcp://0.0.0.0:1883",MqttClient.generateClientId());
	    	ChannelsManagementRegistry.setMqttClient(listenerClient);
	    	ChannelsManagementRegistry.setMqttChannel(topic);
	    	
	    	storageManager = new MySQLStorageController();
	    	storageManager.connectToDB();
	    	  	
	    	notificationManager = new NotificationManager();
	    	notificationManager.start();
	    	
	    	//STARTING CEP ONE
	    	cepMan = new DroolsComplexEventProcessorManager(
	    			CEPInstanceName,
	    			System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl",
	    			username, 
	    			password, CepType.DROOLS,
	    			runningInJMS);
	    	cepMan.start();

	    	while (!cepMan.cepHasCompletedStartup()) {
	    		System.out.println("wait for First CEP start");
	    		Thread.sleep(100);
	    	}
	    	
	    	if(SHUTDOWN) {
		    	ServiceListenerManager.killAllServiceListeners();
		    	
		    	System.exit(0);
	    	}
		} catch (MqttException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
	public static void StartComponents(ActiveMQConnectionFactory factory, String brokerUrl, long maxMemoryUsage, long maxCacheUsage) throws InterruptedException {

		//storage = new InfluxDBStorageController();
		if (LOCALBROKER) {
			broker = new ActiveMQBrokerManager(brokerUrl, maxMemoryUsage, maxCacheUsage, username, password);
			logger = LogManager.getLogger(ConcernApp.class);
			logger.debug(ConcernApp.class.getSimpleName() + " is launching the broker.");
			broker.run();
			logger.debug(ConcernApp.class.getSimpleName() + " broker launched.");
		} 
		factory = new ActiveMQConnectionFactory(username, password, brokerUrl);
		
		channelRegistry = new ChannelsManagementRegistry();

    	logger.debug("Channels Management Registry created");
    	System.out.println("PATH: " + System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl");
    	channelRegistry.setConnectionFactory(factory);

    	storageManager = new MySQLStorageController();
    	storageManager.connectToDB();
    	  	
    	notificationManager = new NotificationManager();
    	notificationManager.start();
    	
    	//STARTING CEP ONE
    	cepMan = new DroolsComplexEventProcessorManager(
    			"InstanceOne",
    			System.getProperty("user.dir")+ "/src/main/resources/startupRule.drl",
    			username, 
    			password, CepType.DROOLS,
    			runningInJMS);
    	cepMan.start();

    	while (!cepMan.cepHasCompletedStartup()) {
    		System.out.println("wait for First CEP start");
    		Thread.sleep(100);
    	}
    	
    	if(SHUTDOWN) {
	    	ServiceListenerManager.killAllServiceListeners();
	    	ActiveMQBrokerManager.StopActiveMQBroker();
	    	System.exit(0);
    	}
	}
}
