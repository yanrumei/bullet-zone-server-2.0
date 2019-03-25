/*    */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.security.core.GrantedAuthority;
/*    */ import org.springframework.security.core.authority.AuthorityUtils;
/*    */ import org.springframework.util.ObjectUtils;
/*    */ import org.springframework.util.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FixedAuthoritiesExtractor
/*    */   implements AuthoritiesExtractor
/*    */ {
/*    */   private static final String AUTHORITIES = "authorities";
/* 41 */   private static final String[] AUTHORITY_KEYS = { "authority", "role", "value" };
/*    */   
/*    */   public List<GrantedAuthority> extractAuthorities(Map<String, Object> map)
/*    */   {
/* 45 */     String authorities = "ROLE_USER";
/* 46 */     if (map.containsKey("authorities")) {
/* 47 */       authorities = asAuthorities(map.get("authorities"));
/*    */     }
/* 49 */     return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
/*    */   }
/*    */   
/*    */   private String asAuthorities(Object object) {
/* 53 */     List<Object> authorities = new ArrayList();
/* 54 */     if ((object instanceof Collection)) {
/* 55 */       Collection<?> collection = (Collection)object;
/* 56 */       object = collection.toArray(new Object[0]);
/*    */     }
/* 58 */     if (ObjectUtils.isArray(object)) {
/* 59 */       Object[] array = (Object[])object;
/* 60 */       for (Object value : array) {
/* 61 */         if ((value instanceof String)) {
/* 62 */           authorities.add(value);
/*    */         }
/* 64 */         else if ((value instanceof Map)) {
/* 65 */           authorities.add(asAuthority((Map)value));
/*    */         }
/*    */         else {
/* 68 */           authorities.add(value);
/*    */         }
/*    */       }
/* 71 */       return StringUtils.collectionToCommaDelimitedString(authorities);
/*    */     }
/* 73 */     return object.toString();
/*    */   }
/*    */   
/*    */   private Object asAuthority(Map<?, ?> map) {
/* 77 */     if (map.size() == 1) {
/* 78 */       return map.values().iterator().next();
/*    */     }
/* 80 */     for (String key : AUTHORITY_KEYS) {
/* 81 */       if (map.containsKey(key)) {
/* 82 */         return map.get(key);
/*    */       }
/*    */     }
/* 85 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\FixedAuthoritiesExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */