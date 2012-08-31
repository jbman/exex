package com.github.jbman.exex.impl.javaclass;

import java.util.List;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.service.ExecutableSupplier;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class JavaClassExecutableSupplier implements ExecutableSupplier {

	private final List<String> classNames;

	public JavaClassExecutableSupplier(List<String> classNames) {
		this.classNames = classNames;
	}

	@Override
	public List<Executable> get() {
		return Lists.newArrayList(Iterables.transform(classNames,
				new Function<String, Executable>() {

					@Override
					public Executable apply(String className) {
						try {
							return new JavaClassExecutable(Class
									.forName(className));
						} catch (ClassNotFoundException e) {
							throw new IllegalArgumentException(e);
						}
					}
				}));
	}

}
