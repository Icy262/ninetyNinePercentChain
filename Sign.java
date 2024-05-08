import java.security.Signature;

public class Sign {
	public static byte[] privateKeySign(byte[] toSign, String keyName) {
		try {
			Signature signature = Signature.getInstance("SHA256WithDSA");
			signature.initSign(KeyPairManager.readKey(keyName).getPrivate());
			signature.update(toSign);
			return signature.sign();
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
}