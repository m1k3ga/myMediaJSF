package org.m1k3ga.playlist;

import org.m1k3ga.lab.model.MediaFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One element of a playlist
 * The actual MediaFile instance is embedded in the class along with additional attributes like
 * - position number in the play list
 * - path to the file in the file system
 * 
 * @author m1k3ga
 *
 */
public class PlaylistItem {

	/** logback instance */
	private static final Logger	LOG					= LoggerFactory.getLogger( "PlaylistItem" );

	/** The position number in the list of the item */ 
	private int					position			= 0;

	/** The loaded media file */
	private MediaFile			mediafile			= null;


	/**
	 * Create a playlist item which is part of a playlist in the view
	 * 
	 * @param	String		path to the media file
	 */
	public PlaylistItem( String path ) {
		init( path );
	}

	private void init( String path ) {
		if ( mediafile == null ) {
			LOG.info( "no media file for playlist item, loading for path '" + path + "'" );
			mediafile = new MediaFile( path );
			mediafile.getMediaFileForPath();
		}
	}

	/**
	 * The embedded MediaFile class holds the MP3 file with tags
	 * For a playlist item it should be initialized via constructor
	 * 
	 * @return	MediaFile
	 */
	public MediaFile getMediafile() {
		return mediafile;
	}

	public int getPosition() {
		return this.position;
	}
	public void setPosition( int position ) {
		this.position = position;
	}

}