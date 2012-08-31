package com.github.jbman.exex.service.impl;

import static org.mockito.Mockito.*;

import org.junit.Test;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.service.Runner;
import com.google.common.base.Predicates;

public class ResolvingRunnerTest {

	@Test
	public void test() {

		final Executable a = mock(Executable.class);
		final Executable b = mock(Executable.class);
		final Runner runnerA = mock(Runner.class);
		final Runner runnerB = mock(Runner.class);
		final Runner runnerWhichRunsBToo = mock(Runner.class);

		final ResolvingRunner resolvingRunner = new ResolvingRunner();
		resolvingRunner.addRunner(runnerA, Predicates.equalTo(a));
		resolvingRunner.addRunner(runnerB, Predicates.equalTo(b));
		resolvingRunner.addRunner(runnerWhichRunsBToo, Predicates.equalTo(a));

		resolvingRunner.run(b, "1", "2", "3");

		verifyZeroInteractions(runnerA);
		verify(runnerB).run(b, "1", "2", "3");
		verifyZeroInteractions(runnerWhichRunsBToo);
	}
}
