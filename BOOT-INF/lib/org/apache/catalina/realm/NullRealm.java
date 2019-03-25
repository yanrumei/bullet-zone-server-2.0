/*    */ package org.apache.catalina.realm;
/*    */ 
/*    */ import java.security.Principal;
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
/*    */ public class NullRealm
/*    */   extends RealmBase
/*    */ {
/*    */   private static final String NAME = "NullRealm";
/*    */   
/*    */   @Deprecated
/*    */   protected String getName()
/*    */   {
/* 33 */     return "NullRealm";
/*    */   }
/*    */   
/*    */ 
/*    */   protected String getPassword(String username)
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   protected Principal getPrincipal(String username)
/*    */   {
/* 45 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\realm\NullRealm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */