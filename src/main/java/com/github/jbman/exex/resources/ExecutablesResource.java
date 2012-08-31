package com.github.jbman.exex.resources;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.github.jbman.exex.model.Executable;
import com.github.jbman.exex.model.ExecutableId;
import com.github.jbman.exex.model.Execution;
import com.github.jbman.exex.representations.AtomLink;
import com.github.jbman.exex.representations.ExecutableAtomRep;
import com.github.jbman.exex.representations.ExecutablesAtomRep;
import com.github.jbman.exex.representations.ExecutionAtomRep;
import com.github.jbman.exex.representations.Link;
import com.github.jbman.exex.service.ExecutableService;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.jersey.spi.resource.Singleton;

/**
 * Class with JAX-RS annotations to deliver representations of executables.
 * 
 * @author Johannes Bergmann
 */
@Singleton
@Path("/executables")
@Produces(MediaType.APPLICATION_ATOM_XML)
public class ExecutablesResource {

	private final ExecutableService executableService;

	public ExecutablesResource(final ExecutableService executableService) {
		this.executableService = executableService;
	}

	private static ExecutableAtomRep createExecutableRep(UriInfo uriInfo,
			Executable executable) {

		ExecutableAtomRep result = new ExecutableAtomRep();

		result.id = executable.getId().getValue();
		result.name = executable.getName();
		final UriBuilder selfBuilder = uriInfo.getBaseUriBuilder()
				.path(ExecutablesResource.class)
				.path(ExecutablesResource.class, "getExecutable");
		result.self = AtomLink.self(selfBuilder.build(result.id).toString());

		// TODO: "up" link only for Executable representation, not in list
		final UriBuilder upBuilder = uriInfo.getBaseUriBuilder().path(
				ExecutablesResource.class);
		result.up = AtomLink.up(upBuilder.build().toString());

		final UriBuilder runBuilder = uriInfo.getBaseUriBuilder()
				.path(ExecutablesResource.class)
				.path(ExecutablesResource.class, "runExecutable");
		// TODO Don't use AtomLink but own XML namespace, because "run" is no
		// standard rel type.
		result.run = new Link("run", runBuilder.build(result.id).toString());

		return result;
	}

	private static ExecutionAtomRep createExecutionRep(UriInfo uriInfo,
			Execution execution) {
		ExecutionAtomRep result = new ExecutionAtomRep();
		result.finished = execution.getResult().isDone();
		result.output = execution.getOutput();
		final UriBuilder relatedExecutableBuilder = uriInfo.getBaseUriBuilder()
				.path(ExecutablesResource.class)
				.path(ExecutablesResource.class, "getExecutable");
		result.relatedExecutable = AtomLink.related(relatedExecutableBuilder
				.build(execution.getExecutableId().getValue()).toString());

		return result;
	}

	private static String[] splitArguments(final String argumentsParam) {
		if (argumentsParam != null) {
			return Iterables.toArray(Splitter.on(";").split(argumentsParam),
					String.class);

		} else {
			return new String[0];
		}
	}

	@GET
	public ExecutablesAtomRep getExecutables(
			@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("size") @DefaultValue("10") int size,
			@Context final UriInfo uriInfo) {

		// TODO: Maybe optimize service using the start/size values?
		List<Executable> executables = executableService.getExecutables();

		final Function<Executable, ExecutableAtomRep> toExecutableRepresentation = new Function<Executable, ExecutableAtomRep>() {
			@Override
			public ExecutableAtomRep apply(Executable executable) {
				return createExecutableRep(uriInfo, executable);
			}
		};

		ExecutablesAtomRep result = new ExecutablesAtomRep();
		result.list = Lists.transform(
				executables.subList(start,
						Math.min(start + size, executables.size())),
				toExecutableRepresentation);

		if (start > 0) {
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.queryParam("start", Math.max(start - size, 0));
			builder.queryParam("size", size);
			result.prev = new AtomLink("previous", builder.build().toString());
		}
		if (executables.size() > start + size) {
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.queryParam("start", start + size);
			builder.queryParam("size", size);
			result.next = new AtomLink("next", builder.build().toString());
		}

		return result;
	}

	@GET
	@Path("{id}")
	public ExecutableAtomRep getExecutable(@PathParam("id") final String id,
			@Context final UriInfo uriInfo) {
		final Executable executable = executableService
				.getExecutable(new ExecutableId(id));
		return createExecutableRep(uriInfo, executable);
	}

	@GET
	@Path("{id}/run")
	public ExecutionAtomRep runExecutable(@PathParam("id") final String id,
			@QueryParam("arguments") final String argumentsParam,
			@Context final UriInfo uriInfo) {
		final Executable executable = executableService
				.getExecutable(new ExecutableId(id));
		final String[] arguments = splitArguments(argumentsParam);
		final Execution execution = executableService
				.run(executable, arguments);
		// TODO Persist execution as new resource and return the link to this
		// new resource immediatly. For now just wait until execution is
		// finished.
		execution.waitForResult();
		return createExecutionRep(uriInfo, execution);
	}
}
