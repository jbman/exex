package com.github.jbman.exex.impl.file;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class PatternFileSelectionTest {

	private static final int EXPECTED_NO_OF_EXECUTABLES = 2;
	@Rule
	public ExpectedException exception = ExpectedException.none();

	private static void assertContainsFileNamed(String name, Set<File> files) {
		final Function<File, String> fileToName = new Function<File, String>() {
			@Override
			public String apply(File input) {
				return input.getName();
			}
		};
		assertTrue("Expected files to contain a file named " + name,
				Sets.newHashSet(Iterables.transform(files, fileToName))
						.contains(name));
	}

	@Test
	public void testGetFiles() {
		final PatternFileSelection finder = new PatternFileSelection(
				"src/test/resources", "*.cmd");

		final Set<File> files = finder.getFiles();

		assertEquals(EXPECTED_NO_OF_EXECUTABLES, files.size());
		assertContainsFileNamed("echo.cmd", files);
	}

	@Test
	public void testGetFilesIsCaseInsensitive() {
		final PatternFileSelection finder = new PatternFileSelection(
				"src/test/resources", "*.CMD");

		final Set<File> files = finder.getFiles();

		assertEquals(EXPECTED_NO_OF_EXECUTABLES, files.size());
		assertContainsFileNamed("echo.cmd", files);
	}

	@Test
	public void testGetFilesTwoPatterns() {
		final PatternFileSelection finder = new PatternFileSelection(
				"src/test/resources", "*.cmd", "*.txt");

		final Set<File> files = finder.getFiles();

		assertEquals(EXPECTED_NO_OF_EXECUTABLES + 1, files.size());
	}

	@Test
	public void testGetFilesNonExistingDirectory() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("src/test/not-existing"); // The wrong folder
															// should be
															// mentioned in the
															// message

		new PatternFileSelection("src/test/not-existing");
	}
}
