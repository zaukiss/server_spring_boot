package serveur.mobile.entities;

public class UserAccount {

	private int id;
	private int etat;


	public UserAccount(int id, int etat) {
		;
		this.id = id;
		this.etat = etat;
	}

	public int getId() {
		
		return id;
	}
	
	public void setId(int id) {
		
		this.id = id;
	}
	
	public int getEtat() {
		
		return etat;
	}
	
	public void setEtat(int etat) {
		
		this.etat = etat;
	}

}
