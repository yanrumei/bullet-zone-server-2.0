/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.security.MessageDigest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ApplicationTemp
/*     */ {
/*  35 */   private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
/*     */   
/*     */ 
/*     */   private final Class<?> sourceClass;
/*     */   
/*     */   private volatile File dir;
/*     */   
/*     */ 
/*     */   public ApplicationTemp()
/*     */   {
/*  45 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ApplicationTemp(Class<?> sourceClass)
/*     */   {
/*  53 */     this.sourceClass = sourceClass;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  58 */     return getDir().getAbsolutePath();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getDir(String subDir)
/*     */   {
/*  67 */     File dir = new File(getDir(), subDir);
/*  68 */     dir.mkdirs();
/*  69 */     return dir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getDir()
/*     */   {
/*  77 */     if (this.dir == null) {
/*  78 */       synchronized (this) {
/*  79 */         byte[] hash = generateHash(this.sourceClass);
/*  80 */         this.dir = new File(getTempDirectory(), toHexString(hash));
/*  81 */         this.dir.mkdirs();
/*  82 */         Assert.state(this.dir.exists(), "Unable to create temp directory " + this.dir);
/*     */       }
/*     */     }
/*     */     
/*  86 */     return this.dir;
/*     */   }
/*     */   
/*     */   private File getTempDirectory() {
/*  90 */     String property = System.getProperty("java.io.tmpdir");
/*  91 */     Assert.state(StringUtils.hasLength(property), "No 'java.io.tmpdir' property set");
/*  92 */     File file = new File(property);
/*  93 */     Assert.state(file.exists(), "Temp directory" + file + " does not exist");
/*  94 */     Assert.state(file.isDirectory(), "Temp location " + file + " is not a directory");
/*  95 */     return file;
/*     */   }
/*     */   
/*     */   private byte[] generateHash(Class<?> sourceClass) {
/*  99 */     ApplicationHome home = new ApplicationHome(sourceClass);
/*     */     try
/*     */     {
/* 102 */       MessageDigest digest = MessageDigest.getInstance("SHA-1");
/* 103 */       update(digest, home.getSource());
/* 104 */       update(digest, home.getDir());
/* 105 */       update(digest, System.getProperty("user.dir"));
/* 106 */       update(digest, System.getProperty("java.home"));
/* 107 */       update(digest, System.getProperty("java.class.path"));
/* 108 */       update(digest, System.getProperty("sun.java.command"));
/* 109 */       update(digest, System.getProperty("sun.boot.class.path"));
/* 110 */       return digest.digest();
/*     */     }
/*     */     catch (Exception ex) {
/* 113 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void update(MessageDigest digest, Object source) {
/* 118 */     if (source != null) {
/* 119 */       digest.update(getUpdateSourceBytes(source));
/*     */     }
/*     */   }
/*     */   
/*     */   private byte[] getUpdateSourceBytes(Object source) {
/* 124 */     if ((source instanceof File)) {
/* 125 */       return getUpdateSourceBytes(((File)source).getAbsolutePath());
/*     */     }
/* 127 */     return source.toString().getBytes();
/*     */   }
/*     */   
/*     */   private String toHexString(byte[] bytes) {
/* 131 */     char[] hex = new char[bytes.length * 2];
/* 132 */     for (int i = 0; i < bytes.length; i++) {
/* 133 */       int b = bytes[i] & 0xFF;
/* 134 */       hex[(i * 2)] = HEX_CHARS[(b >>> 4)];
/* 135 */       hex[(i * 2 + 1)] = HEX_CHARS[(b & 0xF)];
/*     */     }
/* 137 */     return new String(hex);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ApplicationTemp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */