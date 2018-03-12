package serveur.mobile.utils;

public class Constants {
	
	public static final String USER_PROFILE_TABLE =  "userprofiles";
	public static final String COMPTE_TABLE =  "compte";
	public static final String CONTACT_TABLE =  "contact";
	public static final String MSG_TXT_TABLE =  "msgTxt";
	public static final String ASSOCIATION_COMPTE_TABLE =  "associationCompte";
	public static final String DESTINATION_MSG_TABLE =  "destinationMsg";
	public static String MYSQL_ADDRESS_AND_PORT ="localhost:3306"; 
	public static final String MYSQL_TABLE_NAME ="/user_db"; 
	public static final String MYSQL_LOGIN ="root"; 
	public static final String MYSQL_PASSWORD ="root"; 	
	public static final String REGEX_MAIL = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
	
	public static final int NO_ACTION = -1;
	
	public static final int SEARCH_BY_EMAIL = 0;
	public static final int SEARCH_BY_PSEUDO = 1;
	public static final int COMPTE_CONNECTED = 2;
	public static final int COMPTE_DISCONNECTED = 3;
	
	public static final int SUBSCRIBE_SUCCESS = 200;
	public static final int CONNECT_SUCCESS = 200;
	public static final int CONTACT_CREATED = 200;
	public static final int ACTION_UPDATE_CONTACT = 204;
	
	public static final int USER_EXCEPTION_RAISED = 400;
	public static final int USER_NOT_FOUND = 404;
	public static final int SUBSCRIBE_FAILED = 456;
	public static final int USER_BAD_PASSWORD = 486;
	public static final int USER_ALREADY_EXIST = 487;
	
	public static final int CONTACT_ALREADY_ASSOCIATED =  503;
	public static final int CONTACT_NOT_CREATE = 508;
	
	
	
}
