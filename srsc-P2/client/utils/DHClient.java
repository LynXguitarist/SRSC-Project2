package utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Signature;

import javax.crypto.KeyAgreement;

public class DHClient {

	private KeyAgreement bKeyAgree;

	public DHClient() {
		// nothing here
	}

	public KeyPair init() throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
		// KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
		keyGen.initialize(512);

		// Simulation in B side
		bKeyAgree = KeyAgreement.getInstance("DH");
		KeyPair bPair = keyGen.generateKeyPair();

		bKeyAgree.init(bPair.getPrivate());

		//Signature signature = Signature.getInstance("SHA256withDSA");
		//signature.initSign(bPair.getPrivate());
		
		return bPair;
	}

	public byte[] finish(KeyPair aPair) throws Exception {
		// B generates
		bKeyAgree.doPhase(aPair.getPublic(), true);

		// generate the key bytes
		MessageDigest hash = MessageDigest.getInstance("SHA1");

		// Then B generates the final agreement key
		//byte[] bShared = hash.digest(bKeyAgree.generateSecret());

		
		return bKeyAgree.generateSecret();
	}
}
