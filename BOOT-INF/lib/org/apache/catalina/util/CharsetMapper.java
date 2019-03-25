/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class CharsetMapper
/*     */ {
/*     */   public static final String DEFAULT_RESOURCE = "/org/apache/catalina/util/CharsetMapperDefault.properties";
/*     */   
/*     */   public CharsetMapper()
/*     */   {
/*  59 */     this("/org/apache/catalina/util/CharsetMapperDefault.properties");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharsetMapper(String name)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       InputStream stream = getClass().getResourceAsStream(name);Throwable localThrowable4 = null;
/*  73 */       try { this.map.load(stream);
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/*  72 */         localThrowable4 = localThrowable2;throw localThrowable2;
/*     */       } finally {
/*  74 */         if (stream != null) if (localThrowable4 != null) try { stream.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else stream.close();
/*  75 */       } } catch (Throwable t) { ExceptionUtils.handleThrowable(t);
/*  76 */       throw new IllegalArgumentException(t.toString());
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
/*  88 */   private Properties map = new Properties();
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
/*     */   public String getCharset(Locale locale)
/*     */   {
/* 105 */     String charset = this.map.getProperty(locale.toString());
/* 106 */     if (charset == null) {
/* 107 */       charset = this.map.getProperty(locale.getLanguage() + "_" + locale
/* 108 */         .getCountry());
/* 109 */       if (charset == null) {
/* 110 */         charset = this.map.getProperty(locale.getLanguage());
/*     */       }
/*     */     }
/* 113 */     return charset;
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
/*     */   public void addCharsetMappingFromDeploymentDescriptor(String locale, String charset)
/*     */   {
/* 127 */     this.map.put(locale, charset);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\CharsetMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */