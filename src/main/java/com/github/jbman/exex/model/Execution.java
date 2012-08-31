package com.github.jbman.exex.model;

import java.util.concurrent.Future;

import com.google.common.util.concurrent.Futures;

/**
 * Represents an execution of an {@link Executable}.
 * 
 * @author Johannes Bergmann
 */
public abstract class Execution {

	private final ExecutableId executableId;

	public Execution(ExecutableId executableId) {
		this.executableId = executableId;
	}

	public ExecutableId getExecutableId() {
		return executableId;
	}

	/**
	 * Waits for the result uninterruptibly.
	 */
	public ExecutionResult waitForResult() {
		return Futures.getUnchecked(getResult());
	}

	/**
	 * @return a Future which is done when the execution is finished.
	 */
	public abstract Future<ExecutionResult> getResult();

	/**
	 * The output of running the {@link Executable}. This may be an intermediary
	 * result, if the execution is still running. If the execution is still
	 * running can be checked by calling {@link getResult#isDone()}. The final
	 * output will be returned if {@code isDone} returns <code>true</code>.
	 */
	public abstract String getOutput();
}
