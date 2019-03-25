/*     */ package org.springframework.web.servlet.view.json;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonView;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.http.converter.json.MappingJacksonValue;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.validation.BindingResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingJackson2JsonView
/*     */   extends AbstractJackson2View
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "application/json";
/*     */   public static final String DEFAULT_JSONP_CONTENT_TYPE = "application/javascript";
/*  77 */   private static final Pattern CALLBACK_PARAM_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");
/*     */   
/*     */ 
/*     */   private String jsonPrefix;
/*     */   
/*     */   private Set<String> modelKeys;
/*     */   
/*  84 */   private boolean extractValueFromSingleKeyModel = false;
/*     */   
/*  86 */   private Set<String> jsonpParameterNames = new LinkedHashSet(Arrays.asList(new String[] { "jsonp", "callback" }));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappingJackson2JsonView()
/*     */   {
/*  95 */     super(Jackson2ObjectMapperBuilder.json().build(), "application/json");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MappingJackson2JsonView(ObjectMapper objectMapper)
/*     */   {
/* 104 */     super(objectMapper, "application/json");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJsonPrefix(String jsonPrefix)
/*     */   {
/* 114 */     this.jsonPrefix = jsonPrefix;
/*     */   }
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
/* 126 */     this.jsonPrefix = (prefixJson ? ")]}', " : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModelKey(String modelKey)
/*     */   {
/* 134 */     this.modelKeys = Collections.singleton(modelKey);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModelKeys(Set<String> modelKeys)
/*     */   {
/* 142 */     this.modelKeys = modelKeys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Set<String> getModelKeys()
/*     */   {
/* 149 */     return this.modelKeys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel)
/*     */   {
/* 161 */     this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJsonpParameterNames(Set<String> jsonpParameterNames)
/*     */   {
/* 173 */     this.jsonpParameterNames = jsonpParameterNames;
/*     */   }
/*     */   
/*     */   private String getJsonpParameterValue(HttpServletRequest request) {
/* 177 */     if (this.jsonpParameterNames != null) {
/* 178 */       for (String name : this.jsonpParameterNames) {
/* 179 */         String value = request.getParameter(name);
/* 180 */         if (!StringUtils.isEmpty(value))
/*     */         {
/*     */ 
/* 183 */           if (!isValidJsonpQueryParam(value)) {
/* 184 */             if (this.logger.isDebugEnabled()) {
/* 185 */               this.logger.debug("Ignoring invalid jsonp parameter value: " + value);
/*     */             }
/*     */           }
/*     */           else
/* 189 */             return value; }
/*     */       }
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidJsonpQueryParam(String value)
/*     */   {
/* 203 */     return CALLBACK_PARAM_PATTERN.matcher(value).matches();
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
/*     */   protected Object filterModel(Map<String, Object> model)
/*     */   {
/* 216 */     Map<String, Object> result = new HashMap(model.size());
/* 217 */     Set<String> modelKeys = !CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet();
/* 218 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 219 */       if ((!(entry.getValue() instanceof BindingResult)) && (modelKeys.contains(entry.getKey())) && 
/* 220 */         (!((String)entry.getKey()).equals(JsonView.class.getName())) && 
/* 221 */         (!((String)entry.getKey()).equals(FilterProvider.class.getName()))) {
/* 222 */         result.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 225 */     return (this.extractValueFromSingleKeyModel) && (result.size() == 1) ? result.values().iterator().next() : result;
/*     */   }
/*     */   
/*     */   protected Object filterAndWrapModel(Map<String, Object> model, HttpServletRequest request)
/*     */   {
/* 230 */     Object value = super.filterAndWrapModel(model, request);
/* 231 */     String jsonpParameterValue = getJsonpParameterValue(request);
/* 232 */     if (jsonpParameterValue != null) {
/* 233 */       if ((value instanceof MappingJacksonValue)) {
/* 234 */         ((MappingJacksonValue)value).setJsonpFunction(jsonpParameterValue);
/*     */       }
/*     */       else {
/* 237 */         MappingJacksonValue container = new MappingJacksonValue(value);
/* 238 */         container.setJsonpFunction(jsonpParameterValue);
/* 239 */         value = container;
/*     */       }
/*     */     }
/* 242 */     return value;
/*     */   }
/*     */   
/*     */   protected void writePrefix(JsonGenerator generator, Object object) throws IOException
/*     */   {
/* 247 */     if (this.jsonPrefix != null) {
/* 248 */       generator.writeRaw(this.jsonPrefix);
/*     */     }
/*     */     
/* 251 */     String jsonpFunction = null;
/* 252 */     if ((object instanceof MappingJacksonValue)) {
/* 253 */       jsonpFunction = ((MappingJacksonValue)object).getJsonpFunction();
/*     */     }
/* 255 */     if (jsonpFunction != null) {
/* 256 */       generator.writeRaw("/**/");
/* 257 */       generator.writeRaw(jsonpFunction + "(");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeSuffix(JsonGenerator generator, Object object) throws IOException
/*     */   {
/* 263 */     String jsonpFunction = null;
/* 264 */     if ((object instanceof MappingJacksonValue)) {
/* 265 */       jsonpFunction = ((MappingJacksonValue)object).getJsonpFunction();
/*     */     }
/* 267 */     if (jsonpFunction != null) {
/* 268 */       generator.writeRaw(");");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setResponseContentType(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 274 */     if (getJsonpParameterValue(request) != null) {
/* 275 */       response.setContentType("application/javascript");
/*     */     }
/*     */     else {
/* 278 */       super.setResponseContentType(request, response);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\json\MappingJackson2JsonView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */