package ninetyNinePercentChain.Keys;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class NewKeyPair {
	/*
	Name: newKeyPair
	Description: Creates a new KeyPair
	Precondition: None
	Postcondition: Random valid DSA KeyPair returned
	*/
	public static KeyPair newKeyPair() {
		try {
			return KeyPairGenerator.getInstance("DSA").generateKeyPair();
		} catch(Exception e) {
			return null;
		}
	}
}