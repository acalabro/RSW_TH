package it.acalabro.transponder.storage;

import it.acalabro.transponder.ConcernApp;
import it.acalabro.transponder.event.Event;

public class InfluxDBStorageController implements StorageController {

	@Override
	public boolean connectToDB() {
		// TODO Auto-generated method stub
		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
		return true;
	}

	@Override
	public boolean disconnectFromDB() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveMessage(Event<?> message) {
		// TODO Auto-generated method stub
		return false;
	}

}
