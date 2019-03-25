/*     */ package org.apache.catalina.session;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.juli.logging.Log;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FileStore
/*     */   extends StoreBase
/*     */ {
/*     */   private static final String FILE_EXT = ".session";
/*  60 */   private String directory = ".";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private File directoryFile = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String storeName = "fileStore";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String threadName = "FileStore";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDirectory()
/*     */   {
/*  87 */     return this.directory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDirectory(String path)
/*     */   {
/*  97 */     String oldDirectory = this.directory;
/*  98 */     this.directory = path;
/*  99 */     this.directoryFile = null;
/* 100 */     this.support.firePropertyChange("directory", oldDirectory, this.directory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getThreadName()
/*     */   {
/* 108 */     return "FileStore";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStoreName()
/*     */   {
/* 117 */     return "fileStore";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSize()
/*     */     throws IOException
/*     */   {
/* 129 */     File file = directory();
/* 130 */     if (file == null) {
/* 131 */       return 0;
/*     */     }
/* 133 */     String[] files = file.list();
/*     */     
/*     */ 
/* 136 */     int keycount = 0;
/* 137 */     if (files != null) {
/* 138 */       for (int i = 0; i < files.length; i++) {
/* 139 */         if (files[i].endsWith(".session")) {
/* 140 */           keycount++;
/*     */         }
/*     */       }
/*     */     }
/* 144 */     return keycount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */     throws IOException
/*     */   {
/* 157 */     String[] keys = keys();
/* 158 */     for (int i = 0; i < keys.length; i++) {
/* 159 */       remove(keys[i]);
/*     */     }
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
/*     */   public String[] keys()
/*     */     throws IOException
/*     */   {
/* 174 */     File file = directory();
/* 175 */     if (file == null) {
/* 176 */       return new String[0];
/*     */     }
/*     */     
/* 179 */     String[] files = file.list();
/*     */     
/*     */ 
/* 182 */     if ((files == null) || (files.length < 1)) {
/* 183 */       return new String[0];
/*     */     }
/*     */     
/*     */ 
/* 187 */     ArrayList<String> list = new ArrayList();
/* 188 */     int n = ".session".length();
/* 189 */     for (int i = 0; i < files.length; i++) {
/* 190 */       if (files[i].endsWith(".session")) {
/* 191 */         list.add(files[i].substring(0, files[i].length() - n));
/*     */       }
/*     */     }
/* 194 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Session load(String id)
/*     */     throws java.lang.ClassNotFoundException, IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial 26	org/apache/catalina/session/FileStore:file	(Ljava/lang/String;)Ljava/io/File;
/*     */     //   5: astore_2
/*     */     //   6: aload_2
/*     */     //   7: ifnonnull +5 -> 12
/*     */     //   10: aconst_null
/*     */     //   11: areturn
/*     */     //   12: aload_2
/*     */     //   13: invokevirtual 27	java/io/File:exists	()Z
/*     */     //   16: ifne +5 -> 21
/*     */     //   19: aconst_null
/*     */     //   20: areturn
/*     */     //   21: aload_0
/*     */     //   22: invokevirtual 28	org/apache/catalina/session/FileStore:getManager	()Lorg/apache/catalina/Manager;
/*     */     //   25: invokeinterface 29 1 0
/*     */     //   30: astore_3
/*     */     //   31: aload_3
/*     */     //   32: invokeinterface 30 1 0
/*     */     //   37: astore 4
/*     */     //   39: aload 4
/*     */     //   41: invokeinterface 31 1 0
/*     */     //   46: ifeq +53 -> 99
/*     */     //   49: aload 4
/*     */     //   51: getstatic 32	org/apache/catalina/session/FileStore:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   54: new 33	java/lang/StringBuilder
/*     */     //   57: dup
/*     */     //   58: invokespecial 34	java/lang/StringBuilder:<init>	()V
/*     */     //   61: aload_0
/*     */     //   62: invokevirtual 35	org/apache/catalina/session/FileStore:getStoreName	()Ljava/lang/String;
/*     */     //   65: invokevirtual 36	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   68: ldc 37
/*     */     //   70: invokevirtual 36	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   73: invokevirtual 38	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   76: iconst_2
/*     */     //   77: anewarray 39	java/lang/Object
/*     */     //   80: dup
/*     */     //   81: iconst_0
/*     */     //   82: aload_1
/*     */     //   83: aastore
/*     */     //   84: dup
/*     */     //   85: iconst_1
/*     */     //   86: aload_2
/*     */     //   87: invokevirtual 40	java/io/File:getAbsolutePath	()Ljava/lang/String;
/*     */     //   90: aastore
/*     */     //   91: invokevirtual 41	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   94: invokeinterface 42 2 0
/*     */     //   99: aload_3
/*     */     //   100: getstatic 43	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */     //   103: aconst_null
/*     */     //   104: invokeinterface 44 3 0
/*     */     //   109: astore 5
/*     */     //   111: new 45	java/io/FileInputStream
/*     */     //   114: dup
/*     */     //   115: aload_2
/*     */     //   116: invokevirtual 40	java/io/File:getAbsolutePath	()Ljava/lang/String;
/*     */     //   119: invokespecial 46	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
/*     */     //   122: astore 6
/*     */     //   124: aconst_null
/*     */     //   125: astore 7
/*     */     //   127: aload_0
/*     */     //   128: aload 6
/*     */     //   130: invokevirtual 47	org/apache/catalina/session/FileStore:getObjectInputStream	(Ljava/io/InputStream;)Ljava/io/ObjectInputStream;
/*     */     //   133: astore 8
/*     */     //   135: aconst_null
/*     */     //   136: astore 9
/*     */     //   138: aload_0
/*     */     //   139: getfield 48	org/apache/catalina/session/FileStore:manager	Lorg/apache/catalina/Manager;
/*     */     //   142: invokeinterface 49 1 0
/*     */     //   147: checkcast 50	org/apache/catalina/session/StandardSession
/*     */     //   150: astore 10
/*     */     //   152: aload 10
/*     */     //   154: aload 8
/*     */     //   156: invokevirtual 51	org/apache/catalina/session/StandardSession:readObjectData	(Ljava/io/ObjectInputStream;)V
/*     */     //   159: aload 10
/*     */     //   161: aload_0
/*     */     //   162: getfield 48	org/apache/catalina/session/FileStore:manager	Lorg/apache/catalina/Manager;
/*     */     //   165: invokevirtual 52	org/apache/catalina/session/StandardSession:setManager	(Lorg/apache/catalina/Manager;)V
/*     */     //   168: aload 10
/*     */     //   170: astore 11
/*     */     //   172: aload 8
/*     */     //   174: ifnull +33 -> 207
/*     */     //   177: aload 9
/*     */     //   179: ifnull +23 -> 202
/*     */     //   182: aload 8
/*     */     //   184: invokevirtual 53	java/io/ObjectInputStream:close	()V
/*     */     //   187: goto +20 -> 207
/*     */     //   190: astore 12
/*     */     //   192: aload 9
/*     */     //   194: aload 12
/*     */     //   196: invokevirtual 55	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   199: goto +8 -> 207
/*     */     //   202: aload 8
/*     */     //   204: invokevirtual 53	java/io/ObjectInputStream:close	()V
/*     */     //   207: aload 6
/*     */     //   209: ifnull +33 -> 242
/*     */     //   212: aload 7
/*     */     //   214: ifnull +23 -> 237
/*     */     //   217: aload 6
/*     */     //   219: invokevirtual 56	java/io/FileInputStream:close	()V
/*     */     //   222: goto +20 -> 242
/*     */     //   225: astore 12
/*     */     //   227: aload 7
/*     */     //   229: aload 12
/*     */     //   231: invokevirtual 55	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   234: goto +8 -> 242
/*     */     //   237: aload 6
/*     */     //   239: invokevirtual 56	java/io/FileInputStream:close	()V
/*     */     //   242: aload_3
/*     */     //   243: getstatic 43	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */     //   246: aload 5
/*     */     //   248: invokeinterface 57 3 0
/*     */     //   253: aload 11
/*     */     //   255: areturn
/*     */     //   256: astore 10
/*     */     //   258: aload 10
/*     */     //   260: astore 9
/*     */     //   262: aload 10
/*     */     //   264: athrow
/*     */     //   265: astore 13
/*     */     //   267: aload 8
/*     */     //   269: ifnull +33 -> 302
/*     */     //   272: aload 9
/*     */     //   274: ifnull +23 -> 297
/*     */     //   277: aload 8
/*     */     //   279: invokevirtual 53	java/io/ObjectInputStream:close	()V
/*     */     //   282: goto +20 -> 302
/*     */     //   285: astore 14
/*     */     //   287: aload 9
/*     */     //   289: aload 14
/*     */     //   291: invokevirtual 55	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   294: goto +8 -> 302
/*     */     //   297: aload 8
/*     */     //   299: invokevirtual 53	java/io/ObjectInputStream:close	()V
/*     */     //   302: aload 13
/*     */     //   304: athrow
/*     */     //   305: astore 8
/*     */     //   307: aload 8
/*     */     //   309: astore 7
/*     */     //   311: aload 8
/*     */     //   313: athrow
/*     */     //   314: astore 15
/*     */     //   316: aload 6
/*     */     //   318: ifnull +33 -> 351
/*     */     //   321: aload 7
/*     */     //   323: ifnull +23 -> 346
/*     */     //   326: aload 6
/*     */     //   328: invokevirtual 56	java/io/FileInputStream:close	()V
/*     */     //   331: goto +20 -> 351
/*     */     //   334: astore 16
/*     */     //   336: aload 7
/*     */     //   338: aload 16
/*     */     //   340: invokevirtual 55	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   343: goto +8 -> 351
/*     */     //   346: aload 6
/*     */     //   348: invokevirtual 56	java/io/FileInputStream:close	()V
/*     */     //   351: aload 15
/*     */     //   353: athrow
/*     */     //   354: astore 6
/*     */     //   356: aload 4
/*     */     //   358: invokeinterface 31 1 0
/*     */     //   363: ifeq +12 -> 375
/*     */     //   366: aload 4
/*     */     //   368: ldc 59
/*     */     //   370: invokeinterface 42 2 0
/*     */     //   375: aconst_null
/*     */     //   376: astore 7
/*     */     //   378: aload_3
/*     */     //   379: getstatic 43	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */     //   382: aload 5
/*     */     //   384: invokeinterface 57 3 0
/*     */     //   389: aload 7
/*     */     //   391: areturn
/*     */     //   392: astore 17
/*     */     //   394: aload_3
/*     */     //   395: getstatic 43	org/apache/catalina/Globals:IS_SECURITY_ENABLED	Z
/*     */     //   398: aload 5
/*     */     //   400: invokeinterface 57 3 0
/*     */     //   405: aload 17
/*     */     //   407: athrow
/*     */     // Line number table:
/*     */     //   Java source line #211	-> byte code offset #0
/*     */     //   Java source line #212	-> byte code offset #6
/*     */     //   Java source line #213	-> byte code offset #10
/*     */     //   Java source line #216	-> byte code offset #12
/*     */     //   Java source line #217	-> byte code offset #19
/*     */     //   Java source line #220	-> byte code offset #21
/*     */     //   Java source line #221	-> byte code offset #31
/*     */     //   Java source line #223	-> byte code offset #39
/*     */     //   Java source line #224	-> byte code offset #49
/*     */     //   Java source line #227	-> byte code offset #99
/*     */     //   Java source line #229	-> byte code offset #111
/*     */     //   Java source line #230	-> byte code offset #127
/*     */     //   Java source line #229	-> byte code offset #135
/*     */     //   Java source line #232	-> byte code offset #138
/*     */     //   Java source line #233	-> byte code offset #152
/*     */     //   Java source line #234	-> byte code offset #159
/*     */     //   Java source line #235	-> byte code offset #168
/*     */     //   Java source line #236	-> byte code offset #172
/*     */     //   Java source line #242	-> byte code offset #242
/*     */     //   Java source line #235	-> byte code offset #253
/*     */     //   Java source line #229	-> byte code offset #256
/*     */     //   Java source line #236	-> byte code offset #265
/*     */     //   Java source line #229	-> byte code offset #305
/*     */     //   Java source line #236	-> byte code offset #314
/*     */     //   Java source line #237	-> byte code offset #356
/*     */     //   Java source line #238	-> byte code offset #366
/*     */     //   Java source line #240	-> byte code offset #375
/*     */     //   Java source line #242	-> byte code offset #378
/*     */     //   Java source line #240	-> byte code offset #389
/*     */     //   Java source line #242	-> byte code offset #392
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	408	0	this	FileStore
/*     */     //   0	408	1	id	String
/*     */     //   5	111	2	file	File
/*     */     //   30	365	3	context	Context
/*     */     //   37	330	4	contextLog	Log
/*     */     //   109	290	5	oldThreadContextCL	ClassLoader
/*     */     //   122	225	6	fis	java.io.FileInputStream
/*     */     //   354	3	6	e	java.io.FileNotFoundException
/*     */     //   125	265	7	localThrowable6	Throwable
/*     */     //   133	165	8	ois	java.io.ObjectInputStream
/*     */     //   305	7	8	localThrowable4	Throwable
/*     */     //   136	152	9	localThrowable7	Throwable
/*     */     //   150	19	10	session	StandardSession
/*     */     //   256	7	10	localThrowable2	Throwable
/*     */     //   170	84	11	localStandardSession1	StandardSession
/*     */     //   190	5	12	localThrowable	Throwable
/*     */     //   225	5	12	localThrowable1	Throwable
/*     */     //   265	38	13	localObject1	Object
/*     */     //   285	5	14	localThrowable3	Throwable
/*     */     //   314	38	15	localObject2	Object
/*     */     //   334	5	16	localThrowable5	Throwable
/*     */     //   392	14	17	localObject3	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   182	187	190	java/lang/Throwable
/*     */     //   217	222	225	java/lang/Throwable
/*     */     //   138	172	256	java/lang/Throwable
/*     */     //   138	172	265	finally
/*     */     //   256	267	265	finally
/*     */     //   277	282	285	java/lang/Throwable
/*     */     //   127	207	305	java/lang/Throwable
/*     */     //   256	305	305	java/lang/Throwable
/*     */     //   127	207	314	finally
/*     */     //   256	316	314	finally
/*     */     //   326	331	334	java/lang/Throwable
/*     */     //   111	242	354	java/io/FileNotFoundException
/*     */     //   256	354	354	java/io/FileNotFoundException
/*     */     //   111	242	392	finally
/*     */     //   256	378	392	finally
/*     */     //   392	394	392	finally
/*     */   }
/*     */   
/*     */   public void remove(String id)
/*     */     throws IOException
/*     */   {
/* 258 */     File file = file(id);
/* 259 */     if (file == null) {
/* 260 */       return;
/*     */     }
/* 262 */     if (this.manager.getContext().getLogger().isDebugEnabled()) {
/* 263 */       this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".removing", new Object[] { id, file
/* 264 */         .getAbsolutePath() }));
/*     */     }
/* 266 */     file.delete();
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
/*     */   public void save(Session session)
/*     */     throws IOException
/*     */   {
/* 281 */     File file = file(session.getIdInternal());
/* 282 */     if (file == null) {
/* 283 */       return;
/*     */     }
/* 285 */     if (this.manager.getContext().getLogger().isDebugEnabled()) {
/* 286 */       this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".saving", new Object[] {session
/* 287 */         .getIdInternal(), file.getAbsolutePath() }));
/*     */     }
/*     */     
/* 290 */     FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());Throwable localThrowable6 = null;
/* 291 */     try { ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));Throwable localThrowable7 = null;
/* 292 */       try { ((StandardSession)session).writeObjectData(oos);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 290 */         localThrowable7 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
/*     */     }
/*     */     finally {
/* 293 */       if (fos != null) { if (localThrowable6 != null) try { fos.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else { fos.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private File directory()
/*     */     throws IOException
/*     */   {
/* 305 */     if (this.directory == null) {
/* 306 */       return null;
/*     */     }
/* 308 */     if (this.directoryFile != null)
/*     */     {
/* 310 */       return this.directoryFile;
/*     */     }
/* 312 */     File file = new File(this.directory);
/* 313 */     if (!file.isAbsolute()) {
/* 314 */       Context context = this.manager.getContext();
/* 315 */       ServletContext servletContext = context.getServletContext();
/* 316 */       File work = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
/* 317 */       file = new File(work, this.directory);
/*     */     }
/* 319 */     if ((!file.exists()) || (!file.isDirectory())) {
/* 320 */       if ((!file.delete()) && (file.exists())) {
/* 321 */         throw new IOException(sm.getString("fileStore.deleteFailed", new Object[] { file }));
/*     */       }
/* 323 */       if ((!file.mkdirs()) && (!file.isDirectory())) {
/* 324 */         throw new IOException(sm.getString("fileStore.createFailed", new Object[] { file }));
/*     */       }
/*     */     }
/* 327 */     this.directoryFile = file;
/* 328 */     return file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private File file(String id)
/*     */     throws IOException
/*     */   {
/* 340 */     if (this.directory == null) {
/* 341 */       return null;
/*     */     }
/* 343 */     String filename = id + ".session";
/* 344 */     File file = new File(directory(), filename);
/* 345 */     return file;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\session\FileStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */