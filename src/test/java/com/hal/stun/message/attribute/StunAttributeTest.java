package com.hal.stun.message.attribute;

import com.hal.stun.message.StunParseException;
import com.hal.stun.message.StunMessageUtils;

import org.junit.Test;
import org.junit.Assert;

public class StunAttributeTest {

  private static final String ATTRIBUTE_VALUE_HEX = "000107d07f000001";
  
  @Test
  public void testInitializeStunAttribute() throws Exception {
    new StunAttribute(AttributeType.MAPPED_ADDRESS, 8, ATTRIBUTE_VALUE_HEX);
  }

  @Test(expected = StunParseException.class)
  public void testInitializeStunAttributeSizeMismatch() throws StunParseException, Exception {
    new StunAttribute(AttributeType.MAPPED_ADDRESS, 7, ATTRIBUTE_VALUE_HEX);
  }

  @Test
  public void testToByteArray() throws Exception {
    int expectedLength = 8 + 4;
    StunAttribute attribute = new StunAttribute(AttributeType.MAPPED_ADDRESS, expectedLength - 4, ATTRIBUTE_VALUE_HEX);
    byte[] attributeBytes = attribute.toByteArray();
    Assert.assertEquals(expectedLength, attributeBytes.length);
    String expectedAttributeHex = "00010008" + ATTRIBUTE_VALUE_HEX;
    byte[] expectedAttributeBytes = StunMessageUtils.convertHexToByteArray(expectedAttributeHex);
    Assert.assertArrayEquals(expectedAttributeBytes, attributeBytes);
  }
  
}