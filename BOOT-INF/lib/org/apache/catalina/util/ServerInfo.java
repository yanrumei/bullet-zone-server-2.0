/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class ServerInfo
/*     */ {
/*     */   private static final String serverInfo;
/*     */   private static final String serverBuilt;
/*     */   private static final String serverNumber;
/*     */   
/*     */   static
/*     */   {
/*  57 */     String info = null;
/*  58 */     String built = null;
/*  59 */     String number = null;
/*     */     
/*  61 */     Properties props = new Properties();
/*     */     try {
/*  63 */       InputStream is = ServerInfo.class.getResourceAsStream("/org/apache/catalina/util/ServerInfo.properties");Throwable localThrowable4 = null;
/*  64 */       try { props.load(is);
/*  65 */         info = props.getProperty("server.info");
/*  66 */         built = props.getProperty("server.built");
/*  67 */         number = props.getProperty("server.number");
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/*  62 */         localThrowable4 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*  68 */         if (is != null) if (localThrowable4 != null) try { is.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else is.close();
/*  69 */       } } catch (Throwable t) { ExceptionUtils.handleThrowable(t);
/*     */     }
/*  71 */     if (info == null)
/*  72 */       info = "Apache Tomcat 8.5.x-dev";
/*  73 */     if (built == null)
/*  74 */       built = "unknown";
/*  75 */     if (number == null) {
/*  76 */       number = "8.5.x";
/*     */     }
/*  78 */     serverInfo = info;
/*  79 */     serverBuilt = built;
/*  80 */     serverNumber = number;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getServerInfo()
/*     */   {
/*  92 */     return serverInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getServerBuilt()
/*     */   {
/* 101 */     return serverBuilt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getServerNumber()
/*     */   {
/* 110 */     return serverNumber;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 115 */     System.out.println("Server version: " + getServerInfo());
/* 116 */     System.out.println("Server built:   " + getServerBuilt());
/* 117 */     System.out.println("Server number:  " + getServerNumber());
/* 118 */     System.out.println("OS Name:        " + 
/* 119 */       System.getProperty("os.name"));
/* 120 */     System.out.println("OS Version:     " + 
/* 121 */       System.getProperty("os.version"));
/* 122 */     System.out.println("Architecture:   " + 
/* 123 */       System.getProperty("os.arch"));
/* 124 */     System.out.println("JVM Version:    " + 
/* 125 */       System.getProperty("java.runtime.version"));
/* 126 */     System.out.println("JVM Vendor:     " + 
/* 127 */       System.getProperty("java.vm.vendor"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\ServerInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */