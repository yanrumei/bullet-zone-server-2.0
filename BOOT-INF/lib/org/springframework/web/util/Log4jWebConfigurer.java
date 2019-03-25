/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.util.Log4jConfigurer;
/*     */ import org.springframework.util.ResourceUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @Deprecated
/*     */ public abstract class Log4jWebConfigurer
/*     */ {
/*     */   public static final String CONFIG_LOCATION_PARAM = "log4jConfigLocation";
/*     */   public static final String REFRESH_INTERVAL_PARAM = "log4jRefreshInterval";
/*     */   public static final String EXPOSE_WEB_APP_ROOT_PARAM = "log4jExposeWebAppRoot";
/*     */   
/*     */   public static void initLogging(ServletContext servletContext)
/*     */   {
/* 118 */     if (exposeWebAppRoot(servletContext)) {
/* 119 */       WebUtils.setWebAppRootSystemProperty(servletContext);
/*     */     }
/*     */     
/*     */ 
/* 123 */     String location = servletContext.getInitParameter("log4jConfigLocation");
/* 124 */     if (location != null)
/*     */     {
/*     */       try
/*     */       {
/* 128 */         location = ServletContextPropertyUtils.resolvePlaceholders(location, servletContext);
/*     */         
/*     */ 
/* 131 */         if (!ResourceUtils.isUrl(location))
/*     */         {
/* 133 */           location = WebUtils.getRealPath(servletContext, location);
/*     */         }
/*     */         
/*     */ 
/* 137 */         servletContext.log("Initializing log4j from [" + location + "]");
/*     */         
/*     */ 
/* 140 */         String intervalString = servletContext.getInitParameter("log4jRefreshInterval");
/* 141 */         if (StringUtils.hasText(intervalString))
/*     */         {
/*     */           try
/*     */           {
/* 145 */             long refreshInterval = Long.parseLong(intervalString);
/* 146 */             Log4jConfigurer.initLogging(location, refreshInterval);
/*     */           }
/*     */           catch (NumberFormatException ex) {
/* 149 */             throw new IllegalArgumentException("Invalid 'log4jRefreshInterval' parameter: " + ex.getMessage());
/*     */           }
/*     */           
/*     */         }
/*     */         else {
/* 154 */           Log4jConfigurer.initLogging(location);
/*     */         }
/*     */       }
/*     */       catch (FileNotFoundException ex) {
/* 158 */         throw new IllegalArgumentException("Invalid 'log4jConfigLocation' parameter: " + ex.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static void shutdownLogging(ServletContext servletContext)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: ldc 30
/*     */     //   3: invokeinterface 16 2 0
/*     */     //   8: invokestatic 31	org/springframework/util/Log4jConfigurer:shutdownLogging	()V
/*     */     //   11: aload_0
/*     */     //   12: invokestatic 2	org/springframework/web/util/Log4jWebConfigurer:exposeWebAppRoot	(Ljavax/servlet/ServletContext;)Z
/*     */     //   15: ifeq +24 -> 39
/*     */     //   18: aload_0
/*     */     //   19: invokestatic 32	org/springframework/web/util/WebUtils:removeWebAppRootSystemProperty	(Ljavax/servlet/ServletContext;)V
/*     */     //   22: goto +17 -> 39
/*     */     //   25: astore_1
/*     */     //   26: aload_0
/*     */     //   27: invokestatic 2	org/springframework/web/util/Log4jWebConfigurer:exposeWebAppRoot	(Ljavax/servlet/ServletContext;)Z
/*     */     //   30: ifeq +7 -> 37
/*     */     //   33: aload_0
/*     */     //   34: invokestatic 32	org/springframework/web/util/WebUtils:removeWebAppRootSystemProperty	(Ljavax/servlet/ServletContext;)V
/*     */     //   37: aload_1
/*     */     //   38: athrow
/*     */     //   39: return
/*     */     // Line number table:
/*     */     //   Java source line #170	-> byte code offset #0
/*     */     //   Java source line #172	-> byte code offset #8
/*     */     //   Java source line #176	-> byte code offset #11
/*     */     //   Java source line #177	-> byte code offset #18
/*     */     //   Java source line #176	-> byte code offset #25
/*     */     //   Java source line #177	-> byte code offset #33
/*     */     //   Java source line #180	-> byte code offset #39
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	40	0	servletContext	ServletContext
/*     */     //   25	13	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   8	11	25	finally
/*     */   }
/*     */   
/*     */   private static boolean exposeWebAppRoot(ServletContext servletContext)
/*     */   {
/* 188 */     String exposeWebAppRootParam = servletContext.getInitParameter("log4jExposeWebAppRoot");
/* 189 */     return (exposeWebAppRootParam == null) || (Boolean.valueOf(exposeWebAppRootParam).booleanValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\Log4jWebConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */