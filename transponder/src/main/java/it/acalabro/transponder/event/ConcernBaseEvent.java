package it.acalabro.transponder.event;

import it.acalabro.transponder.cep.CepType;

public class ConcernBaseEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private String aSpecificExtension;
	
	public ConcernBaseEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T ruleData,
			CepType type,
			String aSpecificExtension) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, ruleData, type);
		this.aSpecificExtension = aSpecificExtension;	
	}

	public void setSpecificExtension(String aSpecificExtension) {
		this.aSpecificExtension = aSpecificExtension;
	}
	
	public String getSpecificExtension() {
		return this.aSpecificExtension;
	}
}
