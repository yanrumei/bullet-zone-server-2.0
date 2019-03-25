/*     */ package org.apache.catalina.webresources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.WebResource;
/*     */ import org.apache.catalina.WebResourceRoot.ResourceSetType;
/*     */ import org.apache.catalina.startup.ExpandWar;
/*     */ import org.apache.catalina.util.IOTools;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class ExtractingRoot
/*     */   extends StandardRoot
/*     */ {
/*  39 */   private static final StringManager sm = StringManager.getManager(ExtractingRoot.class);
/*     */   
/*     */ 
/*     */   private static final String APPLICATION_JARS_DIR = "application-jars";
/*     */   
/*     */ 
/*     */   protected void processWebInfLib()
/*     */     throws LifecycleException
/*     */   {
/*  48 */     if (!super.isPackedWarFile()) {
/*  49 */       super.processWebInfLib();
/*  50 */       return;
/*     */     }
/*     */     
/*  53 */     File expansionTarget = getExpansionTarget();
/*  54 */     if ((!expansionTarget.isDirectory()) && 
/*  55 */       (!expansionTarget.mkdirs()))
/*     */     {
/*  57 */       throw new LifecycleException(sm.getString("extractingRoot.targetFailed", new Object[] { expansionTarget }));
/*     */     }
/*     */     
/*     */ 
/*  61 */     WebResource[] possibleJars = listResources("/WEB-INF/lib", false);
/*     */     
/*  63 */     for (WebResource possibleJar : possibleJars) {
/*  64 */       if ((possibleJar.isFile()) && (possibleJar.getName().endsWith(".jar"))) {
/*     */         try {
/*  66 */           File dest = new File(expansionTarget, possibleJar.getName());
/*  67 */           dest = dest.getCanonicalFile();
/*  68 */           InputStream sourceStream = possibleJar.getInputStream();Throwable localThrowable6 = null;
/*  69 */           try { OutputStream destStream = new FileOutputStream(dest);Throwable localThrowable7 = null;
/*  70 */             try { IOTools.flow(sourceStream, destStream);
/*     */             }
/*     */             catch (Throwable localThrowable1)
/*     */             {
/*  68 */               localThrowable7 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable6 = localThrowable4;throw localThrowable4;
/*     */           }
/*     */           finally {
/*  71 */             if (sourceStream != null) if (localThrowable6 != null) try { sourceStream.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else sourceStream.close();
/*     */           }
/*  73 */           createWebResourceSet(WebResourceRoot.ResourceSetType.CLASSES_JAR, "/WEB-INF/classes", dest
/*  74 */             .toURI().toURL(), "/");
/*     */         }
/*     */         catch (IOException ioe) {
/*  77 */           throw new LifecycleException(sm.getString("extractingRoot.jarFailed", new Object[] {possibleJar.getName() }), ioe);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private File getExpansionTarget() {
/*  84 */     File tmpDir = (File)getContext().getServletContext().getAttribute("javax.servlet.context.tempdir");
/*  85 */     File expansionTarget = new File(tmpDir, "application-jars");
/*  86 */     return expansionTarget;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isPackedWarFile()
/*     */   {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/*  98 */     super.stopInternal();
/*     */     
/* 100 */     if (super.isPackedWarFile())
/*     */     {
/* 102 */       File expansionTarget = getExpansionTarget();
/* 103 */       ExpandWar.delete(expansionTarget);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\webresources\ExtractingRoot.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */