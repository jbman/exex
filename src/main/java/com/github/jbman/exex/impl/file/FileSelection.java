

package com.github.jbman.exex.impl.file;

import java.io.File;
import java.util.Set;


/**
 * Specifies a set of files.
 * 
 * @author Johannes Bergmann
 */
public interface FileSelection
{

   /**
    * @return a set of selected files.
    */
   Set<File> getFiles();
}
