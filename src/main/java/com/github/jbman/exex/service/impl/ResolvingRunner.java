package com.github.jbman.exex.service.impl;

import java.util.List;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.service.Runner;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * Resolves and delegates to an appropriate {@link Runner} registered with
 * {@link #addRunner(Runner, Predicate)}.
 * 
 * @author Johannes Bergmann
 */
public class ResolvingRunner implements Runner {

	private final List<Function<Executable, Runner>> resolveFunctions = Lists
			.newArrayList();

	/**
	 * Adds a runner which is used if the {@code applyPredicate} returns
	 * <code>true</code>. {@link Boolean#TRUE}.
	 */
	public ResolvingRunner addRunner(final Runner runner,
			final Predicate<Executable> applyPredicate) {
		resolveFunctions.add(new Function<Executable, Runner>() {
			@Override
			public Runner apply(Executable input) {
				return (applyPredicate.apply(input) ? runner : null);
			}
		});
		return this;
	}

	@Override
	public Execution run(final Executable executable, final String... arguments) {
		// Test all resolve functions until the first returns a non-null runner
		for (Function<Executable, Runner> resolveFunction : resolveFunctions) {
			final Runner runner = resolveFunction.apply(executable);
			if (runner != null) {
				return runner.run(executable, arguments);
			}
		}
		throw new IllegalStateException("No runner found for executable: "
				+ executable);
	}

}
