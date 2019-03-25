/*     */ package org.apache.tomcat.util.http.fileupload.servlet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItem;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItemFactory;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItemIterator;
/*     */ import org.apache.tomcat.util.http.fileupload.FileUpload;
/*     */ import org.apache.tomcat.util.http.fileupload.FileUploadBase;
/*     */ import org.apache.tomcat.util.http.fileupload.FileUploadException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletFileUpload
/*     */   extends FileUpload
/*     */ {
/*     */   private static final String POST_METHOD = "POST";
/*     */   
/*     */   public static final boolean isMultipartContent(HttpServletRequest request)
/*     */   {
/*  67 */     if (!"POST".equalsIgnoreCase(request.getMethod())) {
/*  68 */       return false;
/*     */     }
/*  70 */     return FileUploadBase.isMultipartContent(new ServletRequestContext(request));
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
/*     */   public ServletFileUpload() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServletFileUpload(FileItemFactory fileItemFactory)
/*     */   {
/*  94 */     super(fileItemFactory);
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
/*     */   public Map<String, List<FileItem>> parseParameterMap(HttpServletRequest request)
/*     */     throws FileUploadException
/*     */   {
/* 114 */     return parseParameterMap(new ServletRequestContext(request));
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
/*     */   public FileItemIterator getItemIterator(HttpServletRequest request)
/*     */     throws FileUploadException, IOException
/*     */   {
/* 135 */     return super.getItemIterator(new ServletRequestContext(request));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\servlet\ServletFileUpload.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */