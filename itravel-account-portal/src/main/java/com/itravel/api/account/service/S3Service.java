package com.itravel.api.account.service;

import com.itravel.api.account.model.dto.ArchiveDTO;
import com.itravel.api.account.exception.S3UploadErrorException;

public interface S3Service {

  String uploadImage(ArchiveDTO archiveDTO) throws S3UploadErrorException;

}
