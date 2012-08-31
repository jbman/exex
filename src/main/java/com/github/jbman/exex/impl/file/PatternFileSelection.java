

package com.github.jbman.exex.impl.file;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * Specifies a set of files by a base directory and one or more filename pattern. The filename pattern is case
 * insensitive and supports the characters '?' and '*' to represent a single or multiple wildcard characters.
 * 
 * @author Johannes Bergmann
 */
public class PatternFileSelection implements FileSelection
{
   private final File directory;

   private final String[] filenamePatterns;

   /**
    * Constructs an ExecutableFinder which finds all executable files matching the given {@link FileFilter}.
    */
   public PatternFileSelection(final String path, final String... filenamePatterns)
   {
      directory = new File(path);
      this.filenamePatterns = filenamePatterns;
      Preconditions.checkArgument(directory.isDirectory(), "Given path isn't an existing directory: " + path);
   }

   @Override
   public Set<File> getFiles()
   {
      final FileFilter fileFilter = new WildcardFileFilter(filenamePatterns, IOCase.INSENSITIVE);
      final File[] files = directory.listFiles(fileFilter);
      return Sets.newHashSet(files);
   }
}
