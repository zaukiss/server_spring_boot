package serveur.mobile.entities;

import serveur.mobile.utils.Constants;

public class Contact {
	
	String mail, alias;
	int etat;

	public Contact(String mail, String alias) {
	

		this.mail = mail;
		this.alias = alias;
		this.etat = Constants.COMPTE_DISCONNECTED;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}
}
