package com.cg.ibs.rm.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.bean.FinalCustomer;
import com.cg.ibs.rm.bean.TemporaryCustomer;
import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.RmExceptions;

public class CreditCardDAOImpl implements CreditCardDAO {

	Map<String, TemporaryCustomer> tempMap = DataStoreImpl.getTempMap();
	Map<String, FinalCustomer> finalMap = DataStoreImpl.getFinalMap();
	Iterator<CreditCard> it;
	Set<CreditCard> creditCardList;

	@Override
	public Set<CreditCard> getDetails(String uci) {
		return finalMap.get(uci).getSavedCreditCards();
	}

	@Override
	public void copyDetails(String uci, CreditCard card) throws RmExceptions {
		if (finalMap.get(uci).getSavedCreditCards().contains(card)) {
			throw new RmExceptions(ExceptionMessages.ERROR1);
		} else {
			tempMap.get(uci).getUnapprovedCreditCards().add(card);
		}
	}

	@Override
	public boolean deleteDetails(String uci, BigInteger cardNumber) throws RmExceptions {
		boolean result = false;

		int count = 0;
		for (CreditCard card : finalMap.get(uci).getSavedCreditCards()) {
			if (card.getcreditCardNumber().equals(cardNumber)) {
				count++;
			}
		}
		if (0 == count) {
			throw new RmExceptions(ExceptionMessages.ERROR2);
		} else {
			it = finalMap.get(uci).getSavedCreditCards().iterator();
			while (it.hasNext()) {
				CreditCard card = it.next();
				if (card.getcreditCardNumber().equals(cardNumber)) {
					it.remove();
					result = true;
				}
			}
			return result;
		}
	}

}
