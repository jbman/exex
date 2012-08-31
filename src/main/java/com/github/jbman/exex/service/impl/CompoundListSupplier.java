package com.github.jbman.exex.service.impl;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Supplier which concatenates lists provided by other suppliers of type
 * {@code Supplier<List<T>>}.
 * 
 * @param <T>
 *            The type of the elements in the supplied list.
 * 
 * @author Johannes Bergmann (imm0112)
 */
public class CompoundListSupplier<T> implements Supplier<List<T>> {

	private final List<Supplier<List<T>>> suppliers = Lists.newArrayList();;

	public CompoundListSupplier() {
		// Default constructor
	}

	public CompoundListSupplier(List<Supplier<List<T>>> suppliers) {
		this.suppliers.addAll(suppliers);
	}

	public CompoundListSupplier<T> addAll(List<Supplier<List<T>>> suppliers) {
		Preconditions.checkNotNull(suppliers);
		this.suppliers.addAll(suppliers);
		return this;
	}

	public CompoundListSupplier<T> add(Supplier<List<T>> listSupplier) {
		Preconditions.checkNotNull(listSupplier);
		this.suppliers.add(listSupplier);
		return this;
	}

	@Override
	public List<T> get() {
		return Lists.newArrayList(Iterables.concat(Iterables.transform(
				suppliers, Suppliers.<List<T>> supplierFunction())));
	}
}
