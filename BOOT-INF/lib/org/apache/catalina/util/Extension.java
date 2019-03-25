/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Extension
/*     */ {
/*  54 */   private String extensionName = null;
/*     */   
/*     */   public String getExtensionName()
/*     */   {
/*  58 */     return this.extensionName;
/*     */   }
/*     */   
/*     */   public void setExtensionName(String extensionName) {
/*  62 */     this.extensionName = extensionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private String implementationURL = null;
/*     */   
/*     */   public String getImplementationURL() {
/*  72 */     return this.implementationURL;
/*     */   }
/*     */   
/*     */   public void setImplementationURL(String implementationURL) {
/*  76 */     this.implementationURL = implementationURL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private String implementationVendor = null;
/*     */   
/*     */   public String getImplementationVendor() {
/*  87 */     return this.implementationVendor;
/*     */   }
/*     */   
/*     */   public void setImplementationVendor(String implementationVendor) {
/*  91 */     this.implementationVendor = implementationVendor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   private String implementationVendorId = null;
/*     */   
/*     */   public String getImplementationVendorId() {
/* 102 */     return this.implementationVendorId;
/*     */   }
/*     */   
/*     */   public void setImplementationVendorId(String implementationVendorId) {
/* 106 */     this.implementationVendorId = implementationVendorId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private String implementationVersion = null;
/*     */   
/*     */   public String getImplementationVersion() {
/* 117 */     return this.implementationVersion;
/*     */   }
/*     */   
/*     */   public void setImplementationVersion(String implementationVersion) {
/* 121 */     this.implementationVersion = implementationVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */   private String specificationVendor = null;
/*     */   
/*     */   public String getSpecificationVendor() {
/* 132 */     return this.specificationVendor;
/*     */   }
/*     */   
/*     */   public void setSpecificationVendor(String specificationVendor) {
/* 136 */     this.specificationVendor = specificationVendor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */   private String specificationVersion = null;
/*     */   
/*     */   public String getSpecificationVersion() {
/* 147 */     return this.specificationVersion;
/*     */   }
/*     */   
/*     */   public void setSpecificationVersion(String specificationVersion) {
/* 151 */     this.specificationVersion = specificationVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */   private boolean fulfilled = false;
/*     */   
/*     */   public void setFulfilled(boolean fulfilled) {
/* 162 */     this.fulfilled = fulfilled;
/*     */   }
/*     */   
/*     */   public boolean isFulfilled() {
/* 166 */     return this.fulfilled;
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
/*     */   public boolean isCompatibleWith(Extension required)
/*     */   {
/* 184 */     if (this.extensionName == null)
/* 185 */       return false;
/* 186 */     if (!this.extensionName.equals(required.getExtensionName())) {
/* 187 */       return false;
/*     */     }
/*     */     
/* 190 */     if ((required.getSpecificationVersion() != null) && 
/* 191 */       (!isNewer(this.specificationVersion, required
/* 192 */       .getSpecificationVersion()))) {
/* 193 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 197 */     if (required.getImplementationVendorId() != null) {
/* 198 */       if (this.implementationVendorId == null)
/* 199 */         return false;
/* 200 */       if (!this.implementationVendorId.equals(required
/* 201 */         .getImplementationVendorId())) {
/* 202 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 206 */     if ((required.getImplementationVersion() != null) && 
/* 207 */       (!isNewer(this.implementationVersion, required
/* 208 */       .getImplementationVersion()))) {
/* 209 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 213 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 220 */     StringBuilder sb = new StringBuilder("Extension[");
/* 221 */     sb.append(this.extensionName);
/* 222 */     if (this.implementationURL != null) {
/* 223 */       sb.append(", implementationURL=");
/* 224 */       sb.append(this.implementationURL);
/*     */     }
/* 226 */     if (this.implementationVendor != null) {
/* 227 */       sb.append(", implementationVendor=");
/* 228 */       sb.append(this.implementationVendor);
/*     */     }
/* 230 */     if (this.implementationVendorId != null) {
/* 231 */       sb.append(", implementationVendorId=");
/* 232 */       sb.append(this.implementationVendorId);
/*     */     }
/* 234 */     if (this.implementationVersion != null) {
/* 235 */       sb.append(", implementationVersion=");
/* 236 */       sb.append(this.implementationVersion);
/*     */     }
/* 238 */     if (this.specificationVendor != null) {
/* 239 */       sb.append(", specificationVendor=");
/* 240 */       sb.append(this.specificationVendor);
/*     */     }
/* 242 */     if (this.specificationVersion != null) {
/* 243 */       sb.append(", specificationVersion=");
/* 244 */       sb.append(this.specificationVersion);
/*     */     }
/* 246 */     sb.append("]");
/* 247 */     return sb.toString();
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
/*     */   private boolean isNewer(String first, String second)
/*     */     throws NumberFormatException
/*     */   {
/* 268 */     if ((first == null) || (second == null))
/* 269 */       return false;
/* 270 */     if (first.equals(second)) {
/* 271 */       return true;
/*     */     }
/* 273 */     StringTokenizer fTok = new StringTokenizer(first, ".", true);
/* 274 */     StringTokenizer sTok = new StringTokenizer(second, ".", true);
/* 275 */     int fVersion = 0;
/* 276 */     int sVersion = 0;
/* 277 */     while ((fTok.hasMoreTokens()) || (sTok.hasMoreTokens())) {
/* 278 */       if (fTok.hasMoreTokens()) {
/* 279 */         fVersion = Integer.parseInt(fTok.nextToken());
/*     */       } else
/* 281 */         fVersion = 0;
/* 282 */       if (sTok.hasMoreTokens()) {
/* 283 */         sVersion = Integer.parseInt(sTok.nextToken());
/*     */       } else
/* 285 */         sVersion = 0;
/* 286 */       if (fVersion < sVersion)
/* 287 */         return false;
/* 288 */       if (fVersion > sVersion)
/* 289 */         return true;
/* 290 */       if (fTok.hasMoreTokens())
/* 291 */         fTok.nextToken();
/* 292 */       if (sTok.hasMoreTokens()) {
/* 293 */         sTok.nextToken();
/*     */       }
/*     */     }
/* 296 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\Extension.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */