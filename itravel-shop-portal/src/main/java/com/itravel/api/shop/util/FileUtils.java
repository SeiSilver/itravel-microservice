package com.itravel.api.shop.util;

import com.itravel.api.shop.error.ErrorCode;
import com.itravel.api.shop.error.ErrorMessage;
import com.itravel.api.shop.exception.S3UploadErrorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

  public static File getImageFromBase64(String base64String, String fileName) throws S3UploadErrorException {
    String[] strings = base64String.split(",");
    String extension;
    switch (strings[0]) { // check image's extension
      case "data:image/png;base64":
        extension = "png";
        break;
      case "data:image/jpg;base64":
        extension = "jpg";
        break;
      case "data:image/svg;base64":
        extension = "svg";
        break;
      case "data:image/jpeg;base64":
        extension = "jpeg";
        break;
      default:
        throw new S3UploadErrorException(ErrorCode.FILE_INVALID, ErrorMessage.FILE_TYPE_INVALID);
    }
    // convert base64 string to binary data
    byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
    File file = new File(fileName + "." + extension);
    try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
      outputStream.write(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }


}
