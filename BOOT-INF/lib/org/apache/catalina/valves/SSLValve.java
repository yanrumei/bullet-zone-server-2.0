/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class SSLValve
/*     */   extends ValveBase
/*     */ {
/*  65 */   private static final Log log = LogFactory.getLog(SSLValve.class);
/*     */   
/*  67 */   private String sslClientCertHeader = "ssl_client_cert";
/*  68 */   private String sslCipherHeader = "ssl_cipher";
/*  69 */   private String sslSessionIdHeader = "ssl_session_id";
/*  70 */   private String sslCipherUserKeySizeHeader = "ssl_cipher_usekeysize";
/*     */   
/*     */   public SSLValve()
/*     */   {
/*  74 */     super(true);
/*     */   }
/*     */   
/*     */   public String getSslClientCertHeader()
/*     */   {
/*  79 */     return this.sslClientCertHeader;
/*     */   }
/*     */   
/*     */   public void setSslClientCertHeader(String sslClientCertHeader) {
/*  83 */     this.sslClientCertHeader = sslClientCertHeader;
/*     */   }
/*     */   
/*     */   public String getSslCipherHeader() {
/*  87 */     return this.sslCipherHeader;
/*     */   }
/*     */   
/*     */   public void setSslCipherHeader(String sslCipherHeader) {
/*  91 */     this.sslCipherHeader = sslCipherHeader;
/*     */   }
/*     */   
/*     */   public String getSslSessionIdHeader() {
/*  95 */     return this.sslSessionIdHeader;
/*     */   }
/*     */   
/*     */   public void setSslSessionIdHeader(String sslSessionIdHeader) {
/*  99 */     this.sslSessionIdHeader = sslSessionIdHeader;
/*     */   }
/*     */   
/*     */   public String getSslCipherUserKeySizeHeader() {
/* 103 */     return this.sslCipherUserKeySizeHeader;
/*     */   }
/*     */   
/*     */   public void setSslCipherUserKeySizeHeader(String sslCipherUserKeySizeHeader) {
/* 107 */     this.sslCipherUserKeySizeHeader = sslCipherUserKeySizeHeader;
/*     */   }
/*     */   
/*     */   public String mygetHeader(Request request, String header)
/*     */   {
/* 112 */     String strcert0 = request.getHeader(header);
/* 113 */     if (strcert0 == null) {
/* 114 */       return null;
/*     */     }
/*     */     
/* 117 */     if ("(null)".equals(strcert0)) {
/* 118 */       return null;
/*     */     }
/* 120 */     return strcert0;
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
/*     */ 
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 142 */     String headerValue = mygetHeader(request, this.sslClientCertHeader);
/* 143 */     if (headerValue != null) {
/* 144 */       headerValue = headerValue.trim();
/* 145 */       if (headerValue.length() > 27) {
/* 146 */         String body = headerValue.substring(27, headerValue.length() - 25);
/* 147 */         body = body.replace(' ', '\n');
/* 148 */         body = body.replace('\t', '\n');
/* 149 */         String header = "-----BEGIN CERTIFICATE-----\n";
/* 150 */         String footer = "\n-----END CERTIFICATE-----\n";
/* 151 */         String strcerts = header.concat(body).concat(footer);
/*     */         
/* 153 */         ByteArrayInputStream bais = new ByteArrayInputStream(strcerts.getBytes(StandardCharsets.ISO_8859_1));
/* 154 */         X509Certificate[] jsseCerts = null;
/* 155 */         String providerName = (String)request.getConnector().getProperty("clientCertProvider");
/*     */         try {
/*     */           CertificateFactory cf;
/*     */           CertificateFactory cf;
/* 159 */           if (providerName == null) {
/* 160 */             cf = CertificateFactory.getInstance("X.509");
/*     */           } else {
/* 162 */             cf = CertificateFactory.getInstance("X.509", providerName);
/*     */           }
/* 164 */           X509Certificate cert = (X509Certificate)cf.generateCertificate(bais);
/* 165 */           jsseCerts = new X509Certificate[1];
/* 166 */           jsseCerts[0] = cert;
/*     */         } catch (CertificateException e) {
/* 168 */           log.warn(sm.getString("sslValve.certError", new Object[] { strcerts }), e);
/*     */         } catch (NoSuchProviderException e) {
/* 170 */           log.error(sm.getString("sslValve.invalidProvider", new Object[] { providerName }), e);
/*     */         }
/*     */         
/* 173 */         request.setAttribute("javax.servlet.request.X509Certificate", jsseCerts);
/*     */       }
/*     */     }
/* 176 */     headerValue = mygetHeader(request, this.sslCipherHeader);
/* 177 */     if (headerValue != null) {
/* 178 */       request.setAttribute("javax.servlet.request.cipher_suite", headerValue);
/*     */     }
/* 180 */     headerValue = mygetHeader(request, this.sslSessionIdHeader);
/* 181 */     if (headerValue != null) {
/* 182 */       request.setAttribute("javax.servlet.request.ssl_session_id", headerValue);
/*     */     }
/* 184 */     headerValue = mygetHeader(request, this.sslCipherUserKeySizeHeader);
/* 185 */     if (headerValue != null) {
/* 186 */       request.setAttribute("javax.servlet.request.key_size", Integer.valueOf(headerValue));
/*     */     }
/* 188 */     getNext().invoke(request, response);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\SSLValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */