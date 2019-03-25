/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.server.ServerHttpResponse;
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
/*     */ public class SseEmitter
/*     */   extends ResponseBodyEmitter
/*     */ {
/*  40 */   static final MediaType TEXT_PLAIN = new MediaType("text", "plain", Charset.forName("UTF-8"));
/*     */   
/*  42 */   static final MediaType UTF8_TEXT_EVENTSTREAM = new MediaType("text", "event-stream", Charset.forName("UTF-8"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SseEmitter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SseEmitter(Long timeout)
/*     */   {
/*  61 */     super(timeout);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void extendResponse(ServerHttpResponse outputMessage)
/*     */   {
/*  67 */     super.extendResponse(outputMessage);
/*     */     
/*  69 */     HttpHeaders headers = outputMessage.getHeaders();
/*  70 */     if (headers.getContentType() == null) {
/*  71 */       headers.setContentType(UTF8_TEXT_EVENTSTREAM);
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
/*     */   public void send(Object object)
/*     */     throws IOException
/*     */   {
/*  89 */     send(object, null);
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
/*     */   public void send(Object object, MediaType mediaType)
/*     */     throws IOException
/*     */   {
/* 106 */     if (object != null) {
/* 107 */       send(event().data(object, mediaType));
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
/*     */   public void send(SseEventBuilder builder)
/*     */     throws IOException
/*     */   {
/* 123 */     Set<ResponseBodyEmitter.DataWithMediaType> dataToSend = builder.build();
/* 124 */     synchronized (this) {
/* 125 */       for (ResponseBodyEmitter.DataWithMediaType entry : dataToSend) {
/* 126 */         super.send(entry.getData(), entry.getMediaType());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static SseEventBuilder event()
/*     */   {
/* 133 */     return new SseEventBuilderImpl(null);
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
/*     */   private static class SseEventBuilderImpl
/*     */     implements SseEmitter.SseEventBuilder
/*     */   {
/* 186 */     private final Set<ResponseBodyEmitter.DataWithMediaType> dataToSend = new LinkedHashSet(4);
/*     */     
/*     */     private StringBuilder sb;
/*     */     
/*     */     public SseEmitter.SseEventBuilder comment(String comment)
/*     */     {
/* 192 */       append(":").append(comment != null ? comment : "").append("\n");
/* 193 */       return this;
/*     */     }
/*     */     
/*     */     public SseEmitter.SseEventBuilder name(String name)
/*     */     {
/* 198 */       append("event:").append(name != null ? name : "").append("\n");
/* 199 */       return this;
/*     */     }
/*     */     
/*     */     public SseEmitter.SseEventBuilder id(String id)
/*     */     {
/* 204 */       append("id:").append(id != null ? id : "").append("\n");
/* 205 */       return this;
/*     */     }
/*     */     
/*     */     public SseEmitter.SseEventBuilder reconnectTime(long reconnectTimeMillis)
/*     */     {
/* 210 */       append("retry:").append(String.valueOf(reconnectTimeMillis)).append("\n");
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public SseEmitter.SseEventBuilder data(Object object)
/*     */     {
/* 216 */       return data(object, null);
/*     */     }
/*     */     
/*     */     public SseEmitter.SseEventBuilder data(Object object, MediaType mediaType)
/*     */     {
/* 221 */       append("data:");
/* 222 */       saveAppendedText();
/* 223 */       this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(object, mediaType));
/* 224 */       append("\n");
/* 225 */       return this;
/*     */     }
/*     */     
/*     */     SseEventBuilderImpl append(String text) {
/* 229 */       if (this.sb == null) {
/* 230 */         this.sb = new StringBuilder();
/*     */       }
/* 232 */       this.sb.append(text);
/* 233 */       return this;
/*     */     }
/*     */     
/*     */     public Set<ResponseBodyEmitter.DataWithMediaType> build()
/*     */     {
/* 238 */       if ((!StringUtils.hasLength(this.sb)) && (this.dataToSend.isEmpty())) {
/* 239 */         return Collections.emptySet();
/*     */       }
/* 241 */       append("\n");
/* 242 */       saveAppendedText();
/* 243 */       return this.dataToSend;
/*     */     }
/*     */     
/*     */     private void saveAppendedText() {
/* 247 */       if (this.sb != null) {
/* 248 */         this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(this.sb.toString(), SseEmitter.TEXT_PLAIN));
/* 249 */         this.sb = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface SseEventBuilder
/*     */   {
/*     */     public abstract SseEventBuilder comment(String paramString);
/*     */     
/*     */     public abstract SseEventBuilder name(String paramString);
/*     */     
/*     */     public abstract SseEventBuilder id(String paramString);
/*     */     
/*     */     public abstract SseEventBuilder reconnectTime(long paramLong);
/*     */     
/*     */     public abstract SseEventBuilder data(Object paramObject);
/*     */     
/*     */     public abstract SseEventBuilder data(Object paramObject, MediaType paramMediaType);
/*     */     
/*     */     public abstract Set<ResponseBodyEmitter.DataWithMediaType> build();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\SseEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */