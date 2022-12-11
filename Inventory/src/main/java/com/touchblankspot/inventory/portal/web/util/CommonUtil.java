package com.touchblankspot.inventory.portal.web.util;

import java.nio.ByteBuffer;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtil {

  public static UUID convertBytesToUUID(byte[] bytes) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    long high = byteBuffer.getLong();
    long low = byteBuffer.getLong();
    return new UUID(high, low);
  }
}
