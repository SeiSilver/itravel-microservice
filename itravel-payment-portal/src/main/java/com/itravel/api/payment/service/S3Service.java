package com.itravel.api.payment.service;

import com.itravel.api.payment.model.dto.ArchiveDTO;
import com.itravel.api.payment.exception.S3UploadErrorException;

public interface S3Service {

  String uploadImage(ArchiveDTO archiveDTO) throws S3UploadErrorException;

}
