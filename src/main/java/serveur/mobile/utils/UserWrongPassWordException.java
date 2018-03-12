package serveur.mobile.utils;

public class UserWrongPassWordException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserWrongPassWordException(String mesg) {
		
		super(mesg);
	}
}
