package serveur.mobile.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.PreparedStatement;
import serveur.mobile.entities.UserAccount;
import serveur.mobile.entities.UserProfile;
import serveur.mobile.utils.Constants;

public class DataAccessObject {

	private final String _url = "jdbc:mysql://"+Constants.MYSQL_ADDRESS_AND_PORT+Constants.MYSQL_TABLE_NAME;
	private Connection _connexion;
	private Statement _stamtement;

	protected DataAccessObject(){

		try {

			_connexion = DriverManager.getConnection(_url,Constants.MYSQL_LOGIN, Constants.MYSQL_PASSWORD);
			_stamtement =_connexion.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public Connection get_connexion() {

		return _connexion;
	}

	public void set_connexion(Connection _connexion) {

		this._connexion = _connexion;
	}

	public Statement get_stamtement() {

		return _stamtement;
	}

	public void set_stamtement(Statement _stamtement) {

		this._stamtement = _stamtement;
	}

	public int getNbRowsInTableUser(String table) throws Exception{

		PreparedStatement st =  (PreparedStatement) get_connexion().prepareStatement("select count(*) from "+ table);
		ResultSet result =  st.executeQuery();
		while(result.next()){

			return result.getInt(1);
		}

		return 0;
	}

	public UserProfile getProfileCorrespondingTo(String value, String inTable, int withsearchMethod) throws Exception{

		//We suppose that the sql table does not contain duplicate userProfile
		//if there is duplicate userProfile we just return the first we found.
		//If no method for search is specified we raise an exception.
		String query = "";
		UserProfile finalResult ;
		if(withsearchMethod ==  Constants.SEARCH_BY_EMAIL){

			query = "select pseudo, password, mail,ipAdd from "+inTable+" where "+ inTable+".mail="+"'"+value+"';";
		}else if(withsearchMethod ==  Constants.SEARCH_BY_PSEUDO){

			query = "select pseudo, password, mail, ipAdd from "+inTable+" where "+ inTable+".pseudo="+"'"+value+"';";
		}else{

			//the exception is here just for debug purpose 
			throw new Exception("anny method search indicate");
		}

		PreparedStatement st = (PreparedStatement)get_connexion().prepareStatement(query);
		ResultSet result = st.executeQuery();
		
		while(result.next()){
			
			if(result.getString("pseudo") != null  && result.getString("password") != null && result.getString("mail") !=  null && result.getString("ipAdd") != null ){

				finalResult = new UserProfile(result.getString("pseudo"), result.getString("password"), result.getString("mail"), result.getString("ipAdd"));
				return finalResult;
			}else{
				
				return null;
			}
		}
		return null;
	}

	public int getIdUser(String value) throws Exception{

		if(value.matches(Constants.REGEX_MAIL)){	

			String query = "select id from "+Constants.USER_PROFILE_TABLE+" where "+ Constants.USER_PROFILE_TABLE+".mail="+"'"+value+"';";
			PreparedStatement st = (PreparedStatement)get_connexion().prepareStatement(query);
			ResultSet result = st.executeQuery();
			while(result.next()){

				if(result.getInt("id") != 0 ){

					return result.getInt("id");
				}
			}
		}else{

			String query = "select id from "+Constants.USER_PROFILE_TABLE+" where "+ Constants.USER_PROFILE_TABLE+".pseudo="+"'"+value+"';";
			PreparedStatement st = (PreparedStatement)get_connexion().prepareStatement(query);
			ResultSet result = st.executeQuery();
			while(result.next()){

				if(result.getInt("id") != 0 ){

					return result.getInt("id");
				}
			}
		}

		return -1;
	}

	public UserAccount getUserAccount(int idUser) throws Exception{

		if(idUser > 0){


			int idCompteAssos =  0;
			String queryGetAssociation ="select "+Constants.ASSOCIATION_COMPTE_TABLE+".compte from "
					+Constants.ASSOCIATION_COMPTE_TABLE
					+" where "+Constants.ASSOCIATION_COMPTE_TABLE+".utilisateur="+idUser+";";

			PreparedStatement st = (PreparedStatement)get_connexion().prepareStatement(queryGetAssociation);
			ResultSet result = st.executeQuery();
			while(result.next()){

				if(result.getInt("compte") != 0 ){

					idCompteAssos = result.getInt("compte"); 
				}
			}

			if( idCompteAssos == 0)throw new Exception("id account not found for user ");
			String query = "select "
					+ Constants.COMPTE_TABLE
					+ ".id"+","+ Constants.COMPTE_TABLE+".etat from " 
					+ Constants.COMPTE_TABLE+" where "
					+Constants.COMPTE_TABLE +".id="+idCompteAssos+";";

			PreparedStatement stUa = (PreparedStatement)get_connexion().prepareStatement(query);
			ResultSet resultUa = stUa.executeQuery();
			while(resultUa.next()){

				if(resultUa.getInt("id") != 0 && resultUa.getInt("etat") != 0){

					return new UserAccount(resultUa.getInt("id"), resultUa.getInt("etat"));
				}
			}	
		}
		return null;
	}

}
