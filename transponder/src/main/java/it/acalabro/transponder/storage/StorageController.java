package it.acalabro.transponder.storage;

import it.acalabro.transponder.event.Event;
public interface StorageController {

	public boolean connectToDB();
	public boolean disconnectFromDB();
	public boolean saveMessage(Event<?> message);
}
