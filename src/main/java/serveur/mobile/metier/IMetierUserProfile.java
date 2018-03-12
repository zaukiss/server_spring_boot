package serveur.mobile.metier;

import java.util.ArrayList;
import serveur.mobile.entities.Contact;

public interface IMetierUserProfile {
	
	public ArrayList<Contact> getContacts(String name);//done
	public boolean subscribeUser(String pseudo, String password, String mail, String ip)throws Exception;//done
	public boolean updateInformationProfile(String pseudoProfileToUpdate,String newPseudo, String newPassword, String newMail,String ip);
	public boolean connectUser(String pseudo, String password, String ip) throws Exception;//done
	public boolean createNewContact(String userProfileMail, Contact c)throws Exception;//done
}
