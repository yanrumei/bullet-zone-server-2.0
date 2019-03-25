/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.util.PropertyPlaceholderHelper;
/*     */ import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;
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
/*     */ public abstract class ServletContextPropertyUtils
/*     */ {
/*  38 */   private static final PropertyPlaceholderHelper strictHelper = new PropertyPlaceholderHelper("${", "}", ":", false);
/*     */   
/*     */ 
/*     */ 
/*  42 */   private static final PropertyPlaceholderHelper nonStrictHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
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
/*     */   public static String resolvePlaceholders(String text, ServletContext servletContext)
/*     */   {
/*  59 */     return resolvePlaceholders(text, servletContext, false);
/*     */   }
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
/*     */   public static String resolvePlaceholders(String text, ServletContext servletContext, boolean ignoreUnresolvablePlaceholders)
/*     */   {
/*  76 */     PropertyPlaceholderHelper helper = ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper;
/*  77 */     return helper.replacePlaceholders(text, new ServletContextPlaceholderResolver(text, servletContext));
/*     */   }
/*     */   
/*     */   private static class ServletContextPlaceholderResolver
/*     */     implements PropertyPlaceholderHelper.PlaceholderResolver
/*     */   {
/*     */     private final String text;
/*     */     private final ServletContext servletContext;
/*     */     
/*     */     public ServletContextPlaceholderResolver(String text, ServletContext servletContext)
/*     */     {
/*  88 */       this.text = text;
/*  89 */       this.servletContext = servletContext;
/*     */     }
/*     */     
/*     */     public String resolvePlaceholder(String placeholderName)
/*     */     {
/*     */       try {
/*  95 */         String propVal = this.servletContext.getInitParameter(placeholderName);
/*  96 */         if (propVal == null)
/*     */         {
/*  98 */           propVal = System.getProperty(placeholderName);
/*  99 */           if (propVal != null) {}
/*     */         }
/* 101 */         return System.getenv(placeholderName);
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/*     */ 
/* 107 */         System.err.println("Could not resolve placeholder '" + placeholderName + "' in [" + this.text + "] as ServletContext init-parameter or system property: " + ex);
/*     */       }
/* 109 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\ServletContextPropertyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */