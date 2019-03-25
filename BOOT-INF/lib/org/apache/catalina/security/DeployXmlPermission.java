/*    */ package org.apache.catalina.security;
/*    */ 
/*    */ import java.security.BasicPermission;
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
/*    */ public class DeployXmlPermission
/*    */   extends BasicPermission
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public DeployXmlPermission(String name)
/*    */   {
/* 32 */     super(name);
/*    */   }
/*    */   
/*    */   public DeployXmlPermission(String name, String actions) {
/* 36 */     super(name, actions);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\security\DeployXmlPermission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */