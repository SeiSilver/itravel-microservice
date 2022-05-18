package com.itravel.api.payment.service;

import com.itravel.api.payment.exception.AccountNotFoundException;
import com.itravel.api.payment.model.dto.AccountBasicInfo;
import com.itravel.api.payment.payload.GetAccountsByIdsPayload;
import java.util.List;

public interface AccountService {

  List<AccountBasicInfo> getAccountByAccountIds(GetAccountsByIdsPayload payload) throws AccountNotFoundException;
}
