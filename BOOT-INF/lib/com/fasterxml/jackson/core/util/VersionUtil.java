/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.Versioned;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionUtil
/*     */ {
/*  28 */   private static final Pattern V_SEP = Pattern.compile("[-_./;:]");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Version _v;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected VersionUtil()
/*     */   {
/*  40 */     Version v = null;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  45 */       v = versionFor(getClass());
/*     */     } catch (Exception e) {
/*  47 */       System.err.println("ERROR: Failed to load Version information from " + getClass());
/*     */     }
/*  49 */     if (v == null) {
/*  50 */       v = Version.unknownVersion();
/*     */     }
/*  52 */     this._v = v;
/*     */   }
/*     */   
/*  55 */   public Version version() { return this._v; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Version versionFor(Class<?> cls)
/*     */   {
/*  74 */     Version version = packageVersionFor(cls);
/*  75 */     return version == null ? Version.unknownVersion() : version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Version packageVersionFor(Class<?> cls)
/*     */   {
/*  87 */     Version v = null;
/*     */     try {
/*  89 */       String versionInfoClassName = cls.getPackage().getName() + ".PackageVersion";
/*  90 */       Class<?> vClass = Class.forName(versionInfoClassName, true, cls.getClassLoader());
/*     */       try
/*     */       {
/*  93 */         v = ((Versioned)vClass.newInstance()).version();
/*     */       } catch (Exception e) {
/*  95 */         throw new IllegalArgumentException("Failed to get Versioned out of " + vClass);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 100 */     return v == null ? Version.unknownVersion() : v;
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
/*     */   @Deprecated
/*     */   public static Version mavenVersionFor(ClassLoader cl, String groupId, String artifactId)
/*     */   {
/* 121 */     InputStream pomProperties = cl.getResourceAsStream("META-INF/maven/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/pom.properties");
/*     */     
/* 123 */     if (pomProperties != null) {
/*     */       try {
/* 125 */         Properties props = new Properties();
/* 126 */         props.load(pomProperties);
/* 127 */         String versionStr = props.getProperty("version");
/* 128 */         String pomPropertiesArtifactId = props.getProperty("artifactId");
/* 129 */         String pomPropertiesGroupId = props.getProperty("groupId");
/* 130 */         return parseVersion(versionStr, pomPropertiesGroupId, pomPropertiesArtifactId);
/*     */       }
/*     */       catch (IOException e) {}finally
/*     */       {
/* 134 */         _close(pomProperties);
/*     */       }
/*     */     }
/* 137 */     return Version.unknownVersion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Version parseVersion(String s, String groupId, String artifactId)
/*     */   {
/* 145 */     if ((s != null) && ((s = s.trim()).length() > 0)) {
/* 146 */       String[] parts = V_SEP.split(s);
/* 147 */       return new Version(parseVersionPart(parts[0]), parts.length > 1 ? parseVersionPart(parts[1]) : 0, parts.length > 2 ? parseVersionPart(parts[2]) : 0, parts.length > 3 ? parts[3] : null, groupId, artifactId);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 153 */     return Version.unknownVersion();
/*     */   }
/*     */   
/*     */   protected static int parseVersionPart(String s) {
/* 157 */     int number = 0;
/* 158 */     int i = 0; for (int len = s.length(); i < len; i++) {
/* 159 */       char c = s.charAt(i);
/* 160 */       if ((c > '9') || (c < '0')) break;
/* 161 */       number = number * 10 + (c - '0');
/*     */     }
/* 163 */     return number;
/*     */   }
/*     */   
/*     */   private static final void _close(Closeable c) {
/*     */     try {
/* 168 */       c.close();
/*     */     }
/*     */     catch (IOException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void throwInternal()
/*     */   {
/* 179 */     throw new RuntimeException("Internal error: this code path should never get executed");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\VersionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */