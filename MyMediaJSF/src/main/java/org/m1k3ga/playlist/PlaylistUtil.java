package org.m1k3ga.playlist;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author	m1k3ga
 * @since	20121015
 */
public class PlaylistUtil {

	/** logback instance */
	private static  final	Logger	LOG	= LoggerFactory.getLogger( Playlist.class );


	/** List of files found in a folder scan */
	private ArrayList<String> filesToAdd = new ArrayList<String>();


	/**
	 * Get all files in the specified folder (including subfolders)
	 * 
	 * @param	String	folderToScan	File system path to a folder
	 * 
	 * @return	ArrayList<String>
	 */
	public ArrayList<String> getMediafilesForFolder( String folderToScan ) {
		scanFolder( folderToScan );

		return filesToAdd;
	}


	/**
	 * Scan a folder in the file system and add all found files to a list
	 * The list of files is an instance variable
	 * 
	 * @param	String	folderToScan
	 * 
	 * @return	void
	 */
	private void scanFolder( String folderToScan ) {
		File fileToScan = new File( folderToScan );
		File[] filesInFolder = fileToScan.listFiles();

		for ( File file : filesInFolder ) {
			if ( ! file.exists() ) {
				LOG.info( "File '" + file + "' does not exist." );
				continue;
			}

			String pathToFile = file.getPath();
			if ( file.isDirectory() ) {
				LOG.debug( "Folder '" + pathToFile + "', scan it" );
				scanFolder( pathToFile );
			}
			else {
				LOG.debug( "Adding file '" + pathToFile + "' to file list" );
				filesToAdd.add( pathToFile );
			}
		}
	}

}