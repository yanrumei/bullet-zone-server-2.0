/*     */ package org.springframework.boot.autoconfigure.jackson;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ConfigurationProperties(prefix="spring.jackson")
/*     */ public class JacksonProperties
/*     */ {
/*     */   private String dateFormat;
/*     */   private String jodaDateTimeFormat;
/*     */   private String propertyNamingStrategy;
/*  66 */   private Map<SerializationFeature, Boolean> serialization = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private Map<DeserializationFeature, Boolean> deserialization = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private Map<MapperFeature, Boolean> mapper = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   private Map<JsonParser.Feature, Boolean> parser = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private Map<JsonGenerator.Feature, Boolean> generator = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JsonInclude.Include defaultPropertyInclusion;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private TimeZone timeZone = null;
/*     */   
/*     */ 
/*     */   private Locale locale;
/*     */   
/*     */ 
/*     */   public String getDateFormat()
/*     */   {
/* 106 */     return this.dateFormat;
/*     */   }
/*     */   
/*     */   public void setDateFormat(String dateFormat) {
/* 110 */     this.dateFormat = dateFormat;
/*     */   }
/*     */   
/*     */   public String getJodaDateTimeFormat() {
/* 114 */     return this.jodaDateTimeFormat;
/*     */   }
/*     */   
/*     */   public void setJodaDateTimeFormat(String jodaDataTimeFormat) {
/* 118 */     this.jodaDateTimeFormat = jodaDataTimeFormat;
/*     */   }
/*     */   
/*     */   public String getPropertyNamingStrategy() {
/* 122 */     return this.propertyNamingStrategy;
/*     */   }
/*     */   
/*     */   public void setPropertyNamingStrategy(String propertyNamingStrategy) {
/* 126 */     this.propertyNamingStrategy = propertyNamingStrategy;
/*     */   }
/*     */   
/*     */   public Map<SerializationFeature, Boolean> getSerialization() {
/* 130 */     return this.serialization;
/*     */   }
/*     */   
/*     */   public Map<DeserializationFeature, Boolean> getDeserialization() {
/* 134 */     return this.deserialization;
/*     */   }
/*     */   
/*     */   public Map<MapperFeature, Boolean> getMapper() {
/* 138 */     return this.mapper;
/*     */   }
/*     */   
/*     */   public Map<JsonParser.Feature, Boolean> getParser() {
/* 142 */     return this.parser;
/*     */   }
/*     */   
/*     */   public Map<JsonGenerator.Feature, Boolean> getGenerator() {
/* 146 */     return this.generator;
/*     */   }
/*     */   
/*     */   public JsonInclude.Include getDefaultPropertyInclusion() {
/* 150 */     return this.defaultPropertyInclusion;
/*     */   }
/*     */   
/*     */   public void setDefaultPropertyInclusion(JsonInclude.Include defaultPropertyInclusion)
/*     */   {
/* 155 */     this.defaultPropertyInclusion = defaultPropertyInclusion;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 159 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 163 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   public Locale getLocale() {
/* 167 */     return this.locale;
/*     */   }
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 171 */     this.locale = locale;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jackson\JacksonProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */