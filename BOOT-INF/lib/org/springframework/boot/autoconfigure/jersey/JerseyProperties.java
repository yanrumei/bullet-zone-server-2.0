/*     */ package org.springframework.boot.autoconfigure.jersey;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.jersey")
/*     */ public class JerseyProperties
/*     */ {
/*  38 */   private Type type = Type.SERVLET;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private Map<String, String> init = new HashMap();
/*     */   
/*  45 */   private final Filter filter = new Filter();
/*     */   
/*  47 */   private final Servlet servlet = new Servlet();
/*     */   
/*     */ 
/*     */   private String applicationPath;
/*     */   
/*     */ 
/*     */ 
/*     */   public Filter getFilter()
/*     */   {
/*  56 */     return this.filter;
/*     */   }
/*     */   
/*     */   public Servlet getServlet() {
/*  60 */     return this.servlet;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  64 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Type type) {
/*  68 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Map<String, String> getInit() {
/*  72 */     return this.init;
/*     */   }
/*     */   
/*     */   public void setInit(Map<String, String> init) {
/*  76 */     this.init = init;
/*     */   }
/*     */   
/*     */   public String getApplicationPath() {
/*  80 */     return this.applicationPath;
/*     */   }
/*     */   
/*     */   public void setApplicationPath(String applicationPath) {
/*  84 */     this.applicationPath = applicationPath;
/*     */   }
/*     */   
/*     */   public static enum Type
/*     */   {
/*  89 */     SERVLET,  FILTER;
/*     */     
/*     */ 
/*     */     private Type() {}
/*     */   }
/*     */   
/*     */   public static class Filter
/*     */   {
/*     */     private int order;
/*     */     
/*     */     public int getOrder()
/*     */     {
/* 101 */       return this.order;
/*     */     }
/*     */     
/*     */     public void setOrder(int order) {
/* 105 */       this.order = order;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Servlet
/*     */   {
/* 115 */     private int loadOnStartup = -1;
/*     */     
/*     */     public int getLoadOnStartup() {
/* 118 */       return this.loadOnStartup;
/*     */     }
/*     */     
/*     */     public void setLoadOnStartup(int loadOnStartup) {
/* 122 */       this.loadOnStartup = loadOnStartup;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jersey\JerseyProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */