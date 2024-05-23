package ninetyNinePercentChain.Block.Hashing;
import java.io.File;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;
import ninetyNinePercentChain.Utils.BlockFile;
import ninetyNinePercentChain.Utils.ByteArray;

public class CheckValidity {
	public static boolean checkBlock(Block toCheck) {
		try {
			if(!Block.checkHashZeros(toCheck.hash(), 16)) {
				return false;
			} else if(!(new File("./blockchain/"+toCheck.getIndex()+".ser").exists()&&BlockFile.readBlock(toCheck.getIndex()).equals(toCheck))) {
				return false;
			} else if(!Arrays.equals(BlockFile.readBlock(toCheck.getIndex()-1).hash(), toCheck.getPreviousHash())) {
				return false;
			} else if(!Arrays.equals(toCheck.getMerkleRoot(), new MerkleTree<Transaction>(toCheck.getAllTransactions()).genTree())) {
				return false;
			} if(toCheck.getIndex()<=0) {
				return false;
			} if(toCheck.getAllTransactions().size()==0) {
				return false;
			}
			for(int i=0; i<toCheck.getNumTransactions(); i++) {
				if(!checkTransaction(toCheck.getTransaction(i))) {
					return false;
				}
			}
			return true;
		} catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	public static boolean checkTransaction(Transaction toCheck) {
		try {
			for(int i=0; i<toCheck.getTINLength(); i++) {
				byte[] encryptedHash=toCheck.getTransactionIn(i).getPrivateKeySignature();
				Block block=BlockFile.readBlock(toCheck.getTransactionIn(i).getPreviousOutBlock());
				Transaction transaction=block.getTransaction(toCheck.getTransactionIn(i).getPreviousOutTransaction());
				TransactionOut out=transaction.getTOUT(toCheck.getTransactionIn(i).getPreviousOutOutputNumber());
				byte[] publicKeyBytes=out.getNextTransactionPublicKey();
				PublicKey publicKey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
				byte[] decryptedHash=Sign.publicKeySign(encryptedHash, publicKey);
				if(!Arrays.equals(decryptedHash, toCheck.getTransactionIn(i).hash())) {
					return false;
				}
			}
			ArrayList<TransactionIn> TINArrayList=new ArrayList<TransactionIn>(Arrays.asList(toCheck.getAllTIN()));
			ArrayList<TransactionOut> TOUTArrayList=new ArrayList<TransactionOut>(Arrays.asList(toCheck.getAllTOUT()));
			byte[] TINMerkleRoot=new MerkleTree<TransactionIn>(TINArrayList).genTree();
			byte[] TOUTMerkleRoot=new MerkleTree<TransactionOut>(TOUTArrayList).genTree();
			byte[] calculatedMerkleRoot=ByteArray.merge(TINMerkleRoot, TOUTMerkleRoot);
			if(!Arrays.equals(toCheck.getMerkleRoot(), calculatedMerkleRoot)) {
				return false;
			}
			for(int i=0; i<toCheck.getTINLength(); i++) {
				TransactionIn currentTransactionIn=toCheck.getTransactionIn(i);
				int previousOutBlock=currentTransactionIn.getPreviousOutBlock();
				Block block=BlockFile.readBlock(previousOutBlock);
				int previousOutTransaction=currentTransactionIn.getPreviousOutTransaction();
				Transaction transaction=block.getTransaction(previousOutTransaction);
				int previousOutOutputNumber=toCheck.getTransactionIn(i).getPreviousOutOutputNumber();
				TransactionOut out=transaction.getTOUT(previousOutOutputNumber);
				byte[] publicKeyBytes=out.getNextTransactionPublicKey();
				PublicKey publicKey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
				byte[] signature=Sign.publicKeySign(toCheck.getSignature(i), publicKey);
				if(!Arrays.equals(toCheck.getMerkleRoot(), signature)) {
					return false;
				}
			}
			int totalValueInput=0;
			int totalValueOutput=0;
			for(int i=0; i<toCheck.getTINLength(); i++) {
				totalValueInput+=toCheck.getTIN(i).getValue();
			}
			for(int i=0; i<toCheck.getTOUTLength(); i++) {
				totalValueOutput+=toCheck.getTransactionOut(i).getValue();
			}
			if(totalValueInput!=totalValueOutput) {
				return false;
			}
			//CHECK FOR OUTPUT TRANSACTION ALREADY BEING SPENT
			return true;
		} catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
}