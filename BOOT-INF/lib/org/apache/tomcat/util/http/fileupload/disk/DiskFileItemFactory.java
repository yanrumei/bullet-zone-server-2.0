/*     */ package org.apache.tomcat.util.http.fileupload.disk;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItem;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItemFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiskFileItemFactory
/*     */   implements FileItemFactory
/*     */ {
/*     */   public static final int DEFAULT_SIZE_THRESHOLD = 10240;
/*     */   private File repository;
/*  78 */   private int sizeThreshold = 10240;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private String defaultCharset = "ISO-8859-1";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskFileItemFactory()
/*     */   {
/*  93 */     this(10240, null);
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
/*     */   public DiskFileItemFactory(int sizeThreshold, File repository)
/*     */   {
/* 107 */     this.sizeThreshold = sizeThreshold;
/* 108 */     this.repository = repository;
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
/*     */   public File getRepository()
/*     */   {
/* 123 */     return this.repository;
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
/*     */   public void setRepository(File repository)
/*     */   {
/* 136 */     this.repository = repository;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSizeThreshold()
/*     */   {
/* 148 */     return this.sizeThreshold;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSizeThreshold(int sizeThreshold)
/*     */   {
/* 160 */     this.sizeThreshold = sizeThreshold;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName)
/*     */   {
/* 182 */     DiskFileItem result = new DiskFileItem(fieldName, contentType, isFormField, fileName, this.sizeThreshold, this.repository);
/*     */     
/* 184 */     result.setDefaultCharset(this.defaultCharset);
/* 185 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultCharset()
/*     */   {
/* 194 */     return this.defaultCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultCharset(String pCharset)
/*     */   {
/* 203 */     this.defaultCharset = pCharset;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\disk\DiskFileItemFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */