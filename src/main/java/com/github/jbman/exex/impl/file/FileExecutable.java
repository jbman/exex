package com.github.jbman.exex.impl.file;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

import com.github.jbman.exex.model.Executable;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;

/**
 * Executable for a file or command (e.g. a shell script).
 * 
 * @author Johannes Bergmann
 */
@XmlRootElement
public class FileExecutable extends Executable {

	public static Function<File, FileExecutable> fileToExecutable = new Function<File, FileExecutable>() {
		@Override
		public FileExecutable apply(final File file) {
			return new FileExecutable(file);
		}
	};

	private final File file;

	protected FileExecutable() {
		// Default constructor for JAXB
		file = null;
	}

	/**
	 * Creates a new {@link FileExecutable}. It is checked if the provided file
	 * does a exists and if it is allowed to execute it.
	 * 
	 * @param file
	 *            The file which can be executed.
	 */
	public FileExecutable(final File file) {
		this.file = file;
		Preconditions.checkArgument(file.exists(), "File does not exists: "
				+ file.getAbsolutePath());
		Preconditions.checkArgument(file.canExecute(),
				"Execution of file is not allowed: " + file.getAbsolutePath());
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	/**
	 * The file name of the executable file (only the name without path to the
	 * file).
	 */
	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String toString() {
		return "FileExecutable [absolutePath=" + getAbsolutePath() + "]";
	}

}
