/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.Part;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItem;
/*     */ import org.apache.tomcat.util.http.fileupload.FileItemHeaders;
/*     */ import org.apache.tomcat.util.http.fileupload.ParameterParser;
/*     */ import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
/*     */ import org.apache.tomcat.util.http.parser.HttpParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ApplicationPart
/*     */   implements Part
/*     */ {
/*     */   private final FileItem fileItem;
/*     */   private final File location;
/*     */   
/*     */   public ApplicationPart(FileItem fileItem, File location)
/*     */   {
/*  48 */     this.fileItem = fileItem;
/*  49 */     this.location = location;
/*     */   }
/*     */   
/*     */   public void delete() throws IOException
/*     */   {
/*  54 */     this.fileItem.delete();
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/*  59 */     return this.fileItem.getContentType();
/*     */   }
/*     */   
/*     */   public String getHeader(String name)
/*     */   {
/*  64 */     if ((this.fileItem instanceof DiskFileItem)) {
/*  65 */       return ((DiskFileItem)this.fileItem).getHeaders().getHeader(name);
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public Collection<String> getHeaderNames()
/*     */   {
/*  72 */     if ((this.fileItem instanceof DiskFileItem)) {
/*  73 */       LinkedHashSet<String> headerNames = new LinkedHashSet();
/*     */       
/*  75 */       Iterator<String> iter = ((DiskFileItem)this.fileItem).getHeaders().getHeaderNames();
/*  76 */       while (iter.hasNext()) {
/*  77 */         headerNames.add(iter.next());
/*     */       }
/*  79 */       return headerNames;
/*     */     }
/*  81 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public Collection<String> getHeaders(String name)
/*     */   {
/*  86 */     if ((this.fileItem instanceof DiskFileItem)) {
/*  87 */       LinkedHashSet<String> headers = new LinkedHashSet();
/*     */       
/*  89 */       Iterator<String> iter = ((DiskFileItem)this.fileItem).getHeaders().getHeaders(name);
/*  90 */       while (iter.hasNext()) {
/*  91 */         headers.add(iter.next());
/*     */       }
/*  93 */       return headers;
/*     */     }
/*  95 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public InputStream getInputStream() throws IOException
/*     */   {
/* 100 */     return this.fileItem.getInputStream();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 105 */     return this.fileItem.getFieldName();
/*     */   }
/*     */   
/*     */   public long getSize()
/*     */   {
/* 110 */     return this.fileItem.getSize();
/*     */   }
/*     */   
/*     */   public void write(String fileName) throws IOException
/*     */   {
/* 115 */     File file = new File(fileName);
/* 116 */     if (!file.isAbsolute()) {
/* 117 */       file = new File(this.location, fileName);
/*     */     }
/*     */     try {
/* 120 */       this.fileItem.write(file);
/*     */     } catch (Exception e) {
/* 122 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getString(String encoding) throws UnsupportedEncodingException {
/* 127 */     return this.fileItem.getString(encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSubmittedFileName()
/*     */   {
/* 135 */     String fileName = null;
/* 136 */     String cd = getHeader("Content-Disposition");
/* 137 */     if (cd != null) {
/* 138 */       String cdl = cd.toLowerCase(Locale.ENGLISH);
/* 139 */       if ((cdl.startsWith("form-data")) || (cdl.startsWith("attachment"))) {
/* 140 */         ParameterParser paramParser = new ParameterParser();
/* 141 */         paramParser.setLowerCaseNames(true);
/*     */         
/* 143 */         Map<String, String> params = paramParser.parse(cd, ';');
/* 144 */         if (params.containsKey("filename")) {
/* 145 */           fileName = (String)params.get("filename");
/*     */           
/*     */ 
/* 148 */           if (fileName != null)
/*     */           {
/* 150 */             if (fileName.indexOf('\\') > -1)
/*     */             {
/* 152 */               fileName = HttpParser.unquote(fileName.trim());
/*     */             }
/*     */             else {
/* 155 */               fileName = fileName.trim();
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           else {
/* 161 */             fileName = "";
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 166 */     return fileName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ApplicationPart.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */