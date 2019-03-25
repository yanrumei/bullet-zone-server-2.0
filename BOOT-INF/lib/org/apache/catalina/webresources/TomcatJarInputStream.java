/*    */ package org.apache.catalina.webresources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.jar.JarEntry;
/*    */ import java.util.jar.JarInputStream;
/*    */ import java.util.zip.ZipEntry;
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
/*    */ public class TomcatJarInputStream
/*    */   extends JarInputStream
/*    */ {
/*    */   private JarEntry metaInfEntry;
/*    */   private JarEntry manifestEntry;
/*    */   
/*    */   TomcatJarInputStream(InputStream in)
/*    */     throws IOException
/*    */   {
/* 37 */     super(in);
/*    */   }
/*    */   
/*    */ 
/*    */   protected ZipEntry createZipEntry(String name)
/*    */   {
/* 43 */     ZipEntry ze = super.createZipEntry(name);
/* 44 */     if ((this.metaInfEntry == null) && ("META-INF/".equals(name))) {
/* 45 */       this.metaInfEntry = ((JarEntry)ze);
/* 46 */     } else if ((this.manifestEntry == null) && ("META-INF/MANIFESR.MF".equals(name))) {
/* 47 */       this.manifestEntry = ((JarEntry)ze);
/*    */     }
/* 49 */     return ze;
/*    */   }
/*    */   
/*    */   JarEntry getMetaInfEntry()
/*    */   {
/* 54 */     return this.metaInfEntry;
/*    */   }
/*    */   
/*    */   JarEntry getManifestEntry()
/*    */   {
/* 59 */     return this.manifestEntry;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\TomcatJarInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */