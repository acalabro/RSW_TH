package it.acalabro.transponder.event;

import java.io.Serializable;

import it.acalabro.transponder.cep.CepType;

public abstract class ConcernAbstractEvent<T> implements Event<T>, Serializable {

	private static final long serialVersionUID = 7077313246352116557L;
	private long timestamp;
	private String sender;
	private String destinationID;
	private String sessionID;
	private String checksum;
	private String name;
	private T data;
	private CepType cepType;

	public ConcernAbstractEvent(
			long timestamp,
			String senderID,
			String destinationID,
			String sessionID,
			String checksum,
			String name,
			T data,
			CepType type) {
		this.setTimestamp(timestamp);
		this.setSenderID(senderID);
		this.setDestinationID(destinationID);
		this.setSessionID(sessionID);
		this.setChecksum(checksum);
		this.setName(name);
		this.setData(data);
		this.setCepType(type);
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSenderID() {
		return sender;
	}
	public void setSenderID(String sender) {
		this.sender = sender;
	}
	public String getDestinationID() {
		return destinationID;
	}
	public void setDestinationID(String destinationID) {
		this.destinationID = destinationID;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public CepType getCepType() {
		return cepType;
	}
	public void setCepType(CepType cepType) {
		this.cepType = cepType;
	}
}
