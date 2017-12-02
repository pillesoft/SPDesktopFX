package com.ibh.spdesktop.bl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public final class Crypt {

  private Crypt() { }

  
  private static byte[] keyByte;
  public static void setKeyByte(String key) {
    final int maxlength = 16;
    byte[] tempbyte = key.getBytes(StandardCharsets.UTF_8);
    byte[] pwdbyte = new byte[maxlength];
    if (tempbyte.length < maxlength) {
      int pwdidx = 0;
      int tempidx = 0;
      while (pwdidx < maxlength) {
        if (tempidx < tempbyte.length) {
          pwdbyte[pwdidx++] = tempbyte[tempidx++];
        } else {
          tempidx = 0;
        }
      }
    } else if (tempbyte.length > maxlength) {
      pwdbyte =  Arrays.copyOf(tempbyte, maxlength);
    } else {
      pwdbyte = tempbyte;
    }
    keyByte = pwdbyte;
  }
  
  public static String hash(String stringToHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {

    MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
    messageDigest.update(stringToHash.getBytes(StandardCharsets.UTF_8));
    String hashedString = Base64.getEncoder().encodeToString(messageDigest.digest());
    return hashedString;
  }

  public static String encrypt(final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
    KeyGenerator gen = KeyGenerator.getInstance("AES");
    gen.init(128);
    /* 128-bit AES */
    SecretKey secret = gen.generateKey();
    byte[] key = secret.getEncoded();
//    printByteArray("key", key);

    SecureRandom secRnd = new SecureRandom();
    byte[] ivBytes = new byte[16];
    //Generate a new IV.
    secRnd.nextBytes(ivBytes);
//    printByteArray("iv", ivBytes);
    
    byte[] cypher = transform(Cipher.ENCRYPT_MODE, key, ivBytes, messageBytes);
//    printByteArray("cypher", cypher);
    
    byte[] xorkey = xorByteArray(keyByte, key);
//    printByteArray("xor", cypher);
    
    String xorhex = toHexString(xorkey);
//    System.out.println("xorhex: " + xorhex);
    String ivhex = toHexString(ivBytes);
//    System.out.println("ivhex: " + ivhex);
    String cyphex = toHexString(cypher);
//    System.out.println("cyphex: " + cyphex);
    
    return xorhex+ivhex+cyphex;
    
  }

  public static byte[] decrypt(final String cypher) throws InvalidKeyException, InvalidAlgorithmParameterException, NullPointerException {

    if (cypher == null) {
      throw new NullPointerException("cypher cannot be null");
    }
    
    String xorhex = cypher.substring(0, 32);
//    System.out.println("xorhex: " + xorhex);
    String ivhex = cypher.substring(32, 64);
//    System.out.println("ivhex: " + ivhex);
    String cyphex = cypher.substring(64);
//    System.out.println("cyphex: " + cyphex);
    
    byte[] xorbyte = toByteArray(xorhex);
    byte[] key = xorByteArray(xorbyte, keyByte);
//    printByteArray("xor", xorbyte);
//    printByteArray("key", key);
    
    byte[] ivBytes = toByteArray(ivhex);
//    printByteArray("iv", ivBytes);
    byte[] cyphbyte = toByteArray(cyphex);
//    printByteArray("cypher", cyphbyte);
    
    return transform(Cipher.DECRYPT_MODE, key, ivBytes, cyphbyte);
  }

  private static String toHexString(byte[] array) {
    return DatatypeConverter.printHexBinary(array);
  }

  private static byte[] toByteArray(String hexstring) {
    return DatatypeConverter.parseHexBinary(hexstring);
  }

  private static byte[] xorByteArray(byte[] arr1, byte[] arr2) {
    byte[] xorbyte = new byte[16];
    int i = 0;
    for (byte b : arr1) {
      xorbyte[i] = (byte) (b ^ arr2[i++]);
    }
    return xorbyte;
  }

  private static void printByteArray(String prefix, byte[] arr) {
    System.out.print(prefix + ":");
    for (byte b : arr) {
      System.out.print(b + " ");
    }
    System.out.println();
  }

  private static byte[] transform(final int mode, final byte[] keyBytes, final byte[] ivBytes
          , final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException {
    final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    byte[] transformedBytes = null;

    try {
      //final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
      final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

      cipher.init(mode, keySpec, ivSpec);

      transformedBytes = cipher.doFinal(messageBytes);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
    }
    return transformedBytes;
  }

}
