package com.github.jbman.exex.representations;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "executables")
public class ExecutablesAtomRep {
	@XmlElementRef
	public List<ExecutableAtomRep> list;
	@XmlElementRef
	public AtomLink prev;
	@XmlElementRef
	public AtomLink next;

	@Override
	public String toString() {
		return "ExecutablesAtomRep [list=" + list + "]";
	}
}
