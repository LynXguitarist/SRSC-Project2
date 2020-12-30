package utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;

import javax.crypto.KeyAgreement;

public class DHServer {

	private KeyAgreement aKeyAgree;

	public DHServer() throws Exception {
		// nothing here
	}

	public KeyPair init() throws Exception {
	
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
		keyGen.initialize(512);
		// set up
		// Simulation in A side
		aKeyAgree = KeyAgreement.getInstance("DH");
		KeyPair aPair = keyGen.generateKeyPair();

		aKeyAgree.init(aPair.getPrivate());

		// Signature signature = Signature.getInstance("SHA256withDSA");
		// signature.initSign(aPair.getPrivate());

		return aPair;

	}

	public byte[] finish(KeyPair bPair) throws Exception {
		// Then A generates
		aKeyAgree.doPhase(bPair.getPublic(), true);

		// generate the key bytes
		MessageDigest hash = MessageDigest.getInstance("SHA1");

		// Then A generates the final agreement key
		byte[] aShared = hash.digest(aKeyAgree.generateSecret());

		return aShared;
	}
}
