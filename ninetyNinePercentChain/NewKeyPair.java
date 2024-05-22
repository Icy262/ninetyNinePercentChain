package ninetyNinePercentChain;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

class NewKeyPair {
	public static KeyPair newKeyPair() {
		try {
			return KeyPairGenerator.getInstance("DSA").generateKeyPair();
		} catch(Exception e) {
			return null;
		}
	}
}