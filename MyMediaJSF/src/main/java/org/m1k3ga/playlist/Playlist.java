package org.m1k3ga.playlist;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.m1k3ga.lab.model.MediaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class represents one playlist containing 1..N playlist items
 * A new playlist is empty and has no playlist items
 * A playlist with playlist items needs to be initialized from persistence
 * or from file paths of the playlist items
 * 
 * @author m1k3ga
 */
@ManagedBean(name="playlist")
@SessionScoped
public class Playlist {

	/** logback instance */
	private static  final	Logger	LOG	= LoggerFactory.getLogger( Playlist.class );

	// Database values
	private long playlistId;		/** id of the playlist in the database */
	private long created;			/** creation time of the playlist */
	private long modified;
	private long lastPlayed;

	/** Temporary constants */
	public  static final String		FOLDERPATH	= "/opt/testdaten";
	private static final int		PLAYLIST_ID	= 12345678;

	/** This list holds all the songs for a playlist */
	private ArrayList<PlaylistItem> playlistItems = new ArrayList<PlaylistItem>();

	/** obviously, the name of this playlist */
	private String playlistName = "new playlist";

	/** Playlist item id to delete from the playlist */
	private long pliIdToDelete;


	//
	// Constructor and initialization phase
	//

	/** Constructor for new playlist and for access from JSF */
	public Playlist() {
		playlistId = initPlaylist();

		// TODO Dies muss noch weg, denn jedes Element kann in einem eigenen Ordner liegen
		initPlaylistForFolderPath();
	}

	/** Initialize a previously saved playlist from id (primary key) from the database */
	public Playlist( int id ) {
		playlistId = id;

		// TODO Dies muss noch weg, denn jedes Element kann in einem eigenen Ordner liegen
		initPlaylistForFolderPath();
	}

	private int initPlaylist() {
		return PLAYLIST_ID;
	}

	private void initPlaylistForFolderPath() {
		PlaylistUtil piUtil = new PlaylistUtil();
		addFilesToPlaylist( piUtil.getMediafilesForFolder( FOLDERPATH ) );
	}



	//
	// playlist specific attributes
	//
	// TODO: sum_stars, ...

	/** Playlist name */
	public String getPlaylistName() {
		return playlistName;
	}
	public void setPlaylistName( String playlistName ) {
		this.playlistName = playlistName;
	}



	/**
	 * The last time any of the songs in the playlist were modified
	 * 
	 * @return	long	timestamp value
	 */
	public long getModified() {
		return modified;
	}
	public void setModified(long modified) {
		this.modified = modified;
	}

	/**
	 * The last time any of the songs in the playlist was played
	 * 
	 * @return	long	timestamp value
	 */
	public long getLastPlayed() {
		return lastPlayed;
	}
	public void setLastPlayed( long lastPlayed ) {
		this.lastPlayed = lastPlayed;
	}

	/** Playlist id comes from the database and cannot be changed */
	public long getPlaylistId() {
		return playlistId;
	}

	/** created date comes from the database and cannot be changed */
	public long getCreated() {
		return created;
	}

	/**
	 * Sum of number of times the songs in this playlist were played
	 *  
	 * @return	int
	 */
	public int getSumOfPlayCountsForItemsInPlaylist() {
		int			sumCount = 0;
		MediaFile	mf;

		for ( PlaylistItem pli : playlistItems ) {
			mf = pli.getMediafile();
			sumCount += mf.getPlayCount();
		}

		return sumCount;
	}



	//
	// Playlist management
	//

	/**
	 * Append the given file at the end of the playlist
	 * 
	 * @param	String
	 * 
	 * @return	void
	 */
	public void addToPlaylist( String pathToFile ) {
		PlaylistItem pi = new PlaylistItem( pathToFile );
		pi.setPosition( this.playlistItems.size() + 1 );
		playlistItems.add( pi );
	}

	/**
	 * Get the items from this playlist (used as a getter in JSF page)
	 * 
	 * @return	ArrayList<PlaylistItem>
	 */
	public ArrayList<PlaylistItem> getPlaylistItems() {
		return playlistItems;
	}

	private void addFilesToPlaylist( ArrayList<String> filesToAdd ) {
		for ( String fileToAdd : filesToAdd ) {
			LOG.debug( "Adding file '" + fileToAdd + "' to the playlist as playlist item" );
			addToPlaylist(fileToAdd);
		}
	}

	public void setPliIdToDelete( long id ) {
		LOG.debug( "playlist item id to delete: '" + id + "'" );
		pliIdToDelete = id;
	}

	public String deletePlaylistItem() {
		LOG.info( "Delete from playlist '" + playlistId + "' the item with id: '" + pliIdToDelete + "'" );
		return null;
	}

}