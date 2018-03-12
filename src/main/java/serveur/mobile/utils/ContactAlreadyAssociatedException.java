package serveur.mobile.utils;

public class ContactAlreadyAssociatedException extends Exception{

	private static final long serialVersionUID = 1L;
	public ContactAlreadyAssociatedException(String msg) {
		
		super(msg);
	}

}
