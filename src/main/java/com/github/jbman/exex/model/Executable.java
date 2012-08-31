package com.github.jbman.exex.model;

import com.github.jbman.exex.service.Runner;
import com.google.common.base.Function;

/**
 * Something that can be executed (e.g. a file or a java class) by a
 * {@link Runner}.
 * 
 * @author Johannes Bergmann
 */
public abstract class Executable {

	public static Function<Executable, ExecutableId> toId = new Function<Executable, ExecutableId>() {

		@Override
		public ExecutableId apply(Executable executable) {
			return executable.getId();
		}
	};

	/**
	 * Provides the stable identifier of this executable. By default
	 * {@link #getName()} is returned. A subclass can override this method, e.g.
	 * if the name doasn't identify an Executable unambiguously.
	 */
	public ExecutableId getId() {
		return new ExecutableId(getName());
	}

	/**
	 * Provides a speaking name for this executable
	 */
	public abstract String getName();

}
