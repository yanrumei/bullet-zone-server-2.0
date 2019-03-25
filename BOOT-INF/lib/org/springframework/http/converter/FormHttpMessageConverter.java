/*     */ package org.springframework.http.converter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.StreamingHttpOutputMessage;
/*     */ import org.springframework.http.StreamingHttpOutputMessage.Body;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StreamUtils;
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
/*     */ public class FormHttpMessageConverter
/*     */   implements HttpMessageConverter<MultiValueMap<String, ?>>
/*     */ {
/*  91 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*  94 */   private List<MediaType> supportedMediaTypes = new ArrayList();
/*     */   
/*  96 */   private List<HttpMessageConverter<?>> partConverters = new ArrayList();
/*     */   
/*  98 */   private Charset charset = DEFAULT_CHARSET;
/*     */   
/*     */   private Charset multipartCharset;
/*     */   
/*     */   public FormHttpMessageConverter()
/*     */   {
/* 104 */     this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
/* 105 */     this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
/*     */     
/* 107 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 108 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*     */     
/* 110 */     this.partConverters.add(new ByteArrayHttpMessageConverter());
/* 111 */     this.partConverters.add(stringHttpMessageConverter);
/* 112 */     this.partConverters.add(new ResourceHttpMessageConverter());
/*     */     
/* 114 */     applyDefaultCharset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes)
/*     */   {
/* 122 */     this.supportedMediaTypes = supportedMediaTypes;
/*     */   }
/*     */   
/*     */   public List<MediaType> getSupportedMediaTypes()
/*     */   {
/* 127 */     return Collections.unmodifiableList(this.supportedMediaTypes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPartConverters(List<HttpMessageConverter<?>> partConverters)
/*     */   {
/* 135 */     Assert.notEmpty(partConverters, "'partConverters' must not be empty");
/* 136 */     this.partConverters = partConverters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPartConverter(HttpMessageConverter<?> partConverter)
/*     */   {
/* 144 */     Assert.notNull(partConverter, "'partConverter' must not be null");
/* 145 */     this.partConverters.add(partConverter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 157 */     if (charset != this.charset) {
/* 158 */       this.charset = (charset != null ? charset : DEFAULT_CHARSET);
/* 159 */       applyDefaultCharset();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void applyDefaultCharset()
/*     */   {
/* 167 */     for (HttpMessageConverter<?> candidate : this.partConverters) {
/* 168 */       if ((candidate instanceof AbstractHttpMessageConverter)) {
/* 169 */         AbstractHttpMessageConverter<?> converter = (AbstractHttpMessageConverter)candidate;
/*     */         
/* 171 */         if (converter.getDefaultCharset() != null) {
/* 172 */           converter.setDefaultCharset(this.charset);
/*     */         }
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
/*     */   public void setMultipartCharset(Charset charset)
/*     */   {
/* 187 */     this.multipartCharset = charset;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 193 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/* 194 */       return false;
/*     */     }
/* 196 */     if (mediaType == null) {
/* 197 */       return true;
/*     */     }
/* 199 */     for (MediaType supportedMediaType : getSupportedMediaTypes())
/*     */     {
/* 201 */       if ((!supportedMediaType.equals(MediaType.MULTIPART_FORM_DATA)) && (supportedMediaType.includes(mediaType))) {
/* 202 */         return true;
/*     */       }
/*     */     }
/* 205 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 210 */     if (!MultiValueMap.class.isAssignableFrom(clazz)) {
/* 211 */       return false;
/*     */     }
/* 213 */     if ((mediaType == null) || (MediaType.ALL.equals(mediaType))) {
/* 214 */       return true;
/*     */     }
/* 216 */     for (MediaType supportedMediaType : getSupportedMediaTypes()) {
/* 217 */       if (supportedMediaType.isCompatibleWith(mediaType)) {
/* 218 */         return true;
/*     */       }
/*     */     }
/* 221 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz, HttpInputMessage inputMessage)
/*     */     throws IOException, HttpMessageNotReadableException
/*     */   {
/* 228 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 229 */     Charset charset = contentType.getCharset() != null ? contentType.getCharset() : this.charset;
/* 230 */     String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
/*     */     
/* 232 */     String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
/* 233 */     MultiValueMap<String, String> result = new LinkedMultiValueMap(pairs.length);
/* 234 */     for (String pair : pairs) {
/* 235 */       int idx = pair.indexOf('=');
/* 236 */       if (idx == -1) {
/* 237 */         result.add(URLDecoder.decode(pair, charset.name()), null);
/*     */       }
/*     */       else {
/* 240 */         String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
/* 241 */         String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
/* 242 */         result.add(name, value);
/*     */       }
/*     */     }
/* 245 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void write(MultiValueMap<String, ?> map, MediaType contentType, HttpOutputMessage outputMessage)
/*     */     throws IOException, HttpMessageNotWritableException
/*     */   {
/* 253 */     if (!isMultipart(map, contentType)) {
/* 254 */       writeForm(map, contentType, outputMessage);
/*     */     }
/*     */     else {
/* 257 */       writeMultipart(map, outputMessage);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isMultipart(MultiValueMap<String, ?> map, MediaType contentType)
/*     */   {
/* 263 */     if (contentType != null) {
/* 264 */       return MediaType.MULTIPART_FORM_DATA.includes(contentType);
/*     */     }
/* 266 */     for (String name : map.keySet()) {
/* 267 */       for (Object value : (List)map.get(name)) {
/* 268 */         if ((value != null) && (!(value instanceof String))) {
/* 269 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 273 */     return false;
/*     */   }
/*     */   
/*     */   private void writeForm(MultiValueMap<String, String> form, MediaType contentType, HttpOutputMessage outputMessage) throws IOException
/*     */   {
/*     */     Charset charset;
/*     */     Charset charset;
/* 280 */     if (contentType != null) {
/* 281 */       outputMessage.getHeaders().setContentType(contentType);
/* 282 */       charset = contentType.getCharset() != null ? contentType.getCharset() : this.charset;
/*     */     }
/*     */     else {
/* 285 */       outputMessage.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
/* 286 */       charset = this.charset;
/*     */     }
/* 288 */     StringBuilder builder = new StringBuilder();
/* 289 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();) {
/* 290 */       String name = (String)nameIterator.next();
/* 291 */       for (Iterator<String> valueIterator = ((List)form.get(name)).iterator(); valueIterator.hasNext();) {
/* 292 */         String value = (String)valueIterator.next();
/* 293 */         builder.append(URLEncoder.encode(name, charset.name()));
/* 294 */         if (value != null) {
/* 295 */           builder.append('=');
/* 296 */           builder.append(URLEncoder.encode(value, charset.name()));
/* 297 */           if (valueIterator.hasNext()) {
/* 298 */             builder.append('&');
/*     */           }
/*     */         }
/*     */       }
/* 302 */       if (nameIterator.hasNext()) {
/* 303 */         builder.append('&');
/*     */       }
/*     */     }
/* 306 */     final byte[] bytes = builder.toString().getBytes(charset.name());
/* 307 */     outputMessage.getHeaders().setContentLength(bytes.length);
/*     */     
/* 309 */     if ((outputMessage instanceof StreamingHttpOutputMessage)) {
/* 310 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 311 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */       {
/*     */         public void writeTo(OutputStream outputStream) throws IOException {
/* 314 */           StreamUtils.copy(bytes, outputStream);
/*     */         }
/*     */       });
/*     */     }
/*     */     else {
/* 319 */       StreamUtils.copy(bytes, outputMessage.getBody());
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeMultipart(final MultiValueMap<String, Object> parts, HttpOutputMessage outputMessage) throws IOException {
/* 324 */     final byte[] boundary = generateMultipartBoundary();
/* 325 */     Map<String, String> parameters = Collections.singletonMap("boundary", new String(boundary, "US-ASCII"));
/*     */     
/* 327 */     MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
/* 328 */     HttpHeaders headers = outputMessage.getHeaders();
/* 329 */     headers.setContentType(contentType);
/*     */     
/* 331 */     if ((outputMessage instanceof StreamingHttpOutputMessage)) {
/* 332 */       StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage)outputMessage;
/* 333 */       streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body()
/*     */       {
/*     */         public void writeTo(OutputStream outputStream) throws IOException {
/* 336 */           FormHttpMessageConverter.this.writeParts(outputStream, parts, boundary);
/* 337 */           FormHttpMessageConverter.writeEnd(outputStream, boundary);
/*     */         }
/*     */       });
/*     */     }
/*     */     else {
/* 342 */       writeParts(outputMessage.getBody(), parts, boundary);
/* 343 */       writeEnd(outputMessage.getBody(), boundary);
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeParts(OutputStream os, MultiValueMap<String, Object> parts, byte[] boundary) throws IOException {
/* 348 */     for (Map.Entry<String, List<Object>> entry : parts.entrySet()) {
/* 349 */       name = (String)entry.getKey();
/* 350 */       for (Object part : (List)entry.getValue()) {
/* 351 */         if (part != null) {
/* 352 */           writeBoundary(os, boundary);
/* 353 */           writePart(name, getHttpEntity(part), os);
/* 354 */           writeNewLine(os);
/*     */         }
/*     */       }
/*     */     }
/*     */     String name;
/*     */   }
/*     */   
/*     */   private void writePart(String name, HttpEntity<?> partEntity, OutputStream os) throws IOException {
/* 362 */     Object partBody = partEntity.getBody();
/* 363 */     Class<?> partType = partBody.getClass();
/* 364 */     HttpHeaders partHeaders = partEntity.getHeaders();
/* 365 */     MediaType partContentType = partHeaders.getContentType();
/* 366 */     for (HttpMessageConverter<?> messageConverter : this.partConverters) {
/* 367 */       if (messageConverter.canWrite(partType, partContentType)) {
/* 368 */         HttpOutputMessage multipartMessage = new MultipartHttpOutputMessage(os);
/* 369 */         multipartMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
/* 370 */         if (!partHeaders.isEmpty()) {
/* 371 */           multipartMessage.getHeaders().putAll(partHeaders);
/*     */         }
/* 373 */         messageConverter.write(partBody, partContentType, multipartMessage);
/* 374 */         return;
/*     */       }
/*     */     }
/*     */     
/* 378 */     throw new HttpMessageNotWritableException("Could not write request: no suitable HttpMessageConverter found for request type [" + partType.getName() + "]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected byte[] generateMultipartBoundary()
/*     */   {
/* 388 */     return MimeTypeUtils.generateMultipartBoundary();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpEntity<?> getHttpEntity(Object part)
/*     */   {
/* 398 */     return (part instanceof HttpEntity) ? (HttpEntity)part : new HttpEntity(part);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getFilename(Object part)
/*     */   {
/* 410 */     if ((part instanceof Resource)) {
/* 411 */       Resource resource = (Resource)part;
/* 412 */       String filename = resource.getFilename();
/* 413 */       if ((filename != null) && (this.multipartCharset != null)) {
/* 414 */         filename = MimeDelegate.encode(filename, this.multipartCharset.name());
/*     */       }
/* 416 */       return filename;
/*     */     }
/*     */     
/* 419 */     return null;
/*     */   }
/*     */   
/*     */   private void writeBoundary(OutputStream os, byte[] boundary)
/*     */     throws IOException
/*     */   {
/* 425 */     os.write(45);
/* 426 */     os.write(45);
/* 427 */     os.write(boundary);
/* 428 */     writeNewLine(os);
/*     */   }
/*     */   
/*     */   private static void writeEnd(OutputStream os, byte[] boundary) throws IOException {
/* 432 */     os.write(45);
/* 433 */     os.write(45);
/* 434 */     os.write(boundary);
/* 435 */     os.write(45);
/* 436 */     os.write(45);
/* 437 */     writeNewLine(os);
/*     */   }
/*     */   
/*     */   private static void writeNewLine(OutputStream os) throws IOException {
/* 441 */     os.write(13);
/* 442 */     os.write(10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class MultipartHttpOutputMessage
/*     */     implements HttpOutputMessage
/*     */   {
/*     */     private final OutputStream outputStream;
/*     */     
/*     */ 
/* 454 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/* 456 */     private boolean headersWritten = false;
/*     */     
/*     */     public MultipartHttpOutputMessage(OutputStream outputStream) {
/* 459 */       this.outputStream = outputStream;
/*     */     }
/*     */     
/*     */     public HttpHeaders getHeaders()
/*     */     {
/* 464 */       return this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers;
/*     */     }
/*     */     
/*     */     public OutputStream getBody() throws IOException
/*     */     {
/* 469 */       writeHeaders();
/* 470 */       return this.outputStream;
/*     */     }
/*     */     
/*     */     private void writeHeaders() throws IOException {
/* 474 */       if (!this.headersWritten) {
/* 475 */         for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/* 476 */           headerName = getAsciiBytes((String)entry.getKey());
/* 477 */           for (String headerValueString : (List)entry.getValue()) {
/* 478 */             byte[] headerValue = getAsciiBytes(headerValueString);
/* 479 */             this.outputStream.write(headerName);
/* 480 */             this.outputStream.write(58);
/* 481 */             this.outputStream.write(32);
/* 482 */             this.outputStream.write(headerValue);
/* 483 */             FormHttpMessageConverter.writeNewLine(this.outputStream);
/*     */           } }
/*     */         byte[] headerName;
/* 486 */         FormHttpMessageConverter.writeNewLine(this.outputStream);
/* 487 */         this.headersWritten = true;
/*     */       }
/*     */     }
/*     */     
/*     */     private byte[] getAsciiBytes(String name) {
/*     */       try {
/* 493 */         return name.getBytes("US-ASCII");
/*     */       }
/*     */       catch (UnsupportedEncodingException ex)
/*     */       {
/* 497 */         throw new IllegalStateException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class MimeDelegate
/*     */   {
/*     */     public static String encode(String value, String charset)
/*     */     {
/*     */       try
/*     */       {
/* 510 */         return MimeUtility.encodeText(value, charset, null);
/*     */       }
/*     */       catch (UnsupportedEncodingException ex) {
/* 513 */         throw new IllegalStateException(ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\FormHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */