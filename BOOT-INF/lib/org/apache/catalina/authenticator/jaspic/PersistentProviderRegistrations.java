/*     */ package org.apache.catalina.authenticator.jaspic;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ final class PersistentProviderRegistrations
/*     */ {
/*  46 */   private static final Log log = LogFactory.getLog(PersistentProviderRegistrations.class);
/*     */   
/*  48 */   private static final StringManager sm = StringManager.getManager(PersistentProviderRegistrations.class);
/*     */   
/*     */   /* Error */
/*     */   static Providers loadProviders(File configFile)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 2	java/io/FileInputStream
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 3	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   8: astore_1
/*     */     //   9: aconst_null
/*     */     //   10: astore_2
/*     */     //   11: new 4	org/apache/tomcat/util/digester/Digester
/*     */     //   14: dup
/*     */     //   15: invokespecial 5	org/apache/tomcat/util/digester/Digester:<init>	()V
/*     */     //   18: astore_3
/*     */     //   19: aload_3
/*     */     //   20: ldc 6
/*     */     //   22: iconst_1
/*     */     //   23: invokevirtual 7	org/apache/tomcat/util/digester/Digester:setFeature	(Ljava/lang/String;Z)V
/*     */     //   26: aload_3
/*     */     //   27: iconst_1
/*     */     //   28: invokevirtual 8	org/apache/tomcat/util/digester/Digester:setValidating	(Z)V
/*     */     //   31: aload_3
/*     */     //   32: iconst_1
/*     */     //   33: invokevirtual 9	org/apache/tomcat/util/digester/Digester:setNamespaceAware	(Z)V
/*     */     //   36: goto +15 -> 51
/*     */     //   39: astore 4
/*     */     //   41: new 11	java/lang/SecurityException
/*     */     //   44: dup
/*     */     //   45: aload 4
/*     */     //   47: invokespecial 12	java/lang/SecurityException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   50: athrow
/*     */     //   51: new 13	org/apache/catalina/authenticator/jaspic/PersistentProviderRegistrations$Providers
/*     */     //   54: dup
/*     */     //   55: invokespecial 14	org/apache/catalina/authenticator/jaspic/PersistentProviderRegistrations$Providers:<init>	()V
/*     */     //   58: astore 4
/*     */     //   60: aload_3
/*     */     //   61: aload 4
/*     */     //   63: invokevirtual 15	org/apache/tomcat/util/digester/Digester:push	(Ljava/lang/Object;)V
/*     */     //   66: aload_3
/*     */     //   67: ldc 16
/*     */     //   69: ldc 17
/*     */     //   71: invokevirtual 18	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   74: invokevirtual 19	org/apache/tomcat/util/digester/Digester:addObjectCreate	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   77: aload_3
/*     */     //   78: ldc 16
/*     */     //   80: invokevirtual 20	org/apache/tomcat/util/digester/Digester:addSetProperties	(Ljava/lang/String;)V
/*     */     //   83: aload_3
/*     */     //   84: ldc 16
/*     */     //   86: ldc 21
/*     */     //   88: ldc 17
/*     */     //   90: invokevirtual 18	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   93: invokevirtual 22	org/apache/tomcat/util/digester/Digester:addSetNext	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   96: aload_3
/*     */     //   97: ldc 23
/*     */     //   99: ldc 24
/*     */     //   101: invokevirtual 18	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   104: invokevirtual 19	org/apache/tomcat/util/digester/Digester:addObjectCreate	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   107: aload_3
/*     */     //   108: ldc 23
/*     */     //   110: invokevirtual 20	org/apache/tomcat/util/digester/Digester:addSetProperties	(Ljava/lang/String;)V
/*     */     //   113: aload_3
/*     */     //   114: ldc 23
/*     */     //   116: ldc 25
/*     */     //   118: ldc 24
/*     */     //   120: invokevirtual 18	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   123: invokevirtual 22	org/apache/tomcat/util/digester/Digester:addSetNext	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   126: aload_3
/*     */     //   127: aload_1
/*     */     //   128: invokevirtual 26	org/apache/tomcat/util/digester/Digester:parse	(Ljava/io/InputStream;)Ljava/lang/Object;
/*     */     //   131: pop
/*     */     //   132: aload 4
/*     */     //   134: astore 5
/*     */     //   136: aload_1
/*     */     //   137: ifnull +29 -> 166
/*     */     //   140: aload_2
/*     */     //   141: ifnull +21 -> 162
/*     */     //   144: aload_1
/*     */     //   145: invokevirtual 27	java/io/InputStream:close	()V
/*     */     //   148: goto +18 -> 166
/*     */     //   151: astore 6
/*     */     //   153: aload_2
/*     */     //   154: aload 6
/*     */     //   156: invokevirtual 29	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   159: goto +7 -> 166
/*     */     //   162: aload_1
/*     */     //   163: invokevirtual 27	java/io/InputStream:close	()V
/*     */     //   166: aload 5
/*     */     //   168: areturn
/*     */     //   169: astore_3
/*     */     //   170: aload_3
/*     */     //   171: astore_2
/*     */     //   172: aload_3
/*     */     //   173: athrow
/*     */     //   174: astore 7
/*     */     //   176: aload_1
/*     */     //   177: ifnull +29 -> 206
/*     */     //   180: aload_2
/*     */     //   181: ifnull +21 -> 202
/*     */     //   184: aload_1
/*     */     //   185: invokevirtual 27	java/io/InputStream:close	()V
/*     */     //   188: goto +18 -> 206
/*     */     //   191: astore 8
/*     */     //   193: aload_2
/*     */     //   194: aload 8
/*     */     //   196: invokevirtual 29	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   199: goto +7 -> 206
/*     */     //   202: aload_1
/*     */     //   203: invokevirtual 27	java/io/InputStream:close	()V
/*     */     //   206: aload 7
/*     */     //   208: athrow
/*     */     //   209: astore_1
/*     */     //   210: new 11	java/lang/SecurityException
/*     */     //   213: dup
/*     */     //   214: aload_1
/*     */     //   215: invokespecial 12	java/lang/SecurityException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   218: athrow
/*     */     // Line number table:
/*     */     //   Java source line #57	-> byte code offset #0
/*     */     //   Java source line #59	-> byte code offset #11
/*     */     //   Java source line #62	-> byte code offset #19
/*     */     //   Java source line #63	-> byte code offset #26
/*     */     //   Java source line #64	-> byte code offset #31
/*     */     //   Java source line #67	-> byte code offset #36
/*     */     //   Java source line #65	-> byte code offset #39
/*     */     //   Java source line #66	-> byte code offset #41
/*     */     //   Java source line #71	-> byte code offset #51
/*     */     //   Java source line #72	-> byte code offset #60
/*     */     //   Java source line #75	-> byte code offset #66
/*     */     //   Java source line #76	-> byte code offset #77
/*     */     //   Java source line #77	-> byte code offset #83
/*     */     //   Java source line #79	-> byte code offset #96
/*     */     //   Java source line #80	-> byte code offset #107
/*     */     //   Java source line #81	-> byte code offset #113
/*     */     //   Java source line #84	-> byte code offset #126
/*     */     //   Java source line #86	-> byte code offset #132
/*     */     //   Java source line #87	-> byte code offset #136
/*     */     //   Java source line #86	-> byte code offset #166
/*     */     //   Java source line #57	-> byte code offset #169
/*     */     //   Java source line #87	-> byte code offset #174
/*     */     //   Java source line #88	-> byte code offset #210
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	219	0	configFile	File
/*     */     //   8	195	1	is	java.io.InputStream
/*     */     //   209	6	1	e	Exception
/*     */     //   10	184	2	localThrowable3	Throwable
/*     */     //   18	109	3	digester	org.apache.tomcat.util.digester.Digester
/*     */     //   169	4	3	localThrowable1	Throwable
/*     */     //   39	7	4	e	Exception
/*     */     //   58	75	4	result	Providers
/*     */     //   151	4	6	localThrowable	Throwable
/*     */     //   174	33	7	localObject	Object
/*     */     //   191	4	8	localThrowable2	Throwable
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   19	36	39	java/lang/Exception
/*     */     //   144	148	151	java/lang/Throwable
/*     */     //   11	136	169	java/lang/Throwable
/*     */     //   11	136	174	finally
/*     */     //   169	176	174	finally
/*     */     //   184	188	191	java/lang/Throwable
/*     */     //   0	166	209	java/io/IOException
/*     */     //   0	166	209	org/xml/sax/SAXException
/*     */     //   169	209	209	java/io/IOException
/*     */     //   169	209	209	org/xml/sax/SAXException
/*     */   }
/*     */   
/*     */   static void writeProviders(Providers providers, File configFile)
/*     */   {
/*  94 */     File configFileOld = new File(configFile.getAbsolutePath() + ".old");
/*  95 */     File configFileNew = new File(configFile.getAbsolutePath() + ".new");
/*     */     
/*     */ 
/*  98 */     if ((configFileOld.exists()) && 
/*  99 */       (configFileOld.delete())) {
/* 100 */       throw new SecurityException(sm.getString("persistentProviderRegistrations.existsDeleteFail", new Object[] {configFileOld
/*     */       
/* 102 */         .getAbsolutePath() }));
/*     */     }
/*     */     
/* 105 */     if ((configFileNew.exists()) && 
/* 106 */       (configFileNew.delete())) {
/* 107 */       throw new SecurityException(sm.getString("persistentProviderRegistrations.existsDeleteFail", new Object[] {configFileNew
/*     */       
/* 109 */         .getAbsolutePath() }));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 114 */       OutputStream fos = new FileOutputStream(configFileNew);Throwable localThrowable6 = null;
/* 115 */       try { Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);Throwable localThrowable7 = null;
/* 116 */         try { writer.write("<?xml version='1.0' encoding='utf-8'?>\n<jaspic-providers\n    xmlns=\"http://tomcat.apache.org/xml\"\n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n    xsi:schemaLocation=\"http://tomcat.apache.org/xml jaspic-providers.xsd\"\n    version=\"1.0\">\n");
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */           for (Provider provider : providers.providers) {
/* 124 */             writer.write("  <provider");
/* 125 */             writeOptional("className", provider.getClassName(), writer);
/* 126 */             writeOptional("layer", provider.getLayer(), writer);
/* 127 */             writeOptional("appContext", provider.getAppContext(), writer);
/* 128 */             writeOptional("description", provider.getDescription(), writer);
/* 129 */             writer.write(">\n");
/* 130 */             for (Map.Entry<String, String> entry : provider.getProperties().entrySet()) {
/* 131 */               writer.write("    <property name=\"");
/* 132 */               writer.write((String)entry.getKey());
/* 133 */               writer.write("\" value=\"");
/* 134 */               writer.write((String)entry.getValue());
/* 135 */               writer.write("\"/>\n");
/*     */             }
/* 137 */             writer.write("  </provider>\n");
/*     */           }
/* 139 */           writer.write("</jaspic-providers>\n");
/*     */         }
/*     */         catch (Throwable localThrowable9)
/*     */         {
/* 114 */           localThrowable7 = localThrowable9;throw localThrowable9; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
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
/*     */       }
/*     */       finally
/*     */       {
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
/* 140 */         if (fos != null) if (localThrowable6 != null) try { fos.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else fos.close();
/* 141 */       } } catch (IOException e) { configFileNew.delete();
/* 142 */       throw new SecurityException(e);
/*     */     }
/*     */     
/*     */ 
/* 146 */     if ((configFile.isFile()) && 
/* 147 */       (!configFile.renameTo(configFileOld))) {
/* 148 */       throw new SecurityException(sm.getString("persistentProviderRegistrations.moveFail", new Object[] {configFile
/* 149 */         .getAbsolutePath(), configFileOld.getAbsolutePath() }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 154 */     if (!configFileNew.renameTo(configFile)) {
/* 155 */       throw new SecurityException(sm.getString("persistentProviderRegistrations.moveFail", new Object[] {configFileNew
/* 156 */         .getAbsolutePath(), configFile.getAbsolutePath() }));
/*     */     }
/*     */     
/*     */ 
/* 160 */     if ((configFileOld.exists()) && (!configFileOld.delete())) {
/* 161 */       log.warn(sm.getString("persistentProviderRegistrations.deleteFail", new Object[] {configFileOld
/* 162 */         .getAbsolutePath() }));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void writeOptional(String name, String value, Writer writer) throws IOException
/*     */   {
/* 168 */     if (value != null) {
/* 169 */       writer.write(" " + name + "=\"");
/* 170 */       writer.write(value);
/* 171 */       writer.write("\"");
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Providers
/*     */   {
/* 177 */     private final List<PersistentProviderRegistrations.Provider> providers = new ArrayList();
/*     */     
/*     */     public void addProvider(PersistentProviderRegistrations.Provider provider) {
/* 180 */       this.providers.add(provider);
/*     */     }
/*     */     
/*     */     public List<PersistentProviderRegistrations.Provider> getProviders() {
/* 184 */       return this.providers;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Provider
/*     */   {
/*     */     private String className;
/*     */     private String layer;
/*     */     private String appContext;
/*     */     private String description;
/* 194 */     private final Map<String, String> properties = new HashMap();
/*     */     
/*     */     public String getClassName()
/*     */     {
/* 198 */       return this.className;
/*     */     }
/*     */     
/* 201 */     public void setClassName(String className) { this.className = className; }
/*     */     
/*     */ 
/*     */     public String getLayer()
/*     */     {
/* 206 */       return this.layer;
/*     */     }
/*     */     
/* 209 */     public void setLayer(String layer) { this.layer = layer; }
/*     */     
/*     */ 
/*     */     public String getAppContext()
/*     */     {
/* 214 */       return this.appContext;
/*     */     }
/*     */     
/* 217 */     public void setAppContext(String appContext) { this.appContext = appContext; }
/*     */     
/*     */ 
/*     */     public String getDescription()
/*     */     {
/* 222 */       return this.description;
/*     */     }
/*     */     
/* 225 */     public void setDescription(String description) { this.description = description; }
/*     */     
/*     */ 
/*     */     public void addProperty(PersistentProviderRegistrations.Property property)
/*     */     {
/* 230 */       this.properties.put(property.getName(), property.getValue());
/*     */     }
/*     */     
/* 233 */     void addProperty(String name, String value) { this.properties.put(name, value); }
/*     */     
/*     */     public Map<String, String> getProperties() {
/* 236 */       return this.properties;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Property
/*     */   {
/*     */     private String name;
/*     */     private String value;
/*     */     
/*     */     public String getName()
/*     */     {
/* 247 */       return this.name;
/*     */     }
/*     */     
/* 250 */     public void setName(String name) { this.name = name; }
/*     */     
/*     */ 
/*     */     public String getValue()
/*     */     {
/* 255 */       return this.value;
/*     */     }
/*     */     
/* 258 */     public void setValue(String value) { this.value = value; }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\PersistentProviderRegistrations.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */