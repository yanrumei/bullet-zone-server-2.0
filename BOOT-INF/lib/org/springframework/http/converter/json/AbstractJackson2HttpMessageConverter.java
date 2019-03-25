/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultIndenter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.TypeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJackson2HttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  67 */   public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
/*     */   
/*  69 */   private static final MediaType TEXT_EVENT_STREAM = new MediaType("text", "event-stream");
/*     */   
/*     */   protected ObjectMapper objectMapper;
/*     */   
/*     */   private Boolean prettyPrint;
/*     */   
/*     */   private PrettyPrinter ssePrettyPrinter;
/*     */   
/*     */ 
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper)
/*     */   {
/*  80 */     init(objectMapper);
/*     */   }
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType supportedMediaType) {
/*  84 */     super(supportedMediaType);
/*  85 */     init(objectMapper);
/*     */   }
/*     */   
/*     */   protected AbstractJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
/*  89 */     super(supportedMediaTypes);
/*  90 */     init(objectMapper);
/*     */   }
/*     */   
/*     */   protected void init(ObjectMapper objectMapper) {
/*  94 */     this.objectMapper = objectMapper;
/*  95 */     setDefaultCharset(DEFAULT_CHARSET);
/*  96 */     DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
/*  97 */     prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\ndata:"));
/*  98 */     this.ssePrettyPrinter = prettyPrinter;
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
/*     */   public void setObjectMapper(ObjectMapper objectMapper)
/*     */   {
/* 114 */     Assert.notNull(objectMapper, "ObjectMapper must not be null");
/* 115 */     this.objectMapper = objectMapper;
/* 116 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectMapper getObjectMapper()
/*     */   {
/* 123 */     return this.objectMapper;
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
/*     */   public void setPrettyPrint(boolean prettyPrint)
/*     */   {
/* 136 */     this.prettyPrint = Boolean.valueOf(prettyPrint);
/* 137 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */   private void configurePrettyPrint() {
/* 141 */     if (this.prettyPrint != null) {
/* 142 */       this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint.booleanValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 149 */     return canRead(clazz, null, mediaType);
/*     */   }
/*     */   
/*     */   public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType)
/*     */   {
/* 154 */     if (!canRead(mediaType)) {
/* 155 */       return false;
/*     */     }
/* 157 */     JavaType javaType = getJavaType(type, contextClass);
/* 158 */     AtomicReference<Throwable> causeRef = new AtomicReference();
/* 159 */     if (this.objectMapper.canDeserialize(javaType, causeRef)) {
/* 160 */       return true;
/*     */     }
/* 162 */     logWarningIfNecessary(javaType, (Throwable)causeRef.get());
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*     */   {
/* 168 */     if (!canWrite(mediaType)) {
/* 169 */       return false;
/*     */     }
/* 171 */     AtomicReference<Throwable> causeRef = new AtomicReference();
/* 172 */     if (this.objectMapper.canSerialize(clazz, causeRef)) {
/* 173 */       return true;
/*     */     }
/* 175 */     logWarningIfNecessary(clazz, (Throwable)causeRef.get());
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void logWarningIfNecessary(Type type, Throwable cause)
/*     */   {
/* 188 */     if (cause == null) {
/* 189 */       return;
/*     */     }
/*     */     
/*     */ 
/* 193 */     boolean debugLevel = ((cause instanceof JsonMappingException)) && (cause.getMessage().startsWith("Can not find"));
/*     */     
/* 195 */     if (debugLevel ? this.logger.isDebugEnabled() : this.logger.isWarnEnabled()) {
/* 196 */       String msg = "Failed to evaluate Jackson " + ((type instanceof JavaType) ? "de" : "") + "serialization for type [" + type + "]";
/*     */       
/* 198 */       if (debugLevel) {
/* 199 */         this.logger.debug(msg, cause);
/*     */       }
/* 201 */       else if (this.logger.isDebugEnabled()) {
/* 202 */         this.logger.warn(msg, cause);
/*     */       }
/*     */       else {
/* 205 */         this.logger.warn(msg + ": " + cause);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
/*     */     throws IOException, HttpMessageNotReadableException
/*     */   {
/* 214 */     JavaType javaType = getJavaType(clazz, null);
/* 215 */     return readJavaType(javaType, inputMessage);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
/*     */     throws IOException, HttpMessageNotReadableException
/*     */   {
/* 222 */     JavaType javaType = getJavaType(type, contextClass);
/* 223 */     return readJavaType(javaType, inputMessage);
/*     */   }
/*     */   
/*     */   private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
/*     */     try {
/* 228 */       if ((inputMessage instanceof MappingJacksonInputMessage)) {
/* 229 */         Class<?> deserializationView = ((MappingJacksonInputMessage)inputMessage).getDeserializationView();
/* 230 */         if (deserializationView != null) {
/* 231 */           return 
/* 232 */             this.objectMapper.readerWithView(deserializationView).forType(javaType).readValue(inputMessage.getBody());
/*     */         }
/*     */       }
/* 235 */       return this.objectMapper.readValue(inputMessage.getBody(), javaType);
/*     */     }
/*     */     catch (JsonProcessingException ex) {
/* 238 */       throw new HttpMessageNotReadableException("JSON parse error: " + ex.getOriginalMessage(), ex);
/*     */     }
/*     */     catch (IOException ex) {
/* 241 */       throw new HttpMessageNotReadableException("I/O error while reading input message", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
/*     */     throws IOException, HttpMessageNotWritableException
/*     */   {
/* 249 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/* 250 */     JsonEncoding encoding = getJsonEncoding(contentType);
/* 251 */     JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
/*     */     try {
/* 253 */       writePrefix(generator, object);
/*     */       
/* 255 */       Class<?> serializationView = null;
/* 256 */       FilterProvider filters = null;
/* 257 */       Object value = object;
/* 258 */       JavaType javaType = null;
/* 259 */       if ((object instanceof MappingJacksonValue)) {
/* 260 */         MappingJacksonValue container = (MappingJacksonValue)object;
/* 261 */         value = container.getValue();
/* 262 */         serializationView = container.getSerializationView();
/* 263 */         filters = container.getFilters();
/*     */       }
/* 265 */       if ((type != null) && (value != null) && (TypeUtils.isAssignable(type, value.getClass())))
/* 266 */         javaType = getJavaType(type, null);
/*     */       ObjectWriter objectWriter;
/*     */       ObjectWriter objectWriter;
/* 269 */       if (serializationView != null) {
/* 270 */         objectWriter = this.objectMapper.writerWithView(serializationView);
/*     */       } else { ObjectWriter objectWriter;
/* 272 */         if (filters != null) {
/* 273 */           objectWriter = this.objectMapper.writer(filters);
/*     */         }
/*     */         else
/* 276 */           objectWriter = this.objectMapper.writer();
/*     */       }
/* 278 */       if ((javaType != null) && (javaType.isContainerType())) {
/* 279 */         objectWriter = objectWriter.forType(javaType);
/*     */       }
/* 281 */       SerializationConfig config = objectWriter.getConfig();
/* 282 */       if ((contentType != null) && (contentType.isCompatibleWith(TEXT_EVENT_STREAM)) && 
/* 283 */         (config.isEnabled(SerializationFeature.INDENT_OUTPUT))) {
/* 284 */         objectWriter = objectWriter.with(this.ssePrettyPrinter);
/*     */       }
/* 286 */       objectWriter.writeValue(generator, value);
/*     */       
/* 288 */       writeSuffix(generator, object);
/* 289 */       generator.flush();
/*     */     }
/*     */     catch (JsonProcessingException ex)
/*     */     {
/* 293 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
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
/*     */   protected void writePrefix(JsonGenerator generator, Object object)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeSuffix(JsonGenerator generator, Object object)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType getJavaType(Type type, Class<?> contextClass)
/*     */   {
/* 333 */     TypeFactory typeFactory = this.objectMapper.getTypeFactory();
/* 334 */     if (contextClass != null) {
/* 335 */       ResolvableType resolvedType = ResolvableType.forType(type);
/* 336 */       if ((type instanceof TypeVariable)) {
/* 337 */         ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable)type, 
/* 338 */           ResolvableType.forClass(contextClass));
/* 339 */         if (resolvedTypeVariable != ResolvableType.NONE) {
/* 340 */           return typeFactory.constructType(resolvedTypeVariable.resolve());
/*     */         }
/*     */       }
/* 343 */       else if (((type instanceof ParameterizedType)) && (resolvedType.hasUnresolvableGenerics())) {
/* 344 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 345 */         Class<?>[] generics = new Class[parameterizedType.getActualTypeArguments().length];
/* 346 */         Type[] typeArguments = parameterizedType.getActualTypeArguments();
/* 347 */         for (int i = 0; i < typeArguments.length; i++) {
/* 348 */           Type typeArgument = typeArguments[i];
/* 349 */           if ((typeArgument instanceof TypeVariable)) {
/* 350 */             ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable)typeArgument, 
/* 351 */               ResolvableType.forClass(contextClass));
/* 352 */             if (resolvedTypeArgument != ResolvableType.NONE) {
/* 353 */               generics[i] = resolvedTypeArgument.resolve();
/*     */             }
/*     */             else {
/* 356 */               generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */             }
/*     */           }
/*     */           else {
/* 360 */             generics[i] = ResolvableType.forType(typeArgument).resolve();
/*     */           }
/*     */         }
/* 363 */         return typeFactory.constructType(
/* 364 */           ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType());
/*     */       }
/*     */     }
/* 367 */     return typeFactory.constructType(type);
/*     */   }
/*     */   
/*     */   private ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType)
/*     */   {
/* 372 */     if (contextType.hasGenerics()) {
/* 373 */       ResolvableType resolvedType = ResolvableType.forType(typeVariable, contextType);
/* 374 */       if (resolvedType.resolve() != null) {
/* 375 */         return resolvedType;
/*     */       }
/*     */     }
/*     */     
/* 379 */     ResolvableType superType = contextType.getSuperType();
/* 380 */     if (superType != ResolvableType.NONE) {
/* 381 */       ResolvableType resolvedType = resolveVariable(typeVariable, superType);
/* 382 */       if (resolvedType.resolve() != null) {
/* 383 */         return resolvedType;
/*     */       }
/*     */     }
/* 386 */     for (ResolvableType ifc : contextType.getInterfaces()) {
/* 387 */       ResolvableType resolvedType = resolveVariable(typeVariable, ifc);
/* 388 */       if (resolvedType.resolve() != null) {
/* 389 */         return resolvedType;
/*     */       }
/*     */     }
/* 392 */     return ResolvableType.NONE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonEncoding getJsonEncoding(MediaType contentType)
/*     */   {
/* 401 */     if ((contentType != null) && (contentType.getCharset() != null)) {
/* 402 */       Charset charset = contentType.getCharset();
/* 403 */       for (JsonEncoding encoding : JsonEncoding.values()) {
/* 404 */         if (charset.name().equals(encoding.getJavaName())) {
/* 405 */           return encoding;
/*     */         }
/*     */       }
/*     */     }
/* 409 */     return JsonEncoding.UTF8;
/*     */   }
/*     */   
/*     */   protected MediaType getDefaultContentType(Object object) throws IOException
/*     */   {
/* 414 */     if ((object instanceof MappingJacksonValue)) {
/* 415 */       object = ((MappingJacksonValue)object).getValue();
/*     */     }
/* 417 */     return super.getDefaultContentType(object);
/*     */   }
/*     */   
/*     */   protected Long getContentLength(Object object, MediaType contentType) throws IOException
/*     */   {
/* 422 */     if ((object instanceof MappingJacksonValue)) {
/* 423 */       object = ((MappingJacksonValue)object).getValue();
/*     */     }
/* 425 */     return super.getContentLength(object, contentType);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\json\AbstractJackson2HttpMessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */