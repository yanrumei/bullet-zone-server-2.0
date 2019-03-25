/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MappingJacksonValue
/*     */ {
/*     */   private Object value;
/*     */   private Class<?> serializationView;
/*     */   private FilterProvider filters;
/*     */   private String jsonpFunction;
/*     */   
/*     */   public MappingJacksonValue(Object value)
/*     */   {
/*  52 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(Object value)
/*     */   {
/*  60 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getValue()
/*     */   {
/*  67 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSerializationView(Class<?> serializationView)
/*     */   {
/*  76 */     this.serializationView = serializationView;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getSerializationView()
/*     */   {
/*  85 */     return this.serializationView;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFilters(FilterProvider filters)
/*     */   {
/*  96 */     this.filters = filters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FilterProvider getFilters()
/*     */   {
/* 106 */     return this.filters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setJsonpFunction(String functionName)
/*     */   {
/* 113 */     this.jsonpFunction = functionName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getJsonpFunction()
/*     */   {
/* 120 */     return this.jsonpFunction;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\json\MappingJacksonValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */