package serveur.mobile.dao;

import java.util.ArrayList;

import serveur.mobile.entities.Contact;
import serveur.mobile.entities.UserProfile;

public interface IUserProfileDAO {
	
	//basics
	public boolean insert(UserProfile up) throws Exception;//done
	public boolean update(UserProfile up,int state) throws Exception;//done
	
	//extras
	public UserProfile getProfileByPseudo(String p ) throws Exception;//done
	public ArrayList<Contact> getAssociateContact(String mail) throws Exception;//done
	public boolean addContact(String userMail, Contact c) throws Exception;//done
	public boolean removeContact(String userMail, Contact c) throws Exception;
}
