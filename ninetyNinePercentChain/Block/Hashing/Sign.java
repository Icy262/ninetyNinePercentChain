package ninetyNinePercentChain.Block.Hashing;
import java.security.PublicKey;
import java.security.Signature;

import ninetyNinePercentChain.Keys.KeyPairManager;

import java.security.PrivateKey;

public class Sign {
	/*
	Name: privateKeySign
	Description: Signs the provided data using the given private key.
	Precondition: None
	Postcondition: The signature of the data returned as a byte array
	*/
	public static byte[] privateKeySign(byte[] toSign, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance("SHA256WithDSA"); //Gets the algorithm
			signature.initSign(privateKey); //Prepares the algorithm to sign with the private key
			signature.update(toSign); //Gives the algorithm the data to sign
			return signature.sign(); //Signs the data and returns the output
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	/*
	Name: privateKeySign
	Description: Loads the key associated with keyName from the keystore and passes it to the other privateKeySign method
	Precondition: keyName is the name of a valid key in the keystore
	Postcondition: toSign is signed with the key and returned.
	*/
	public static byte[] privateKeySign(byte[] toSign, String keyName) {
		return privateKeySign(toSign, KeyPairManager.readKey(keyName).getPrivate()); //Reads the key and passes through to the other method
	}
	public static byte[] publicKeySign(byte[] toSign, PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance("SHA256WithDSA"); //Gets the algorithm
			signature.initVerify(publicKey); //Prepares the algorithm to sign the signature with the public key
			signature.update(toSign); //Passes the signature
			return signature.sign(); //Signs the signature with the public key. This should reverse the private key and return the original data.
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
}