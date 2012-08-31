package com.github.jbman.exex.impl.javaclass;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.jbman.exex.model.Execution;

public class JavaMainRunnerTest {

	private JavaMainRunner runner;

	@Before
	public void setUp() {
		runner = new JavaMainRunner();
	}

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testRunNoMainMethod() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(Object.class.getName()); // The class should be
															// mentioned in the
															// message
		new JavaClassExecutable(Object.class);
	}

	@Test
	public void testRunMainMethodNotStatic() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("static");
		exception.expectMessage(NonStaticMain.class.getName()); // The class
																// should be
																// mentioned in
																// the message
		new JavaClassExecutable(NonStaticMain.class);
	}

	@Test
	public void testRunNoArg() {
		final JavaClassExecutable executable = new JavaClassExecutable(
				TestClassWithMain.class);
		final Execution execution = runner.run(executable);
		assertEquals("hello", execution.getOutput());
	}

	@Test
	public void testRunTwoArgs() {
		final JavaClassExecutable executable = new JavaClassExecutable(
				TestClassWithMain.class);
		final Execution execution = runner.run(executable, " world", "!");
		assertEquals("hello world!", execution.getOutput());
	}

	@Test
	public void testRunOutputEncoding() {
		final JavaClassExecutable executable = new JavaClassExecutable(
				TestClassWithMain.class);
		final Execution execution = runner.run(executable, " äöüß");
		assertEquals("hello äöüß", execution.getOutput());
	}

	public static class TestClassWithMain {
		public static void main(final String[] args) {
			System.out.print("hello");
			for (final String arg : args) {
				System.out.print(arg);
			}
		}
	}

	public static class NonStaticMain {
		public void main(final String[] args) {
			// I'm not static
		}
	}
}
