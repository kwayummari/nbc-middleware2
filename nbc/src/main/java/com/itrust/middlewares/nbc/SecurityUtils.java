package com.itrust.middlewares.nbc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.apache.commons.codec.binary.Base64.isBase64;

@Component
public class SecurityUtils {

    static Logger logger;

    public SecurityUtils() {
        logger = LoggerFactory.getLogger(SecurityUtils.class);
    }

    public String encrypt(String strToEncrypt) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            String privateKey = "Itrust@2024";
            SecretKeySpec secretKeySpec = generateKey(privateKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
           logger.info("Error while encrypting {}", e.getMessage());
            return strToEncrypt;
        }
    }

    public String decrypt(String strToDecrypt) {
        if (!isBase64(strToDecrypt.getBytes())) {
            return strToDecrypt;
        }
        try {
            Security.addProvider(new BouncyCastleProvider());
            String publicKey = "Itrust@2024";
            SecretKeySpec secretKeySpec = generateKey(publicKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7PADDING", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new
                    String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            logger.error("Error while decrypting {}", e.getMessage());
            return strToDecrypt;
        }
    }

    private static SecretKeySpec generateKey(String myKey) {
        try {
            byte[] keyBytes = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);
            keyBytes = Arrays.copyOf(keyBytes, 16); // For 128-bit key
            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.getStackTrace();
        }
        return null;
    }

}
