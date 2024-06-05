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
	/*
	Name: checkBlock
	Description: Checks the validity of a given block.
	Precondition: None
	Postcondition: Returns true if the block is valid; otherwise, false.
	*/
	public static boolean checkBlock(Block toCheck) {
		try {
			if(!(new File("./blockchain/"+toCheck.getIndex()+".ser").exists()&&BlockFile.readBlock(toCheck.getIndex()).equals(toCheck))) {
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
	/*
	Name: checkTransaction
	Description: Checks the validity of a given transaction.
	Precondition: None
	Postcondition: Returns true if the transaction is valid; otherwise, false.
	*/
	public static boolean checkTransaction(Transaction toCheck) {
		try {
			//The hash of the TIN | signed by the private key that matches the public key in the TOUT | is correct
			for(int i=0; i<toCheck.getTINLength(); i++) { //For each TIN,
				TransactionIn currentTIN=toCheck.getTIN(i);
				byte[] encryptedHash=currentTIN.getPrivateKeySignature();
				TransactionOut TOUT=BlockFile.getTOUT(currentTIN.getPreviousOutBlock(), currentTIN.getPreviousOutTransaction(), currentTIN.getPreviousOutOutputNumber());
				byte[] publicKeyBytes=TOUT.getNextTransactionPublicKey();
				PublicKey publicKey=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
				byte[] decryptedHash=Sign.publicKeySign(encryptedHash, publicKey);
				if(!Arrays.equals(decryptedHash, toCheck.getTIN(i).hash())) { //Compare the hash of the TIN to the decrypted copy of the encrypted hash. The decrypted hash is made by decrypting the encrypted hash with the public key. If it doesn't match the spender was not authorized or the TIN was changed.
					return false;
				}
			}
			//Checks the merkle root is correct
			ArrayList<TransactionIn> TINArrayList=new ArrayList<TransactionIn>(Arrays.asList(toCheck.getAllTIN()));
			ArrayList<TransactionOut> TOUTArrayList=new ArrayList<TransactionOut>(Arrays.asList(toCheck.getAllTOUT()));
			byte[] TINMerkleRoot=new MerkleTree<TransactionIn>(TINArrayList).genTree();
			byte[] TOUTMerkleRoot=new MerkleTree<TransactionOut>(TOUTArrayList).genTree();
			byte[] calculatedMerkleRoot=ByteArray.merge(TINMerkleRoot, TOUTMerkleRoot);
			if(!Arrays.equals(toCheck.getMerkleRoot(), calculatedMerkleRoot)) {
				return false;
			}
			for(int i=0; i<toCheck.getTINLength(); i++) { //Checks that each of the signatures matches the private keys used in the TIN and the merkle root of the transaction. Verifies the private keys are valid and the merkle root has not changed. If either changed, check would fail. Means the spender was authorized to spend this money and the transaction has not been changed.
				TransactionIn currentTIN=toCheck.getTIN(i);
				TransactionOut TOUT=BlockFile.getTOUT(currentTIN.getPreviousOutBlock(), currentTIN.getPreviousOutTransaction(), currentTIN.getPreviousOutOutputNumber());
				byte[] publicKeyBytes=TOUT.getNextTransactionPublicKey();
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
				totalValueOutput+=toCheck.getTOUT(i).getValue();
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
	/*
	Name: isTOUTSpent
	Description: Checks if the TOUT has already been spent by a different transaction
	Precondition: None
	Postcondition: true returned if there is a TIN the references the TOUT, false if not
	*/
	public static boolean isTOUTSpent(int blockIndex, int transactionIndex, int TOUTIndex) {
		for(int i=1; i<BlockFile.getHighestIndex()+1; i++) { //For each block, (not including block 0)
			Block block=BlockFile.readBlock(i); //Buffer for block. So we don't need to read it multiple times
			for(int ii=0; ii<block.getNumTransactions(); ii++) { //For each Transaction,
				Transaction transaction=block.getTransaction(ii); //Buffer for transaction. So we don't need to get it multiple times
				for(int iii=0; iii<transaction.getTINLength(); iii++) { //For each TIN,
					TransactionIn TIN=transaction.getTIN(iii); //Buffeer for TIN, so we don't need to get it multiple times
					if(TIN.getPreviousOutBlock()==blockIndex&&TIN.getPreviousOutTransaction()==transactionIndex&&TIN.getPreviousOutOutputNumber()==TOUTIndex) { //If there is a TIN pointing to this TOUT,
						return true; //TOUT has already been spent
					}
				}
			}
		}
		return false; //We have checked everything and it is not there, therefore it has not been spent
	}
}