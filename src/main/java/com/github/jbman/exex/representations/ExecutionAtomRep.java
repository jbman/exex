package com.github.jbman.exex.representations;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB mapped representation of an Execution.
 */
@XmlRootElement(name = "execution")
public class ExecutionAtomRep {

	@XmlElementRef
	public AtomLink relatedExecutable;

	@XmlElement
	public boolean finished;

	@XmlElement
	public String output;
}
