package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path(FileStorage.PATH)
public interface AccessControl {

	String PATH = "/path";

	@POST
	@Path("/{username}/{password}")
	@Consumes(MediaType.APPLICATION_JSON)
	void login(@PathParam("username") String username, @PathParam("password") String password);

	@GET
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	String getAccessPermission(@PathParam("username") String username);
}
