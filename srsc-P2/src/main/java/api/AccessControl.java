package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import utils.AServer;

@Path(AccessControl.PATH)
public interface AccessControl {

	String PATH = "/accessControl";

	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response login(@PathParam("username") String username, AServer aServer);

	@GET
	@Path("/token/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	boolean isTokenValid(@PathParam("username") String username);

	@GET
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	String getAccessPermissions(@PathParam("username") String username);
}
