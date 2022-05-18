package com.itravel.api.account.service.impl;

import com.itravel.api.account.constraint.S3Folder;
import com.itravel.api.account.exception.S3UploadErrorException;
import com.itravel.api.account.exception.SellerInfoExistedException;
import com.itravel.api.account.exception.SellerInfoNotFoundException;
import com.itravel.api.account.model.dto.ArchiveDTO;
import com.itravel.api.account.model.dto.SellerInfoResponse;
import com.itravel.api.account.model.entity.SellerInfoEntity;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.model.jpa.SellerInfoRepository;
import com.itravel.api.account.payload.SellerInfoRequest;
import com.itravel.api.account.service.S3Service;
import com.itravel.api.account.service.SellerInfoService;
import com.itravel.api.account.util.ModelMapperUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Log4j2
@RequiredArgsConstructor
public class SellerInfoServiceImpl implements SellerInfoService {

  private final SellerInfoRepository sellerInfoRepository;
  private final ModelMapperUtils modelMapperUtils;
  private final S3Service s3Service;

  @Override
  public SellerInfoResponse getSellerInfoByAccountId(Long accountIdVerified)
      throws SellerInfoNotFoundException {
    Optional<SellerInfoEntity> optionalSellerInfoEntity = sellerInfoRepository.findByAccountId(
        accountIdVerified);
    if (optionalSellerInfoEntity.isEmpty()) {
      throw new SellerInfoNotFoundException();
    }
    return modelMapperUtils.map(optionalSellerInfoEntity.get(), SellerInfoResponse.class);
  }

  @Override
  @Transactional
  public SellerInfoResponse create(AccountEntity accountEntityVerified,
      SellerInfoRequest sellerInfoRequest) throws SellerInfoExistedException, S3UploadErrorException {

    Optional<SellerInfoEntity> optionalSellerInfoEntity = sellerInfoRepository.findByAccountId(
        accountEntityVerified.getId());
    if (optionalSellerInfoEntity.isPresent()) {
      throw new SellerInfoExistedException();
    }

    SellerInfoEntity sellerInfoEntity = modelMapperUtils.map(sellerInfoRequest,
        SellerInfoEntity.class);
    sellerInfoEntity.setAccountId(accountEntityVerified.getId());
    sellerInfoEntity.setNationalIdCardImageFront(uploadFile(sellerInfoRequest.getNationalIdCardImageFrontBase64()));
    sellerInfoEntity.setNationalIdCardImageBack(uploadFile(sellerInfoRequest.getNationalIdCardImageBackBase64()));
    sellerInfoEntity.setIsVerified(false);
    return modelMapperUtils.map(sellerInfoRepository.save(sellerInfoEntity),
        SellerInfoResponse.class);
  }

  @Override
  @Transactional
  public void update(AccountEntity accountEntityVerified, SellerInfoRequest sellerInfoRequest)
      throws SellerInfoNotFoundException, S3UploadErrorException {
    Optional<SellerInfoEntity> optionalSellerInfoEntity = sellerInfoRepository.findByAccountId(
        accountEntityVerified.getId());
    if (optionalSellerInfoEntity.isEmpty()) {
      throw new SellerInfoNotFoundException();
    }
    SellerInfoEntity sellerInfoEntity = optionalSellerInfoEntity.get();

    if (StringUtils.hasText(sellerInfoRequest.getNationality())) {
      sellerInfoEntity.setNationality(sellerInfoRequest.getNationality());
    }
    if (StringUtils.hasText(sellerInfoRequest.getNationalIdNumber())) {
      sellerInfoEntity.setNationalIdNumber(sellerInfoRequest.getNationalIdNumber());
    }
    if (StringUtils.hasText(sellerInfoRequest.getPlaceOfOrigin())) {
      sellerInfoEntity.setPlaceOfOrigin(sellerInfoRequest.getPlaceOfOrigin());
    }
    if (StringUtils.hasText(sellerInfoRequest.getPlaceOfResidence())) {
      sellerInfoEntity.setPlaceOfResidence(sellerInfoRequest.getPlaceOfResidence());
    }
    if (sellerInfoRequest.getNationalIdCardImageBackBase64() != null) {
      String url = uploadFile(sellerInfoRequest.getNationalIdCardImageBackBase64());
      sellerInfoEntity.setNationalIdCardImageBack(url);
    }
    if (sellerInfoRequest.getNationalIdCardImageFrontBase64() != null) {
      String url = uploadFile(sellerInfoRequest.getNationalIdCardImageFrontBase64());
      sellerInfoEntity.setNationalIdCardImageFront(url);
    }
    sellerInfoEntity.setIsVerified(false);

    sellerInfoRepository.save(sellerInfoEntity);
  }

  private String uploadFile(String fileContent) throws S3UploadErrorException {

    if (StringUtils.hasText(fileContent)) {
      ArchiveDTO archiveDTO = ArchiveDTO.builder()
          .fileContent(fileContent)
          .folderName(S3Folder.SELLER_INFO)
          .build();
      return s3Service.uploadImage(archiveDTO);
    }

    return null;
  }

}
