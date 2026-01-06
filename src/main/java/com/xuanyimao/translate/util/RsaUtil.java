package com.xuanyimao.translate.util;

import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RSA工具类
 */
public class RsaUtil {

    /** 公钥 */
    private final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsBOO/QfeYo3/3KOn763ZGJjgOT0db5fOp9s1owROlTMMWUCO+NK3jYc9YksxTZV72zKhpJF5kbganQgwWMIRvjhaqCZ6D5L2e3HH+bi9YubTagm+PkSYHws248VgQ5OMdGuhuWeTVZiitXERKEXS7bN1ObV94/b31cHWxEh7mIcgXKUz4ZrhZ2YNJ/CYzwVvAnGrEWEBEj13caO5HJvPs6Hxj+tPrpIsEKy7pLJCQDsujbSRbmpgXbN2YYZ3kgR4bBFbYxTcfvqSdy64Wmx0Rl45rQvC6/g0mnA4AqUmYqIWrPHaizO4ELTu0eZrjaw/RBYmaWu6uq923IaMewXVfwIDAQAB";
    /** 私钥 */
    private final static String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwE479B95ijf/co6fvrdkYmOA5PR1vl86n2zWjBE6VMwxZQI740reNhz1iSzFNlXvbMqGkkXmRuBqdCDBYwhG+OFqoJnoPkvZ7ccf5uL1i5tNqCb4+RJgfCzbjxWBDk4x0a6G5Z5NVmKK1cREoRdLts3U5tX3j9vfVwdbESHuYhyBcpTPhmuFnZg0n8JjPBW8CcasRYQESPXdxo7kcm8+zofGP60+ukiwQrLukskJAOy6NtJFuamBds3ZhhneSBHhsEVtjFNx++pJ3LrhabHRGXjmtC8Lr+DSacDgCpSZiohas8dqLM7gQtO7R5muNrD9EFiZpa7q6r3bchox7BdV/AgMBAAECggEAVYnK50XRVZOify/KP/f3BaoPE/+Ks1HOhWFLF1zaSEU6dImWa28C2PCgc4YP957tQrnAKVC6/H2yACOgSR5OvFhoQlQe7D37HFFUUw7agpeckWEWWO298SbjjZevgmTF0TEQqh+VRfoW4YqCSUxSgkOyaUywUfUCxYebrFOCJ8B8kgKCtf4tkGzJEfQAuwbnPfxS/zUAIoudGtjzzoICxbOsFcIlnb+oGVjIz9RIEjJ6gQA6/Qp2Zu0KQT6P69yPZJ8JEWO4TNfLhzG/y7EGsmJvvTetF0akeM6+BwHEtMqWUpvxu8yhqzrO4BqFf7kK4eY0YJIbhxf9WCWPC6wX8QKBgQD5cUmuqYfrhAu3t/wUtwH+y9JvZid66m/ZS1p9+8fSZbMP1mBNkEDHwT7LxgBxIPXEZ+s09XeEz/7LB9Ps9UkMYluYC1+xGz+9FZZxXDaDGcajeAXSMB9WOMb2VRlA6q+UnXns7lf/9CjnQhnobQFeG0lnInaJeltysJOmkDkB0wKBgQC0tIb7jt/XXFi6yuMLy+tMR8StxS4cjXCS0xSTPh0iLnEw7ijWFp7r6rA5Kf68Lih6ga+ZUHXH6C8rBLd4FUIoeG1VunD0YvklYNhF/AA+PbWwrg5+QjWg16QF/FjXFla8ClYbMIPObwI2A3ox1YfhqpmngKuJ6h6fWzBRzCfmJQKBgQDmN76va1w86+P8rdwEKlu3E9qmYfvyH8kXHcrpF6X281HuPgcnzwla02nnnFjwEGGD4f1dJWgntegM7q6TtlazDVxH2cd5qBKNqwzslxTgrrt8R9vnf+6MH17SHvxtw6xFV4oeOGQToP7XYVSUBXcLqdd3ToFkpP8dvQao5dcXbQKBgD+ORvNvGtdaCXMZLKXXgNiBXXFoqoEsVe2uA8yNyh1djD4TZZV2u0pqbrSMSqXQ7je4KRSZMfOve/d7XzQC9hPZ10qRmmy56rr0xtxGd6dseNy9Oye5DbeWYIAicvSjTLoyQHqro5AVhweMTYv9eB8sq4I4RRzqlP6jSelbDqVpAoGBAMDdeRTJ/yBJS3HxlwsqF8O02F1tqH0uEwgurkvG3BVMzPK0HHBM1wgg8C4T59P2bFxxmf58a9dr/2wsxB2JEalccrk4lKzJj6fOV0OFGzom78vvqCtfQb6miguXmLNJ5QANzGDP1mHKEKKIctCIHuw3zRxdGs/+HqsgtZszDKAd";

    /**
     * 加密
     * @param text
     * @return
     * @throws Exception
     */
    public static String encrypt(String text) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(PUBLIC_KEY);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密
     * @param cipherText
     * @return
     * @throws Exception
     */
    public static String decrypt(String cipherText) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.getDecoder().decode(cipherText.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.getDecoder().decode(PRIVATE_KEY);
        //RSA解密
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 生成rsa密钥。执行后将重写此java文件的公钥和私钥
     */
    public static void generateRsaKey() {
        try {
            // 初始化KeyPairGenerator对象，指定算法为RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，设置密钥长度
            keyGen.initialize(2048);
            // 生成密钥对
            KeyPair keyPair = keyGen.generateKeyPair();

            // 获取公钥和私钥
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 打印或使用公钥和私钥
//            System.out.println("Public Key: " + publicKey.toString());
//            System.out.println("Private Key: " + privateKey.toString());

            String path=System.getProperty("user.dir")+ File.separator+"src/main/java/com/xuanyimao/translate/util/RsaUtil.java".replace("/",File.separator);
            FileInputStream fis=new FileInputStream(path);
            String data=IOUtils.toString(fis,"utf-8");
            IOUtils.closeQuietly(fis);

            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("Public Key: " + publicKeyStr);
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            System.out.println("Private Key: " + privateKeyStr);

            data=data.replaceAll("PUBLIC_KEY = \\\".*?\\\"", "PUBLIC_KEY = \""+publicKeyStr+"\"");
            data=data.replaceAll("PRIVATE_KEY = \\\".*?\\\"", "PRIVATE_KEY = \""+privateKeyStr+"\"");

            FileOutputStream fos=new FileOutputStream(path);
            IOUtils.write(data, fos, "utf-8");
            IOUtils.closeQuietly(fos);

        } catch (Exception e) {
            LogUtil.getLogger().error("生成rsa密钥失败",e);
        }
    }

    public static void main(String[] args) throws Exception {
//        generateRsaKey();

//        String data="11221321312";
//        data=encrypt(data);
//        System.out.println(data);
//        data=decrypt(data);
//        System.out.println(data);

        System.out.println(decrypt("NpPt1XjUpHYKkaLK0vAXjzab+6MK8yJJ+/9PrZvgbtfocTyUootSgFMiNlk0VSTzlgM9sU6qwMslAoeuc0xcHJ2TOdD2bHzuR3NVjqhbTnG1flB70jag3nxwkyrRzL6e7xFc+T0SiT9lxemHhAxMONDZFjVxt+aoan3vGdrz+8fUFa5TypKZJduzWN18RQY2GMbXgNWeOCol4VYxf2fDufus5M9aAYstHb+shk9mXf0Y2JeS/+M4CIEYtOmcznSTKYRiJdbGfg6NE7sebje7VJVwSZ8mXsM87TCeUeu+JP0V7MSXOcDshzWcMOQ4wvfDHG4ixOdjnNtPyn8cgM6MeA"));
    }
}
