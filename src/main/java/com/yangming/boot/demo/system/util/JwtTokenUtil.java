package com.yangming.boot.demo.system.util;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 生成token
     *
     * @param claims
     * @return
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(createExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 根据用户信息生成token
     *
     * @param userDetails
     * @return
     */
    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createToken(claims);
    }

    /**
     * 生成token过期时间
     *
     * @return
     */
    private Date createExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中取出用户信息
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败：{}", token);
        }
        return claims;
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String username = claims.getSubject();
            return username;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token){
        Date expiredDate = getExpiredDateFormToken(token);
        return expiredDate.before(expiredDate);
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFormToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 验证token是否过期
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 验证token是否可以刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token
     * @param time
     * @return
     */
    private boolean tokenRefreshJustBefore(String token,int time){
        Claims claims =getClaimsFromToken(token);
        Date created = claims.getExpiration();
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created,time))){
            return true;
        }
        return false;
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return createToken(claims);
    }

    /**
     * 刷新原来的token  如果token没有过期
     * @param oldToken
     * @return
     */
    public String refreshHeadToken(String oldToken){
        if (StringUtils.isEmpty(oldToken)){
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if (StringUtils.isEmpty(token)){
            return null;
        }
        Claims claims = getClaimsFromToken(oldToken);
        if (claims == null){
            return null;
        }
        if (isTokenExpired(oldToken)){
            return null;
        }
        //如果token在30分钟之内刚刚刷新过，返回原token
        if (tokenRefreshJustBefore(token,30*60)){
            return token;
        }else {
            claims.put(CLAIM_KEY_CREATED,new Date());
            return createToken(claims);
        }

    }

}
