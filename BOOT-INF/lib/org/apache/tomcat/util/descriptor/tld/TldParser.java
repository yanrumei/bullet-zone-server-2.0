/*    */ package org.apache.tomcat.util.descriptor.tld;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.descriptor.DigesterFactory;
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.RuleSet;
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
/*    */ public class TldParser
/*    */ {
/* 39 */   private static final Log log = LogFactory.getLog(TldParser.class);
/*    */   private final Digester digester;
/*    */   
/*    */   public TldParser(boolean namespaceAware, boolean validation, boolean blockExternal)
/*    */   {
/* 44 */     this(namespaceAware, validation, new TldRuleSet(), blockExternal);
/*    */   }
/*    */   
/*    */   public TldParser(boolean namespaceAware, boolean validation, RuleSet ruleSet, boolean blockExternal)
/*    */   {
/* 49 */     this.digester = DigesterFactory.newDigester(validation, namespaceAware, ruleSet, blockExternal);
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public TaglibXml parse(TldResourcePath path)
/*    */     throws java.io.IOException, org.xml.sax.SAXException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 7	org/apache/tomcat/util/descriptor/Constants:IS_SECURITY_ENABLED	Z
/*    */     //   3: ifeq +22 -> 25
/*    */     //   6: new 8	org/apache/tomcat/util/security/PrivilegedGetTccl
/*    */     //   9: dup
/*    */     //   10: invokespecial 9	org/apache/tomcat/util/security/PrivilegedGetTccl:<init>	()V
/*    */     //   13: astore_3
/*    */     //   14: aload_3
/*    */     //   15: invokestatic 10	java/security/AccessController:doPrivileged	(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
/*    */     //   18: checkcast 11	java/lang/ClassLoader
/*    */     //   21: astore_2
/*    */     //   22: goto +10 -> 32
/*    */     //   25: invokestatic 12	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */     //   28: invokevirtual 13	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*    */     //   31: astore_2
/*    */     //   32: aload_1
/*    */     //   33: invokevirtual 14	org/apache/tomcat/util/descriptor/tld/TldResourcePath:openStream	()Ljava/io/InputStream;
/*    */     //   36: astore_3
/*    */     //   37: aconst_null
/*    */     //   38: astore 4
/*    */     //   40: getstatic 7	org/apache/tomcat/util/descriptor/Constants:IS_SECURITY_ENABLED	Z
/*    */     //   43: ifeq +26 -> 69
/*    */     //   46: new 15	org/apache/tomcat/util/security/PrivilegedSetTccl
/*    */     //   49: dup
/*    */     //   50: ldc 16
/*    */     //   52: invokevirtual 17	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*    */     //   55: invokespecial 18	org/apache/tomcat/util/security/PrivilegedSetTccl:<init>	(Ljava/lang/ClassLoader;)V
/*    */     //   58: astore 5
/*    */     //   60: aload 5
/*    */     //   62: invokestatic 10	java/security/AccessController:doPrivileged	(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
/*    */     //   65: pop
/*    */     //   66: goto +14 -> 80
/*    */     //   69: invokestatic 12	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */     //   72: ldc 16
/*    */     //   74: invokevirtual 17	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*    */     //   77: invokevirtual 19	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*    */     //   80: new 20	org/apache/tomcat/util/descriptor/XmlErrorHandler
/*    */     //   83: dup
/*    */     //   84: invokespecial 21	org/apache/tomcat/util/descriptor/XmlErrorHandler:<init>	()V
/*    */     //   87: astore 5
/*    */     //   89: aload_0
/*    */     //   90: getfield 6	org/apache/tomcat/util/descriptor/tld/TldParser:digester	Lorg/apache/tomcat/util/digester/Digester;
/*    */     //   93: aload 5
/*    */     //   95: invokevirtual 22	org/apache/tomcat/util/digester/Digester:setErrorHandler	(Lorg/xml/sax/ErrorHandler;)V
/*    */     //   98: new 23	org/apache/tomcat/util/descriptor/tld/TaglibXml
/*    */     //   101: dup
/*    */     //   102: invokespecial 24	org/apache/tomcat/util/descriptor/tld/TaglibXml:<init>	()V
/*    */     //   105: astore 6
/*    */     //   107: aload_0
/*    */     //   108: getfield 6	org/apache/tomcat/util/descriptor/tld/TldParser:digester	Lorg/apache/tomcat/util/digester/Digester;
/*    */     //   111: aload 6
/*    */     //   113: invokevirtual 25	org/apache/tomcat/util/digester/Digester:push	(Ljava/lang/Object;)V
/*    */     //   116: new 26	org/xml/sax/InputSource
/*    */     //   119: dup
/*    */     //   120: aload_1
/*    */     //   121: invokevirtual 27	org/apache/tomcat/util/descriptor/tld/TldResourcePath:toExternalForm	()Ljava/lang/String;
/*    */     //   124: invokespecial 28	org/xml/sax/InputSource:<init>	(Ljava/lang/String;)V
/*    */     //   127: astore 7
/*    */     //   129: aload 7
/*    */     //   131: aload_3
/*    */     //   132: invokevirtual 29	org/xml/sax/InputSource:setByteStream	(Ljava/io/InputStream;)V
/*    */     //   135: aload_0
/*    */     //   136: getfield 6	org/apache/tomcat/util/descriptor/tld/TldParser:digester	Lorg/apache/tomcat/util/digester/Digester;
/*    */     //   139: aload 7
/*    */     //   141: invokevirtual 30	org/apache/tomcat/util/digester/Digester:parse	(Lorg/xml/sax/InputSource;)Ljava/lang/Object;
/*    */     //   144: pop
/*    */     //   145: aload 5
/*    */     //   147: invokevirtual 31	org/apache/tomcat/util/descriptor/XmlErrorHandler:getWarnings	()Ljava/util/List;
/*    */     //   150: invokeinterface 32 1 0
/*    */     //   155: ifeq +16 -> 171
/*    */     //   158: aload 5
/*    */     //   160: invokevirtual 33	org/apache/tomcat/util/descriptor/XmlErrorHandler:getErrors	()Ljava/util/List;
/*    */     //   163: invokeinterface 32 1 0
/*    */     //   168: ifne +48 -> 216
/*    */     //   171: aload 5
/*    */     //   173: getstatic 34	org/apache/tomcat/util/descriptor/tld/TldParser:log	Lorg/apache/juli/logging/Log;
/*    */     //   176: aload 7
/*    */     //   178: invokevirtual 35	org/xml/sax/InputSource:getSystemId	()Ljava/lang/String;
/*    */     //   181: invokevirtual 36	org/apache/tomcat/util/descriptor/XmlErrorHandler:logFindings	(Lorg/apache/juli/logging/Log;Ljava/lang/String;)V
/*    */     //   184: aload 5
/*    */     //   186: invokevirtual 33	org/apache/tomcat/util/descriptor/XmlErrorHandler:getErrors	()Ljava/util/List;
/*    */     //   189: invokeinterface 32 1 0
/*    */     //   194: ifne +22 -> 216
/*    */     //   197: aload 5
/*    */     //   199: invokevirtual 33	org/apache/tomcat/util/descriptor/XmlErrorHandler:getErrors	()Ljava/util/List;
/*    */     //   202: invokeinterface 37 1 0
/*    */     //   207: invokeinterface 38 1 0
/*    */     //   212: checkcast 39	org/xml/sax/SAXParseException
/*    */     //   215: athrow
/*    */     //   216: aload 6
/*    */     //   218: astore 8
/*    */     //   220: aload_3
/*    */     //   221: ifnull +31 -> 252
/*    */     //   224: aload 4
/*    */     //   226: ifnull +22 -> 248
/*    */     //   229: aload_3
/*    */     //   230: invokevirtual 40	java/io/InputStream:close	()V
/*    */     //   233: goto +19 -> 252
/*    */     //   236: astore 9
/*    */     //   238: aload 4
/*    */     //   240: aload 9
/*    */     //   242: invokevirtual 42	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*    */     //   245: goto +7 -> 252
/*    */     //   248: aload_3
/*    */     //   249: invokevirtual 40	java/io/InputStream:close	()V
/*    */     //   252: aload_0
/*    */     //   253: getfield 6	org/apache/tomcat/util/descriptor/tld/TldParser:digester	Lorg/apache/tomcat/util/digester/Digester;
/*    */     //   256: invokevirtual 43	org/apache/tomcat/util/digester/Digester:reset	()V
/*    */     //   259: getstatic 7	org/apache/tomcat/util/descriptor/Constants:IS_SECURITY_ENABLED	Z
/*    */     //   262: ifeq +22 -> 284
/*    */     //   265: new 15	org/apache/tomcat/util/security/PrivilegedSetTccl
/*    */     //   268: dup
/*    */     //   269: aload_2
/*    */     //   270: invokespecial 18	org/apache/tomcat/util/security/PrivilegedSetTccl:<init>	(Ljava/lang/ClassLoader;)V
/*    */     //   273: astore 9
/*    */     //   275: aload 9
/*    */     //   277: invokestatic 10	java/security/AccessController:doPrivileged	(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
/*    */     //   280: pop
/*    */     //   281: goto +10 -> 291
/*    */     //   284: invokestatic 12	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */     //   287: aload_2
/*    */     //   288: invokevirtual 19	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*    */     //   291: aload 8
/*    */     //   293: areturn
/*    */     //   294: astore 5
/*    */     //   296: aload 5
/*    */     //   298: astore 4
/*    */     //   300: aload 5
/*    */     //   302: athrow
/*    */     //   303: astore 10
/*    */     //   305: aload_3
/*    */     //   306: ifnull +31 -> 337
/*    */     //   309: aload 4
/*    */     //   311: ifnull +22 -> 333
/*    */     //   314: aload_3
/*    */     //   315: invokevirtual 40	java/io/InputStream:close	()V
/*    */     //   318: goto +19 -> 337
/*    */     //   321: astore 11
/*    */     //   323: aload 4
/*    */     //   325: aload 11
/*    */     //   327: invokevirtual 42	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*    */     //   330: goto +7 -> 337
/*    */     //   333: aload_3
/*    */     //   334: invokevirtual 40	java/io/InputStream:close	()V
/*    */     //   337: aload 10
/*    */     //   339: athrow
/*    */     //   340: astore 12
/*    */     //   342: aload_0
/*    */     //   343: getfield 6	org/apache/tomcat/util/descriptor/tld/TldParser:digester	Lorg/apache/tomcat/util/digester/Digester;
/*    */     //   346: invokevirtual 43	org/apache/tomcat/util/digester/Digester:reset	()V
/*    */     //   349: getstatic 7	org/apache/tomcat/util/descriptor/Constants:IS_SECURITY_ENABLED	Z
/*    */     //   352: ifeq +22 -> 374
/*    */     //   355: new 15	org/apache/tomcat/util/security/PrivilegedSetTccl
/*    */     //   358: dup
/*    */     //   359: aload_2
/*    */     //   360: invokespecial 18	org/apache/tomcat/util/security/PrivilegedSetTccl:<init>	(Ljava/lang/ClassLoader;)V
/*    */     //   363: astore 13
/*    */     //   365: aload 13
/*    */     //   367: invokestatic 10	java/security/AccessController:doPrivileged	(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
/*    */     //   370: pop
/*    */     //   371: goto +10 -> 381
/*    */     //   374: invokestatic 12	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*    */     //   377: aload_2
/*    */     //   378: invokevirtual 19	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*    */     //   381: aload 12
/*    */     //   383: athrow
/*    */     // Line number table:
/*    */     //   Java source line #55	-> byte code offset #0
/*    */     //   Java source line #56	-> byte code offset #6
/*    */     //   Java source line #57	-> byte code offset #14
/*    */     //   Java source line #58	-> byte code offset #22
/*    */     //   Java source line #59	-> byte code offset #25
/*    */     //   Java source line #61	-> byte code offset #32
/*    */     //   Java source line #62	-> byte code offset #40
/*    */     //   Java source line #63	-> byte code offset #46
/*    */     //   Java source line #64	-> byte code offset #60
/*    */     //   Java source line #65	-> byte code offset #66
/*    */     //   Java source line #66	-> byte code offset #69
/*    */     //   Java source line #68	-> byte code offset #80
/*    */     //   Java source line #69	-> byte code offset #89
/*    */     //   Java source line #71	-> byte code offset #98
/*    */     //   Java source line #72	-> byte code offset #107
/*    */     //   Java source line #74	-> byte code offset #116
/*    */     //   Java source line #75	-> byte code offset #129
/*    */     //   Java source line #76	-> byte code offset #135
/*    */     //   Java source line #77	-> byte code offset #145
/*    */     //   Java source line #78	-> byte code offset #171
/*    */     //   Java source line #79	-> byte code offset #184
/*    */     //   Java source line #81	-> byte code offset #197
/*    */     //   Java source line #84	-> byte code offset #216
/*    */     //   Java source line #85	-> byte code offset #220
/*    */     //   Java source line #86	-> byte code offset #252
/*    */     //   Java source line #87	-> byte code offset #259
/*    */     //   Java source line #88	-> byte code offset #265
/*    */     //   Java source line #89	-> byte code offset #275
/*    */     //   Java source line #90	-> byte code offset #281
/*    */     //   Java source line #91	-> byte code offset #284
/*    */     //   Java source line #84	-> byte code offset #291
/*    */     //   Java source line #61	-> byte code offset #294
/*    */     //   Java source line #85	-> byte code offset #303
/*    */     //   Java source line #86	-> byte code offset #340
/*    */     //   Java source line #87	-> byte code offset #349
/*    */     //   Java source line #88	-> byte code offset #355
/*    */     //   Java source line #89	-> byte code offset #365
/*    */     //   Java source line #90	-> byte code offset #371
/*    */     //   Java source line #91	-> byte code offset #374
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	384	0	this	TldParser
/*    */     //   0	384	1	path	TldResourcePath
/*    */     //   21	2	2	original	ClassLoader
/*    */     //   31	347	2	original	ClassLoader
/*    */     //   13	2	3	pa	org.apache.tomcat.util.security.PrivilegedGetTccl
/*    */     //   36	298	3	is	java.io.InputStream
/*    */     //   38	286	4	localThrowable3	Throwable
/*    */     //   58	3	5	pa	org.apache.tomcat.util.security.PrivilegedSetTccl
/*    */     //   87	111	5	handler	org.apache.tomcat.util.descriptor.XmlErrorHandler
/*    */     //   294	7	5	localThrowable1	Throwable
/*    */     //   105	112	6	taglibXml	TaglibXml
/*    */     //   127	50	7	source	org.xml.sax.InputSource
/*    */     //   218	74	8	localTaglibXml1	TaglibXml
/*    */     //   236	5	9	localThrowable	Throwable
/*    */     //   273	3	9	pa	org.apache.tomcat.util.security.PrivilegedSetTccl
/*    */     //   303	35	10	localObject1	Object
/*    */     //   321	5	11	localThrowable2	Throwable
/*    */     //   340	42	12	localObject2	Object
/*    */     //   363	3	13	pa	org.apache.tomcat.util.security.PrivilegedSetTccl
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   229	233	236	java/lang/Throwable
/*    */     //   40	220	294	java/lang/Throwable
/*    */     //   40	220	303	finally
/*    */     //   294	305	303	finally
/*    */     //   314	318	321	java/lang/Throwable
/*    */     //   32	252	340	finally
/*    */     //   294	342	340	finally
/*    */   }
/*    */   
/*    */   public void setClassLoader(ClassLoader classLoader)
/*    */   {
/* 97 */     this.digester.setClassLoader(classLoader);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\TldParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */