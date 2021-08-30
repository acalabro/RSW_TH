package it.acalabro.transponder.client;

import it.acalabro.transponder.ConcernApp;

public class ClientManager {

	public ClientManager() {
		ConcernApp.componentStarted.put(this.getClass().getSimpleName(), true);
	}

}
