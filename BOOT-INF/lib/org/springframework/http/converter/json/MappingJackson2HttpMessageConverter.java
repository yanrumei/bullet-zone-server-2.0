/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import org.springframework.http.MediaType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingJackson2HttpMessageConverter
/*     */   extends AbstractJackson2HttpMessageConverter
/*     */ {
/*     */   private String jsonPrefix;
/*     */   
/*     */   public MappingJackson2HttpMessageConverter()
/*     */   {
/*  57 */     this(Jackson2ObjectMapperBuilder.json().build());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappingJackson2HttpMessageConverter(ObjectMapper objectMapper)
/*     */   {
/*  66 */     super(objectMapper, new MediaType[] { MediaType.APPLICATION_JSON, new MediaType("application", "*+json") });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJsonPrefix(String jsonPrefix)
/*     */   {
/*  75 */     this.jsonPrefix = jsonPrefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrefixJson(boolean prefixJson)
/*     */   {
/*  86 */     this.jsonPrefix = (prefixJson ? ")]}', " : null);
/*     */   }
/*     */   
/*     */   protected void writePrefix(JsonGenerator generator, Object object)
/*     */     throws IOException
/*     */   {
/*  92 */     if (this.jsonPrefix != null) {
/*  93 */       generator.writeRaw(this.jsonPrefix);
/*     */     }
/*     */     
/*  96 */     String jsonpFunction = (object instanceof MappingJacksonValue) ? ((MappingJacksonValue)object).getJsonpFunction() : null;
/*  97 */     if (jsonpFunction != null) {
/*  98 */       generator.writeRaw("/**/");
/*  99 */       generator.writeRaw(jsonpFunction + "(");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object)
/*     */     throws IOException
/*     */   {
/* 106 */     String jsonpFunction = (object instanceof MappingJacksonValue) ? ((MappingJacksonValue)object).getJsonpFunction() : null;
/* 107 */     if (jsonpFunction != null) {
/* 108 */       generator.writeRaw(");");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\json\MappingJackson2HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */