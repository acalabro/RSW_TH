package it.acalabro.transponder.event;

import it.acalabro.transponder.cep.CepType;

public class ConcernCANbusEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private String canAddress;
	
	public ConcernCANbusEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T canData,
			CepType type,
			String canAddress) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, canData, type);
		this.canAddress = canAddress;		
	}

	public void setCanAddress(String canAddress) {
		this.canAddress = canAddress;
	}
	
	public String getCanAddress() {
		return this.canAddress;
	}

}
