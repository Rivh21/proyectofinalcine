/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author yancy
 */
public class Encriptar {
  public static String MD2 = "MD2";
    public static String MD5 = "MD5";
    public static String SHA1 = "SHA-1";
    public static String SHA256 = "SHA-256";
    public static String SHA384 = "SHA-384";
    public static String SHA512 = "SHA-512";
    //obtiene un arreglo de tipo byte y retorna un hexadecimal.
    private static String toHexadecimal(byte[] digest){
        String hash="";
        for (byte aux: digest) {
            int b=aux & 0xff;
            if(Integer.toHexString(b).length()==1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }  
    //utilice el metodo especificado y lo aplica a la cadena
    public static String getStringMessageDigest(String cadena, String algoritm){
        byte[] digest=null;
        byte[] buffer=cadena.getBytes();
        try {
            MessageDigest messageDigest=MessageDigest.getInstance(algoritm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest=messageDigest.digest();            
        } catch (NoSuchAlgorithmException e) {
             System.out.println("Error creando digest");
        }  
        return toHexadecimal(digest);
    }
   
}
