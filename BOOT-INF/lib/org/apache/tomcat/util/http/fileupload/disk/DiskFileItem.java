/*     */ package org.apache.tomcat.util.http.fileupload.disk;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.tomcat.util.http.fileupload.DeferredFileOutputStream;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItem;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItemHeaders;
/*     */ import org.apache.tomcat.util.http.fileupload.IOUtils;
/*     */ import org.apache.tomcat.util.http.fileupload.ParameterParser;
/*     */ import org.apache.tomcat.util.http.fileupload.util.Streams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiskFileItem
/*     */   implements FileItem
/*     */ {
/*     */   public static final String DEFAULT_CHARSET = "ISO-8859-1";
/*  78 */   private static final String UID = UUID.randomUUID().toString().replace('-', '_');
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  83 */   private static final AtomicInteger COUNTER = new AtomicInteger(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String fieldName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String contentType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isFormField;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String fileName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 110 */   private long size = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int sizeThreshold;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final File repository;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private byte[] cachedContent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient DeferredFileOutputStream dfos;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient File tempFile;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private FileItemHeaders headers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 147 */   private String defaultCharset = "ISO-8859-1";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskFileItem(String fieldName, String contentType, boolean isFormField, String fileName, int sizeThreshold, File repository)
/*     */   {
/* 171 */     this.fieldName = fieldName;
/* 172 */     this.contentType = contentType;
/* 173 */     this.isFormField = isFormField;
/* 174 */     this.fileName = fileName;
/* 175 */     this.sizeThreshold = sizeThreshold;
/* 176 */     this.repository = repository;
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
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/* 193 */     if (!isInMemory()) {
/* 194 */       return new FileInputStream(this.dfos.getFile());
/*     */     }
/*     */     
/* 197 */     if (this.cachedContent == null) {
/* 198 */       this.cachedContent = this.dfos.getData();
/*     */     }
/* 200 */     return new ByteArrayInputStream(this.cachedContent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 212 */     return this.contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCharSet()
/*     */   {
/* 223 */     ParameterParser parser = new ParameterParser();
/* 224 */     parser.setLowerCaseNames(true);
/*     */     
/* 226 */     Map<String, String> params = parser.parse(getContentType(), ';');
/* 227 */     return (String)params.get("charset");
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
/*     */   public String getName()
/*     */   {
/* 242 */     return Streams.checkFileName(this.fileName);
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
/*     */   public boolean isInMemory()
/*     */   {
/* 256 */     if (this.cachedContent != null) {
/* 257 */       return true;
/*     */     }
/* 259 */     return this.dfos.isInMemory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSize()
/*     */   {
/* 269 */     if (this.size >= 0L)
/* 270 */       return this.size;
/* 271 */     if (this.cachedContent != null)
/* 272 */       return this.cachedContent.length;
/* 273 */     if (this.dfos.isInMemory()) {
/* 274 */       return this.dfos.getData().length;
/*     */     }
/* 276 */     return this.dfos.getFile().length();
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
/*     */   public byte[] get()
/*     */   {
/* 290 */     if (isInMemory()) {
/* 291 */       if ((this.cachedContent == null) && (this.dfos != null)) {
/* 292 */         this.cachedContent = this.dfos.getData();
/*     */       }
/* 294 */       return this.cachedContent;
/*     */     }
/*     */     
/* 297 */     byte[] fileData = new byte[(int)getSize()];
/* 298 */     InputStream fis = null;
/*     */     try
/*     */     {
/* 301 */       fis = new FileInputStream(this.dfos.getFile());
/* 302 */       IOUtils.readFully(fis, fileData);
/*     */     } catch (IOException e) {
/* 304 */       fileData = null;
/*     */     } finally {
/* 306 */       IOUtils.closeQuietly(fis);
/*     */     }
/*     */     
/* 309 */     return fileData;
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
/*     */   public String getString(String charset)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 327 */     return new String(get(), charset);
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
/*     */   public String getString()
/*     */   {
/* 341 */     byte[] rawdata = get();
/* 342 */     String charset = getCharSet();
/* 343 */     if (charset == null) {
/* 344 */       charset = this.defaultCharset;
/*     */     }
/*     */     try {
/* 347 */       return new String(rawdata, charset);
/*     */     } catch (UnsupportedEncodingException e) {}
/* 349 */     return new String(rawdata);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void write(File file)
/*     */     throws java.lang.Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 14	org/apache/tomcat/util/http/fileupload/disk/DiskFileItem:isInMemory	()Z
/*     */     //   4: ifeq +43 -> 47
/*     */     //   7: aconst_null
/*     */     //   8: astore_2
/*     */     //   9: new 43	java/io/FileOutputStream
/*     */     //   12: dup
/*     */     //   13: aload_1
/*     */     //   14: invokespecial 44	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   17: astore_2
/*     */     //   18: aload_2
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual 38	org/apache/tomcat/util/http/fileupload/disk/DiskFileItem:get	()[B
/*     */     //   23: invokevirtual 45	java/io/FileOutputStream:write	([B)V
/*     */     //   26: aload_2
/*     */     //   27: invokevirtual 46	java/io/FileOutputStream:close	()V
/*     */     //   30: aload_2
/*     */     //   31: invokestatic 36	org/apache/tomcat/util/http/fileupload/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/*     */     //   34: goto +10 -> 44
/*     */     //   37: astore_3
/*     */     //   38: aload_2
/*     */     //   39: invokestatic 36	org/apache/tomcat/util/http/fileupload/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/*     */     //   42: aload_3
/*     */     //   43: athrow
/*     */     //   44: goto +117 -> 161
/*     */     //   47: aload_0
/*     */     //   48: invokevirtual 47	org/apache/tomcat/util/http/fileupload/disk/DiskFileItem:getStoreLocation	()Ljava/io/File;
/*     */     //   51: astore_2
/*     */     //   52: aload_2
/*     */     //   53: ifnull +98 -> 151
/*     */     //   56: aload_0
/*     */     //   57: aload_2
/*     */     //   58: invokevirtual 33	java/io/File:length	()J
/*     */     //   61: putfield 4	org/apache/tomcat/util/http/fileupload/disk/DiskFileItem:size	J
/*     */     //   64: aload_2
/*     */     //   65: aload_1
/*     */     //   66: invokevirtual 48	java/io/File:renameTo	(Ljava/io/File;)Z
/*     */     //   69: ifne +92 -> 161
/*     */     //   72: aconst_null
/*     */     //   73: astore_3
/*     */     //   74: aconst_null
/*     */     //   75: astore 4
/*     */     //   77: new 49	java/io/BufferedInputStream
/*     */     //   80: dup
/*     */     //   81: new 15	java/io/FileInputStream
/*     */     //   84: dup
/*     */     //   85: aload_2
/*     */     //   86: invokespecial 18	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   89: invokespecial 50	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
/*     */     //   92: astore_3
/*     */     //   93: new 51	java/io/BufferedOutputStream
/*     */     //   96: dup
/*     */     //   97: new 43	java/io/FileOutputStream
/*     */     //   100: dup
/*     */     //   101: aload_1
/*     */     //   102: invokespecial 44	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   105: invokespecial 52	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
/*     */     //   108: astore 4
/*     */     //   110: aload_3
/*     */     //   111: aload 4
/*     */     //   113: invokestatic 53	org/apache/tomcat/util/http/fileupload/IOUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)I
/*     */     //   116: pop
/*     */     //   117: aload 4
/*     */     //   119: invokevirtual 54	java/io/BufferedOutputStream:close	()V
/*     */     //   122: aload_3
/*     */     //   123: invokestatic 36	org/apache/tomcat/util/http/fileupload/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/*     */     //   126: aload 4
/*     */     //   128: invokestatic 36	org/apache/tomcat/util/http/fileupload/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/*     */     //   131: goto +17 -> 148
/*     */     //   134: astore 5
/*     */     //   136: aload_3
/*     */     //   137: invokestatic 36	org/apache/tomcat/util/http/fileupload/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/*     */     //   140: aload 4
/*     */     //   142: invokestatic 36	org/apache/tomcat/util/http/fileupload/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
/*     */     //   145: aload 5
/*     */     //   147: athrow
/*     */     //   148: goto +13 -> 161
/*     */     //   151: new 55	org/apache/tomcat/util/http/fileupload/FileUploadException
/*     */     //   154: dup
/*     */     //   155: ldc 56
/*     */     //   157: invokespecial 57	org/apache/tomcat/util/http/fileupload/FileUploadException:<init>	(Ljava/lang/String;)V
/*     */     //   160: athrow
/*     */     //   161: return
/*     */     // Line number table:
/*     */     //   Java source line #375	-> byte code offset #0
/*     */     //   Java source line #376	-> byte code offset #7
/*     */     //   Java source line #378	-> byte code offset #9
/*     */     //   Java source line #379	-> byte code offset #18
/*     */     //   Java source line #380	-> byte code offset #26
/*     */     //   Java source line #382	-> byte code offset #30
/*     */     //   Java source line #383	-> byte code offset #34
/*     */     //   Java source line #382	-> byte code offset #37
/*     */     //   Java source line #384	-> byte code offset #44
/*     */     //   Java source line #385	-> byte code offset #47
/*     */     //   Java source line #386	-> byte code offset #52
/*     */     //   Java source line #388	-> byte code offset #56
/*     */     //   Java source line #394	-> byte code offset #64
/*     */     //   Java source line #395	-> byte code offset #72
/*     */     //   Java source line #396	-> byte code offset #74
/*     */     //   Java source line #398	-> byte code offset #77
/*     */     //   Java source line #400	-> byte code offset #93
/*     */     //   Java source line #402	-> byte code offset #110
/*     */     //   Java source line #403	-> byte code offset #117
/*     */     //   Java source line #405	-> byte code offset #122
/*     */     //   Java source line #406	-> byte code offset #126
/*     */     //   Java source line #407	-> byte code offset #131
/*     */     //   Java source line #405	-> byte code offset #134
/*     */     //   Java source line #406	-> byte code offset #140
/*     */     //   Java source line #408	-> byte code offset #148
/*     */     //   Java source line #414	-> byte code offset #151
/*     */     //   Java source line #418	-> byte code offset #161
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	162	0	this	DiskFileItem
/*     */     //   0	162	1	file	File
/*     */     //   8	31	2	fout	java.io.FileOutputStream
/*     */     //   51	35	2	outputFile	File
/*     */     //   37	6	3	localObject1	Object
/*     */     //   73	64	3	in	java.io.BufferedInputStream
/*     */     //   75	66	4	out	java.io.BufferedOutputStream
/*     */     //   134	12	5	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   9	30	37	finally
/*     */     //   77	122	134	finally
/*     */     //   134	136	134	finally
/*     */   }
/*     */   
/*     */   public void delete()
/*     */   {
/* 429 */     this.cachedContent = null;
/* 430 */     File outputFile = getStoreLocation();
/* 431 */     if ((outputFile != null) && (!isInMemory()) && (outputFile.exists())) {
/* 432 */       outputFile.delete();
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
/*     */ 
/*     */   public String getFieldName()
/*     */   {
/* 447 */     return this.fieldName;
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
/*     */   public void setFieldName(String fieldName)
/*     */   {
/* 460 */     this.fieldName = fieldName;
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
/*     */   public boolean isFormField()
/*     */   {
/* 475 */     return this.isFormField;
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
/*     */   public void setFormField(boolean state)
/*     */   {
/* 490 */     this.isFormField = state;
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
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/* 505 */     if (this.dfos == null) {
/* 506 */       File outputFile = getTempFile();
/* 507 */       this.dfos = new DeferredFileOutputStream(this.sizeThreshold, outputFile);
/*     */     }
/* 509 */     return this.dfos;
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
/*     */ 
/*     */ 
/*     */   public File getStoreLocation()
/*     */   {
/* 528 */     if (this.dfos == null) {
/* 529 */       return null;
/*     */     }
/* 531 */     if (isInMemory()) {
/* 532 */       return null;
/*     */     }
/* 534 */     return this.dfos.getFile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void finalize()
/*     */   {
/* 544 */     if ((this.dfos == null) || (this.dfos.isInMemory())) {
/* 545 */       return;
/*     */     }
/* 547 */     File outputFile = this.dfos.getFile();
/*     */     
/* 549 */     if ((outputFile != null) && (outputFile.exists())) {
/* 550 */       outputFile.delete();
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
/*     */ 
/*     */ 
/*     */   protected File getTempFile()
/*     */   {
/* 566 */     if (this.tempFile == null) {
/* 567 */       File tempDir = this.repository;
/* 568 */       if (tempDir == null) {
/* 569 */         tempDir = new File(System.getProperty("java.io.tmpdir"));
/*     */       }
/*     */       
/*     */ 
/* 573 */       String tempFileName = String.format("upload_%s_%s.tmp", new Object[] { UID, getUniqueId() });
/*     */       
/* 575 */       this.tempFile = new File(tempDir, tempFileName);
/*     */     }
/* 577 */     return this.tempFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getUniqueId()
/*     */   {
/* 589 */     int limit = 100000000;
/* 590 */     int current = COUNTER.getAndIncrement();
/* 591 */     String id = Integer.toString(current);
/*     */     
/*     */ 
/*     */ 
/* 595 */     if (current < 100000000) {
/* 596 */       id = ("00000000" + id).substring(id.length());
/*     */     }
/* 598 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 608 */     return String.format("name=%s, StoreLocation=%s, size=%s bytes, isFormField=%s, FieldName=%s", new Object[] {
/* 609 */       getName(), getStoreLocation(), Long.valueOf(getSize()), 
/* 610 */       Boolean.valueOf(isFormField()), getFieldName() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileItemHeaders getHeaders()
/*     */   {
/* 619 */     return this.headers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeaders(FileItemHeaders pHeaders)
/*     */   {
/* 628 */     this.headers = pHeaders;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultCharset()
/*     */   {
/* 637 */     return this.defaultCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultCharset(String charset)
/*     */   {
/* 646 */     this.defaultCharset = charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\disk\DiskFileItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */