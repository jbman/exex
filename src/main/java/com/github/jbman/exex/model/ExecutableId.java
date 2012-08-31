package com.github.jbman.exex.model;


/**
 * An identifier of an executable.
 * 
 * @author Johannes Bergmann
 */
public class ExecutableId {

	private String value;

	protected ExecutableId() {
		// Default Constructor for JAXB
	}

	public ExecutableId(String value) {
		this.value = value;
	}

	/**
	 * Provides a name for referencing this executable.
	 */
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecutableId other = (ExecutableId) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
