package com.github.jbman.exex.resources;

import java.util.ArrayList;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.github.jbman.exex.representations.AtomLink;
import com.github.jbman.exex.representations.Customer;
import com.github.jbman.exex.representations.Customers;
import com.sun.jersey.api.view.Viewable;

@Path("/customers")
public class CustomerResource {

	@GET
	@Produces("application/xml")
	public Customers getCustomers(
			@QueryParam("start") @DefaultValue("0") long start,
			@QueryParam("size") @DefaultValue("10") int size,
			@Context UriInfo uriInfo) {
		Customers all = Customers.load();
		Customers result = new Customers();
		result.list = new ArrayList<Customer>();
		int i = 0;
		for (Customer c : all.list) {
			if ((i >= start) && (i < start + size)) {
				UriBuilder builder = uriInfo.getBaseUriBuilder();
				builder.path(CustomerResource.class);
				builder.path(CustomerResource.class, "getCustomer");
				c.self = new AtomLink("self", builder.build(c.id).toString());
				result.list.add(c);
			}
			i++;
		}
		if (start > 0) {
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.queryParam("start", Math.max(start - size, 0));
			builder.queryParam("size", size);
			result.prev = new AtomLink("previous", builder.build().toString());
		}
		if (i > start + size) {
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.queryParam("start", start + size);
			builder.queryParam("size", size);
			result.next = new AtomLink("next", builder.build().toString());
		}
		return result;
	}

	@GET
	@Path("edit")
	public Viewable getCustomersEdit(@Context UriInfo uriInfo) {
		UriBuilder builder = uriInfo.getBaseUriBuilder();
		builder.path(CustomerResource.class);
		return new Viewable("/customers-edit.jsp", builder.build().toString());
	}

	@GET
	@Path("{id: \\d+}")
	@Produces("application/xml")
	public Response getCustomer(@PathParam("id") long id,
			@Context UriInfo uriInfo) {
		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		Customers all = Customers.load();
		for (Customer c : all.list) {
			if (c.id == id) {
				c.self = new AtomLink("self", uriInfo.getAbsolutePathBuilder()
						.build().toString());
				builder.status(Status.OK).entity(c);
				break;
			}
		}
		return builder.build();
	}
}
