/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompressionConfig
/*     */ {
/*  29 */   private int compressionLevel = 0;
/*  30 */   private Pattern noCompressionUserAgents = null;
/*  31 */   private String compressibleMimeType = "text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json,application/xml";
/*     */   
/*  33 */   private String[] compressibleMimeTypes = null;
/*  34 */   private int compressionMinSize = 2048;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCompression(String compression)
/*     */   {
/*  45 */     if (compression.equals("on")) {
/*  46 */       this.compressionLevel = 1;
/*  47 */     } else if (compression.equals("force")) {
/*  48 */       this.compressionLevel = 2;
/*  49 */     } else if (compression.equals("off")) {
/*  50 */       this.compressionLevel = 0;
/*     */     }
/*     */     else {
/*     */       try
/*     */       {
/*  55 */         setCompressionMinSize(Integer.parseInt(compression));
/*  56 */         this.compressionLevel = 1;
/*     */       } catch (Exception e) {
/*  58 */         this.compressionLevel = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCompression()
/*     */   {
/*  70 */     switch (this.compressionLevel) {
/*     */     case 0: 
/*  72 */       return "off";
/*     */     case 1: 
/*  74 */       return "on";
/*     */     case 2: 
/*  76 */       return "force";
/*     */     }
/*  78 */     return "off";
/*     */   }
/*     */   
/*     */   public int getCompressionLevel()
/*     */   {
/*  83 */     return this.compressionLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getNoCompressionUserAgents()
/*     */   {
/*  94 */     if (this.noCompressionUserAgents == null) {
/*  95 */       return null;
/*     */     }
/*  97 */     return this.noCompressionUserAgents.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public Pattern getNoCompressionUserAgentsPattern()
/*     */   {
/* 103 */     return this.noCompressionUserAgents;
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
/*     */   public void setNoCompressionUserAgents(String noCompressionUserAgents)
/*     */   {
/* 116 */     if ((noCompressionUserAgents == null) || (noCompressionUserAgents.length() == 0)) {
/* 117 */       this.noCompressionUserAgents = null;
/*     */     }
/*     */     else {
/* 120 */       this.noCompressionUserAgents = Pattern.compile(noCompressionUserAgents);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getCompressibleMimeType()
/*     */   {
/* 126 */     return this.compressibleMimeType;
/*     */   }
/*     */   
/*     */   public void setCompressibleMimeType(String valueS)
/*     */   {
/* 131 */     this.compressibleMimeType = valueS;
/* 132 */     this.compressibleMimeTypes = null;
/*     */   }
/*     */   
/*     */   public String[] getCompressibleMimeTypes()
/*     */   {
/* 137 */     String[] result = this.compressibleMimeTypes;
/* 138 */     if (result != null) {
/* 139 */       return result;
/*     */     }
/* 141 */     List<String> values = new ArrayList();
/* 142 */     StringTokenizer tokens = new StringTokenizer(this.compressibleMimeType, ",");
/* 143 */     while (tokens.hasMoreTokens()) {
/* 144 */       String token = tokens.nextToken().trim();
/* 145 */       if (token.length() > 0) {
/* 146 */         values.add(token);
/*     */       }
/*     */     }
/* 149 */     result = (String[])values.toArray(new String[values.size()]);
/* 150 */     this.compressibleMimeTypes = result;
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   public int getCompressionMinSize()
/*     */   {
/* 156 */     return this.compressionMinSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCompressionMinSize(int compressionMinSize)
/*     */   {
/* 167 */     this.compressionMinSize = compressionMinSize;
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
/*     */   public boolean useCompression(Request request, Response response)
/*     */   {
/* 183 */     if (this.compressionLevel == 0) {
/* 184 */       return false;
/*     */     }
/*     */     
/* 187 */     MimeHeaders responseHeaders = response.getMimeHeaders();
/*     */     
/*     */ 
/* 190 */     MessageBytes contentEncodingMB = responseHeaders.getValue("Content-Encoding");
/* 191 */     if ((contentEncodingMB != null) && (contentEncodingMB.indexOf("gzip") != -1)) {
/* 192 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 196 */     if (this.compressionLevel != 2)
/*     */     {
/* 198 */       long contentLength = response.getContentLengthLong();
/* 199 */       if ((contentLength != -1L) && (contentLength < this.compressionMinSize)) {
/* 200 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 204 */       String[] compressibleMimeTypes = this.compressibleMimeTypes;
/* 205 */       if ((compressibleMimeTypes != null) && 
/* 206 */         (!startsWithStringArray(compressibleMimeTypes, response.getContentType()))) {
/* 207 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 213 */     MessageBytes vary = responseHeaders.getValue("Vary");
/* 214 */     if (vary == null)
/*     */     {
/* 216 */       responseHeaders.setValue("Vary").setString("Accept-Encoding");
/* 217 */     } else if (!vary.equals("*"))
/*     */     {
/*     */ 
/*     */ 
/* 221 */       responseHeaders.setValue("Vary").setString(vary.getString() + ",Accept-Encoding");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 226 */     MessageBytes acceptEncodingMB = request.getMimeHeaders().getValue("accept-encoding");
/* 227 */     if ((acceptEncodingMB == null) || (acceptEncodingMB.indexOf("gzip") == -1)) {
/* 228 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 232 */     if (this.compressionLevel != 2)
/*     */     {
/* 234 */       Pattern noCompressionUserAgents = this.noCompressionUserAgents;
/* 235 */       if (noCompressionUserAgents != null) {
/* 236 */         MessageBytes userAgentValueMB = request.getMimeHeaders().getValue("user-agent");
/* 237 */         if (userAgentValueMB != null) {
/* 238 */           String userAgentValue = userAgentValueMB.toString();
/* 239 */           if (noCompressionUserAgents.matcher(userAgentValue).matches()) {
/* 240 */             return false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 249 */     response.setContentLength(-1L);
/*     */     
/* 251 */     responseHeaders.setValue("Content-Encoding").setString("gzip");
/*     */     
/* 253 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean startsWithStringArray(String[] sArray, String value)
/*     */   {
/* 264 */     if (value == null) {
/* 265 */       return false;
/*     */     }
/* 267 */     for (int i = 0; i < sArray.length; i++) {
/* 268 */       if (value.startsWith(sArray[i])) {
/* 269 */         return true;
/*     */       }
/*     */     }
/* 272 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\CompressionConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */