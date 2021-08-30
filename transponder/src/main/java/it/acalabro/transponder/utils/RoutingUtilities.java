package it.acalabro.transponder.utils;

import java.util.Random;

import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.event.ConcernAbstractEvent;
import it.acalabro.transponder.event.ConcernEvaluationRequestEvent;
import it.acalabro.transponder.eventListener.ChannelProperties;
import it.acalabro.transponder.register.ChannelsManagementRegistry;
import it.acalabro.transponder.register.TopicAndProperties;

public class RoutingUtilities {

	private static CepType cepType;
	private static ChannelProperties requestedProperties;

	public static TopicAndProperties BestCepSelectionForRules(ConcernEvaluationRequestEvent<?> message) {

		cepType = message.getCepType();
		requestedProperties = message.getPropertyRequested();
		TopicAndProperties localQaP;

		for (int i = 0; i<ChannelsManagementRegistry.ActiveCep.size();i++) {
			localQaP = (TopicAndProperties) ChannelsManagementRegistry.ActiveCep.keySet().toArray()[i];
			if (cepType ==  localQaP.getLocalCepType() && requestedProperties ==  localQaP.getServiceChannelProperties()) {
				return localQaP;
					}
			}
		return null;
		}
	
	public static TopicAndProperties BestCepSelectionForEvents(ConcernAbstractEvent<?> message) {

		cepType = message.getCepType();
		TopicAndProperties localQaP;
		int maxSize = ChannelsManagementRegistry.ActiveCep.size();
		for (int i = 0; i<maxSize;i++) {
			Random rand = new Random();
			int theRand = rand.ints(0, maxSize)
		      .findFirst()
		      .getAsInt();
			localQaP = (TopicAndProperties) ChannelsManagementRegistry.ActiveCep.keySet().toArray()[theRand];
			if (cepType ==  localQaP.getLocalCepType()) {
				return localQaP;
					}
			}
		return null;
		}
	
}
