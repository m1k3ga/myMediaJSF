package org.m1k3ga.lab.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Login page
 * 
 * @author	m1k3ga
 * @since	20121001
 */
@ManagedBean
@SessionScoped
public class Login {
	private static  final	Logger	LOG	= LoggerFactory.getLogger( Login.class );

	private static final String SHOW_CUSTOMER_PAGE_URL = "/showPlaylists.xhtml";
	
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String firstName) {
		LOG.debug( "set username" );
		this.username = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String lastName) {
		this.password = lastName;
	}

	/**
	 * By sending the form we return the URL to go to
	 * 
	 * @return	String
	 */
	public String login() {
		// TODO Do the login stuff and initialize the playlist
		// Then return.
		LOG.info("do login..");

		return SHOW_CUSTOMER_PAGE_URL;
	}

/*
	public String getCwd() {
		File cwd = new File (".");
		File testDatenDir = new File( TESTFILENAMEPATH );
		StringBuffer sb = new StringBuffer();

		try {
			sb.append( "== Current dir : " + cwd.getCanonicalPath());
			sb.append( " - - - " );
			sb.append( "== Testdaten dir : " + testDatenDir.getCanonicalPath() + " - - - ");
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		sb.append( "can read:" + testDatenDir.canRead() );

		return sb.toString();
	}

	public void setCwd() {
		LOG.debug( "Trying to set current working directory - Not supported" );
	}
*/
}