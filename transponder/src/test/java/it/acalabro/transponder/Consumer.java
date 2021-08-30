package it.acalabro.transponder;

import javax.jms.JMSException;

import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.consumer.ConcernAbstractConsumer;
import it.acalabro.transponder.event.ConcernEvaluationRequestEvent;
import it.acalabro.transponder.eventListener.ChannelProperties;

public class Consumer {

	public static void main(String[] args) throws InterruptedException {
		String brokerUrl = "tcp://localhost:61616";
		//String brokerUrl = "tcp://146.48.84.187:61616";

		ConcernAbstractConsumer cons = new ConcernAbstractConsumer();
		try {
			cons.init(brokerUrl,"vera", "griselda");
			ConcernEvaluationRequestEvent<String> ruleToEvaluate = 
					new ConcernEvaluationRequestEvent<String>(
							System.currentTimeMillis(),"Consumer-ONE", "monitoring", "session-ONE", "2392397923", "EvaluationRequest", "//created on: May 13, 2021\n"
									+ "package it.cnr.isti.labsedc.concern.event;\n"
									+ "import it.cnr.isti.labsedc.concern.event.ConcernAbstractEvent;\n"
									+ "import it.cnr.isti.labsedc.concern.event.ConcernArduinoEvent;\n"
									+ "import it.cnr.isti.labsedc.concern.notification.NotificationManager;\n"
									+ "\n"
									+ "dialect \"java\"\n"
									+ "\n"
									+ "declare ConcernArduinoEvent\n"
									+ "    @role( event )\n"
									+ "    @timestamp( timestamp )\n"
									+ "end\n"
									+ "            \n"
									+ "		rule \"SERVICE_NAME_MACHINE_IP_INFRASTRUCTUREVIOLATION\"\n"
									+ "		no-loop\n"
									+ "		salience 10\n"
									+ "		dialect \"java\"\n"
									+ "		when\n"
									+ "			$aEvent : ConcernArduinoEvent(\n"
									+ "			this.getName == \"SLA Alert\");\n"
									+ "			\n"
									+ "			$bEvent : ConcernArduinoEvent(\n"
									+ "			this.getName == \"load_one\",\n"
									+ "			this after[0,10s] $aEvent);\n"
									+ "		then\n"
									+ "			NotificationManager.NotifyToConsumer(\"consumerName\", \"retracted\");	\n"
									+ "			retract($aEvent);\n"
									+ "			retract($bEvent);	\n"
									+ "		end"
									+ "", CepType.DROOLS, "monitor a after b", ChannelProperties.GENERICREQUESTS);
			cons.sendEvaluationRequest("DROOLS-InstanceOne", ruleToEvaluate);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		System.out.println("Rule to be monitored Sent");
	}
}
