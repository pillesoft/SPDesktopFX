/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.bl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

public class Crypt {

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
    }
    else if (tempbyte.length > maxlength) {
      pwdbyte =  Arrays.copyOf(tempbyte, maxlength);
    }
    else {
      pwdbyte = tempbyte;
    }
    keyByte = pwdbyte;
  }
  
//  private static class TestCrypt {
//
//    String freeword;
//    String cypherword;
//
//    public TestCrypt(String freeword, String cypherword) {
//      this.freeword = freeword;
//      this.cypherword = cypherword;
//    }
//
//    public String getFreeword() {
//      return freeword;
//    }
//
//    public String getCypherword() {
//      return cypherword;
//    }
//
//  }

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
  
//    public static byte[] encrypt(final byte[] keyBytes, final byte[] ivBytes, final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException
//    {       
//        return transform(Cipher.ENCRYPT_MODE, keyBytes, ivBytes, messageBytes);
//    }
//  public static byte[] decrypt(final byte[] keyBytes, final byte[] ivBytes, final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException {
//    return transform(Cipher.DECRYPT_MODE, keyBytes, ivBytes, messageBytes);
//  }

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

  private static byte[] transform(final int mode, final byte[] keyBytes, final byte[] ivBytes, final byte[] messageBytes) throws InvalidKeyException, InvalidAlgorithmParameterException {
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

//  public static void main(final String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException, IOException, NoSuchAlgorithmException {
//
////      http://stackoverflow.com/questions/24487006/java-xor-byte-array-not-returning-expected-result
////      public static byte[] hexStringToByteArray(String s) {
////    HexBinaryAdapter adapter = new HexBinaryAdapter();
////    byte[] bytes = adapter.unmarshal(s);
////    return bytes;
////}
////public static byte[] xor(byte[] a, byte[] b) {
////    byte[] result = null;
////    if (a.length > b.length) {
////        for (int i = 0; i < b.length; i++) {
////            result = new byte[b.length];
////            result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
////        }
////    } else {
////        for (int i = 0; i < a.length; i++) {
////            result = new byte[a.length];
////            result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
////        }
////    }
////    return result;
////}
////public static void main(String[] args){
////    byte[] bytes1 = hexStringToByteArray(ciphertext1);
////    byte[] bytes2 = hexStringToByteArray(ciphertext2);
////    byte[] cipher1 = xor(bytes1, bytes2);
////    System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(bytes1));
////    System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(bytes2));
////    System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(cipher1));
////}
//    String pwd = "jelszó";
//    setKeyByte(pwd);
////    byte[] tempbyte = pwd.getBytes(StandardCharsets.UTF_8);
////    byte[] pwdbyte = new byte[16];
////    if (tempbyte.length < pwdbyte.length) {
////      int pwdidx = 0;
////      int tempidx = 0;
////      while (pwdidx < 16) {
////        if (tempidx < tempbyte.length) {
////          pwdbyte[pwdidx++] = tempbyte[tempidx++];
////        } else {
////          tempidx = 0;
////        }
////      }
////    }
////    printByteArray("pwd", pwdbyte);
//
////    //String newpwd = new String(pwdbyte);
////    KeyGenerator gen = KeyGenerator.getInstance("AES");
////    gen.init(128);
////    /* 128-bit AES */
////    SecretKey secret = gen.generateKey();
////    byte[] key = secret.getEncoded();
////    printByteArray("key", key);
////
////    byte[] realkey = xorByteArray(pwdbyte, key);
////    printByteArray("xorkey", realkey);
////
////    byte[] pwdtest = xorByteArray(realkey, pwdbyte);
////    printByteArray("result", pwdtest);
//
////      System.out.println(new String(Base64.getDecoder().decode("ABEiM0RVZneImaq7zN3u/w==")));
////
////      System.out.println(Cipher.getMaxAllowedKeyLength("AES"));      
////      
////      for (int i = 0; i < 1000; i++) {
////        
////      
////      KeyGenerator gen = KeyGenerator.getInstance("AES");
////      gen.init(128); /* 128-bit AES */
////      SecretKey secret = gen.generateKey();
////      byte[] binary = secret.getEncoded();
////      // 16 byte[] -> 32 in hex
////      String text1 = String.format("%032X", new BigInteger(+1, binary));
////        System.out.println(new BigInteger(+1, binary));
////      System.out.println(text1);
////
////      for(byte b : binary) {
////        System.out.print(b + " ");
////      }
////      System.out.println();
////      
////      System.out.println(new BigInteger(text1, 16));
////      byte[] binarytemp = new BigInteger(text1, 16).toByteArray();
////      byte[] binary2 = new byte[16];
////      if (binarytemp.length != 16) {
////        System.arraycopy(binarytemp, 1, binary2, 0, binarytemp.length-1);
////        System.out.println("namost " + binarytemp.length);
////        for(byte b : binarytemp) {
////          System.out.print(b + " ");
////        }
////        System.out.println();
////      }
////      else {
////        binary2 = binarytemp;
////      }
////      
////      for(byte b : binary2) {
////        System.out.print(b + " ");
////      }
////      System.out.println();
////      }
//    String text = new String(Files.readAllBytes(Paths.get("c:\\Users\\ihorvath\\Documents\\NetBeansProjects\\SF_Desktop\\sample.txt")));
//
////    SecureRandom secRnd = new SecureRandom();
////
////    byte[] ivBytes = new byte[16];
////    //Generate a new IV.
////    secRnd.nextBytes(ivBytes);
//
////      String key = "jelszó";
////      byte[] keybyte = key.getBytes(StandardCharsets.UTF_8);
////      byte[] realkey = new byte[16];
////      int i = 0;
////      for (byte b : binary) {
////        realkey[i] = (byte)(b ^ keybyte[i++]);
////      }
////      String base64key = hash(key);
////      System.out.println(base64key);
////      byte[] keyBytes = Base64.getDecoder().decode(text1);
//    Pattern regex = Pattern.compile("(\\w+)");
//    Matcher matcher = regex.matcher(text);
//
//    ArrayList<TestCrypt> words = new ArrayList<>();
////    if (matcher.find()) {
//    while (matcher.find()) {
//      String w = matcher.group(1);
////        System.out.println(w);
////        byte[] bytetoencr = Base64.getDecoder().decode(w);
//      byte[] bytetoencr = w.getBytes(StandardCharsets.UTF_8);
//      String c = encrypt(bytetoencr);
//      TestCrypt tc = new TestCrypt(w, c);
//      words.add(tc);
//      //System.out.println(matcher.group(1));
//    }
//
////    for (TestCrypt tc : words) {
////      System.out.println(String.format("%s - %s", tc.getFreeword(), tc.getCypherword()));
////    }
//
//    System.out.println("DECRYPT");
//    for (TestCrypt tc : words) {
//      String decrypted = new String(decrypt(tc.getCypherword()));
//      if (!tc.getFreeword().equals(decrypted)) {
//        System.out.println(String.format("%s - %s - %s", tc.getFreeword(), tc.getCypherword(), decrypted));
//      }
//    }
//
//    /*
//      
//      
//        //Retrieved from a protected local file.
//        //Do not hard-code and do not version control.
//        final String base64Key = "ABEiM0RVZneImaq7zN3u/w==";
//
//        //Retrieved from a protected database.
//        //Do not hard-code and do not version control.
////        final String shadowEntry = "AAECAwQFBgcICQoLDA0ODw==:ZtrkahwcMzTu7e/WuJ3AZmF09DE=";
//        final String shadowEntry = "ZifeuxNmpyR7PHjknmgRHQ==:LEBM2pwkD94WdH4iNGtKswjqWOs=";
//
//        //Extract the iv and the ciphertext from the shadow entry.
//        final String[] shadowData = shadowEntry.split(":");        
//        final String base64Iv = shadowData[0];
//        final String base64Ciphertext = shadowData[1];
//
//        //Convert to raw bytes.
//        final byte[] keyBytes = Base64.getDecoder().decode(base64Key);
//        final byte[] ivBytes = Base64.getDecoder().decode(base64Iv);
//        final byte[] encryptedBytes = Base64.getDecoder().decode(base64Ciphertext);
//
//        //Decrypt data and do something with it.
//        final byte[] decryptedBytes = decrypt(keyBytes, ivBytes, encryptedBytes);
//
//        //Use non-blocking SecureRandom implementation for the new IV.
//        final SecureRandom secureRandom = new SecureRandom();
//
//        //Generate a new IV.
//        secureRandom.nextBytes(ivBytes);
//
//        //At this point instead of printing to the screen, 
//        //one should replace the old shadow entry with the new one.
//        System.out.println("Old Shadow Entry      = " + shadowEntry);
//        System.out.println("Decrytped Shadow Data = " + new String(decryptedBytes, StandardCharsets.UTF_8));
//        System.out.println("New Shadow Entry      = " + Base64.getEncoder().encodeToString(ivBytes) + ":" + Base64.getEncoder().encodeToString(encrypt(keyBytes, ivBytes, decryptedBytes)));
//        
//        String texttoencr = "7481";
//        byte[] bytetoencr = Base64.getDecoder().decode(texttoencr);
//        System.out.println(Base64.getEncoder().encodeToString(encrypt(keyBytes, ivBytes, bytetoencr)));
//        secureRandom.nextBytes(ivBytes);
//        System.out.println(Base64.getEncoder().encodeToString(encrypt(keyBytes, ivBytes, bytetoencr)));
//        
//        
//    try {
//      System.out.println(hash("jelszó"));
//      System.out.println(hash("jelszó"));
//    } catch (NoSuchAlgorithmException ex) {
//      Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
//    }
//     */
//  }

}
