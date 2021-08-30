package it.acalabro.transponder.event;

import it.acalabro.transponder.cep.CepType;

public class ConcernGpsEvent<T> extends ConcernAbstractEvent<T> {

	private static final long serialVersionUID = 1L;
	private GpsType gpsType;
	
	public ConcernGpsEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T data,
			CepType cepType,
			String gpsPosition,
			GpsType gpsType) {
		super(timestamp, senderID, destinationID, sessionID, checksum, name, data, cepType);
		this.gpsType = gpsType;
	}

	public void setGpsType(GpsType gpsType) {
		this.gpsType = gpsType;
	}
	
	public GpsType getGpsType() {
		return this.gpsType;
	}
}
