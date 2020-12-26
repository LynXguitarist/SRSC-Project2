package api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import utils.UserInfo;

@Path(FileStorage.PATH)
public interface Auth {

	String PATH = "/auth";

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	void addUser(UserInfo userInfo);

	@PUT
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	void updateUser(@PathParam("username") String username, UserInfo userInfo);

	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	boolean isActive(@PathParam("username") String username);
}
