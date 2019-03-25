/*    */ package org.apache.catalina.connector;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CoyotePrincipal
/*    */   implements Principal, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final String name;
/*    */   
/*    */   public CoyotePrincipal(String name)
/*    */   {
/* 37 */     this.name = name;
/*    */   }
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
/*    */   public String getName()
/*    */   {
/* 52 */     return this.name;
/*    */   }
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
/*    */   public String toString()
/*    */   {
/* 66 */     StringBuilder sb = new StringBuilder("CoyotePrincipal[");
/* 67 */     sb.append(this.name);
/* 68 */     sb.append("]");
/* 69 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\connector\CoyotePrincipal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */