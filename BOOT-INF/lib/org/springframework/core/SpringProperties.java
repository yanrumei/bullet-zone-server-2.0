/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public abstract class SpringProperties
/*     */ {
/*     */   private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";
/*  51 */   private static final Log logger = LogFactory.getLog(SpringProperties.class);
/*     */   
/*  53 */   private static final Properties localProperties = new Properties();
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
/*     */   public static void setProperty(String key, String value)
/*     */   {
/*  87 */     if (value != null) {
/*  88 */       localProperties.setProperty(key, value);
/*     */     }
/*     */     else {
/*  91 */       localProperties.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getProperty(String key)
/*     */   {
/* 102 */     String value = localProperties.getProperty(key);
/* 103 */     if (value == null) {
/*     */       try {
/* 105 */         value = System.getProperty(key);
/*     */       }
/*     */       catch (Throwable ex) {
/* 108 */         if (logger.isDebugEnabled()) {
/* 109 */           logger.debug("Could not retrieve system property '" + key + "': " + ex);
/*     */         }
/*     */       }
/*     */     }
/* 113 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setFlag(String key)
/*     */   {
/* 122 */     localProperties.put(key, Boolean.TRUE.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean getFlag(String key)
/*     */   {
/* 132 */     return Boolean.parseBoolean(getProperty(key));
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   static
/*     */   {
/*     */     // Byte code:
/*     */     //   0: ldc 23
/*     */     //   2: invokestatic 24	org/apache/commons/logging/LogFactory:getLog	(Ljava/lang/Class;)Lorg/apache/commons/logging/Log;
/*     */     //   5: putstatic 8	org/springframework/core/SpringProperties:logger	Lorg/apache/commons/logging/Log;
/*     */     //   8: new 25	java/util/Properties
/*     */     //   11: dup
/*     */     //   12: invokespecial 26	java/util/Properties:<init>	()V
/*     */     //   15: putstatic 2	org/springframework/core/SpringProperties:localProperties	Ljava/util/Properties;
/*     */     //   18: ldc 23
/*     */     //   20: invokevirtual 27	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   23: astore_0
/*     */     //   24: aload_0
/*     */     //   25: ifnull +12 -> 37
/*     */     //   28: aload_0
/*     */     //   29: ldc 28
/*     */     //   31: invokevirtual 29	java/lang/ClassLoader:getResource	(Ljava/lang/String;)Ljava/net/URL;
/*     */     //   34: goto +8 -> 42
/*     */     //   37: ldc 28
/*     */     //   39: invokestatic 30	java/lang/ClassLoader:getSystemResource	(Ljava/lang/String;)Ljava/net/URL;
/*     */     //   42: astore_1
/*     */     //   43: aload_1
/*     */     //   44: ifnull +39 -> 83
/*     */     //   47: getstatic 8	org/springframework/core/SpringProperties:logger	Lorg/apache/commons/logging/Log;
/*     */     //   50: ldc 31
/*     */     //   52: invokeinterface 32 2 0
/*     */     //   57: aload_1
/*     */     //   58: invokevirtual 33	java/net/URL:openStream	()Ljava/io/InputStream;
/*     */     //   61: astore_2
/*     */     //   62: getstatic 2	org/springframework/core/SpringProperties:localProperties	Ljava/util/Properties;
/*     */     //   65: aload_2
/*     */     //   66: invokevirtual 34	java/util/Properties:load	(Ljava/io/InputStream;)V
/*     */     //   69: aload_2
/*     */     //   70: invokevirtual 35	java/io/InputStream:close	()V
/*     */     //   73: goto +10 -> 83
/*     */     //   76: astore_3
/*     */     //   77: aload_2
/*     */     //   78: invokevirtual 35	java/io/InputStream:close	()V
/*     */     //   81: aload_3
/*     */     //   82: athrow
/*     */     //   83: goto +42 -> 125
/*     */     //   86: astore_0
/*     */     //   87: getstatic 8	org/springframework/core/SpringProperties:logger	Lorg/apache/commons/logging/Log;
/*     */     //   90: invokeinterface 37 1 0
/*     */     //   95: ifeq +30 -> 125
/*     */     //   98: getstatic 8	org/springframework/core/SpringProperties:logger	Lorg/apache/commons/logging/Log;
/*     */     //   101: new 10	java/lang/StringBuilder
/*     */     //   104: dup
/*     */     //   105: invokespecial 11	java/lang/StringBuilder:<init>	()V
/*     */     //   108: ldc 38
/*     */     //   110: invokevirtual 13	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   113: aload_0
/*     */     //   114: invokevirtual 15	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   117: invokevirtual 16	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   120: invokeinterface 32 2 0
/*     */     //   125: return
/*     */     // Line number table:
/*     */     //   Java source line #51	-> byte code offset #0
/*     */     //   Java source line #53	-> byte code offset #8
/*     */     //   Java source line #58	-> byte code offset #18
/*     */     //   Java source line #59	-> byte code offset #24
/*     */     //   Java source line #60	-> byte code offset #39
/*     */     //   Java source line #61	-> byte code offset #43
/*     */     //   Java source line #62	-> byte code offset #47
/*     */     //   Java source line #63	-> byte code offset #57
/*     */     //   Java source line #65	-> byte code offset #62
/*     */     //   Java source line #68	-> byte code offset #69
/*     */     //   Java source line #69	-> byte code offset #73
/*     */     //   Java source line #68	-> byte code offset #76
/*     */     //   Java source line #76	-> byte code offset #83
/*     */     //   Java source line #72	-> byte code offset #86
/*     */     //   Java source line #73	-> byte code offset #87
/*     */     //   Java source line #74	-> byte code offset #98
/*     */     //   Java source line #77	-> byte code offset #125
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   23	6	0	cl	ClassLoader
/*     */     //   86	28	0	ex	java.io.IOException
/*     */     //   42	16	1	url	java.net.URL
/*     */     //   61	17	2	is	java.io.InputStream
/*     */     //   76	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   62	69	76	finally
/*     */     //   18	83	86	java/io/IOException
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\SpringProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */