package com.github.jbman.exex.representations;

import javax.xml.bind.annotation.XmlAttribute;

public class Link {
	public @XmlAttribute
	String rel;
	public @XmlAttribute
	String href;
	public @XmlAttribute
	String type;

	public Link() {
	}

	public Link(String rel, String href) {
		this(rel, href, "application/xml");
	}

	public Link(String rel, String href, String type) {
		this.rel = rel;
		this.href = href;
		this.type = type;
	}
}
