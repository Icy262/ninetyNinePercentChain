package ninetyNinePercentChain.NetworkTransaction;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;
import ninetyNinePercentChain.Keys.KeyPairManager;
import ninetyNinePercentChain.Network.DNS.QueryDNS;
import ninetyNinePercentChain.Network.DNS.RegisterDNS;
import ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;
import ninetyNinePercentChain.Utils.BlockFile;

public class NetworkInterface {
	/*
	Name: setup
	Description: When we are running a non hashing node, that will be used to create and recieve transactions only, as part of a larger project, this will be used instead of main
	Precondition: None
	Postcondition: Program is ready to create and process transactions
	*/
	public static void setup() {
		new QueryDNS().start();
		new RegisterDNS().start();
		new KeepAliveManager().start();
		new KeepAliveResponse().start();
		new NetworkReceiveHandler(false).start();
		new SyncChainResponseManager().start();
	}
	/*
	Name: createTransaction
	Description: Creates a transaction of a certain value, spending tokens from a certain address, to a certain address
	Precondition: None
	Postcondition: Transaction created and sent, or insufficient funds exception thrown
	*/
	public static void createTransaction(int value, String keyName, PublicKey outKey) throws InsuffientFundsException {
		KeyPair keyPair=KeyPairManager.readKey(keyName);
		byte[] keyAsByteArray=keyPair.getPublic().getEncoded();
		TransactionIn[] transactionIns=findInputs(keyAsByteArray, value, keyPair.getPrivate());
		int totalValue=findTotalValue(transactionIns);
		TransactionOut[] transactionOuts;
		if(totalValue==value) {
			transactionOuts=new TransactionOut[] {new TransactionOut(outKey.getEncoded(), value)};
		} else {
			transactionOuts=new TransactionOut[] {new TransactionOut(outKey.getEncoded(), value), new TransactionOut(keyPair.getPublic().getEncoded(), totalValue-value)};
		}
		Transaction transaction=new Transaction(transactionIns, transactionOuts, System.currentTimeMillis());
		String[] keys=new String[transactionIns.length];
		for(int i=0; i<keys.length; i++) {
			keys[i]=keyName;
		}
		transaction.signTransaction(keys);
		NetworkSendManager.addToQueue((Object) transaction);
	}
	/*
	Name: createTransaction
	Description: Same as other createTransaction, but it allows for a byte[] to be passed as output address
	Precondition: None
	Postcondition: Transaction created and sent, or insufficient funds exception thrown
	*/
	public static void createTransaction(int value, String keyName, byte[] outKey) throws InsuffientFundsException {
		try {
			PublicKey outKeyAsObject=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(outKey));
			createTransaction(value, keyName, outKeyAsObject);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	/*
	Name: findInputs
	Description: Scans the blockchain for all TOUTs owned by an address until we find enough that we meet the value required. It compiles these into an array of TINs and returns it
	Precondition: keyAsByteArray is the address we are searching for. Value is the total token value. privateKey is the private key coresponding to the address we are searcching for
	Postcondition: TransactionIn[] generated and sent or insufficient funds exception thrown
	*/
	private static TransactionIn[] findInputs(byte[] keyAsByteArray, int value, PrivateKey privateKey) throws InsuffientFundsException {
		ArrayList<TransactionIn> transactionList=new ArrayList<TransactionIn>();
		int addressBalance=0;
		for(int i=BlockFile.getHighestIndex(); i>=0; i--) {
			Block block=BlockFile.readBlock(i);
			for(int ii=0; ii<block.getNumTransactions(); ii++) {
				Transaction transaction=block.getTransaction(ii);
				for(int iii=0; i<transaction.getTOUTLength(); iii++) {
					if(Arrays.equals(transaction.getTOUT(iii).getNextTransactionPublicKey(), keyAsByteArray)) {
						transactionList.add(new TransactionIn(i, ii, iii));
						transactionList.get(transactionList.size()-1).sign(privateKey);
						addressBalance+=transaction.getTOUT(iii).getValue();
						if(addressBalance>=value) {
							return (TransactionIn[]) transactionList.toArray();
						}
					}
				}
			}
		}
		throw new InsuffientFundsException();
	}
	/*
	Name: findTotalValue
	Description: Finds the sum of the values of an array of TINs
	Precondition: All TINs are valid and point to blocks that we have in our blockchain directory
	Postcondition: Total value of TINs returned
	*/
	private static int findTotalValue(TransactionIn[] transactionIns) {
		int totalValue=0;
		for(int i=0; i<transactionIns.length; i++) {
			totalValue+=transactionIns[i].getValue();
		}
		return totalValue;
	}
}