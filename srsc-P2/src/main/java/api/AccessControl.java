package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.AServer;


@Path(AccessControl.PATH)
public interface AccessControl {

	String PATH = "/accessControl";

	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	String login(@PathParam("username") String username, AServer aServer);

	@GET
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	String getAccessPermissions(@PathParam("username") String username);
}
