/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.tomcat.Jar;
/*     */ import org.apache.tomcat.JarScannerCallback;
/*     */ import org.xml.sax.InputSource;
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
/*     */ public class FragmentJarScannerCallback
/*     */   implements JarScannerCallback
/*     */ {
/*     */   private static final String FRAGMENT_LOCATION = "META-INF/web-fragment.xml";
/*     */   private final WebXmlParser webXmlParser;
/*     */   private final boolean delegate;
/*     */   private final boolean parseRequired;
/*  41 */   private final Map<String, WebXml> fragments = new HashMap();
/*  42 */   private boolean ok = true;
/*     */   
/*     */   public FragmentJarScannerCallback(WebXmlParser webXmlParser, boolean delegate, boolean parseRequired)
/*     */   {
/*  46 */     this.webXmlParser = webXmlParser;
/*  47 */     this.delegate = delegate;
/*  48 */     this.parseRequired = parseRequired;
/*     */   }
/*     */   
/*     */ 
/*     */   public void scan(Jar jar, String webappPath, boolean isWebapp)
/*     */     throws IOException
/*     */   {
/*  55 */     InputStream is = null;
/*  56 */     WebXml fragment = new WebXml();
/*  57 */     fragment.setWebappJar(isWebapp);
/*  58 */     fragment.setDelegate(this.delegate);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  65 */       if ((isWebapp) && (this.parseRequired)) {
/*  66 */         is = jar.getInputStream("META-INF/web-fragment.xml");
/*     */       }
/*     */       
/*  69 */       if (is == null)
/*     */       {
/*     */ 
/*  72 */         fragment.setDistributable(true);
/*     */       } else {
/*  74 */         String fragmentUrl = jar.getURL("META-INF/web-fragment.xml");
/*  75 */         InputSource source = new InputSource(fragmentUrl);
/*  76 */         source.setByteStream(is);
/*  77 */         if (!this.webXmlParser.parseWebXml(source, fragment, true)) {
/*  78 */           this.ok = false;
/*     */         }
/*     */       }
/*     */     } finally {
/*  82 */       fragment.setURL(jar.getJarFileURL());
/*  83 */       if (fragment.getName() == null) {
/*  84 */         fragment.setName(fragment.getURL().toString());
/*     */       }
/*  86 */       fragment.setJarName(extractJarFileName(jar.getJarFileURL()));
/*  87 */       this.fragments.put(fragment.getName(), fragment);
/*     */     }
/*     */   }
/*     */   
/*     */   private String extractJarFileName(URL input)
/*     */   {
/*  93 */     String url = input.toString();
/*  94 */     if (url.endsWith("!/"))
/*     */     {
/*  96 */       url = url.substring(0, url.length() - 2);
/*     */     }
/*     */     
/*     */ 
/* 100 */     return url.substring(url.lastIndexOf('/') + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public void scan(File file, String webappPath, boolean isWebapp)
/*     */     throws IOException
/*     */   {
/* 107 */     WebXml fragment = new WebXml();
/* 108 */     fragment.setWebappJar(isWebapp);
/* 109 */     fragment.setDelegate(this.delegate);
/*     */     
/* 111 */     File fragmentFile = new File(file, "META-INF/web-fragment.xml");
/*     */     try {
/* 113 */       if (fragmentFile.isFile()) {
/* 114 */         InputStream stream = new FileInputStream(fragmentFile);Throwable localThrowable3 = null;
/*     */         try {
/* 116 */           InputSource source = new InputSource(fragmentFile.toURI().toURL().toString());
/* 117 */           source.setByteStream(stream);
/* 118 */           if (!this.webXmlParser.parseWebXml(source, fragment, true)) {
/* 119 */             this.ok = false;
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 114 */           localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/* 121 */           if (stream != null) if (localThrowable3 != null) try { stream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else stream.close();
/*     */         }
/*     */       }
/*     */       else {
/* 125 */         fragment.setDistributable(true);
/*     */       }
/*     */     } finally {
/* 128 */       fragment.setURL(file.toURI().toURL());
/* 129 */       if (fragment.getName() == null) {
/* 130 */         fragment.setName(fragment.getURL().toString());
/*     */       }
/* 132 */       fragment.setJarName(file.getName());
/* 133 */       this.fragments.put(fragment.getName(), fragment);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void scanWebInfClasses() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isOk()
/*     */   {
/* 146 */     return this.ok;
/*     */   }
/*     */   
/*     */   public Map<String, WebXml> getFragments() {
/* 150 */     return this.fragments;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\FragmentJarScannerCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */