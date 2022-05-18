package com.itravel.api.account.service;


import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.model.dto.AccountBasicInfo;
import com.itravel.api.account.payload.GetAccountsByIdsPayload;
import java.util.List;

public interface AccountService {

  List<AccountBasicInfo> getAccountByAccountIds(GetAccountsByIdsPayload payload) throws AccountNotFoundException;
}
