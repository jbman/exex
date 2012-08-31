package com.github.jbman.exex.service;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.service.impl.ResolvingRunner;
import com.google.common.base.Predicate;

/**
 * Base class for {@code Runner} implementations which can run a particular
 * {@link Executable} subclass.
 * 
 * @author Johannes Bergmann
 */
public abstract class AbstractRunner<E extends Executable> implements Runner {

	private final Class<E> type;

	protected AbstractRunner(final Class<E> type) {
		this.type = type;
	}

	public void registerAt(ResolvingRunner resolvingRunner) {
		resolvingRunner.addRunner(this, new Predicate<Executable>() {

			@Override
			public boolean apply(Executable input) {
				return type.isAssignableFrom(input.getClass());
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Execution run(Executable executable, String... arguments) {
		return doRun((E) executable, arguments);
	}

	public abstract Execution doRun(E executable, String... arguments);

}
