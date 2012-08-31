package com.github.jbman.exex.representations;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
public class AtomLink extends Link {

	public AtomLink() {
	}

	public AtomLink(String rel, String href) {
		super(rel, href, "application/xml");
	}

	/**
	 * Conveys an identifier for the link's context.
	 * 
	 * See http://www.iana.org/assignments/link-relations/link-relations.xml
	 */
	public static AtomLink self(String href) {
		return new AtomLink("self", href);
	}

	/**
	 * Refers to a parent document in a hierarchy of documents.
	 * 
	 * See http://www.iana.org/assignments/link-relations/link-relations.xml
	 */
	public static AtomLink up(String href) {
		return new AtomLink("up", href);
	}

	public static AtomLink related(String href) {
		return new AtomLink("related", href);
	}
}