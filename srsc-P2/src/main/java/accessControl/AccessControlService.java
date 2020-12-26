package accessControl;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import api.AccessControl;

public class AccessControlService implements AccessControl {

	private static final String PATH = "access.conf";

	private Properties prop;

	public AccessControlService() {
		init();
	}

	private void init() {
		prop = new Properties();
		FileReader file;
		try {
			file = new FileReader(PATH);
			prop.load(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void login(String username, String password) {
		// chama o MainDispatcher, que chama o AuthService para verificar se um cliente
		// existe ou nao
		// gera token
		// guarda token em session para aquele user
		// verifica para aquele user as permissoes
	}

	@Override
	public String getAccessPermission(String username) {
		String permission = prop.getProperty(username);
		return permission;
	}

}
