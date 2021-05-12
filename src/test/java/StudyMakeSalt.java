import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class StudyMakeSalt {
    public static void main(String[] args) {
        byte[] big_int_byte = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(big_int_byte);
        String randomBigIntegerStr = Hex.encodeHexString(big_int_byte);

        System.out.println(new BigInteger(big_int_byte));
        System.out.println(randomBigIntegerStr);
        System.out.println(randomBigIntegerStr.length());

        String sha512_hash = new String(Hex.encodeHexString(DigestUtils.sha512("github@xfl12345")).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        System.out.println(sha512_hash);

        String md5_hash = DigestUtils.md5Hex("github@xfl12345");

        System.out.println(md5_hash);

        System.out.println(DigestUtils.md5Hex(Hex.encodeHexString(DigestUtils.sha512("github@xfl12345")) + Hex.encodeHexString(DigestUtils.sha512("github@xfl12345"))) );

    }
}
