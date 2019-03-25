/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GsonHttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  59 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*     */ 
/*  62 */   private Gson gson = new Gson();
/*     */   
/*     */ 
/*     */   private String jsonPrefix;
/*     */   
/*     */ 
/*     */ 
/*     */   public GsonHttpMessageConverter()
/*     */   {
/*  71 */     super(new MediaType[] { MediaType.APPLICATION_JSON, new MediaType("application", "*+json") });
/*  72 */     setDefaultCharset(DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGson(Gson gson)
/*     */   {
/*  83 */     Assert.notNull(gson, "'gson' is required");
/*  84 */     this.gson = gson;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Gson getGson()
/*     */   {
/*  91 */     return this.gson;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJsonPrefix(String jsonPrefix)
/*     */   {
/*  99 */     this.jsonPrefix = jsonPrefix;
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
/*     */   public void setPrefixJson(boolean prefixJson)
/*     */   {
/* 112 */     this.jsonPrefix = (prefixJson ? ")]}', " : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
/*     */     throws IOException, HttpMessageNotReadableException
/*     */   {
/* 121 */     TypeToken<?> token = getTypeToken(type);
/* 122 */     return readTypeToken(token, inputMessage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
/*     */     throws IOException, HttpMessageNotReadableException
/*     */   {
/* 130 */     TypeToken<?> token = getTypeToken(clazz);
/* 131 */     return readTypeToken(token, inputMessage);
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
/*     */   @Deprecated
/*     */   protected TypeToken<?> getTypeToken(Type type)
/*     */   {
/* 155 */     return TypeToken.get(type);
/*     */   }
/*     */   
/*     */   private Object readTypeToken(TypeToken<?> token, HttpInputMessage inputMessage) throws IOException {
/* 159 */     Reader json = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
/*     */     try {
/* 161 */       return this.gson.fromJson(json, token.getType());
/*     */     }
/*     */     catch (JsonParseException ex) {
/* 164 */       throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private Charset getCharset(HttpHeaders headers) {
/* 169 */     if ((headers == null) || (headers.getContentType() == null) || (headers.getContentType().getCharset() == null)) {
/* 170 */       return DEFAULT_CHARSET;
/*     */     }
/* 172 */     return headers.getContentType().getCharset();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage)
/*     */     throws IOException, HttpMessageNotWritableException
/*     */   {
/* 179 */     Charset charset = getCharset(outputMessage.getHeaders());
/* 180 */     OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
/*     */     try {
/* 182 */       if (this.jsonPrefix != null) {
/* 183 */         writer.append(this.jsonPrefix);
/*     */       }
/* 185 */       if (type != null) {
/* 186 */         this.gson.toJson(o, type, writer);
/*     */       }
/*     */       else {
/* 189 */         this.gson.toJson(o, writer);
/*     */       }
/* 191 */       writer.close();
/*     */     }
/*     */     catch (JsonIOException ex) {
/* 194 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\json\GsonHttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */