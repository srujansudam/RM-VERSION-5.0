package com.cg.ibs.rm.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.cg.ibs.rm.bean.Beneficiary;
import com.cg.ibs.rm.bean.FinalCustomer;
import com.cg.ibs.rm.bean.TemporaryCustomer;
import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.RmExceptions;

public class BeneficiaryDAOImpl implements BeneficiaryDAO {
	Map<String, TemporaryCustomer> tempMap = DataStoreImpl.getTempMap();
	Map<String, FinalCustomer> finalMap = DataStoreImpl.getFinalMap();
	Iterator<Beneficiary> it;

	public List<Beneficiary> getDetails(String uci) {
		return finalMap.get(uci).getSavedBeneficiaries();

	}

	@Override
	public void copyDetails(String uci, Beneficiary beneficiary) throws RmExceptions {
		if (finalMap.get(uci).getSavedBeneficiaries().contains(beneficiary)) {
			throw new RmExceptions(ExceptionMessages.ERROR3);
		} else {
			tempMap.get(uci).getUnapprovedBeneficiaries().add(beneficiary);
		}

	}

	public Beneficiary getBeneficiary(String uci, BigInteger accountNumber) {
		Beneficiary beneficiary1 = null;
		it = finalMap.get(uci).getSavedBeneficiaries().iterator();
		while (it.hasNext()) {
			Beneficiary beneficiary = it.next();
			if (beneficiary.getAccountNumber().equals(accountNumber)) {
				beneficiary1 = beneficiary;
			}
		}
		return beneficiary1;
	}

	@Override
	public boolean updateDetails(String uci, Beneficiary beneficiary1) throws RmExceptions {

		boolean result = false;
		if (!(finalMap.get(uci).getSavedBeneficiaries().contains(beneficiary1))) {
			throw new RmExceptions(ExceptionMessages.ERROR4);

		}
		it = finalMap.get(uci).getSavedBeneficiaries().iterator();
		while (it.hasNext()) {
			Beneficiary beneficiary = it.next();
			if (beneficiary.getAccountNumber().equals(beneficiary1.getAccountNumber())) {
				it.remove();
				result = true;
			}
		}
		System.out.println(beneficiary1);
		if(tempMap.containsKey(uci))
		{
		tempMap.get(uci).getUnapprovedBeneficiaries().add(beneficiary1);
		}else {
			TemporaryCustomer customer = new TemporaryCustomer();
			ArrayList<Beneficiary> unapprovedBeneficiaries = new ArrayList<>();
			customer.setUnapprovedBeneficiaries(unapprovedBeneficiaries);
			tempMap.put(uci, customer);
		}
		return result;

	}

	@Override
	public boolean deleteDetails(String uci, BigInteger accountNumber) throws RmExceptions {
		boolean result = false;
		int count = 0;
		for (Beneficiary beneficiary : finalMap.get(uci).getSavedBeneficiaries()) {
			if (beneficiary.getAccountNumber().equals(accountNumber)) {
				count++;
			}
		}
		if (0 == count) {
			throw new RmExceptions(ExceptionMessages.ERROR4);
		} else {
			it = finalMap.get(uci).getSavedBeneficiaries().iterator();
			while (it.hasNext()) {
				Beneficiary beneficiary = it.next();
				if (beneficiary.getAccountNumber().equals(accountNumber)) {
					it.remove();
					result = true;
				}
			}
		}
		return result;
	}

}
