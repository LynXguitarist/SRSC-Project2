package api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import utils.File;
import utils.FilesToCopy;

@Path(FileStorage.PATH)
public interface FileStorage {

	String PATH = "/storage";

	@POST
	@Path("/createUserDir/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	void createUserPath(@PathParam("username") String username);

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
	List<String> ls(@PathParam("username") String username, @Context HttpHeaders headers);

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
	List<String> ls(@PathParam("username") String username, @PathParam("path") String path,
			@Context HttpHeaders headers);

	/**
	 * creates a dir in the path
	 * 
	 * @param username
	 * @param path
	 */
	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	void mkdir(@PathParam("username") String username, String path, @Context HttpHeaders headers);

	/**
	 * Puts a file in the path
	 * 
	 * @param username
	 * @param path
	 * @param file
	 */
	@POST
	@Path("/{username}/{path}")
	@Consumes(MediaType.APPLICATION_JSON)
	void put(@PathParam("username") String username, @PathParam("path") String path, String fileName,
			@Context HttpHeaders headers);

	/**
	 * Returns the file in the path
	 * 
	 * @param username
	 * @param path
	 * @param file
	 * @return
	 */
	@GET
	@Path("/{username}/{path}/{fileName}")
	@Produces(MediaType.APPLICATION_JSON)
	File get(@PathParam("username") String username, @PathParam("path") String path,
			@PathParam("fileName") String fileName, @Context HttpHeaders headers);

	/**
	 * copies file in path to file2 in path2
	 * 
	 * @param username
	 * @param filesToCopy
	 */
	@POST
	@Path("/cp/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	void cp(@PathParam("username") String username, FilesToCopy filesToCopy, @Context HttpHeaders headers);

	/**
	 * Deletes the file from path
	 * 
	 * @param username
	 * @param path
	 * @param file
	 */
	@DELETE
	@Path("/{username}/{path}/{fileName}")
	void rm(@PathParam("username") String username, @PathParam("path") String path,
			@PathParam("fileName") String fileName, @Context HttpHeaders headers);

	/**
	 * Deletes the dir from path. If there are no more files/dirs, removes the path
	 * Otherwise returns error
	 * 
	 * @param username
	 * @param path
	 */
	@DELETE
	@Path("/{username}/{path}")
	void rmdir(@PathParam("username") String username, @PathParam("path") String path, @Context HttpHeaders headers);

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
	@Path("/file/{username}/{path}/{fileName}")
	@Produces(MediaType.APPLICATION_JSON)
	List<String> file(@PathParam("username") String username, @PathParam("path") String path,
			@PathParam("fileName") String fileName, @Context HttpHeaders headers);

}
