import java.util.ArrayList;

class MerkleTree<T> {
	ArrayList<byte[]> hash;
	public byte[] genTree() {
		while(hash.size()>1) {
			balanceTree();
			for(int i=0; i<hash.size()/2; i++) {
				hash.set(i, SHA256Hash.hash(ByteArray.merge(hash.get(i*2), hash.get((i*2)+1))));
			}
			trimList()
		}
		return hash.get(0);
	}
	private void balanceTree() { //Makes number of inputs even
		if(hash.size()%2==1) {
			hash.add(hash.get(hash.size()-1));
		}
	}
	private void trimList() { //Chops off the second half of the list
		for(int i=hash.size()-1; i>hash.size()/2; i--) {
			hash.remove(i);
		}
	}
	public MerkleTree(ArrayList<T> toHash) {
		hash=new ArrayList<byte[]>();
		for(int i=0; i<toHash.size(); i++) {
			hash.set(toHash.hash());
		}
	}
}
