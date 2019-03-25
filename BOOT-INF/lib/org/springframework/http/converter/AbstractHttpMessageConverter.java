/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.http.StreamingHttpOutputMessage.Body;
/*     */ import org.springframework.util.Assert;
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
/*     */ public abstract class AbstractHttpMessageConverter<T>
/*     */   implements HttpMessageConverter<T>
/*     */ {
/*  52 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private List<MediaType> supportedMediaTypes = Collections.emptyList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Charset defaultCharset;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHttpMessageConverter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHttpMessageConverter(MediaType supportedMediaType)
/*     */   {
/*  71 */     setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHttpMessageConverter(MediaType... supportedMediaTypes)
/*     */   {
/*  79 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHttpMessageConverter(Charset defaultCharset, MediaType... supportedMediaTypes)
/*     */   {
/*  90 */     this.defaultCharset = defaultCharset;
/*  91 */     setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes)
/*     */   {
/*  99 */     Assert.notEmpty(supportedMediaTypes, "MediaType List must not be empty");
/* 100 */     this.supportedMediaTypes = new ArrayList(supportedMediaTypes);
/*     */   }
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes()
/*     */   {
/* 105 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultCharset(Charset defaultCharset)
/*     */   {
/* 113 */     this.defaultCharset = defaultCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getDefaultCharset()
/*     */   {
/* 121 */     return this.defaultCharset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 132 */     return (supports(clazz)) && (canRead(mediaType));
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
/*     */   protected boolean canRead(MediaType mediaType)
/*     */   {
/* 145 */     if (mediaType == null) {
/* 146 */       return true;
/*     */     }
/* 148 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 149 */       if (supportedMediaType.includes(mediaType)) {
/* 150 */         return true;
/*     */       }
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 164 */     return (supports(clazz)) && (canWrite(mediaType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canWrite(MediaType mediaType)
/*     */   {
/* 176 */     if ((mediaType == null) || (MediaType.ALL.equals(mediaType))) {
/* 177 */       return true;
/*     */     }
/* 179 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 180 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/* 181 */         return true;
/*     */       }
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
/*     */     throws IOException
/*     */   {
/* 193 */     return (T)readInternal(clazz, inputMessage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void write(final T t, MediaType contentType, HttpOutputMessage outputMessage)
/*     */     throws IOException, HttpMessageNotWritableException
/*     */   {
/* 204 */     final HttpHeaders headers = outputMessage.getHeaders();
/* 205 */     addDefaultHeaders(headers, t, contentType);
/*     */     
/* 207 */     if ((outputMessage instanceof StreamingHttpOutputMessage)) {
/* 208 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/*     */       
/* 210 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */       {
/*     */         public void writeTo(final OutputStream outputStream) throws IOException {
/* 213 */           AbstractHttpMessageConverter.this.writeInternal(t, new HttpOutputMessage()
/*     */           {
/*     */             public OutputStream getBody() throws IOException {
/* 216 */               return outputStream;
/*     */             }
/*     */             
/*     */             public HttpHeaders getHeaders() {
/* 220 */               return AbstractHttpMessageConverter.1.this.val$headers;
/*     */             }
/*     */           });
/*     */         }
/*     */       });
/*     */     }
/*     */     else {
/* 227 */       writeInternal(t, outputMessage);
/* 228 */       outputMessage.getBody().flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addDefaultHeaders(HttpHeaders headers, T t, MediaType contentType)
/*     */     throws IOException
/*     */   {
/* 240 */     if (headers.getContentType() == null) {
/* 241 */       MediaType contentTypeToUse = contentType;
/* 242 */       if ((contentType == null) || (contentType.isWildcardType()) || (contentType.isWildcardSubtype())) {
/* 243 */         contentTypeToUse = getDefaultContentType(t);
/*     */       }
/* 245 */       else if (MediaType.APPLICATION_OCTET_STREAM.equals(contentType)) {
/* 246 */         MediaType mediaType = getDefaultContentType(t);
/* 247 */         contentTypeToUse = mediaType != null ? mediaType : contentTypeToUse;
/*     */       }
/* 249 */       if (contentTypeToUse != null) {
/* 250 */         if (contentTypeToUse.getCharset() == null) {
/* 251 */           Charset defaultCharset = getDefaultCharset();
/* 252 */           if (defaultCharset != null) {
/* 253 */             contentTypeToUse = new MediaType(contentTypeToUse, defaultCharset);
/*     */           }
/*     */         }
/* 256 */         headers.setContentType(contentTypeToUse);
/*     */       }
/*     */     }
/* 259 */     if ((headers.getContentLength() < 0L) && (!headers.containsKey("Transfer-Encoding"))) {
/* 260 */       Long contentLength = getContentLength(t, headers.getContentType());
/* 261 */       if (contentLength != null) {
/* 262 */         headers.setContentLength(contentLength.longValue());
/*     */       }
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
/*     */   protected MediaType getDefaultContentType(T t)
/*     */     throws IOException
/*     */   {
/* 277 */     List<MediaType> mediaTypes = getSupportedMediaTypes();
/* 278 */     return !mediaTypes.isEmpty() ? (MediaType)mediaTypes.get(0) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Long getContentLength(T t, MediaType contentType)
/*     */     throws IOException
/*     */   {
/* 289 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract boolean supports(Class<?> paramClass);
/*     */   
/*     */   protected abstract T readInternal(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage)
/*     */     throws IOException, HttpMessageNotReadableException;
/*     */   
/*     */   protected abstract void writeInternal(T paramT, HttpOutputMessage paramHttpOutputMessage)
/*     */     throws IOException, HttpMessageNotWritableException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\AbstractHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */