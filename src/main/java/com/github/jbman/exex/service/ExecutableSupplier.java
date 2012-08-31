package com.github.jbman.exex.service;

import java.util.List;

import com.github.jbman.exex.model.Executable;
import com.google.common.base.Supplier;

/**
 * Supplier which supplies a list of Executables.
 * 
 * @author Johannes Bergmann
 */
public interface ExecutableSupplier extends Supplier<List<Executable>> {

}
