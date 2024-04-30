import java.security.Signature;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.KeyPairGenerator;

public class Sign {
	public static byte[] privateKeySign(byte[] toSign) {
		byte[] digitalSignature=null;
		try {
			Signature signature = Signature.getInstance("SHA256WithDSA");
			SecureRandom secureRandom = new SecureRandom();
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			signature.initSign(keyPair.getPrivate(), secureRandom);
			signature.update(toSign);
			digitalSignature = signature.sign();
		} catch(Exception e) {
		}
		return digitalSignature;
	}
}