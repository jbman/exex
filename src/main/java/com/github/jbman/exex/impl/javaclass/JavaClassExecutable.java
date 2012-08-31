package com.github.jbman.exex.impl.javaclass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.github.jbman.exex.model.Executable;

/**
 * Executable for a java class with a {@code main} method.
 * 
 * @author Johannes Bergmann
 */
public class JavaClassExecutable extends Executable {
	private final Class<?> javaClass;

	public JavaClassExecutable(final Class<?> classWithMainMethod) {
		javaClass = classWithMainMethod;
		// Check if the static main method exists:
		getMainMethod();
	}

	public Method getMainMethod() {
		final Class<?>[] parameterTypes = new Class[] { String[].class };
		try {
			final Method mainMethod = javaClass.getMethod("main",
					parameterTypes);
			if (!Modifier.isStatic(mainMethod.getModifiers())) {
				throw new IllegalArgumentException("Main method of class"
						+ javaClass + "isn't static");
			}
			return mainMethod;
		} catch (final SecurityException e) {
			throw new IllegalStateException(
					"Getting main method isn't allowed by Security Manager", e);
		} catch (final NoSuchMethodException e) {
			throw new IllegalArgumentException("The class " + javaClass
					+ " doesn't have a main method", e);
		}
	}

	public String getName() {
		return javaClass.getSimpleName();
	}

	@Override
	public String toString() {
		return "JavaClassExecutable [javaClass=" + javaClass + "]";
	}
}
