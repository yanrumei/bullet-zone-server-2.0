/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ContextName
/*     */ {
/*     */   public static final String ROOT_NAME = "ROOT";
/*     */   private static final String VERSION_MARKER = "##";
/*     */   private static final String FWD_SLASH_REPLACEMENT = "#";
/*     */   private final String baseName;
/*     */   private final String path;
/*     */   private final String version;
/*     */   private final String name;
/*     */   
/*     */   public ContextName(String name, boolean stripFileExtension)
/*     */   {
/*  47 */     String tmp1 = name;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  52 */     if (tmp1.startsWith("/")) {
/*  53 */       tmp1 = tmp1.substring(1);
/*     */     }
/*     */     
/*     */ 
/*  57 */     tmp1 = tmp1.replaceAll("/", "#");
/*     */     
/*     */ 
/*  60 */     if ((tmp1.startsWith("##")) || ("".equals(tmp1))) {
/*  61 */       tmp1 = "ROOT" + tmp1;
/*     */     }
/*     */     
/*     */ 
/*  65 */     if ((stripFileExtension) && (
/*  66 */       (tmp1.toLowerCase(Locale.ENGLISH).endsWith(".war")) || 
/*  67 */       (tmp1.toLowerCase(Locale.ENGLISH).endsWith(".xml")))) {
/*  68 */       tmp1 = tmp1.substring(0, tmp1.length() - 4);
/*     */     }
/*     */     
/*  71 */     this.baseName = tmp1;
/*     */     
/*     */ 
/*     */ 
/*  75 */     int versionIndex = this.baseName.indexOf("##");
/*  76 */     String tmp2; String tmp2; if (versionIndex > -1) {
/*  77 */       this.version = this.baseName.substring(versionIndex + 2);
/*  78 */       tmp2 = this.baseName.substring(0, versionIndex);
/*     */     } else {
/*  80 */       this.version = "";
/*  81 */       tmp2 = this.baseName;
/*     */     }
/*     */     
/*  84 */     if ("ROOT".equals(tmp2)) {
/*  85 */       this.path = "";
/*     */     } else {
/*  87 */       this.path = ("/" + tmp2.replaceAll("#", "/"));
/*     */     }
/*     */     
/*  90 */     if (versionIndex > -1) {
/*  91 */       this.name = (this.path + "##" + this.version);
/*     */     } else {
/*  93 */       this.name = this.path;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContextName(String path, String version)
/*     */   {
/* 105 */     if ((path == null) || ("/".equals(path)) || ("/ROOT".equals(path))) {
/* 106 */       this.path = "";
/*     */     } else {
/* 108 */       this.path = path;
/*     */     }
/*     */     
/*     */ 
/* 112 */     if (version == null) {
/* 113 */       this.version = "";
/*     */     } else {
/* 115 */       this.version = version;
/*     */     }
/*     */     
/*     */ 
/* 119 */     if ("".equals(this.version)) {
/* 120 */       this.name = this.path;
/*     */     } else {
/* 122 */       this.name = (this.path + "##" + this.version);
/*     */     }
/*     */     
/*     */ 
/* 126 */     StringBuilder tmp = new StringBuilder();
/* 127 */     if ("".equals(this.path)) {
/* 128 */       tmp.append("ROOT");
/*     */     } else {
/* 130 */       tmp.append(this.path.substring(1).replaceAll("/", "#"));
/*     */     }
/*     */     
/* 133 */     if (this.version.length() > 0) {
/* 134 */       tmp.append("##");
/* 135 */       tmp.append(this.version);
/*     */     }
/* 137 */     this.baseName = tmp.toString();
/*     */   }
/*     */   
/*     */   public String getBaseName() {
/* 141 */     return this.baseName;
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 145 */     return this.path;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 149 */     return this.version;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 153 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/* 157 */     StringBuilder tmp = new StringBuilder();
/* 158 */     if ("".equals(this.path)) {
/* 159 */       tmp.append('/');
/*     */     } else {
/* 161 */       tmp.append(this.path);
/*     */     }
/*     */     
/* 164 */     if (!"".equals(this.version)) {
/* 165 */       tmp.append("##");
/* 166 */       tmp.append(this.version);
/*     */     }
/*     */     
/* 169 */     return tmp.toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 174 */     return getDisplayName();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\ContextName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */