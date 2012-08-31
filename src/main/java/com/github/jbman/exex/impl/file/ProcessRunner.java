package com.github.jbman.exex.impl.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.github.jbman.exex.model.ExecutableId;
import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.model.ExecutionResult;
import com.github.jbman.exex.service.AbstractRunner;
import com.google.common.util.concurrent.SettableFuture;

/**
 * Runner implementation which starts a new process on the host operating
 * system.
 * 
 * @author Johannes Bergmann
 */
public class ProcessRunner extends AbstractRunner<FileExecutable> {

	public ProcessRunner() {
		super(FileExecutable.class);
	}

	@Override
	public Execution doRun(final FileExecutable executable,
			final String... arguments) {

		final List<String> commands = new ArrayList<String>(
				arguments.length + 1);
		commands.add(executable.getAbsolutePath());
		commands.addAll(Arrays.asList(arguments));
		final ProcessBuilder processBuilder = new ProcessBuilder(commands);
		processBuilder.redirectErrorStream(true);

		final ProcessExecution execution = new ProcessExecution(
				executable.getId());
		try {
			final Process process = processBuilder.start();
			pipeProcessToOutput(process.getInputStream(),
					execution.outputWriter);
			// Wait in other thread until process finished
			waitForProcessFinishInNewThread(process, execution);
		} catch (final IOException e) {
			throw new IllegalStateException("Process could not be started", e);
		}
		return execution;
	}

	private void waitForProcessFinishInNewThread(final Process process,
			final ProcessExecution execution) {
		final Runnable waitingRunnble = new Runnable() {
			@Override
			public void run() {
				try {
					// Include to simulate long running process:
					// Thread.sleep(1000);
					final int exitValue = process.waitFor();
					execution.resultFuture.set(new ProcessExecutionResult(
							exitValue));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new IllegalStateException(
							"Waiting for process to be finished was interrupted",
							e);
				}
			}
		};
		Thread thread = new Thread(waitingRunnble);
		thread.start();
	}

	private void pipeProcessToOutput(final InputStream in, final Writer out) {
		try {
			int read;
			while ((read = in.read()) > 0) {
				out.write(read);
			}
		} catch (final IOException e) {
			throw new IllegalStateException(
					"Error while reading process output", e);
		}
	}

	private static class ProcessExecution extends Execution {

		public ProcessExecution(ExecutableId executableId) {
			super(executableId);
		}

		final StringWriter outputWriter = new StringWriter();

		final SettableFuture<ExecutionResult> resultFuture = SettableFuture
				.create();

		@Override
		public Future<ExecutionResult> getResult() {
			return resultFuture;
		}

		@Override
		public String getOutput() {
			return outputWriter.toString();
		}
	}

	public static class ProcessExecutionResult extends ExecutionResult {
		final int exitValue;

		public ProcessExecutionResult(int exitValue) {
			this.exitValue = exitValue;
		}

		public int getExitValue() {
			return exitValue;
		}
	}

}
