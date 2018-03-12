package serveur.mobile.entities;

public class UserProfile {

	private String _pseudo;
	private String _password;
	private String _mail;
	private String _ipAdd;



	public UserProfile(String _pseudo, String _password, String mail, String ip) {
		
		super();
		this._pseudo = _pseudo;
		this._password = _password;
		this._mail = mail;
		this._ipAdd = ip;
	}
	
	public String get_pseudo() {
		
		return _pseudo;
	}
	
	public void set_pseudo(String _pseudo) {
		
		this._pseudo = _pseudo;
	}
	
	public String get_password() {
		
		return _password;
	}
	
	public void set_password(String _password) {
		
		this._password = _password;
	}
	
	public String get_mail() {
		
		return _mail;
	}
	
	public void set_mail(String _mail) {
		
		this._mail = _mail;
	}
	
	public String get_ipAdd() {
		
		return _ipAdd;
	}

	public void set_ipAdd(String _ipAdd) {
		
		this._ipAdd = _ipAdd;
	}
}
