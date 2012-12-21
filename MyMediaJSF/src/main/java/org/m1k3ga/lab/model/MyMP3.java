package org.m1k3ga.lab.model;

import java.io.IOException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.slf4j.LoggerFactory;

/**
 * Wrapper class for the actual implementation.
 * For the rest of the application the implementation of the mp3 files/tags/... is encapsulated
 * 
 * 'MP3File' is the 3rd party backend for mp3 files
 * 
 * @author m1k3ga
 */
public class MyMP3 extends MP3File {

	static final org.slf4j.Logger LOG = LoggerFactory.getLogger( MyMP3.class );

	MP3File mp3file;

	public MyMP3( String mp3Filename ) throws IOException, TagException, InvalidAudioFrameException {
		try {
			mp3file = new MP3File( mp3Filename );
		}
		catch ( ReadOnlyFileException e ) {
			LOG.info( "This is a read-only file: '" + mp3Filename + "'" );
		}
	}

	/** Get the absolute file path in the file system */
	public String getFullPath() {
		return mp3file.getFile().getAbsolutePath();
	}

}