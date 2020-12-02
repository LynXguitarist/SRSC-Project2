package api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(FileStorage.PATH)
public interface FileStorage {

	String PATH = "/storage";

	/**
	 * shows files or dirs of the user in his home root
	 * 
	 * @param username
	 * @param path
	 * @return
	 */
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	List<String> ls(@PathParam("username") String username);

	/**
	 * shows files or dirs of the user in the path
	 * 
	 * @param username
	 * @param path
	 * @return
	 */
	@GET
	@Path("/{username}/{path}")
	@Produces(MediaType.APPLICATION_JSON)
	List<String> ls(@PathParam("username") String username, @PathParam("path") String path);

	/**
	 * creates a dir in the path
	 * 
	 * @param username
	 * @param path
	 */
	@POST
	@Path("/{username}/{path}")
	@Consumes(MediaType.APPLICATION_JSON)
	void mkdir(@PathParam("username") String username, @PathParam("path") String path);

	/**
	 * Puts a file in the path
	 * 
	 * @param username
	 * @param path
	 * @param file
	 */
	@POST
	@Path("/{username}/{path}/{file}")
	@Consumes(MediaType.APPLICATION_JSON)
	void put(@PathParam("username") String username, @PathParam("path") String path, @PathParam("file") String file);

	/**
	 * Returns the file in the path
	 * 
	 * @param username
	 * @param path
	 * @param file
	 * @return
	 */
	@GET
	@Path("/{username}/{path}/{file}")
	@Produces(MediaType.APPLICATION_JSON)
	String get(@PathParam("username") String username, @PathParam("path") String path, @PathParam("file") String file);

	/**
	 * copies file in path to file2 in path2
	 * 
	 * @param username
	 * @param path
	 * @param file
	 * @param path2
	 * @param file2
	 */
	@POST
	@Path("/{username}/{path}/{file}/{path2}/{file2}")
	@Consumes(MediaType.APPLICATION_JSON)
	void cp(@PathParam("username") String username, @PathParam("path") String path, @PathParam("file") String file,
			@PathParam("path2") String path2, @PathParam("file2") String file2);

	/**
	 * Deletes the file from path
	 * 
	 * @param username
	 * @param path
	 * @param file
	 */
	@DELETE
	@Path("/{username}/{path}/{file}")
	void rm(@PathParam("username") String username, @PathParam("path") String path, @PathParam("file") String file);

	/**
	 * Deletes the dir from path. If there are no more files/dirs, removes the path
	 * Otherwise returns error
	 * 
	 * @param username
	 * @param path
	 */
	@DELETE
	@Path("/{username}/{path}")
	void rmdir(@PathParam("username") String username, @PathParam("path") String path);

	/**
	 * Returns a list of attributes from the file. If it is a dir, returns its name,
	 * if it is a file, returns {typeOfFile, CreatedOn, UpdatedOn}
	 * 
	 * @param username
	 * @param path
	 * @param file
	 * @return
	 */
	@GET
	@Path("/file/{username}/{path}/{file}")
	@Produces(MediaType.APPLICATION_JSON)
	List<String> file(@PathParam("username") String username, @PathParam("path") String path,
			@PathParam("file") String file);

}
