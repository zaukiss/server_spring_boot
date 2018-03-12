package serveur.mobile.controls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import serveur.mobile.entities.Contact;
import serveur.mobile.metier.IMetierUserProfile;
import serveur.mobile.utils.Constants;
import serveur.mobile.utils.ContactAlreadyAssociatedException;
import serveur.mobile.utils.UserNotFoundException;
import serveur.mobile.utils.UserWrongPassWordException;
import serveur.mobile.web.ContactResponse;
import serveur.mobile.web.ConnectionResponse;
import serveur.mobile.web.SubscribeResponse;

@RestController
public class UserProfileController{

	@Autowired
	private IMetierUserProfile _metierUserProfile;
	
	//allow to add a contact to a user account 
	@RequestMapping(value="/addContact", method=RequestMethod.GET)
	public ContactResponse addContact(
			@RequestParam("mailUser") String userMail,
			@RequestParam("mailContact") String mailC){
		
		ContactResponse response =  new ContactResponse();
		if(!userMail.isEmpty() && !mailC.isEmpty()){
			
			//the alias of contact will be create later when we add it to database
			Contact c = new Contact(mailC, "");
			try {
				
				if(_metierUserProfile.createNewContact(userMail, c)){
					
					response.set_status(Constants.CONTACT_CREATED);
					response.set_action(Constants.ACTION_UPDATE_CONTACT);
					response.set_contacts(_metierUserProfile.getContacts(userMail));
				}else{
					
					response.set_status(Constants.CONTACT_NOT_CREATE);
					response.set_action(Constants.NO_ACTION);
					response.set_contacts(_metierUserProfile.getContacts(userMail));
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				if(e instanceof UserNotFoundException){
					
					response.set_status(Constants.CONTACT_NOT_CREATE);
				}else if(e instanceof ContactAlreadyAssociatedException){
					
					response.set_status(Constants.CONTACT_ALREADY_ASSOCIATED);
				}
				
				response.set_action(Constants.NO_ACTION);
				response.set_contacts(_metierUserProfile.getContacts(userMail));
			}
		}
		return response;
	}
	
	//allow to get all contacts associated to a user 
	@RequestMapping(value="/contact",method=RequestMethod.GET)
	public ContactResponse getContacts(@RequestParam("mail") String mail){
		
		ContactResponse response =  new ContactResponse();
		response.set_status(200);
		response.set_action(Constants.ACTION_UPDATE_CONTACT);
		response.set_contacts(_metierUserProfile.getContacts(mail));
		return response;
	}
	
	//allow to subscribe a user 
	@RequestMapping(value="/subscribe",method=RequestMethod.GET)
	public SubscribeResponse subscribeUser(
			@RequestParam("name") String name, 
			@RequestParam("password") String password, 
			@RequestParam("mail")String mail,
			@RequestParam("ip")String ip){
		
		//TODO : Do not forget encrypt the user name and password in client side and implement decryption here
		
		SubscribeResponse response =  new SubscribeResponse();
		try {
			
			if(_metierUserProfile.subscribeUser(name, password,mail,ip)){
				
				response.set_status(Constants.SUBSCRIBE_SUCCESS);
			}else{
				
				response.set_status(Constants.SUBSCRIBE_FAILED);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			response.set_status(Constants.USER_ALREADY_EXIST);
		}
		response.set_action(Constants.NO_ACTION);
		return response;
	}
	
	//allow to connect a user account
	@RequestMapping(value="/connect",method=RequestMethod.GET)
	public ConnectionResponse connectUser(
			@RequestParam("name") String name, 
			@RequestParam("password") String password, 
			@RequestParam("ip")String ip){
		
		ConnectionResponse response = new ConnectionResponse();
		try {
			
			if(_metierUserProfile.connectUser(name, password, ip)){
				
				response.set_status(Constants.CONNECT_SUCCESS);
			}	
		} catch (Exception e) {
			
			e.printStackTrace();
			if(e instanceof UserNotFoundException){
				
				response.set_status(Constants.USER_NOT_FOUND);
			}else if(e instanceof UserWrongPassWordException){
				
				response.set_status(Constants.USER_BAD_PASSWORD);
			}else{
				
				response.set_status(Constants.USER_EXCEPTION_RAISED);
			}
		}
		return response;
	}
}
