/*     */ package org.springframework.remoting.httpinvoker;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.springframework.context.i18n.LocaleContext;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.remoting.support.RemoteInvocationResult;
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
/*     */ public class SimpleHttpInvokerRequestExecutor
/*     */   extends AbstractHttpInvokerRequestExecutor
/*     */ {
/*  49 */   private int connectTimeout = -1;
/*     */   
/*  51 */   private int readTimeout = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectTimeout(int connectTimeout)
/*     */   {
/*  61 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadTimeout(int readTimeout)
/*     */   {
/*  71 */     this.readTimeout = readTimeout;
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
/*     */   protected RemoteInvocationResult doExecuteRequest(HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  90 */     HttpURLConnection con = openConnection(config);
/*  91 */     prepareConnection(con, baos.size());
/*  92 */     writeRequestBody(config, con, baos);
/*  93 */     validateResponse(config, con);
/*  94 */     InputStream responseBody = readResponseBody(config, con);
/*     */     
/*  96 */     return readRemoteInvocationResult(responseBody, config.getCodebaseUrl());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpURLConnection openConnection(HttpInvokerClientConfiguration config)
/*     */     throws IOException
/*     */   {
/* 108 */     URLConnection con = new URL(config.getServiceUrl()).openConnection();
/* 109 */     if (!(con instanceof HttpURLConnection))
/*     */     {
/* 111 */       throw new IOException("Service URL [" + config.getServiceUrl() + "] does not resolve to an HTTP connection");
/*     */     }
/* 113 */     return (HttpURLConnection)con;
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
/*     */   protected void prepareConnection(HttpURLConnection connection, int contentLength)
/*     */     throws IOException
/*     */   {
/* 128 */     if (this.connectTimeout >= 0) {
/* 129 */       connection.setConnectTimeout(this.connectTimeout);
/*     */     }
/* 131 */     if (this.readTimeout >= 0) {
/* 132 */       connection.setReadTimeout(this.readTimeout);
/*     */     }
/*     */     
/* 135 */     connection.setDoOutput(true);
/* 136 */     connection.setRequestMethod("POST");
/* 137 */     connection.setRequestProperty("Content-Type", getContentType());
/* 138 */     connection.setRequestProperty("Content-Length", Integer.toString(contentLength));
/*     */     
/* 140 */     LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
/* 141 */     if (localeContext != null) {
/* 142 */       Locale locale = localeContext.getLocale();
/* 143 */       if (locale != null) {
/* 144 */         connection.setRequestProperty("Accept-Language", StringUtils.toLanguageTag(locale));
/*     */       }
/*     */     }
/*     */     
/* 148 */     if (isAcceptGzipEncoding()) {
/* 149 */       connection.setRequestProperty("Accept-Encoding", "gzip");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeRequestBody(HttpInvokerClientConfiguration config, HttpURLConnection con, ByteArrayOutputStream baos)
/*     */     throws IOException
/*     */   {
/* 170 */     baos.writeTo(con.getOutputStream());
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
/*     */   protected void validateResponse(HttpInvokerClientConfiguration config, HttpURLConnection con)
/*     */     throws IOException
/*     */   {
/* 186 */     if (con.getResponseCode() >= 300)
/*     */     {
/*     */ 
/* 189 */       throw new IOException("Did not receive successful HTTP response: status code = " + con.getResponseCode() + ", status message = [" + con.getResponseMessage() + "]");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected InputStream readResponseBody(HttpInvokerClientConfiguration config, HttpURLConnection con)
/*     */     throws IOException
/*     */   {
/* 212 */     if (isGzipResponse(con))
/*     */     {
/* 214 */       return new GZIPInputStream(con.getInputStream());
/*     */     }
/*     */     
/*     */ 
/* 218 */     return con.getInputStream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isGzipResponse(HttpURLConnection con)
/*     */   {
/* 229 */     String encodingHeader = con.getHeaderField("Content-Encoding");
/* 230 */     return (encodingHeader != null) && (encodingHeader.toLowerCase().contains("gzip"));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\httpinvoker\SimpleHttpInvokerRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */