package serveur.mobile.web;

import java.util.ArrayList;
import serveur.mobile.entities.DataResponse;
import serveur.mobile.entities.Message;

public class ConnectionResponse extends DataResponse {
	
	ArrayList<Message> messages;
	
	public ConnectionResponse() {
		super();
	}
}
