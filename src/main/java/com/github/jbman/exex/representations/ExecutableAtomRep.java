package com.github.jbman.exex.representations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jbman.exex.model.Executable;

/**
 * JAXB mapped representation of an {@link Executable}.
 */
@XmlRootElement(name = "executable")
public class ExecutableAtomRep {

	@XmlAttribute
	public String id;

	@XmlElement
	public String name;

	@XmlElementRef
	public AtomLink self;

	@XmlElementRef
	public AtomLink up;

	@XmlElementRef
	public Link run;

	@Override
	public String toString() {
		return "ExecutableAtomRep [id=" + id + "]";
	}
}