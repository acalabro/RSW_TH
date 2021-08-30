package it.acalabro.transponder.event;

import it.acalabro.transponder.cep.CepType;

public class ConcernMQTTMobileAppEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private int numberOfPersonInvolved;
	private String distressHints;
	
	public ConcernMQTTMobileAppEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T distressSituationID,
			CepType type,
			int numberOfPersonInvolved,
			String distressHints) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, distressSituationID, type);
		this.numberOfPersonInvolved = numberOfPersonInvolved;	
		this.distressHints = distressHints;
	}

	public void setNumberOfPersonInvolved(int numberOfPersonInvolved) {
		this.numberOfPersonInvolved = numberOfPersonInvolved;
	}
	
	public int getNumberOfPersonInvolved() {
		return this.numberOfPersonInvolved;
	}

	public void setDistressHints(String distressHints) {
		this.distressHints = distressHints;
	}
	
	public String getDistressHints() {
		return this.distressHints;
	}
}
