/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class CatalinaProperties
/*     */ {
/*  38 */   private static final Log log = LogFactory.getLog(CatalinaProperties.class);
/*     */   
/*  40 */   private static Properties properties = null;
/*     */   
/*     */   static
/*     */   {
/*  44 */     loadProperties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getProperty(String name)
/*     */   {
/*  53 */     return properties.getProperty(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void loadProperties()
/*     */   {
/*  62 */     InputStream is = null;
/*     */     try {
/*  64 */       String configUrl = System.getProperty("catalina.config");
/*  65 */       if (configUrl != null) {
/*  66 */         is = new URL(configUrl).openStream();
/*     */       }
/*     */     } catch (Throwable t) {
/*  69 */       handleThrowable(t);
/*     */     }
/*     */     
/*  72 */     if (is == null) {
/*     */       try {
/*  74 */         File home = new File(Bootstrap.getCatalinaBase());
/*  75 */         File conf = new File(home, "conf");
/*  76 */         File propsFile = new File(conf, "catalina.properties");
/*  77 */         is = new FileInputStream(propsFile);
/*     */       } catch (Throwable t) {
/*  79 */         handleThrowable(t);
/*     */       }
/*     */     }
/*     */     
/*  83 */     if (is == null) {
/*     */       try
/*     */       {
/*  86 */         is = CatalinaProperties.class.getResourceAsStream("/org/apache/catalina/startup/catalina.properties");
/*     */       } catch (Throwable t) {
/*  88 */         handleThrowable(t);
/*     */       }
/*     */     }
/*     */     
/*  92 */     if (is != null) {
/*     */       try {
/*  94 */         properties = new Properties();
/*  95 */         properties.load(is);
/*     */         
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 101 */           is.close();
/*     */         } catch (IOException ioe) {
/* 103 */           log.warn("Could not close catalina.properties", ioe);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 108 */         if (is != null) {
/*     */           break label235;
/*     */         }
/*     */       }
/*     */       catch (Throwable t)
/*     */       {
/*  97 */         handleThrowable(t);
/*  98 */         log.warn(t);
/*     */       } finally {
/*     */         try {
/* 101 */           is.close();
/*     */         } catch (IOException ioe) {
/* 103 */           log.warn("Could not close catalina.properties", ioe);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 110 */     log.warn("Failed to load catalina.properties");
/*     */     
/* 112 */     properties = new Properties();
/*     */     
/*     */     label235:
/*     */     
/* 116 */     Enumeration<?> enumeration = properties.propertyNames();
/* 117 */     while (enumeration.hasMoreElements()) {
/* 118 */       String name = (String)enumeration.nextElement();
/* 119 */       String value = properties.getProperty(name);
/* 120 */       if (value != null) {
/* 121 */         System.setProperty(name, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void handleThrowable(Throwable t)
/*     */   {
/* 129 */     if ((t instanceof ThreadDeath)) {
/* 130 */       throw ((ThreadDeath)t);
/*     */     }
/* 132 */     if ((t instanceof VirtualMachineError)) {
/* 133 */       throw ((VirtualMachineError)t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\CatalinaProperties.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */