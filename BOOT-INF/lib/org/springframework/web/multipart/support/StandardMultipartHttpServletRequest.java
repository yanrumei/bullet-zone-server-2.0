/*     */ package org.springframework.web.multipart.support;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardMultipartHttpServletRequest
/*     */   extends AbstractMultipartHttpServletRequest
/*     */ {
/*     */   private static final String CONTENT_DISPOSITION = "content-disposition";
/*     */   private static final String FILENAME_KEY = "filename=";
/*     */   private static final String FILENAME_WITH_CHARSET_KEY = "filename*=";
/*  60 */   private static final Charset US_ASCII = Charset.forName("us-ascii");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<String> multipartParameterNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardMultipartHttpServletRequest(HttpServletRequest request)
/*     */     throws MultipartException
/*     */   {
/*  73 */     this(request, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardMultipartHttpServletRequest(HttpServletRequest request, boolean lazyParsing)
/*     */     throws MultipartException
/*     */   {
/*  84 */     super(request);
/*  85 */     if (!lazyParsing) {
/*  86 */       parseRequest(request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseRequest(HttpServletRequest request)
/*     */   {
/*     */     try {
/*  93 */       Collection<Part> parts = request.getParts();
/*  94 */       this.multipartParameterNames = new LinkedHashSet(parts.size());
/*  95 */       MultiValueMap<String, MultipartFile> files = new LinkedMultiValueMap(parts.size());
/*  96 */       for (Part part : parts) {
/*  97 */         String disposition = part.getHeader("content-disposition");
/*  98 */         String filename = extractFilename(disposition);
/*  99 */         if (filename == null) {
/* 100 */           filename = extractFilenameWithCharset(disposition);
/*     */         }
/* 102 */         if (filename != null) {
/* 103 */           files.add(part.getName(), new StandardMultipartFile(part, filename));
/*     */         }
/*     */         else {
/* 106 */           this.multipartParameterNames.add(part.getName());
/*     */         }
/*     */       }
/* 109 */       setMultipartFiles(files);
/*     */     }
/*     */     catch (Throwable ex) {
/* 112 */       throw new MultipartException("Could not parse multipart servlet request", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private String extractFilename(String contentDisposition, String key) {
/* 117 */     if (contentDisposition == null) {
/* 118 */       return null;
/*     */     }
/* 120 */     int startIndex = contentDisposition.indexOf(key);
/* 121 */     if (startIndex == -1) {
/* 122 */       return null;
/*     */     }
/* 124 */     String filename = contentDisposition.substring(startIndex + key.length());
/* 125 */     if (filename.startsWith("\"")) {
/* 126 */       int endIndex = filename.indexOf("\"", 1);
/* 127 */       if (endIndex != -1) {
/* 128 */         return filename.substring(1, endIndex);
/*     */       }
/*     */     }
/*     */     else {
/* 132 */       int endIndex = filename.indexOf(";");
/* 133 */       if (endIndex != -1) {
/* 134 */         return filename.substring(0, endIndex);
/*     */       }
/*     */     }
/* 137 */     return filename;
/*     */   }
/*     */   
/*     */   private String extractFilename(String contentDisposition) {
/* 141 */     return extractFilename(contentDisposition, "filename=");
/*     */   }
/*     */   
/*     */   private String extractFilenameWithCharset(String contentDisposition) {
/* 145 */     String filename = extractFilename(contentDisposition, "filename*=");
/* 146 */     if (filename == null) {
/* 147 */       return null;
/*     */     }
/* 149 */     int index = filename.indexOf("'");
/* 150 */     if (index != -1) {
/* 151 */       Charset charset = null;
/*     */       try {
/* 153 */         charset = Charset.forName(filename.substring(0, index));
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*     */       
/*     */ 
/* 158 */       filename = filename.substring(index + 1);
/*     */       
/* 160 */       index = filename.indexOf("'");
/* 161 */       if (index != -1) {
/* 162 */         filename = filename.substring(index + 1);
/*     */       }
/* 164 */       if (charset != null) {
/* 165 */         filename = new String(filename.getBytes(US_ASCII), charset);
/*     */       }
/*     */     }
/* 168 */     return filename;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initializeMultipart()
/*     */   {
/* 174 */     parseRequest(getRequest());
/*     */   }
/*     */   
/*     */   public Enumeration<String> getParameterNames()
/*     */   {
/* 179 */     if (this.multipartParameterNames == null) {
/* 180 */       initializeMultipart();
/*     */     }
/* 182 */     if (this.multipartParameterNames.isEmpty()) {
/* 183 */       return super.getParameterNames();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 188 */     Set<String> paramNames = new LinkedHashSet();
/* 189 */     Enumeration<String> paramEnum = super.getParameterNames();
/* 190 */     while (paramEnum.hasMoreElements()) {
/* 191 */       paramNames.add(paramEnum.nextElement());
/*     */     }
/* 193 */     paramNames.addAll(this.multipartParameterNames);
/* 194 */     return Collections.enumeration(paramNames);
/*     */   }
/*     */   
/*     */   public Map<String, String[]> getParameterMap()
/*     */   {
/* 199 */     if (this.multipartParameterNames == null) {
/* 200 */       initializeMultipart();
/*     */     }
/* 202 */     if (this.multipartParameterNames.isEmpty()) {
/* 203 */       return super.getParameterMap();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 208 */     Map<String, String[]> paramMap = new LinkedHashMap();
/* 209 */     paramMap.putAll(super.getParameterMap());
/* 210 */     for (String paramName : this.multipartParameterNames) {
/* 211 */       if (!paramMap.containsKey(paramName)) {
/* 212 */         paramMap.put(paramName, getParameterValues(paramName));
/*     */       }
/*     */     }
/* 215 */     return paramMap;
/*     */   }
/*     */   
/*     */   public String getMultipartContentType(String paramOrFileName)
/*     */   {
/*     */     try {
/* 221 */       Part part = getPart(paramOrFileName);
/* 222 */       return part != null ? part.getContentType() : null;
/*     */     }
/*     */     catch (Throwable ex) {
/* 225 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public HttpHeaders getMultipartHeaders(String paramOrFileName)
/*     */   {
/*     */     try {
/* 232 */       Part part = getPart(paramOrFileName);
/* 233 */       if (part != null) {
/* 234 */         HttpHeaders headers = new HttpHeaders();
/* 235 */         for (String headerName : part.getHeaderNames()) {
/* 236 */           headers.put(headerName, new ArrayList(part.getHeaders(headerName)));
/*     */         }
/* 238 */         return headers;
/*     */       }
/*     */       
/* 241 */       return null;
/*     */     }
/*     */     catch (Throwable ex)
/*     */     {
/* 245 */       throw new MultipartException("Could not access multipart servlet request", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class StandardMultipartFile
/*     */     implements MultipartFile, Serializable
/*     */   {
/*     */     private final Part part;
/*     */     
/*     */     private final String filename;
/*     */     
/*     */ 
/*     */     public StandardMultipartFile(Part part, String filename)
/*     */     {
/* 261 */       this.part = part;
/* 262 */       this.filename = filename;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 267 */       return this.part.getName();
/*     */     }
/*     */     
/*     */     public String getOriginalFilename()
/*     */     {
/* 272 */       return this.filename;
/*     */     }
/*     */     
/*     */     public String getContentType()
/*     */     {
/* 277 */       return this.part.getContentType();
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 282 */       return this.part.getSize() == 0L;
/*     */     }
/*     */     
/*     */     public long getSize()
/*     */     {
/* 287 */       return this.part.getSize();
/*     */     }
/*     */     
/*     */     public byte[] getBytes() throws IOException
/*     */     {
/* 292 */       return FileCopyUtils.copyToByteArray(this.part.getInputStream());
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException
/*     */     {
/* 297 */       return this.part.getInputStream();
/*     */     }
/*     */     
/*     */     public void transferTo(File dest) throws IOException, IllegalStateException
/*     */     {
/* 302 */       this.part.write(dest.getPath());
/* 303 */       if ((dest.isAbsolute()) && (!dest.exists()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 310 */         FileCopyUtils.copy(this.part.getInputStream(), new FileOutputStream(dest));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\multipart\support\StandardMultipartHttpServletRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */