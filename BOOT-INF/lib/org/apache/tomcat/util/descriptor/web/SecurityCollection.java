/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.tomcat.util.buf.UDecoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecurityCollection
/*     */   extends XmlEncodingBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public SecurityCollection()
/*     */   {
/*  50 */     this(null, null);
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
/*     */   public SecurityCollection(String name, String description)
/*     */   {
/*  64 */     setName(name);
/*  65 */     setDescription(description);
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
/*  76 */   private String description = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   private String[] methods = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   private String[] omittedMethods = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private String name = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   private String[] patterns = new String[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */   private boolean isFromDescriptor = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 116 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/* 128 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 138 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 150 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFromDescriptor()
/*     */   {
/* 159 */     return this.isFromDescriptor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromDescriptor(boolean isFromDescriptor)
/*     */   {
/* 168 */     this.isFromDescriptor = isFromDescriptor;
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
/*     */   public void addMethod(String method)
/*     */   {
/* 182 */     if (method == null)
/* 183 */       return;
/* 184 */     String[] results = new String[this.methods.length + 1];
/* 185 */     for (int i = 0; i < this.methods.length; i++)
/* 186 */       results[i] = this.methods[i];
/* 187 */     results[this.methods.length] = method;
/* 188 */     this.methods = results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addOmittedMethod(String method)
/*     */   {
/* 199 */     if (method == null)
/* 200 */       return;
/* 201 */     String[] results = new String[this.omittedMethods.length + 1];
/* 202 */     for (int i = 0; i < this.omittedMethods.length; i++)
/* 203 */       results[i] = this.omittedMethods[i];
/* 204 */     results[this.omittedMethods.length] = method;
/* 205 */     this.omittedMethods = results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPattern(String pattern)
/*     */   {
/* 213 */     addPatternDecoded(UDecoder.URLDecode(pattern, StandardCharsets.UTF_8));
/*     */   }
/*     */   
/*     */   public void addPatternDecoded(String pattern) {
/* 217 */     if (pattern == null) {
/* 218 */       return;
/*     */     }
/* 220 */     String decodedPattern = UDecoder.URLDecode(pattern);
/* 221 */     String[] results = new String[this.patterns.length + 1];
/* 222 */     for (int i = 0; i < this.patterns.length; i++) {
/* 223 */       results[i] = this.patterns[i];
/*     */     }
/* 225 */     results[this.patterns.length] = decodedPattern;
/* 226 */     this.patterns = results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean findMethod(String method)
/*     */   {
/* 238 */     if ((this.methods.length == 0) && (this.omittedMethods.length == 0))
/* 239 */       return true;
/* 240 */     if (this.methods.length > 0) {
/* 241 */       for (int i = 0; i < this.methods.length; i++) {
/* 242 */         if (this.methods[i].equals(method))
/* 243 */           return true;
/*     */       }
/* 245 */       return false;
/*     */     }
/* 247 */     if (this.omittedMethods.length > 0) {
/* 248 */       for (int i = 0; i < this.omittedMethods.length; i++) {
/* 249 */         if (this.omittedMethods[i].equals(method))
/* 250 */           return false;
/*     */       }
/*     */     }
/* 253 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findMethods()
/*     */   {
/* 264 */     return this.methods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findOmittedMethods()
/*     */   {
/* 276 */     return this.omittedMethods;
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
/*     */   public boolean findPattern(String pattern)
/*     */   {
/* 289 */     for (int i = 0; i < this.patterns.length; i++) {
/* 290 */       if (this.patterns[i].equals(pattern))
/* 291 */         return true;
/*     */     }
/* 293 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] findPatterns()
/*     */   {
/* 305 */     return this.patterns;
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
/*     */   public void removeMethod(String method)
/*     */   {
/* 318 */     if (method == null)
/* 319 */       return;
/* 320 */     int n = -1;
/* 321 */     for (int i = 0; i < this.methods.length; i++) {
/* 322 */       if (this.methods[i].equals(method)) {
/* 323 */         n = i;
/* 324 */         break;
/*     */       }
/*     */     }
/* 327 */     if (n >= 0) {
/* 328 */       int j = 0;
/* 329 */       String[] results = new String[this.methods.length - 1];
/* 330 */       for (int i = 0; i < this.methods.length; i++) {
/* 331 */         if (i != n)
/* 332 */           results[(j++)] = this.methods[i];
/*     */       }
/* 334 */       this.methods = results;
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
/*     */   public void removeOmittedMethod(String method)
/*     */   {
/* 348 */     if (method == null)
/* 349 */       return;
/* 350 */     int n = -1;
/* 351 */     for (int i = 0; i < this.omittedMethods.length; i++) {
/* 352 */       if (this.omittedMethods[i].equals(method)) {
/* 353 */         n = i;
/* 354 */         break;
/*     */       }
/*     */     }
/* 357 */     if (n >= 0) {
/* 358 */       int j = 0;
/* 359 */       String[] results = new String[this.omittedMethods.length - 1];
/* 360 */       for (int i = 0; i < this.omittedMethods.length; i++) {
/* 361 */         if (i != n)
/* 362 */           results[(j++)] = this.omittedMethods[i];
/*     */       }
/* 364 */       this.omittedMethods = results;
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
/*     */   public void removePattern(String pattern)
/*     */   {
/* 378 */     if (pattern == null)
/* 379 */       return;
/* 380 */     int n = -1;
/* 381 */     for (int i = 0; i < this.patterns.length; i++) {
/* 382 */       if (this.patterns[i].equals(pattern)) {
/* 383 */         n = i;
/* 384 */         break;
/*     */       }
/*     */     }
/* 387 */     if (n >= 0) {
/* 388 */       int j = 0;
/* 389 */       String[] results = new String[this.patterns.length - 1];
/* 390 */       for (int i = 0; i < this.patterns.length; i++) {
/* 391 */         if (i != n)
/* 392 */           results[(j++)] = this.patterns[i];
/*     */       }
/* 394 */       this.patterns = results;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 406 */     StringBuilder sb = new StringBuilder("SecurityCollection[");
/* 407 */     sb.append(this.name);
/* 408 */     if (this.description != null) {
/* 409 */       sb.append(", ");
/* 410 */       sb.append(this.description);
/*     */     }
/* 412 */     sb.append("]");
/* 413 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\SecurityCollection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */