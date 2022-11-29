package com.touchblankspot.common.data.attribute.encryptor;

import jakarta.persistence.AttributeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AttributeEncryptor implements AttributeConverter<String, String> {

  private final Key key;
  private final Cipher cipher;

  public AttributeEncryptor(
      @Value("${encryption.attribute.key:KdqZ01i0U7WF2J4OQqVjVCUIOCVrPoXh}") String secretKey,
      @Value("${encryption.attribute.algorithm:AES}") String algorithm)
      throws Exception {
    key = new SecretKeySpec(secretKey.getBytes(), algorithm);
    cipher = Cipher.getInstance(algorithm);
  }

  @Override
  public String convertToDatabaseColumn(String attribute) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, key);
      return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
      log.error("Unable to encrypt user data ", e);
      throw new IllegalStateException(e);
    }
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    try {
      cipher.init(Cipher.DECRYPT_MODE, key);
      return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
    } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
      log.error("Unable to decrypt db column data ", e);
      throw new IllegalStateException(e);
    }
  }
}
