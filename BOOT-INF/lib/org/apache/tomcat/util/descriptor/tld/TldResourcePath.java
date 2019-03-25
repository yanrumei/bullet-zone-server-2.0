/*     */ package org.apache.tomcat.util.descriptor.tld;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Objects;
/*     */ import org.apache.tomcat.Jar;
/*     */ import org.apache.tomcat.util.scan.JarFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TldResourcePath
/*     */ {
/*     */   private final URL url;
/*     */   private final String webappPath;
/*     */   private final String entryName;
/*     */   
/*     */   public TldResourcePath(URL url, String webappPath)
/*     */   {
/*  54 */     this(url, webappPath, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TldResourcePath(URL url, String webappPath, String entryName)
/*     */   {
/*  65 */     this.url = url;
/*  66 */     this.webappPath = webappPath;
/*  67 */     this.entryName = entryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URL getUrl()
/*     */   {
/*  76 */     return this.url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getWebappPath()
/*     */   {
/*  87 */     return this.webappPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEntryName()
/*     */   {
/*  97 */     return this.entryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toExternalForm()
/*     */   {
/* 108 */     if (this.entryName == null) {
/* 109 */       return this.url.toExternalForm();
/*     */     }
/* 111 */     return "jar:" + this.url.toExternalForm() + "!/" + this.entryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream openStream()
/*     */     throws IOException
/*     */   {
/* 122 */     if (this.entryName == null) {
/* 123 */       return this.url.openStream();
/*     */     }
/* 125 */     URL entryUrl = JarFactory.getJarEntryURL(this.url, this.entryName);
/* 126 */     return entryUrl.openStream();
/*     */   }
/*     */   
/*     */   public Jar openJar() throws IOException
/*     */   {
/* 131 */     if (this.entryName == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     return JarFactory.newInstance(this.url);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 140 */     if (this == o) {
/* 141 */       return true;
/*     */     }
/* 143 */     if ((o == null) || (getClass() != o.getClass())) {
/* 144 */       return false;
/*     */     }
/*     */     
/* 147 */     TldResourcePath other = (TldResourcePath)o;
/*     */     
/* 149 */     return (this.url.equals(other.url)) && 
/* 150 */       (Objects.equals(this.webappPath, other.webappPath)) && 
/* 151 */       (Objects.equals(this.entryName, other.entryName));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 156 */     int result = this.url.hashCode();
/* 157 */     result = result * 31 + Objects.hashCode(this.webappPath);
/* 158 */     result = result * 31 + Objects.hashCode(this.entryName);
/* 159 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\tld\TldResourcePath.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */