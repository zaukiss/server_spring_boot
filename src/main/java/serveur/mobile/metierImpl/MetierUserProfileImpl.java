package serveur.mobile.metierImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serveur.mobile.dao.IUserProfileDAO;
import serveur.mobile.entities.Contact;
import serveur.mobile.entities.UserProfile;
import serveur.mobile.metier.IMetierUserProfile;
import serveur.mobile.utils.Constants;
import serveur.mobile.utils.UserAlreadyExistException;
import serveur.mobile.utils.UserNotFoundException;
import serveur.mobile.utils.UserWrongPassWordException;

@Service
public class MetierUserProfileImpl implements IMetierUserProfile{

	@Autowired
	private IUserProfileDAO _daoProfile;

	@Override
	public ArrayList<Contact> getContacts(String mail) {

		try {
			
			return _daoProfile.getAssociateContact(mail);
		} catch (Exception e) {
			
			return new ArrayList<Contact>();
		}
	}

	@Override
	public boolean subscribeUser(String pseudo, String password, String mail,String ip) throws Exception {

		//we must ensure that there will not have duplicate userProfile in the database
		try {

			return _daoProfile.insert(new UserProfile(mail, password, mail, ip));	
		} catch (Exception e) {

			e.printStackTrace();
			if(e instanceof UserAlreadyExistException){

				throw new UserAlreadyExistException("userExist");
			}
			return false;
		}
	}

	@Override
	public boolean updateInformationProfile(String pseudoProfileToUpdate, String newPseudo, String newPassword,
			String newMail,String ip) {

		return false;
	}

	@Override
	public boolean connectUser(String pseudo, String password, String ip) throws Exception{

		UserProfile usp;
		usp= _daoProfile.getProfileByPseudo(pseudo);
		if( usp == null){

			throw new UserNotFoundException("User not in data base");
		}else{

			if(password.equals(usp.get_password())){

				usp.set_ipAdd(ip);
				return _daoProfile.update(usp,Constants.COMPTE_CONNECTED);
			}else{

				throw new UserWrongPassWordException("wrong password");
			}
		}
	}

	@Override
	public boolean createNewContact(String userProfileMail, Contact c) throws Exception {
		
		return _daoProfile.addContact(userProfileMail, c);
	}
	
	

}
