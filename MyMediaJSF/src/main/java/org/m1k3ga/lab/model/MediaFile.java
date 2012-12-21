package org.m1k3ga.lab.model;


import java.io.IOException;
import java.util.Random;

import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is an adapter class for the actual implementation of the ID3/MP3 media file handling
 * It uses the 3rd party lib implementation and we provide a wrapped API.
 * 
 * This class holds the media file (MP3, ...) 
 * and meta data like "is modified"
 * as well as the id3 tags
 *
 * @author mgassmann
 * @since	20121008
 */
public class MediaFile {

	/** logback instance */
	private static  final	Logger	LOG	= LoggerFactory.getLogger( MediaFile.class );

	/** Name and path of the mp3 file */
	private String			pathToMediafile;

	/** Is the MP3 file modified and needs to be saved? */
	private boolean			isModified			= false;

	/** The primary key id of this media file in the database */
	private long			mediaFileId;


	/** Wrapper classes for the framework specific implementations for mp3 files, tags, ... */
	private MP3File			mp3file;
	private AudioHeader		audioHeader;
	private ID3v24Tag		id3V24Tag;


	public MediaFile( String path ) {
		this.pathToMediafile = path;
		mediaFileId = new Random().nextLong();
	}



	/**
	 * - Open the media file which is located at the file path given in the constructor of the instance
	 * - Use the 3rd party lib for opening the file
	 * - MediaFile -> MP3File -> AudioFile -> File
	 * 
	 * @return	void
	 */
	public void getMediaFileForPath( ) {

		try {
			this.mp3file = new MP3File( pathToMediafile );
		}
		catch ( ReadOnlyFileException ex ) {
			LOG.info( "This is a read-only file: '" + pathToMediafile + "'" );
		}
		catch (IOException ex) {
			LOG.error( "File could not be opened. " + ex.getMessage() + " - Filename: '" + pathToMediafile + "'" );
		}
		catch ( TagException ex ) {
			LOG.error( "Tag exception in file. " + ex.getMessage() + " - Filename: '" + pathToMediafile + "'" );
		}
		catch (InvalidAudioFrameException e) {
			LOG.error( "Invalid audio frame exception in file '" + pathToMediafile + "'" );
		}

		this.audioHeader	= this.mp3file.getMP3AudioHeader();
		this.id3V24Tag		= this.mp3file.getID3v2TagAsv24();
	}



	/**
	 * Save the mp3 file which is embedded in the MediaFile container
	 * Discard the invalid information (frames) and save only the valid frames back to the .mp3 file
	 * 
	 * @return	void
	 * 
	 * @throws TagException 
	 */
	public void saveMediaFile() throws TagException {
		if ( isModified() ) {
			try {
				this.mp3file.save();
				resetModified();
			}
			catch (IOException e) {
				LOG.error( "Could not save file '"+ this.pathToMediafile + "' due to an error: " + e.getMessage() );
			}
		}
		else {
			LOG.debug("File was not modified, nothing to save" );
		}
	}



	public String getPathForMediaFile() {
		return this.pathToMediafile;
	}



	//
	// Below are the setters ( no getters yet ) for the JSF page attributes
	//


	/** Title of the song */
	public String getTitle() {
		return id3V24Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
	}

	/** Artist of the song */
	public String getArtist() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_ARTIST );
	}

	/** The album the media file belongs to */
	public String getAlbum() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_ALBUM );
	}

	public String getGenre() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_GENRE );
	}

	/** The track number of the media file */
	public int getTrackNumber() {
		String trackNumberString = id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_TRACK );
		return getNumberForString(trackNumberString, "track number" );
	}

	/** The year of publishing the media file */
	public int getYear() {
		String yearString = id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_YEAR );
		return getNumberForString(yearString, "Year of publishing" );
	}

	/** The comment for the media file */
	public String getComment() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_COMMENT );
	}

	/** The lyrics for the media file */
	public String getLyrics() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_UNSYNC_LYRICS );
	}

	/** The song writer for the media file */
	public String getSongWriter() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_COMPOSER );
	}

	public int getPlayCount() {
		String playCount = id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_PLAY_COUNTER );
		return getNumberForString( playCount, "play count");
	}

	/** The rating for the media file
	 *  The rating is 1-255 where 1 is worst and 255 is best. 0 is unknown.
     *  If no song rating has been specified, then 0 is returned.
	 * 
	 * @return	int
	 */
	public int getRating() {
//		return mp3file.getRating();
// FIXME not yet supported
		return -1;
	}

	/** The label (Publisher) for the media file */
	public String getPublisher() {
		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_PUBLISHER );
	}


	/** The cover image for the media file */
	public String getPictureForType( PictureTypeIdv23 pictureType ) {
		// TODO Implementation
		return null; //mp3.getPicture( pictureType );
	}
	public void addPicture( String songWriter ) {
		// TODO Implementation
		// mp3.setPicture(null, null);
//		setModified();
	}

	/** The file name the media file */
	public String getFilename() {
		return this.pathToMediafile;
	}

	/** Get the absolute path */
	public String getFullPath() {
		return mp3file.getFile().getAbsolutePath();
	}

	/** The play length for the media file in seconds */
	public int getPlayLength() {
//		return id3V24Tag.getFirst( ID3v24Frames.FRAME_ID_LENGTH );
		return audioHeader.getTrackLength();
	}


	private int getNumberForString( String stringToParse, String type ) {
		int parsedValue = 0;

		try {
			parsedValue = Integer.parseInt( stringToParse );
		}
		catch( NumberFormatException ex ) {
			LOG.info( type + " for file '" + this.pathToMediafile + "' could not be determined. Set to 0" );
			parsedValue = 0;
		}

		return parsedValue;
	}


	/**
	 * Is the embedded MP3 file modified and needs to be stored?
	 * 
	 * @return	boolean		True, if the file needs to be stored, false otherwise
	 */
	public boolean isModified() {
		return isModified;
	}
	public void setModified() {
		isModified = true;
	}
	protected void resetModified() {
		isModified = false;
	}

	public long getId() {
		return this.mediaFileId;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append( "--  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -\n" );
		sb.append( "PK_id........:" + getId() + "\n" );
		sb.append( "Title........:" + getTitle() + "\n" );
		sb.append( "Band.........:" + getArtist() + "\n" );
		sb.append( "Album........:" + getAlbum() + "\n" );
		sb.append( "Path.........:" + getFilename() + "\n" );
		sb.append( "Rating.......:" + getRating() + "\n" );
		sb.append( "Year.........:" + getYear() + "\n" );
		sb.append( "Duration.....:" + getPlayLength() + "ms\n");
//		sb.append( "lyrics by.....: " + lyricsBy );
		sb.append( "Written by...: " + getSongWriter() + "\n" );
//		sb.append( "picture.......: " + picture );
		sb.append( "Publisher....: " + getPublisher() + "\n" );
//		sb.append( "lyrics........: " + lyrics );
		sb.append( "--  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -\n" );

		return sb.toString();
	}
	
}