package com.itravel.api.account.service;

import com.itravel.api.account.payload.SellerInfoRequest;
import com.itravel.api.account.model.dto.SellerInfoResponse;
import com.itravel.api.account.exception.S3UploadErrorException;
import com.itravel.api.account.exception.SellerInfoExistedException;
import com.itravel.api.account.exception.SellerInfoNotFoundException;
import com.itravel.api.account.model.entitydto.AccountEntity;

public interface SellerInfoService {

  SellerInfoResponse getSellerInfoByAccountId(Long accountIdVerified) throws SellerInfoNotFoundException;

  SellerInfoResponse create(AccountEntity accountEntityVerified, SellerInfoRequest sellerInfoRequest)
      throws SellerInfoExistedException, S3UploadErrorException;

  void update(AccountEntity accountEntityVerified, SellerInfoRequest sellerInfoRequest) throws SellerInfoNotFoundException, S3UploadErrorException;



}
