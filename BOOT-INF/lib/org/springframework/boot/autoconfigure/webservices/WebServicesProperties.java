/*    */ package org.springframework.boot.autoconfigure.webservices;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*    */ import org.springframework.util.Assert;
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
/*    */ @ConfigurationProperties(prefix="spring.webservices")
/*    */ public class WebServicesProperties
/*    */ {
/* 38 */   private String path = "/services";
/*    */   
/* 40 */   private final Servlet servlet = new Servlet();
/*    */   
/*    */   public String getPath() {
/* 43 */     return this.path;
/*    */   }
/*    */   
/*    */   public void setPath(String path) {
/* 47 */     Assert.notNull(path, "Path must not be null");
/* 48 */     Assert.isTrue((path.isEmpty()) || (path.startsWith("/")), "Path must start with / or be empty");
/*    */     
/* 50 */     this.path = path;
/*    */   }
/*    */   
/*    */   public Servlet getServlet() {
/* 54 */     return this.servlet;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static class Servlet
/*    */   {
/* 62 */     private Map<String, String> init = new HashMap();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 67 */     private int loadOnStartup = -1;
/*    */     
/*    */     public Map<String, String> getInit() {
/* 70 */       return this.init;
/*    */     }
/*    */     
/*    */     public void setInit(Map<String, String> init) {
/* 74 */       this.init = init;
/*    */     }
/*    */     
/*    */     public int getLoadOnStartup() {
/* 78 */       return this.loadOnStartup;
/*    */     }
/*    */     
/*    */     public void setLoadOnStartup(int loadOnStartup) {
/* 82 */       this.loadOnStartup = loadOnStartup;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\webservices\WebServicesProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */