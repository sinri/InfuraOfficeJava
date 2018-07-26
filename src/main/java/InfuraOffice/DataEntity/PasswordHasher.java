package InfuraOffice.DataEntity;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class PasswordHasher {
    // The higher the number of iterations the more
    // expensive computing the hash is for us and
    // also for an attacker.
    private static final int iterations = 20 * 1000;
    private static final int saltLen = 32;
    private static final int desiredKeyLen = 256;

    /**
     * Computes a salted PBKDF2 hash of given plaintext password
     * suitable for storing in a database.
     * Empty passwords are not supported.
     */
    public static String getSaltedHash(String password) throws Exception {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen);
        // store the salt with the password
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    /**
     * Checks whether given plaintext password corresponds
     * to a stored salted hash of the password.
     */
    public static boolean check(String password, String stored) throws Exception {
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalStateException(
                    "The stored password have the form 'salt$hash'");
        }
        String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
        return hashOfInput.equals(saltAndPass[1]);
    }

    /**
     * using PBKDF2 from Sun, an alternative is https://github.com/wg/scryptcf.
     * http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
     */
    private static String hash(String password, byte[] salt) throws Exception {
        if (password == null || password.length() == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, desiredKeyLen)
        );
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 对字符串md5加密
     *
     * @param str String
     * @return String
     */
    public static String md5(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误，" + e.toString());
        }
    }
}
