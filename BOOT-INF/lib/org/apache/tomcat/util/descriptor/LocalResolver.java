/*     */ package org.apache.tomcat.util.descriptor;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.EntityResolver2;
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
/*     */ public class LocalResolver
/*     */   implements EntityResolver2
/*     */ {
/*  38 */   private static final StringManager sm = StringManager.getManager(Constants.PACKAGE_NAME);
/*     */   
/*  40 */   private static final String[] JAVA_EE_NAMESPACES = { "http://java.sun.com/xml/ns/j2ee", "http://java.sun.com/xml/ns/javaee", "http://xmlns.jcp.org/xml/ns/javaee" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<String, String> publicIds;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<String, String> systemIds;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean blockExternal;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LocalResolver(Map<String, String> publicIds, Map<String, String> systemIds, boolean blockExternal)
/*     */   {
/*  64 */     this.publicIds = publicIds;
/*  65 */     this.systemIds = systemIds;
/*  66 */     this.blockExternal = blockExternal;
/*     */   }
/*     */   
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/*  73 */     return resolveEntity(null, publicId, null, systemId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputSource resolveEntity(String name, String publicId, String base, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/*  82 */     String resolved = (String)this.publicIds.get(publicId);
/*  83 */     if (resolved != null) {
/*  84 */       InputSource is = new InputSource(resolved);
/*  85 */       is.setPublicId(publicId);
/*  86 */       return is;
/*     */     }
/*     */     
/*     */ 
/*  90 */     if (systemId == null) {
/*  91 */       throw new FileNotFoundException(sm.getString("localResolver.unresolvedEntity", new Object[] { name, publicId, null, base }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  96 */     resolved = (String)this.systemIds.get(systemId);
/*  97 */     InputSource is; if (resolved != null) {
/*  98 */       is = new InputSource(resolved);
/*  99 */       is.setPublicId(publicId);
/* 100 */       return is;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 105 */     for (String javaEENamespace : JAVA_EE_NAMESPACES) {
/* 106 */       String javaEESystemId = javaEENamespace + '/' + systemId;
/* 107 */       resolved = (String)this.systemIds.get(javaEESystemId);
/* 108 */       if (resolved != null) {
/* 109 */         InputSource is = new InputSource(resolved);
/* 110 */         is.setPublicId(publicId);
/* 111 */         return is;
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*     */       URI systemUri;
/* 118 */       if (base == null) {
/* 119 */         systemUri = new URI(systemId);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 125 */         URI baseUri = new URI(base);
/* 126 */         systemUri = new URL(baseUri.toURL(), systemId).toURI();
/*     */       }
/* 128 */       URI systemUri = systemUri.normalize();
/*     */     }
/*     */     catch (URISyntaxException e)
/*     */     {
/* 132 */       if (this.blockExternal)
/*     */       {
/* 134 */         throw new MalformedURLException(e.getMessage());
/*     */       }
/*     */       
/* 137 */       return new InputSource(systemId);
/*     */     }
/*     */     URI systemUri;
/* 140 */     if (systemUri.isAbsolute())
/*     */     {
/* 142 */       resolved = (String)this.systemIds.get(systemUri.toString());
/* 143 */       if (resolved != null) {
/* 144 */         InputSource is = new InputSource(resolved);
/* 145 */         is.setPublicId(publicId);
/* 146 */         return is;
/*     */       }
/* 148 */       if (!this.blockExternal) {
/* 149 */         InputSource is = new InputSource(systemUri.toString());
/* 150 */         is.setPublicId(publicId);
/* 151 */         return is;
/*     */       }
/*     */     }
/*     */     
/* 155 */     throw new FileNotFoundException(sm.getString("localResolver.unresolvedEntity", new Object[] { name, publicId, systemId, base }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InputSource getExternalSubset(String name, String baseURI)
/*     */     throws SAXException, IOException
/*     */   {
/* 163 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\LocalResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */