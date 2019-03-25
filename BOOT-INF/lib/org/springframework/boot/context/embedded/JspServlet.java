/*    */ package org.springframework.boot.context.embedded;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JspServlet
/*    */ {
/* 36 */   private String className = "org.apache.jasper.servlet.JspServlet";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 41 */   private Map<String, String> initParameters = new HashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 47 */   private boolean registered = true;
/*    */   
/*    */   public JspServlet() {
/* 50 */     this.initParameters.put("development", "false");
/*    */   }
/*    */   
/*    */   public String getClassName() {
/* 54 */     return this.className;
/*    */   }
/*    */   
/*    */   public void setClassName(String className) {
/* 58 */     this.className = className;
/*    */   }
/*    */   
/*    */   public Map<String, String> getInitParameters() {
/* 62 */     return this.initParameters;
/*    */   }
/*    */   
/*    */   public void setInitParameters(Map<String, String> initParameters) {
/* 66 */     this.initParameters = initParameters;
/*    */   }
/*    */   
/*    */   public boolean getRegistered() {
/* 70 */     return this.registered;
/*    */   }
/*    */   
/*    */   public void setRegistered(boolean registered) {
/* 74 */     this.registered = registered;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\JspServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */