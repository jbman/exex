package com.github.jbman.exex.impl.file;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.service.ExecutableSupplier;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

public class FileExecutableSupplier implements ExecutableSupplier {

	private final FileSelection fileSelection;

	public FileExecutableSupplier(final FileSelection fileSelection) {
		this.fileSelection = fileSelection;
	}

	@Override
	public List<Executable> get() {
		final Collection<File> files = fileSelection.getFiles();
		return Lists.<Executable> newArrayList(Iterators.transform(
				files.iterator(), FileExecutable.fileToExecutable));
	}

}
