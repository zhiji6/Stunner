package com.hal.stun.message.attribute;

import com.hal.stun.message.StunMessageUtils;
import com.hal.stun.message.attribute.value.StunAttributeValue;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class StunAttribute {

    private static final int ATTRIBUTE_HEADER_SIZE_BYTES = 4;

    private AttributeType attributeType; // the attribute type to be set by the implementing subclass
    private int length; // the size of this attribute's value in bytes
    private StunAttributeValue attributeValue;

    public StunAttribute(AttributeType attributeType, int length, byte[] value) throws StunAttributeParseException {
        this.attributeType = attributeType;
        this.length = length;
        verifyValueLength(value);
        byte[] unpaddedValue = valueFromLength(value, length);
        this.attributeValue = attributeType.buildAttributeValue(unpaddedValue);
    }

    public StunAttribute(AttributeType attributeType, StunAttributeValue attributeValue) {
        this.attributeType = attributeType;
        this.length = attributeValue.getBytes().length;
        this.attributeValue = attributeValue;
    }

    private void verifyValueLength(byte[] value) throws StunAttributeParseException {
        if (!lengthIsValid(value)) {
            String valueHex = StunMessageUtils.convertByteArrayToHex(value);
            throw new StunAttributeParseException("attribute valueHex " + valueHex + " is " + value.length
                    + " bytes, but the attribute length specified is " + length);
        }
    }

    public byte[] getBytes() {
        byte[] typeBytes = StunMessageUtils.toBytes(attributeType.getTypeBytes());
        byte[] lengthBytes = StunMessageUtils.toBytes((short) length);

        List<byte[]> unjoinedAttributeBytes = new ArrayList<>();
        unjoinedAttributeBytes.add(typeBytes);
        unjoinedAttributeBytes.add(lengthBytes);
        unjoinedAttributeBytes.add(attributeValue.getPaddedBytes());

        return StunMessageUtils.joinByteArrays(unjoinedAttributeBytes);
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public int getLength() {
        return length;
    }

    public int getWholeLength() {
        return StunMessageUtils.nextMultipleOfFour(length) + ATTRIBUTE_HEADER_SIZE_BYTES;
    }

    public StunAttributeValue getValue() {
        return attributeValue;
    }

    public static List<StunAttribute> parseAttributes(byte[] attributesBytes) throws StunAttributeParseException {
        validateAttributesBytes(attributesBytes);
        List<StunAttribute> attributes = new ArrayList<>();

        int offset = 0;
        int paddedLength = 0;
        while (offset + paddedLength < attributesBytes.length) {
            int attributeType = StunMessageUtils.extractByteSequence(attributesBytes, offset, 2);
            int length = StunMessageUtils.extractByteSequence(attributesBytes, offset + 2, 2);
            // the length is in the attribute header.
            // Anything extra is discarded

            int arrayStart = offset + ATTRIBUTE_HEADER_SIZE_BYTES;
            paddedLength = StunMessageUtils.nextMultipleOfFour(length);
            int arrayEnd = arrayStart + paddedLength;
            byte[] value = Arrays.copyOfRange(attributesBytes, arrayStart, arrayEnd);

            AttributeType type = AttributeType.fromBytes((short) attributeType);
            attributes.add(new StunAttribute(type, length, value));

            offset += paddedLength + ATTRIBUTE_HEADER_SIZE_BYTES;
        }

        return attributes;
    }

    private static void validateAttributesBytes(byte[] attributesBytes) throws StunAttributeParseException {
        if (attributesBytes.length % 4 != 0) {
            throw new StunAttributeParseException("attributes must have bit count that is multiple of 32");
        }
        if (attributesBytes.length < 8) {
            throw new StunAttributeParseException("there must be at least one attribute of 16 bytes or more.");
        }
    }

    private static byte[] valueFromLength(byte[] rawValue, int length) {
        return Arrays.copyOfRange(rawValue, 0, length);
    }

    private boolean lengthIsValid(byte[] value) {
        return (length <= value.length) && (length > value.length - 4);
    }
}
