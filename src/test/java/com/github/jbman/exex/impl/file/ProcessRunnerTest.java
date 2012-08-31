package com.github.jbman.exex.impl.file;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.github.jbman.exex.impl.file.ProcessRunner.ProcessExecutionResult;
import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.model.ExecutionResult;

public class ProcessRunnerTest {

	private ProcessRunner runner;

	private void assertOutputStartsWith(final Execution execution,
			String expected) {
		assertTrue("Output '" + execution.getOutput() + "' starts with "
				+ expected, execution.getOutput().startsWith(expected));
	}

	@Before
	public void setUp() {
		runner = new ProcessRunner();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRunWithNonExistingFile() {
		final FileExecutable executable = new FileExecutable(new File(
				"src/test/resources/not-existing"));
		runner.run(executable);
	}

	@Test
	public void testRun() {
		final FileExecutable executable = new FileExecutable(new File(
				"src/test/resources/echo.cmd"));
		final Execution execution = runner.run(executable, "hello world");
		final ExecutionResult result = execution.waitForResult();
		// Remark: Under Windows calling echo in a script seems to add quotes to
		// the argument
		assertOutputStartsWith(execution, "\"hello world\"");
		assertTrue("result instanceof ProcessExecutionResult",
				result instanceof ProcessExecutionResult);
		assertEquals("Exit value is 0", 0,
				((ProcessExecutionResult) result).getExitValue());
	}

	@Test
	@Ignore("fails")
	public void testOutputEncoding() {
		final FileExecutable executable = new FileExecutable(new File(
				"src/test/resources/echo.cmd"));
		final Execution execution = runner.run(executable, "дцья");
		execution.waitForResult();
		assertOutputStartsWith(execution, "\"дцья\"");
	}
}
