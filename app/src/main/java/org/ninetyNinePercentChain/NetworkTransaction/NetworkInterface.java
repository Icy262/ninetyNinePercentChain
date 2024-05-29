package org.ninetyNinePercentChain.NetworkTransaction;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import org.ninetyNinePercentChain.Block.Block;
import org.ninetyNinePercentChain.Block.Transaction;
import org.ninetyNinePercentChain.Block.TransactionIn;
import org.ninetyNinePercentChain.Block.TransactionOut;
import org.ninetyNinePercentChain.Keys.KeyPairManager;
import org.ninetyNinePercentChain.Network.DNS.QueryDNS;
import org.ninetyNinePercentChain.Network.DNS.RegisterDNS;
import org.ninetyNinePercentChain.Network.InterNode.Read.NetworkReceiveHandler;
import org.ninetyNinePercentChain.Network.InterNode.Sync.SyncChainResponseManager;
import org.ninetyNinePercentChain.Network.InterNode.Write.NetworkSendManager;
import org.ninetyNinePercentChain.Network.KeepAlive.KeepAliveManager;
import org.ninetyNinePercentChain.Network.KeepAlive.KeepAliveResponse;
import org.ninetyNinePercentChain.Utils.BlockFile;

public class NetworkInterface {
	public static void setup() {
		new QueryDNS().start();
		new RegisterDNS().start();
		new KeepAliveManager().start();
		new KeepAliveResponse().start();
		new NetworkReceiveHandler(false).start();
		new SyncChainResponseManager().start();
	}
	public static void setupAsNode() {
	}
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
		NetworkSendManager.addToQueue((Object) new Transaction(transactionIns, transactionOuts, System.currentTimeMillis()));
	}
	public static void createTransaction(int value, String keyName, byte[] outKey) throws InsuffientFundsException {
		try {
			PublicKey outKeyAsObject=KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(outKey));
			createTransaction(value, keyName, outKeyAsObject);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
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
	private static int findTotalValue(TransactionIn[] transactionIns) {
		int totalValue=0;
		for(int i=0; i<transactionIns.length; i++) {
			totalValue+=transactionIns[i].getValue();
		}
		return totalValue;
	}
}