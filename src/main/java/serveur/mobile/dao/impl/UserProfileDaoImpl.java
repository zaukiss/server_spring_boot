package serveur.mobile.dao.impl;

import java.util.ArrayList;
import org.springframework.stereotype.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import serveur.mobile.dao.DataAccessObject;
import serveur.mobile.dao.IUserProfileDAO;
import serveur.mobile.entities.Contact;
import serveur.mobile.entities.UserAccount;
import serveur.mobile.entities.UserProfile;
import serveur.mobile.utils.Constants;
import serveur.mobile.utils.ContactAlreadyAssociatedException;
import serveur.mobile.utils.UserAlreadyExistException;
import serveur.mobile.utils.UserNotFoundException;

@Service
public class UserProfileDaoImpl extends DataAccessObject implements IUserProfileDAO {
	
	public UserProfileDaoImpl() {
		
		super();
	}

	@Override
	public boolean insert(UserProfile up) throws Exception,UserAlreadyExistException {
		
		UserProfile resultSearch  = getProfileByPseudo(up.get_mail());

		//we suppose that is a subscribe http request.So, the pseudo as same value than mail 
		if(resultSearch != null){ 

			//a profile already exist
			throw new UserAlreadyExistException ("user exist in base");
		}
		
		int nbRowUserAfter = 0;
		int nbRowUserBefor = 0;
		int nbRowCompteAfter = 0;
		int nbRowCompteBefor = 0;
		int nbRowAsoCompteAfter = 0;
		int nbRowAsoCompteBefor = 0;
		int idCompte = 0;
		int idUser = 0;
		
		//Check number of row before insertion in each table
		nbRowUserBefor = getNbRowsInTableUser(Constants.USER_PROFILE_TABLE);
		nbRowCompteBefor = getNbRowsInTableUser(Constants.COMPTE_TABLE);
		nbRowAsoCompteBefor = getNbRowsInTableUser(Constants.ASSOCIATION_COMPTE_TABLE);
		
		//Create the request String 
		String queryUserInsert = "insert into "
				+Constants.USER_PROFILE_TABLE
				+" ( pseudo, password, mail, ipAdd ) values ('"
				+up.get_pseudo()
				+"', '"
				+up.get_password()+"', '"
				+up.get_mail()+"','"
				+up.get_ipAdd()+ "');";

		String queryCompteInsert ="insert into "
				+Constants.COMPTE_TABLE
				+" ( etat ) values ("
				+Constants.COMPTE_CONNECTED+");";

		//get the id of userProfile record in order to put it in associationCompte table
		PreparedStatement insertUserPS = get_connexion().prepareStatement(queryUserInsert,Statement.RETURN_GENERATED_KEYS);
		insertUserPS.execute();
		ResultSet rsUser =  insertUserPS.getGeneratedKeys();
		if (rsUser.next()) {

			idUser = rsUser.getInt(1);//id of user created
		}
		
		//get the id of the account record created in order to put it in associationCompte table
		PreparedStatement insertComptePS = get_connexion().prepareStatement(queryCompteInsert,Statement.RETURN_GENERATED_KEYS);
		insertComptePS.execute();
		ResultSet rsCompte =  insertComptePS.getGeneratedKeys();
		if (rsCompte.next()) {

			idCompte = rsCompte.getInt(1);//id of compte created
		}
		
		//build the relation between userProfile and userAccount
		String queryassociationCompteInsert = " insert into "+Constants.ASSOCIATION_COMPTE_TABLE
				+"(utilisateur, compte ) "
				+" values ("+idUser +", "+idCompte+");" ;
		
		get_stamtement().execute(queryassociationCompteInsert);

		//check if insertion has been done in the right way
		nbRowUserAfter = getNbRowsInTableUser(Constants.USER_PROFILE_TABLE);
		nbRowCompteAfter = getNbRowsInTableUser(Constants.COMPTE_TABLE);
		nbRowAsoCompteAfter = getNbRowsInTableUser(Constants.ASSOCIATION_COMPTE_TABLE);
		if( nbRowUserBefor == nbRowUserAfter || nbRowAsoCompteBefor == nbRowAsoCompteAfter || nbRowCompteBefor == nbRowCompteAfter){ 

			return false; //was not executed as expected
		}
		return true;//everything is good
	}

	@Override
	public boolean update(UserProfile up, int state) throws Exception, UserNotFoundException {
		
		if(up == null)return false;
		UserAccount ua = getUserAccount(getIdUser(up.get_mail()));
		if(ua == null)throw new UserNotFoundException("account for user not found cannot update.");
		String queryUpUser = "update "
		+ Constants.USER_PROFILE_TABLE
		+" set ipAdd='"+up.get_ipAdd()
		+"' where "+ Constants.USER_PROFILE_TABLE+".id="+getIdUser(up.get_mail())+ ";";
		
		String queryUpAccount = "update "
				+ Constants.COMPTE_TABLE
				+" set etat='"+state
				+"' where "+ Constants.COMPTE_TABLE+".id="+ua.getId()+ ";";
		get_stamtement().executeUpdate(queryUpUser);
		get_stamtement().executeUpdate(queryUpAccount);
		return true;
	}

	@Override
	public UserProfile getProfileByPseudo(String p) throws Exception {
		
		//prevent the fact that pseudo can be different of mail
		if(p == null)return null;

		else if(p.matches(Constants.REGEX_MAIL)) return getProfileCorrespondingTo(p, Constants.USER_PROFILE_TABLE, Constants.SEARCH_BY_EMAIL);

		else return getProfileCorrespondingTo(p, Constants.USER_PROFILE_TABLE, Constants.SEARCH_BY_PSEUDO);	
	}

	@Override
	public ArrayList<Contact> getAssociateContact(String mail) throws Exception {
		
		UserAccount userAcc =  getUserAccount(getIdUser(mail));
		ArrayList<Contact> res = new ArrayList<Contact>();
		if(userAcc != null){
			
			//get all contact associate to userProfile
			String query ="select mail,alias,compte from "
					+Constants.CONTACT_TABLE+" where "
					+Constants.CONTACT_TABLE+".compte="
					+userAcc.getId()+";";
			
			PreparedStatement getContactQuery = get_connexion().prepareStatement(query);
			ResultSet rsContact =  getContactQuery.executeQuery();
			while (rsContact.next()) {
				
				if(rsContact.getString("mail") !=  null 
						&& rsContact.getString("alias") != null 
						&& rsContact.getInt("compte") != 0){
					
					Contact c  =  new Contact(rsContact.getString("mail"), rsContact.getString("alias"));
				
					//suppose the contact exist in userProfiles table else an exception is raise
					UserAccount contactAccount =  getUserAccount(getIdUser(c.getMail()));
					if(contactAccount !=  null && contactAccount.getEtat() == Constants.COMPTE_CONNECTED){
						
						c.setEtat(Constants.COMPTE_CONNECTED);
					}else if(contactAccount !=  null && contactAccount.getEtat() == Constants.COMPTE_DISCONNECTED){
						
						c.setEtat(Constants.COMPTE_DISCONNECTED);
					}else if(contactAccount ==  null){
						
						continue;
					}
					res.add(c);
				}			
			}
		}else{
			
			throw new UserNotFoundException("Account not found");
		}
		return res;//never null but can be empty in case user have no contact referenced
	}

	@Override
	public boolean addContact(String userMail, Contact c) throws Exception {
		
		UserAccount userAcc =  getUserAccount(getIdUser(userMail));
		UserAccount contactAcc = getUserAccount(getIdUser(c.getMail()));
		
		
		//contact will be add only if he is subscribe 
		if(contactAcc == null) throw new UserNotFoundException("contact to add not exist in data base");
		if(userAcc != null){
			
			String queryAlreadyAssociate = "select compte,mail from "+Constants.CONTACT_TABLE
					+" where "+Constants.CONTACT_TABLE+".compte="+userAcc.getId();
			PreparedStatement alreadyAssosQuery = get_connexion().prepareStatement(queryAlreadyAssociate);
			ResultSet assosContact =  alreadyAssosQuery.executeQuery();
			while(assosContact.next()){
				
				if(assosContact.getString("mail") != null && assosContact.getString("mail").equals(c.getMail()) ){
					
					throw new ContactAlreadyAssociatedException("the contact is already associate to the user");
				}
			}
			
			//each contact of a user have same two first number in their alias 
			//we not sure for now if there is the possible case where we've got the same alias for different contact 
			int nbRowTableBeforInsert = getNbRowsInTableUser(Constants.CONTACT_TABLE);
			c.setAlias(""+userMail.length()+""+(c.getMail().length()*contactAcc.getId()+userAcc.getId()));
			String queryInsertContact  = "insert into "
					+Constants.CONTACT_TABLE+" (mail,alias,compte) values ('"
					+c.getMail()+"','"
					+c.getAlias()+"',"
					+userAcc.getId()+");";
			get_stamtement().executeUpdate(queryInsertContact);	
			int nbRowTableAfterInsert =  getNbRowsInTableUser(Constants.CONTACT_TABLE);
			if(nbRowTableAfterInsert > nbRowTableBeforInsert){
				
				return true;
			}
		}else{
			
			throw new UserNotFoundException("cannot add contact to user ( userprofile not found )");
		}
		return false;
	}

	@Override
	public boolean removeContact(String userMail, Contact c) throws Exception {
		
		return false;
	}
}
