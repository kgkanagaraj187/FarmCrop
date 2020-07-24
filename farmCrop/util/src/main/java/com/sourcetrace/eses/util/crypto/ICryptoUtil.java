package com.sourcetrace.eses.util.crypto;

public interface ICryptoUtil {

    /**
     * Encrypt.
     * @param clearText the clear text
     * @return the string
     */
    public String encrypt(String clearText);

    /**
     * Decrypt.
     * @param cipherText the cipher text
     * @return the string
     */
    public String decrypt(String cipherText);
}
