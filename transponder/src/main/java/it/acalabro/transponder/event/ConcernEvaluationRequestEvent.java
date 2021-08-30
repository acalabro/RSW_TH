package it.acalabro.transponder.event;

import it.acalabro.transponder.cep.CepType;
import it.acalabro.transponder.eventListener.ChannelProperties;

  public class ConcernEvaluationRequestEvent<T> extends ConcernAbstractEvent<T> {
    
	private static final long serialVersionUID = 6545740721731539243L;
	private String evaluationRuleName;
	private ChannelProperties propertyRequested;

	public ConcernEvaluationRequestEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T ruleData,
			CepType type,
			String evaluationRuleName,
			ChannelProperties propertyRequested) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, ruleData, type);
		this.setEvaluationRuleName(evaluationRuleName);
		this.setPropertyRequested(propertyRequested);
	}

	public ChannelProperties getPropertyRequested() {
		return propertyRequested;
	}

	public void setPropertyRequested(ChannelProperties propertyRequested) {
		this.propertyRequested = propertyRequested;
	}

	public String getEvaluationRuleName() {
		return evaluationRuleName;
	}

	public void setEvaluationRuleName(String evaluationRuleName) {
		this.evaluationRuleName = evaluationRuleName;
	}
}