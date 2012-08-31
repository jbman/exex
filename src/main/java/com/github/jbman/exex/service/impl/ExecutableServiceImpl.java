package com.github.jbman.exex.service.impl;

import java.util.List;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.model.ExecutableId;
import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.service.AbstractRunner;
import com.github.jbman.exex.service.ExecutableService;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;

public class ExecutableServiceImpl implements ExecutableService {

	private final CompoundListSupplier<Executable> executableSupplier = new CompoundListSupplier<Executable>();

	private final ResolvingRunner resolvingRunner = new ResolvingRunner();

	public void addExecutableSupplier(
			Supplier<List<Executable>> executableSupplier) {
		this.executableSupplier.add(executableSupplier);
	}

	public void addRunner(AbstractRunner<? extends Executable> runner) {
		runner.registerAt(resolvingRunner);
	}

	@Override
	public List<Executable> getExecutables() {
		return executableSupplier.get();
	}

	@Override
	public Executable getExecutable(final ExecutableId id) {
		return Iterables.find(getExecutables(),
				Predicates.compose(Predicates.equalTo(id), Executable.toId));
	}

	@Override
	public Execution run(Executable executable, String... arguments) {
		return resolvingRunner.run(executable, arguments);
	}

}
