import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DigestUtilsTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //生成1KiB伪随机二进制数据
        Random random = new Random(System.currentTimeMillis());
        byte[] dataBuffer = new byte[(1<<20)];
        random.nextBytes(dataBuffer);

        //转换 1KiB 二进制数据为 2KiB
        String str = Hex.encodeHexString(dataBuffer);

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] strInBytes = str.getBytes();
        byte[] hash = messageDigest.digest(strInBytes);

        System.out.println(Hex.encodeHexString(hash, false));

        messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[4];
        for (int i = 0; i < strInBytes.length; i++) {
            for (int j = 0; j < buffer.length; j++, i++) {
                if( i >=  strInBytes.length){
                    break;
                }
                buffer[j] = strInBytes[i];
            }
            DigestUtils.updateDigest(messageDigest, buffer);
        }
        hash = messageDigest.digest();
        System.out.println(Hex.encodeHexString(hash, false));

    }
}
