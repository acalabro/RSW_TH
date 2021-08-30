package it.acalabro.transponder.event;

import it.acalabro.transponder.cep.CepType;

public class ConcernWiFiEvent<T> extends ConcernAbstractEvent<T> {

		private static final long serialVersionUID = 1L;
		private PacketType packetType;
		private String receivedDb;
		
		public ConcernWiFiEvent(
				long timestamp,
				String senderID,
				String destinationID,
				String sessionID,
				String checksum,
				String name,
				T data,
				CepType cepType,
				String macAddress,
				PacketType packetType,
				String receivedDb) {
			super(timestamp, senderID, destinationID, sessionID, checksum, name, data, cepType);
			this.packetType = packetType;
			this.receivedDb = receivedDb;
		}

		public void setPacketType(PacketType packetType ) {
			this.packetType = packetType;
		}
		
		public PacketType getPacketType() {
			return this.packetType ;
		}
		
		public void setReceivedDb(String receivedDb) {
			this.receivedDb = receivedDb;
		}
		public String getReceivedDb() {
			return this.receivedDb;
		}
}
