package accessControl;

import api.AccessControl;

public class AccessControlService implements AccessControl {

	@Override
	public void login(String username, String password) {
		// gera token
		// guarda token em session para aquele user
		// verifica para aquele user as permissoes

	}

}
