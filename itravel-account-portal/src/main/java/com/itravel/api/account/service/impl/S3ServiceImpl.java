package com.itravel.api.account.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.itravel.api.account.constraint.ErrorCode;
import com.itravel.api.account.constraint.ErrorMessage;
import com.itravel.api.account.model.dto.ArchiveDTO;
import com.itravel.api.account.exception.S3UploadErrorException;
import com.itravel.api.account.service.S3Service;
import com.itravel.api.account.util.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3ServiceImpl implements S3Service {

  private static final String ENDPOINT_URL = "https://itravel-bucket.s3.ap-southeast-1.amazonaws.com";

  @Value("${archive.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  @Override
  public String uploadImage(ArchiveDTO archiveDTO) throws S3UploadErrorException {
    String fileUrl;
    String fileName = getDate() + "-" + UUID.randomUUID();
    File file = FileUtils.getImageFromBase64(archiveDTO.getFileContent(), fileName);

    try (InputStream fileData = new FileInputStream(file)) {
      if (!StringUtils.hasText(archiveDTO.getFolderName())) {
        archiveDTO.setFolderName("unknown");
      }
      String s3filePath = String.format("%s/%s", archiveDTO.getFolderName(), file.getName());
      fileUrl = ENDPOINT_URL + "/" + s3filePath;
      amazonS3.putObject(
          new PutObjectRequest(bucket, s3filePath,
              fileData,
              getObjectMetadata(file))
              .withCannedAcl(CannedAccessControlList.PublicRead));
      log.info("upload file to s3 success");
      Files.delete(Path.of(file.getPath()));
      log.info("Delete temp image file success");
    } catch (AmazonServiceException e) {
      log.error("Error uploading file to S3", e);
      throw new S3UploadErrorException(e.getErrorCode() + " " + e.getErrorMessage(), e);
    } catch (IOException e) {
      log.error("UNKNOWN_ERROR uploading file to S3", e);
      throw new S3UploadErrorException(ErrorCode.UNKNOWN_ERROR, ErrorMessage.FILE_UPLOAD_FAIL);
    }

    return fileUrl;
  }

  private ObjectMetadata getObjectMetadata(File file) {
    FileNameMap fileNameMap = URLConnection.getFileNameMap();
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(fileNameMap.getContentTypeFor(file.getName()));
    return objectMetadata;
  }

  private String getDate() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return simpleDateFormat.format(new Date());
  }
}
