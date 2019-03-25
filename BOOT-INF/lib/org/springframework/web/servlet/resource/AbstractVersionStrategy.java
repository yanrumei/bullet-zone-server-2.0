/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractVersionStrategy
/*     */   implements VersionStrategy
/*     */ {
/*  46 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private final VersionPathStrategy pathStrategy;
/*     */   
/*     */   protected AbstractVersionStrategy(VersionPathStrategy pathStrategy)
/*     */   {
/*  52 */     Assert.notNull(pathStrategy, "VersionPathStrategy is required");
/*  53 */     this.pathStrategy = pathStrategy;
/*     */   }
/*     */   
/*     */   public VersionPathStrategy getVersionPathStrategy()
/*     */   {
/*  58 */     return this.pathStrategy;
/*     */   }
/*     */   
/*     */ 
/*     */   public String extractVersion(String requestPath)
/*     */   {
/*  64 */     return this.pathStrategy.extractVersion(requestPath);
/*     */   }
/*     */   
/*     */   public String removeVersion(String requestPath, String version)
/*     */   {
/*  69 */     return this.pathStrategy.removeVersion(requestPath, version);
/*     */   }
/*     */   
/*     */   public String addVersion(String requestPath, String version)
/*     */   {
/*  74 */     return this.pathStrategy.addVersion(requestPath, version);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class PrefixVersionPathStrategy
/*     */     implements VersionPathStrategy
/*     */   {
/*     */     private final String prefix;
/*     */     
/*     */ 
/*     */     public PrefixVersionPathStrategy(String version)
/*     */     {
/*  87 */       Assert.hasText(version, "'version' must not be empty");
/*  88 */       this.prefix = version;
/*     */     }
/*     */     
/*     */     public String extractVersion(String requestPath)
/*     */     {
/*  93 */       return requestPath.startsWith(this.prefix) ? this.prefix : null;
/*     */     }
/*     */     
/*     */     public String removeVersion(String requestPath, String version)
/*     */     {
/*  98 */       return requestPath.substring(this.prefix.length());
/*     */     }
/*     */     
/*     */     public String addVersion(String path, String version)
/*     */     {
/* 103 */       if (path.startsWith(".")) {
/* 104 */         return path;
/*     */       }
/*     */       
/* 107 */       return this.prefix + '/' + path;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static class FileNameVersionPathStrategy
/*     */     implements VersionPathStrategy
/*     */   {
/* 120 */     private static final Pattern pattern = Pattern.compile("-(\\S*)\\.");
/*     */     
/*     */     public String extractVersion(String requestPath)
/*     */     {
/* 124 */       Matcher matcher = pattern.matcher(requestPath);
/* 125 */       if (matcher.find()) {
/* 126 */         String match = matcher.group(1);
/* 127 */         return match.contains("-") ? match.substring(match.lastIndexOf('-') + 1) : match;
/*     */       }
/*     */       
/* 130 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */     public String removeVersion(String requestPath, String version)
/*     */     {
/* 136 */       return StringUtils.delete(requestPath, "-" + version);
/*     */     }
/*     */     
/*     */     public String addVersion(String requestPath, String version)
/*     */     {
/* 141 */       String baseFilename = StringUtils.stripFilenameExtension(requestPath);
/* 142 */       String extension = StringUtils.getFilenameExtension(requestPath);
/* 143 */       return baseFilename + '-' + version + '.' + extension;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\resource\AbstractVersionStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */