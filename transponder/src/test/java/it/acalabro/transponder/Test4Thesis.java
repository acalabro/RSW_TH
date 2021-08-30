package it.acalabro.transponder;

import org.json.JSONObject;

import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.event.ConcernMQTTMobileAppEvent;

public class Test4Thesis {

	public static void main (String[] args) {
		
		ConcernMQTTMobileAppEvent<String> event = new ConcernMQTTMobileAppEvent<String>(
			    System.currentTimeMillis(), "MobileApp", "monitoring", 
			    "emergencyDataSession", "none", "8c:8d:ab:10:40:bd", "3", 
				CepType.DROOLS, 2," ");
		
		
		JSONObject jsonObj = new JSONObject( event );
        System.out.println( jsonObj );
	}
}



//private ConcernWiFiEvent<String> packEvent(String senderID, ) {
//
//	 ConcernWiFiEvent<String> event = new ConcernWiFiEvent<String>(System.currentTimeMillis(),
//			"WiFi-Probe", "monitoring", "emergencyDataSession", "none", 
//			"device found", "2412Mhz", CepType.DROOLS, "8c:8d:ab:10:40:bd",
//			PacketType.PROBE_REQUEST,-38f);
//	 return event;
//}

//private static void testJson() {
//	ConcernWiFiEvent<String> event = new ConcernWiFiEvent<String>(System.currentTimeMillis(),
//			"WiFi-Probe", "monitoring", "emergencyDataSession", "none", 
//			"device found", "2412Mhz", CepType.DROOLS, "8c:8d:ab:10:40:bd",
//			PacketType.PROBE_REQUEST,-38f);
//	JSONObject jsonObj = new JSONObject( event );
//    System.out.println( jsonObj );		
//}