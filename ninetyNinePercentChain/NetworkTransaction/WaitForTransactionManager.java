package ninetyNinePercentChain.NetworkTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Transaction;

public class WaitForTransactionManager {
	private static ArrayList<WaitForTransaction> waiting=new ArrayList<WaitForTransaction>();
	public static void addWait(WaitForTransaction waiter) {
		waiting.add(waiter);
	}
	public static void update(Transaction transaction) {
		if(waiting.size()==0) {
			return;
		}
		for(int i=0; i<waiting.size(); i++) {
			for(int ii=0; ii<transaction.getTOUTLength(); ii++) {
				if(Arrays.equals(waiting.get(i).getAddress(), transaction.getTOUT(ii).getNextTransactionPublicKey())&&waiting.get(i).getValue()==transaction.getTOUT(ii).getValue()) {
					waiting.get(i).notify();
				}
			}
		}
	}
}