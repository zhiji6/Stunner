package com.hal.stun.message;

import com.hal.stun.message.attribute.StunAttribute;
import com.hal.stun.message.attribute.AttributeType;

import java.net.InetSocketAddress;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

public class StunMessageRegressionTest {
  @Test
  public void testStunMessage() throws Exception {
    InetSocketAddress address = new InetSocketAddress(StunMessageTestData.REAL_STUN_ADDRESS, 2000);
    byte[] messageBytes = StunMessageTestHelper.convertArray(StunMessageTestData.REAL_STUN_MESSAGE);
    StunMessage message = new StunMessage(messageBytes, address);
    StunHeader header = message.getHeader();
    List<StunAttribute> attributes = message.getAttributes();
    StunAttribute attribute = attributes.get(0);

    Assert.assertEquals("message type is binding", (short) 1, header.getMessageMethod());
    Assert.assertEquals("message length is eight bytes", (short) 8, header.getMessageLength());
    Assert.assertEquals("has one attribute", 1, attributes.size());
    Assert.assertEquals("is a fingerprint attribute", AttributeType.FINGERPRINT, attribute.getAttributeType());
  }
}