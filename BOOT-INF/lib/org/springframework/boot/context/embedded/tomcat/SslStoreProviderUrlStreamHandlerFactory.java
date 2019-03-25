/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.net.URLStreamHandlerFactory;
/*     */ import java.security.KeyStore;
/*     */ import org.springframework.boot.context.embedded.SslStoreProvider;
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
/*     */ class SslStoreProviderUrlStreamHandlerFactory
/*     */   implements URLStreamHandlerFactory
/*     */ {
/*     */   private static final String PROTOCOL = "springbootssl";
/*     */   private static final String KEY_STORE_PATH = "keyStore";
/*     */   static final String KEY_STORE_URL = "springbootssl:keyStore";
/*     */   private static final String TRUST_STORE_PATH = "trustStore";
/*     */   static final String TRUST_STORE_URL = "springbootssl:trustStore";
/*     */   private final SslStoreProvider sslStoreProvider;
/*     */   
/*     */   SslStoreProviderUrlStreamHandlerFactory(SslStoreProvider sslStoreProvider)
/*     */   {
/*  52 */     this.sslStoreProvider = sslStoreProvider;
/*     */   }
/*     */   
/*     */   public URLStreamHandler createURLStreamHandler(String protocol)
/*     */   {
/*  57 */     if ("springbootssl".equals(protocol)) {
/*  58 */       new URLStreamHandler()
/*     */       {
/*     */         protected URLConnection openConnection(URL url) throws IOException
/*     */         {
/*     */           try {
/*  63 */             if ("keyStore".equals(url.getPath())) {
/*  64 */               return new SslStoreProviderUrlStreamHandlerFactory.KeyStoreUrlConnection(url, 
/*  65 */                 SslStoreProviderUrlStreamHandlerFactory.this.sslStoreProvider
/*  66 */                 .getKeyStore(), null);
/*     */             }
/*  68 */             if ("trustStore".equals(url.getPath())) {
/*  69 */               return new SslStoreProviderUrlStreamHandlerFactory.KeyStoreUrlConnection(url, 
/*  70 */                 SslStoreProviderUrlStreamHandlerFactory.this.sslStoreProvider
/*  71 */                 .getTrustStore(), null);
/*     */             }
/*     */           }
/*     */           catch (Exception ex) {
/*  75 */             throw new IOException(ex);
/*     */           }
/*  77 */           throw new IOException("Invalid path: " + url.getPath());
/*     */         }
/*     */       };
/*     */     }
/*  81 */     return null;
/*     */   }
/*     */   
/*     */   private static final class KeyStoreUrlConnection extends URLConnection
/*     */   {
/*     */     private final KeyStore keyStore;
/*     */     
/*     */     private KeyStoreUrlConnection(URL url, KeyStore keyStore) {
/*  89 */       super();
/*  90 */       this.keyStore = keyStore;
/*     */     }
/*     */     
/*     */     public void connect()
/*     */       throws IOException
/*     */     {}
/*     */     
/*     */     public InputStream getInputStream()
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 102 */         ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 103 */         this.keyStore.store(stream, new char[0]);
/* 104 */         return new ByteArrayInputStream(stream.toByteArray());
/*     */       }
/*     */       catch (Exception ex) {
/* 107 */         throw new IOException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\SslStoreProviderUrlStreamHandlerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */