package com.itravel.api.shop.service;

import com.itravel.api.shop.exception.S3UploadErrorException;
import com.itravel.api.shop.model.dto.ArchiveDTO;

public interface S3Service {

  String uploadImage(ArchiveDTO archiveDTO) throws S3UploadErrorException;

}
