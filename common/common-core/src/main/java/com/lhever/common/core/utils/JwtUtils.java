package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import io.jsonwebtoken.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/2 16:56
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/2 16:56
 * @modify by reason:{方法名}:{原因}
 */
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final Map<String, Object> DEFAULT_HEADER = new HashMap() {{
        put("type", "JWT");
    }};

    public static Jws<Claims> parseJWT(String jsonWebToken, SecretKey key) {
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jsonWebToken);
        } catch (ExpiredJwtException e) {
            logger.error("jwt token expire", e);
        } catch (UnsupportedJwtException e) {
            logger.error("jwt token not recognized", e);
        } catch (MalformedJwtException e) {
            logger.error("jwt token malformed", e);
        } catch (SignatureException e) {
            logger.error("jwt token signature error", e);
        } catch (IllegalArgumentException e) {
            logger.error("jwt token argument error", e);
        } catch (Exception e) {
            logger.error("jwt token parse error", e);
        }
        return claimsJws;

    }

    public static Jws<Claims> parseJWT(String jsonWebToken, SecretKey key, String subject, Object... keyvalues) {
        Jws<Claims> claimsJws = null;
        try {
            checkClaims(keyvalues);

            JwtParser parser = Jwts.parser();
            parser.requireSubject(subject);

            int total = keyvalues.length / 2;
            for (int i = 0; i < total; i++) {
                parser.require(keyvalues[2 * i].toString(), keyvalues[2 * i + 1]);
            }

            return parser.
                    setSigningKey(key).
                    parseClaimsJws(jsonWebToken);

        } catch (ExpiredJwtException e) {
            logger.error("jwt token expire", e);
        } catch (UnsupportedJwtException e) {
            logger.error("jwt token not recognized", e);
        } catch (MalformedJwtException e) {
            logger.error("jwt token malformed", e);
        } catch (SignatureException e) {
            logger.error("jwt token signature error", e);
        } catch (IllegalArgumentException e) {
            logger.error("jwt token argument error", e);
        } catch (Exception e) {
            logger.error("jwt token parse error", e);
        }
        return claimsJws;

    }

    private static Map<String, Object> buildMap(Object... keyvalues) {
        checkClaims(keyvalues);
        Map<String, Object> map = new HashMap<>();
        int total = keyvalues.length / 2;
        for (int i = 0; i < total; i++) {
            map.put(keyvalues[2 * i].toString(), keyvalues[2 * i + 1]);
        }

        return map;
    }

    public static void checkClaims(Object... keyvalues) {

        if (ObjectUtils.isAnyNull(keyvalues)) {
            throw new IllegalArgumentException("null param not permited");
        }

        if ((keyvalues.length % 2) != 0) {
            throw new IllegalArgumentException("param num > 0 and even required");
        }

    }


    public static Map<String, Object> buildClaims(Object... keyvalues) {
        return buildMap(keyvalues);
    }

    public static Map<String, Object> buildHeaders(Object... keyvalues) {
        return buildMap(keyvalues);
    }


    /**
     * 方法的功能说明
     *
     * @param id
     * @param claims
     * @param subject
     * @param signatureAlgorithm 指定签名的时候使用的签名算法，也就是header那部分，
     *                           jjwt已经将这部分内容封装好了。
     * @param secretKey          生成签名的时候使用的秘钥secret,一般可以从本地配置文件中读取，
     *                           切记这个秘钥不能外露哦。 它就是你服务端的私钥，在任何场景都不应该流露出去。
     *                           一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
     * @param ttlMillis          jwt的有效期
     * @return
     * @author lihong10 2019/4/2 17:25
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/2 17:25
     * @modify by reason:{原因}
     */
    public static String createJWT(String id, Map<String, Object> headers, Map<String, Object> claims,
                                   String issuer, String audience, String subject, SignatureAlgorithm signatureAlgorithm,
                                   SecretKey secretKey, long ttlMillis) {

        long nowMillis = System.currentTimeMillis();
        //生成JWT的时间
        Date now = new Date(nowMillis);

        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder =
                Jwts.builder() //new一个JwtBuilder，设置jwt的body
                        // 私有声明，一定要先设置这个自己创建的私有的声明，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                        .setHeader(headers)
                        .setClaims(claims)
                        //设置jti(JWT ID)：是JWT的唯一标识，建议设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                        .setId(id)
                        //iat: jwt的签发时间
                        .setIssuer(issuer)
                        .setAudience(audience)
                        .setIssuedAt(now)
                        //sub(Subject)：代表这个JWT的主体，即它的所有人
                        .setSubject(subject)
                        //设置签名使用的签名算法和签名使用的秘钥
                        .signWith(signatureAlgorithm, secretKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            //设置过期时间
            builder.setExpiration(new Date(expMillis));
        }

        //开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
        return builder.compact();
    }

    public static String createJWT(String id, Map<String, Object> claims,
                                   String issuer, String audience, String subject, SignatureAlgorithm signatureAlgorithm,
                                   SecretKey secretKey, long ttlMillis) {
        return createJWT(id, DEFAULT_HEADER, claims, issuer, audience, subject, signatureAlgorithm, secretKey, ttlMillis);
    }


    public static SecretKey secretKey(byte[] key, String algorithms) {
        /*
         * 根据给定的字节数组使用加密算法algorithms构造一个密钥，
         */
        SecretKey secretKey = new SecretKeySpec(key, algorithms);
        return secretKey;
    }

    public static SecretKey secretKey(byte[] key, int from, int to, String algorithms) {
        /*
         * 根据给定的字节数组使用加密算法algorithms构造一个密钥，
         * 使用 content中的始于且包含 0 到前 leng 个字节这是当然是所有
         */
        SecretKey secretKey = new SecretKeySpec(key, from, to, algorithms);
        return secretKey;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {

        byte[] keys = "lihong10".getBytes(CommonConsts.CHARSET_UTF8);
        System.out.println("keys: " + Arrays.toString(keys));
        SecretKey secretKey = secretKey(keys, "AES");

        SecretKey secretKey2 = secretKey(new byte[]{108, 105, 104, 111, 110}, "AES");

        byte[] encoded = secretKey.getEncoded();
        System.out.println("reverted keys: " + Arrays.toString(encoded));


        String id = StringUtils.getUuid();
        System.out.println("id: " + id);

        Map<String, Object> claims = new HashMap() {{
            put("exchanger", "email_exchanger");
        }};

        String jwt = createJWT(id, DEFAULT_HEADER, claims, "mcs", "ps_fs_das", "update_email", SignatureAlgorithm.HS256,
                secretKey, CommonConsts.ONE_DAY_MILLIS);

        System.out.println("jwt is: \n\t" + jwt);

        Jws<Claims> claimsJws = parseJWT(jwt, secretKey);
//        Jws<Claims> claimsJws1 = parseJWT(jwt, secretKey2); //使用错误的密钥，解密会报错

        String signature = claimsJws.getSignature();
        System.out.println(JsonUtils.object2Json(signature));

        Claims body = claimsJws.getBody();
        System.out.println(JsonUtils.object2Json(body));

        JwsHeader header = claimsJws.getHeader();
        System.out.println(JsonUtils.object2Json(header));
    }


    @Test
    public void testToken() {

        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzA4MjM0MTYsInVzZXJfbmFtZSI6ImxpaG9uZzEwIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImIzNTBmNDUzLWM2MzMtNDZkNC04ZjU3LWFkOWQ5NTFiZTQ3MCIsImNsaWVudF9pZCI6ImNsaWVudDIiLCJzY29wZSI6WyJhbGwiXX0.LFuKYhPG5DwoMLOKZOq0MP1MizHGmclpa5pUoLMrd2A";

        Jws<Claims> claimsJws = parseJWT(jwt, secretKey("internet_plus".getBytes(), "HMACSHA256"));

        System.out.println(claimsJws);

    }


}
