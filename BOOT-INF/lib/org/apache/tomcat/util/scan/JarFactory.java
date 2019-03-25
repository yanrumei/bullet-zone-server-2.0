/*    */ package org.apache.tomcat.util.scan;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.util.regex.Matcher;
/*    */ import org.apache.tomcat.Jar;
/*    */ import org.apache.tomcat.util.buf.UriUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JarFactory
/*    */ {
/*    */   public static Jar newInstance(URL url)
/*    */     throws IOException
/*    */   {
/* 38 */     String urlString = url.toString();
/* 39 */     if (urlString.startsWith("jar:file:")) {
/* 40 */       if (urlString.endsWith("!/")) {
/* 41 */         return new JarFileUrlJar(url, true);
/*    */       }
/* 43 */       return new JarFileUrlNestedJar(url);
/*    */     }
/* 45 */     if (urlString.startsWith("war:file:")) {
/* 46 */       URL jarUrl = UriUtil.warToJar(url);
/* 47 */       return new JarFileUrlNestedJar(jarUrl); }
/* 48 */     if (urlString.startsWith("file:")) {
/* 49 */       return new JarFileUrlJar(url, false);
/*    */     }
/* 51 */     return new UrlJar(url);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static URL getJarEntryURL(URL baseUrl, String entryName)
/*    */     throws MalformedURLException
/*    */   {
/* 59 */     String baseExternal = baseUrl.toExternalForm();
/*    */     
/* 61 */     if (baseExternal.startsWith("jar"))
/*    */     {
/*    */ 
/* 64 */       baseExternal = baseExternal.replaceFirst("^jar:", "war:");
/* 65 */       baseExternal = baseExternal.replaceFirst("!/", 
/* 66 */         Matcher.quoteReplacement(UriUtil.getWarSeparator()));
/*    */     }
/*    */     
/* 69 */     return new URL("jar:" + baseExternal + "!/" + entryName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\scan\JarFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */