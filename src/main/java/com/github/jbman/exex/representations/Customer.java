package com.github.jbman.exex.representations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customer")
public class Customer {
	@XmlAttribute
	public long id;
	@XmlElement(name = "first-name")
	public String firstName;
	@XmlElement(name = "last-name")
	public String lastName;
	@XmlElement
	public String street;
	@XmlElement
	public String city;
	@XmlElement
	public String state;
	@XmlElement
	public String zip;
	@XmlElementRef
	public AtomLink self;
}