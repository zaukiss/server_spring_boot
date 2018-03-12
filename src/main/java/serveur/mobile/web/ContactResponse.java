package serveur.mobile.web;

import java.util.ArrayList;

import serveur.mobile.entities.Contact;
import serveur.mobile.entities.DataResponse;

public class ContactResponse extends DataResponse  {
	
	private ArrayList<Contact> _contacts;
	public ContactResponse(){
		super();
	}
	
	public ArrayList<Contact> get_contacts() {
		return _contacts;
	}
	public void set_contacts(ArrayList<Contact> _contacts) {
		this._contacts = _contacts;
	}
}
