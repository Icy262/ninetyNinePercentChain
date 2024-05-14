public class NetworkRead {
	public static void add(Object toAdd) {
		if(toAdd.getClass()==Transaction.class) {
			//CHECK VALIDITY
			FindBlockHashManager.addTransaction((Transaction) toAdd);
		} else if(toAdd.getClass()==Block.class) {
			//CHECK VALIDITY
			BlockFile.writeBlock((Block) toAdd);
		}
	}
}