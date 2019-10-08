package com.cg.ibs.rm.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.bean.CreditCard;
import com.cg.ibs.rm.bean.FinalCustomer;
import com.cg.ibs.rm.bean.TemporaryCustomer;
import com.cg.ibs.rm.exception.RmExceptions;

public class BankRepresentativeDAOImpl implements BankRepresentativeDAO {

	Map<String, TemporaryCustomer> tempMap = DataStoreImpl.getTempMap();
	Map<String, FinalCustomer> finalMap = DataStoreImpl.getFinalMap();

	public Set<String> getRequests() {
		return tempMap.keySet();
		
	}

	public List<Beneficiary> getBeneficiaryDetails(String uci) {
		return tempMap.get(uci).getUnapprovedBeneficiaries();
		 
	}

	public Set<CreditCard> getCreditCardDetails(String uci) throws RmExceptions {
		return tempMap.get(uci).getUnapprovedCreditCards();
		 
	}

	@Override
	public void copyCreditCardDetails(String uci, CreditCard card) {
		finalMap.get(uci).getSavedCreditCards().add(card);

	}

	public void deleteTempCreditCardDetails(String uci, CreditCard card) {

		if (tempMap.get(uci).getUnapprovedCreditCards().contains(card)) {
			tempMap.get(uci).getUnapprovedCreditCards().remove(card);
		}
	}

	public void deleteTempBeneficiaryDetails(String uci, Beneficiary beneficiary) {
		if (tempMap.get(uci).getUnapprovedBeneficiaries().contains(beneficiary)) {
			tempMap.get(uci).getUnapprovedBeneficiaries().remove(beneficiary);
		}

	}

	@Override
	public void copyBeneficiaryDetails(String uci, Beneficiary beneficiary) {
		finalMap.get(uci).getSavedBeneficiaries().add(beneficiary);
	}

}
