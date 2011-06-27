package com.google.code.tvrenamer.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class FileEpisodeTest {
	private static Logger logger = Logger.getLogger(FileEpisodeTest.class.getName());
	
	private FileEpisode fileEpisode;
	
	private List<File> testFiles;
	
	private UserPreferences prefs;

	@Before
	public void setUp() throws Exception {
		testFiles = new ArrayList<File>();
		prefs = Mockito.mock(UserPreferences.class);
	}

	/**
	 * Test case for Issue 37 where the title "$pringfield"
	 * breaks the regex used for String.replaceAll()
	 */
	@Test
	public void testGetNewFilenameSpecialRegexChars() throws Exception {
		String showName = "The Simpsons";
		String title = "$pringfield";
		int seasonNum = 5;
		int episodeNum = 10;
		File file = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "the.simpsons.5.10.avi");
		file.createNewFile();
		testFiles.add(file);
		
		Show show = new Show("1", showName, "http://www.tvrage.com/shows/id-6190");
		Season season5 = new Season(seasonNum);
		season5.setEpisode(episodeNum, title);
		show.setSeason(seasonNum, season5);
		ShowStore.addShow(showName, show);		
		
		Mockito.when(prefs.getRenameReplacementString()).thenReturn("%S [%sx%e] %t");
		
		fileEpisode = new FileEpisode(showName, seasonNum, episodeNum, file);
		fileEpisode.setStatus(EpisodeStatus.RENAMED);
		
		String newFilename = fileEpisode.getNewFilename(prefs);
		
		Assert.assertEquals("The Simpsons [5x10] $pringfield.avi", newFilename);
	}
	
	@After
	public void teardown() throws Exception {
		for (File file : testFiles) {
			logger.info("Deleting " + file);
			file.delete();
		}
	}
}
