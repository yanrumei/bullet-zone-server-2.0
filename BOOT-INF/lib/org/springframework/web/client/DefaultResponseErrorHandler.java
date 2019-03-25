/*     */ package org.springframework.web.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.HttpStatus.Series;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.client.ClientHttpResponse;
/*     */ import org.springframework.util.FileCopyUtils;
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
/*     */ public class DefaultResponseErrorHandler
/*     */   implements ResponseErrorHandler
/*     */ {
/*     */   public boolean hasError(ClientHttpResponse response)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  51 */       return hasError(getHttpStatusCode(response));
/*     */     }
/*     */     catch (UnknownHttpStatusCodeException ex) {}
/*  54 */     return false;
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
/*     */   protected boolean hasError(HttpStatus statusCode)
/*     */   {
/*  69 */     return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR) || 
/*  70 */       (statusCode.series() == HttpStatus.Series.SERVER_ERROR);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleError(ClientHttpResponse response)
/*     */     throws IOException
/*     */   {
/*  81 */     HttpStatus statusCode = getHttpStatusCode(response);
/*  82 */     switch (statusCode.series())
/*     */     {
/*     */     case CLIENT_ERROR: 
/*  85 */       throw new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
/*     */     
/*     */     case SERVER_ERROR: 
/*  88 */       throw new HttpServerErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
/*     */     }
/*  90 */     throw new RestClientException("Unknown status code [" + statusCode + "]");
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
/*     */   protected HttpStatus getHttpStatusCode(ClientHttpResponse response)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 106 */       return response.getStatusCode();
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 110 */       throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected byte[] getResponseBody(ClientHttpResponse response)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       return FileCopyUtils.copyToByteArray(response.getBody());
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/*     */ 
/* 128 */     return new byte[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Charset getCharset(ClientHttpResponse response)
/*     */   {
/* 138 */     HttpHeaders headers = response.getHeaders();
/* 139 */     MediaType contentType = headers.getContentType();
/* 140 */     return contentType != null ? contentType.getCharset() : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\client\DefaultResponseErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */