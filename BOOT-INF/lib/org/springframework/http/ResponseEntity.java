/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseEntity<T>
/*     */   extends HttpEntity<T>
/*     */ {
/*     */   private final Object status;
/*     */   
/*     */   public ResponseEntity(HttpStatus status)
/*     */   {
/*  78 */     this(null, null, status);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResponseEntity(T body, HttpStatus status)
/*     */   {
/*  87 */     this(body, null, status);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status)
/*     */   {
/*  96 */     this(null, headers, status);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus status)
/*     */   {
/* 106 */     super(body, headers);
/* 107 */     Assert.notNull(status, "HttpStatus must not be null");
/* 108 */     this.status = status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ResponseEntity(T body, MultiValueMap<String, String> headers, Object status)
/*     */   {
/* 119 */     super(body, headers);
/* 120 */     this.status = status;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpStatus getStatusCode()
/*     */   {
/* 129 */     if ((this.status instanceof HttpStatus)) {
/* 130 */       return (HttpStatus)this.status;
/*     */     }
/*     */     
/* 133 */     return HttpStatus.valueOf(((Integer)this.status).intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatusCodeValue()
/*     */   {
/* 143 */     if ((this.status instanceof HttpStatus)) {
/* 144 */       return ((HttpStatus)this.status).value();
/*     */     }
/*     */     
/* 147 */     return ((Integer)this.status).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 154 */     if (this == other) {
/* 155 */       return true;
/*     */     }
/* 157 */     if (!super.equals(other)) {
/* 158 */       return false;
/*     */     }
/* 160 */     ResponseEntity<?> otherEntity = (ResponseEntity)other;
/* 161 */     return ObjectUtils.nullSafeEquals(this.status, otherEntity.status);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 166 */     return super.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.status);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 171 */     StringBuilder builder = new StringBuilder("<");
/* 172 */     builder.append(this.status.toString());
/* 173 */     if ((this.status instanceof HttpStatus)) {
/* 174 */       builder.append(' ');
/* 175 */       builder.append(((HttpStatus)this.status).getReasonPhrase());
/*     */     }
/* 177 */     builder.append(',');
/* 178 */     T body = getBody();
/* 179 */     HttpHeaders headers = getHeaders();
/* 180 */     if (body != null) {
/* 181 */       builder.append(body);
/* 182 */       if (headers != null) {
/* 183 */         builder.append(',');
/*     */       }
/*     */     }
/* 186 */     if (headers != null) {
/* 187 */       builder.append(headers);
/*     */     }
/* 189 */     builder.append('>');
/* 190 */     return builder.toString();
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
/*     */   public static BodyBuilder status(HttpStatus status)
/*     */   {
/* 203 */     Assert.notNull(status, "HttpStatus must not be null");
/* 204 */     return new DefaultBuilder(status);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BodyBuilder status(int status)
/*     */   {
/* 214 */     return new DefaultBuilder(Integer.valueOf(status));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BodyBuilder ok()
/*     */   {
/* 223 */     return status(HttpStatus.OK);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> ResponseEntity<T> ok(T body)
/*     */   {
/* 233 */     BodyBuilder builder = ok();
/* 234 */     return builder.body(body);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BodyBuilder created(URI location)
/*     */   {
/* 245 */     BodyBuilder builder = status(HttpStatus.CREATED);
/* 246 */     return (BodyBuilder)builder.location(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BodyBuilder accepted()
/*     */   {
/* 255 */     return status(HttpStatus.ACCEPTED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HeadersBuilder<?> noContent()
/*     */   {
/* 264 */     return status(HttpStatus.NO_CONTENT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BodyBuilder badRequest()
/*     */   {
/* 273 */     return status(HttpStatus.BAD_REQUEST);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HeadersBuilder<?> notFound()
/*     */   {
/* 282 */     return status(HttpStatus.NOT_FOUND);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BodyBuilder unprocessableEntity()
/*     */   {
/* 292 */     return status(HttpStatus.UNPROCESSABLE_ENTITY);
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
/*     */   private static class DefaultBuilder
/*     */     implements ResponseEntity.BodyBuilder
/*     */   {
/*     */     private final Object statusCode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 427 */     private final HttpHeaders headers = new HttpHeaders();
/*     */     
/*     */     public DefaultBuilder(Object statusCode) {
/* 430 */       this.statusCode = statusCode;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder header(String headerName, String... headerValues)
/*     */     {
/* 435 */       for (String headerValue : headerValues) {
/* 436 */         this.headers.add(headerName, headerValue);
/*     */       }
/* 438 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder headers(HttpHeaders headers)
/*     */     {
/* 443 */       if (headers != null) {
/* 444 */         this.headers.putAll(headers);
/*     */       }
/* 446 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder allow(HttpMethod... allowedMethods)
/*     */     {
/* 451 */       this.headers.setAllow(new LinkedHashSet(Arrays.asList(allowedMethods)));
/* 452 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder contentLength(long contentLength)
/*     */     {
/* 457 */       this.headers.setContentLength(contentLength);
/* 458 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder contentType(MediaType contentType)
/*     */     {
/* 463 */       this.headers.setContentType(contentType);
/* 464 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder eTag(String etag)
/*     */     {
/* 469 */       if (etag != null) {
/* 470 */         if ((!etag.startsWith("\"")) && (!etag.startsWith("W/\""))) {
/* 471 */           etag = "\"" + etag;
/*     */         }
/* 473 */         if (!etag.endsWith("\"")) {
/* 474 */           etag = etag + "\"";
/*     */         }
/*     */       }
/* 477 */       this.headers.setETag(etag);
/* 478 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder lastModified(long date)
/*     */     {
/* 483 */       this.headers.setLastModified(date);
/* 484 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder location(URI location)
/*     */     {
/* 489 */       this.headers.setLocation(location);
/* 490 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder cacheControl(CacheControl cacheControl)
/*     */     {
/* 495 */       String ccValue = cacheControl.getHeaderValue();
/* 496 */       if (ccValue != null) {
/* 497 */         this.headers.setCacheControl(cacheControl.getHeaderValue());
/*     */       }
/* 499 */       return this;
/*     */     }
/*     */     
/*     */     public ResponseEntity.BodyBuilder varyBy(String... requestHeaders)
/*     */     {
/* 504 */       this.headers.setVary(Arrays.asList(requestHeaders));
/* 505 */       return this;
/*     */     }
/*     */     
/*     */     public <T> ResponseEntity<T> build()
/*     */     {
/* 510 */       return body(null);
/*     */     }
/*     */     
/*     */     public <T> ResponseEntity<T> body(T body)
/*     */     {
/* 515 */       return new ResponseEntity(body, this.headers, this.statusCode, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface BodyBuilder
/*     */     extends ResponseEntity.HeadersBuilder<BodyBuilder>
/*     */   {
/*     */     public abstract BodyBuilder contentLength(long paramLong);
/*     */     
/*     */     public abstract BodyBuilder contentType(MediaType paramMediaType);
/*     */     
/*     */     public abstract <T> ResponseEntity<T> body(T paramT);
/*     */   }
/*     */   
/*     */   public static abstract interface HeadersBuilder<B extends HeadersBuilder<B>>
/*     */   {
/*     */     public abstract B header(String paramString, String... paramVarArgs);
/*     */     
/*     */     public abstract B headers(HttpHeaders paramHttpHeaders);
/*     */     
/*     */     public abstract B allow(HttpMethod... paramVarArgs);
/*     */     
/*     */     public abstract B eTag(String paramString);
/*     */     
/*     */     public abstract B lastModified(long paramLong);
/*     */     
/*     */     public abstract B location(URI paramURI);
/*     */     
/*     */     public abstract B cacheControl(CacheControl paramCacheControl);
/*     */     
/*     */     public abstract B varyBy(String... paramVarArgs);
/*     */     
/*     */     public abstract <T> ResponseEntity<T> build();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\ResponseEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */