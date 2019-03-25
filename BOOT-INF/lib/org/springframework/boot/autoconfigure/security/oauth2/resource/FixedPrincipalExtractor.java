/*    */ package org.springframework.boot.autoconfigure.security.oauth2.resource;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ public class FixedPrincipalExtractor
/*    */   implements PrincipalExtractor
/*    */ {
/* 30 */   private static final String[] PRINCIPAL_KEYS = { "user", "username", "userid", "user_id", "login", "id", "name" };
/*    */   
/*    */ 
/*    */   public Object extractPrincipal(Map<String, Object> map)
/*    */   {
/* 35 */     for (String key : PRINCIPAL_KEYS) {
/* 36 */       if (map.containsKey(key)) {
/* 37 */         return map.get(key);
/*    */       }
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\resource\FixedPrincipalExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */