package com.github.jbman.exex.representations;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customers")
public class Customers {
	@XmlElementRef
	public List<Customer> list;
	@XmlElementRef
	public AtomLink prev;
	@XmlElementRef
	public AtomLink next;

	public static Customers load() {
		try {
			JAXBContext context = JAXBContext.newInstance(Customers.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Customers) unmarshaller.unmarshal(new File(
					"D:/MY/workspace/exex/src/test/resources/customers.xml"));
		} catch (JAXBException e) {
			throw new IllegalStateException("Unable to load customers", e);
		}
	}
}
