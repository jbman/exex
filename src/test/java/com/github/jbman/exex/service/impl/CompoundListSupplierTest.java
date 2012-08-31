package com.github.jbman.exex.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

public class CompoundListSupplierTest {

	final Supplier<List<String>> supplierA = Suppliers
			.ofInstance((List<String>) Lists.newArrayList("a1", "a2"));

	final Supplier<List<String>> supplierB = Suppliers
			.ofInstance((List<String>) Lists.newArrayList("b1", "b2"));

	@Test(expected = NullPointerException.class)
	public void testNullCheck() {

		new CompoundListSupplier<String>().add(null);
	}

	@Test
	public void test() {

		final CompoundListSupplier<String> compoundListSupplier = new CompoundListSupplier<String>()
				.add(supplierA).add(supplierB);

		final List<String> result = compoundListSupplier.get();

		assertEquals(Lists.newArrayList("a1", "a2", "b1", "b2"), result);
	}

	@Test
	public void testAddAll() {
		@SuppressWarnings("unchecked")
		final List<Supplier<List<String>>> suppliers = Lists
				.<Supplier<List<String>>> newArrayList(supplierA, supplierB);
		final CompoundListSupplier<String> compoundListSupplier = new CompoundListSupplier<String>()
				.addAll(suppliers);

		final List<String> result = compoundListSupplier.get();

		assertEquals(Lists.newArrayList("a1", "a2", "b1", "b2"), result);
	}
}
