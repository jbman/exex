package com.github.jbman.exex.service;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.model.Execution;

/**
 * Runs an {@link Executable}.
 * 
 * @author Johannes Bergmann
 */
public interface Runner {

	/**
	 * Runs this executable and returns an {@link Execution} which allows to
	 * access the result. A separate process may be started so that, the
	 * {@link Execution} is returned before the execution is finished.
	 */
	Execution run(final Executable executable, final String... arguments);

}
