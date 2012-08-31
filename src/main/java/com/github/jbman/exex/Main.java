package com.github.jbman.exex;

import java.util.Set;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.github.jbman.exex.impl.file.FileExecutableSupplier;
import com.github.jbman.exex.impl.file.FileSelection;
import com.github.jbman.exex.impl.file.PatternFileSelection;
import com.github.jbman.exex.impl.file.ProcessRunner;
import com.github.jbman.exex.impl.javaclass.JavaClassExecutableSupplier;
import com.github.jbman.exex.impl.javaclass.JavaMainRunner;
import com.github.jbman.exex.resources.CustomerResource;
import com.github.jbman.exex.resources.ExecutablesResource;
import com.github.jbman.exex.service.ExecutableService;
import com.github.jbman.exex.service.ExecutableSupplier;
import com.github.jbman.exex.service.impl.ExecutableServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Main class of the exex server. Starts an embedded jetty. Requests are handled
 * by JAX-RS annotated classes in same package as this class.
 * 
 * @author Johannes Bergmann
 */
public class Main extends DefaultResourceConfig {

	public static void main(final String[] args) throws Exception {
		final ServletHolder servletHolder = new ServletHolder(
				ServletContainer.class);
		servletHolder.setInitParameter(ServletContainer.RESOURCE_CONFIG_CLASS,
				Main.class.getCanonicalName());
		// TODO: Linking feature causes strange exception in hashCode()
		// execution when it processes all fields in *AtomRep instance
		// servletHolder.setInitParameter(
		// "com.sun.jersey.spi.container.ContainerResponseFilters",
		// LinkFilter.class.getCanonicalName());
		final Server server = new Server(8088);
		final ServletContextHandler servletContextHandler = new ServletContextHandler(
				server, "/");
		servletContextHandler.addServlet(servletHolder, "/*");
		server.start();
		server.join();
	}

	public Main() {
		// getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
		// Boolean.TRUE);
	}

	@Override
	public Set<Object> getSingletons() {
		final ExecutableService executableService = createExecutableService();
		final ExecutablesResource executablesResource = new ExecutablesResource(
				executableService);
		return Sets.<Object> newHashSet(executablesResource,
				new CustomerResource());
	}

	private ExecutableService createExecutableService() {
		final ExecutableServiceImpl executableService = new ExecutableServiceImpl();

		// Add Executable suppliers
		executableService.addExecutableSupplier(createFileExecutableSupplier());
		executableService
				.addExecutableSupplier(createJavaClassExecutableSupplier());

		// Add runners
		executableService.addRunner(new ProcessRunner());
		executableService.addRunner(new JavaMainRunner());
		return executableService;
	}

	private ExecutableSupplier createFileExecutableSupplier() {
		final FileSelection fileSelection = new PatternFileSelection(
				"src/test/resources", "*.cmd", "*.sh");
		return new FileExecutableSupplier(fileSelection);
	}

	private ExecutableSupplier createJavaClassExecutableSupplier() {
		// TODO: Write suitable demo class, e.g. a class to read files in
		// directory with exposed commands
		return new JavaClassExecutableSupplier(Lists.newArrayList(Main.class
				.getCanonicalName()));
	}
}
