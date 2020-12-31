package utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.KeyAgreement;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DHServer {

	private KeyAgreement aKeyAgree;

	public DHServer() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
	}

	public KeyPair init() throws Exception {
	
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH","BC");
		keyGen.initialize(512);
		// set up
		// Simulation in A side
		aKeyAgree = KeyAgreement.getInstance("DH","BC");
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
		MessageDigest hash = MessageDigest.getInstance("SHA1","BC");

		// Then A generates the final agreement key
		byte[] aShared = hash.digest(aKeyAgree.generateSecret());

		return aShared;
	}
}
