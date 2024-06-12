package ninetyNinePercentChain.Keys;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/*
 * Creates a new KeyPair. This class is not used often. It is only used when making a new "address". It just generates a random KeyPair and returns it.
 */

public class NewKeyPair {
	/*
	Name: newKeyPair
	Description: Creates a new KeyPair
	Precondition: None
	Postcondition: Random valid DSA KeyPair returned
	*/
	public static KeyPair newKeyPair() {
		try {
			return KeyPairGenerator.getInstance("RSA").generateKeyPair(); //Generates a new GSA keypair
		} catch(Exception e) {
			return null;
		}
	}
}