package com.github.jbman.exex.service;

import java.util.List;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.model.ExecutableId;
import com.github.jbman.exex.model.Execution;

/**
 * Service to list and run executable files.
 * 
 * @author Johannes Bergmann
 */
public interface ExecutableService {

	public List<Executable> getExecutables();

	public Executable getExecutable(ExecutableId id);

	public Execution run(Executable executable, String... arguments);
}
