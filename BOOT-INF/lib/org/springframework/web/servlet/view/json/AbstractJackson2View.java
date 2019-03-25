/*     */ package org.springframework.web.servlet.view.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.converter.json.MappingJacksonValue;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.view.AbstractView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJackson2View
/*     */   extends AbstractView
/*     */ {
/*     */   private ObjectMapper objectMapper;
/*  54 */   private JsonEncoding encoding = JsonEncoding.UTF8;
/*     */   
/*     */   private Boolean prettyPrint;
/*     */   
/*  58 */   private boolean disableCaching = true;
/*     */   
/*  60 */   protected boolean updateContentLength = false;
/*     */   
/*     */   protected AbstractJackson2View(ObjectMapper objectMapper, String contentType)
/*     */   {
/*  64 */     setObjectMapper(objectMapper);
/*  65 */     setContentType(contentType);
/*  66 */     setExposePathVariables(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObjectMapper(ObjectMapper objectMapper)
/*     */   {
/*  77 */     Assert.notNull(objectMapper, "'objectMapper' must not be null");
/*  78 */     this.objectMapper = objectMapper;
/*  79 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final ObjectMapper getObjectMapper()
/*     */   {
/*  86 */     return this.objectMapper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEncoding(JsonEncoding encoding)
/*     */   {
/*  94 */     Assert.notNull(encoding, "'encoding' must not be null");
/*  95 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final JsonEncoding getEncoding()
/*     */   {
/* 102 */     return this.encoding;
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
/* 115 */     this.prettyPrint = Boolean.valueOf(prettyPrint);
/* 116 */     configurePrettyPrint();
/*     */   }
/*     */   
/*     */   private void configurePrettyPrint() {
/* 120 */     if (this.prettyPrint != null) {
/* 121 */       this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint.booleanValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDisableCaching(boolean disableCaching)
/*     */   {
/* 130 */     this.disableCaching = disableCaching;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUpdateContentLength(boolean updateContentLength)
/*     */   {
/* 140 */     this.updateContentLength = updateContentLength;
/*     */   }
/*     */   
/*     */   protected void prepareResponse(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 145 */     setResponseContentType(request, response);
/* 146 */     response.setCharacterEncoding(this.encoding.getJavaName());
/* 147 */     if (this.disableCaching) {
/* 148 */       response.addHeader("Cache-Control", "no-store");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 156 */     OutputStream stream = this.updateContentLength ? createTemporaryOutputStream() : response.getOutputStream();
/* 157 */     Object value = filterAndWrapModel(model, request);
/*     */     
/* 159 */     writeContent(stream, value);
/* 160 */     if (this.updateContentLength) {
/* 161 */       writeToResponse(response, (ByteArrayOutputStream)stream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object filterAndWrapModel(Map<String, Object> model, HttpServletRequest request)
/*     */   {
/* 172 */     Object value = filterModel(model);
/* 173 */     Class<?> serializationView = (Class)model.get(JsonView.class.getName());
/* 174 */     FilterProvider filters = (FilterProvider)model.get(FilterProvider.class.getName());
/* 175 */     if ((serializationView != null) || (filters != null)) {
/* 176 */       MappingJacksonValue container = new MappingJacksonValue(value);
/* 177 */       container.setSerializationView(serializationView);
/* 178 */       container.setFilters(filters);
/* 179 */       value = container;
/*     */     }
/* 181 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void writeContent(OutputStream stream, Object object)
/*     */     throws IOException
/*     */   {
/* 191 */     JsonGenerator generator = this.objectMapper.getFactory().createGenerator(stream, this.encoding);
/*     */     
/* 193 */     writePrefix(generator, object);
/* 194 */     Class<?> serializationView = null;
/* 195 */     FilterProvider filters = null;
/* 196 */     Object value = object;
/*     */     
/* 198 */     if ((value instanceof MappingJacksonValue)) {
/* 199 */       MappingJacksonValue container = (MappingJacksonValue)value;
/* 200 */       value = container.getValue();
/* 201 */       serializationView = container.getSerializationView();
/* 202 */       filters = container.getFilters();
/*     */     }
/* 204 */     if (serializationView != null) {
/* 205 */       this.objectMapper.writerWithView(serializationView).writeValue(generator, value);
/*     */     }
/* 207 */     else if (filters != null) {
/* 208 */       this.objectMapper.writer(filters).writeValue(generator, value);
/*     */     }
/*     */     else {
/* 211 */       this.objectMapper.writeValue(generator, value);
/*     */     }
/* 213 */     writeSuffix(generator, object);
/* 214 */     generator.flush();
/*     */   }
/*     */   
/*     */   public abstract void setModelKey(String paramString);
/*     */   
/*     */   protected abstract Object filterModel(Map<String, Object> paramMap);
/*     */   
/*     */   protected void writePrefix(JsonGenerator generator, Object object)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object)
/*     */     throws IOException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\json\AbstractJackson2View.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */