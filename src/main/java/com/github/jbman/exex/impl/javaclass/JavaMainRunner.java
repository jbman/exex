package com.github.jbman.exex.impl.javaclass;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Future;

import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.model.ExecutionResult;
import com.github.jbman.exex.service.AbstractRunner;
import com.google.common.util.concurrent.Futures;

/**
 * Implementation which runs a java class with a {@code main} method.
 * 
 * @author Johannes Bergmann
 */
public class JavaMainRunner extends AbstractRunner<JavaClassExecutable> {

	public JavaMainRunner() {
		super(JavaClassExecutable.class);
	}

	@Override
	public synchronized Execution doRun(final JavaClassExecutable executable,
			final String... arguments) {
		// Synchronized, because System.out is redirected temporarily and then
		// reset to its original value

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final PrintStream originalOut = System.out;
		System.setOut(new PrintStream(outputStream));

		invokeMainMethod(executable.getMainMethod(), arguments);

		System.setOut(originalOut);

		return new Execution(executable.getId()) {

			final String encoding = System.getProperty("file.encoding");

			@Override
			public Future<ExecutionResult> getResult() {
				return Futures.immediateFuture(new ExecutionResult());
			}

			@Override
			public String getOutput() {
				try {
					return outputStream.toString(encoding);
				} catch (final UnsupportedEncodingException e) {
					throw new IllegalStateException(e);
				}
			}
		};
	}

	private void invokeMainMethod(final Method mainMethod,
			final String... arguments) {
		try {
			mainMethod.invoke(null, (Object) arguments);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException("Error invoking main method", e);
		} catch (final InvocationTargetException e) {
			throw new IllegalStateException("Error invoking main method", e);
		}
	}
}
