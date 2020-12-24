package api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(FileStorage.PATH)
public interface Auth {

	String PATH = "/auth";
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	boolean isActive(@PathParam("username") String username);
}
