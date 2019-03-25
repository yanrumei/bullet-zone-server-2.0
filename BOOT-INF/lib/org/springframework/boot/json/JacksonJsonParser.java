/*    */ package org.springframework.boot.json;
/*    */ 
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JacksonJsonParser
/*    */   implements JsonParser
/*    */ {
/* 33 */   private static final TypeReference<?> MAP_TYPE = new MapTypeReference(null);
/*    */   
/* 35 */   private static final TypeReference<?> LIST_TYPE = new ListTypeReference(null);
/*    */   private ObjectMapper objectMapper;
/*    */   
/*    */   public Map<String, Object> parseMap(String json)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       return (Map)getObjectMapper().readValue(json, MAP_TYPE);
/*    */     }
/*    */     catch (Exception ex) {
/* 45 */       throw new IllegalArgumentException("Cannot parse JSON", ex);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<Object> parseList(String json)
/*    */   {
/*    */     try {
/* 52 */       return (List)getObjectMapper().readValue(json, LIST_TYPE);
/*    */     }
/*    */     catch (Exception ex) {
/* 55 */       throw new IllegalArgumentException("Cannot parse JSON", ex);
/*    */     }
/*    */   }
/*    */   
/*    */   private ObjectMapper getObjectMapper() {
/* 60 */     if (this.objectMapper == null) {
/* 61 */       this.objectMapper = new ObjectMapper();
/*    */     }
/* 63 */     return this.objectMapper;
/*    */   }
/*    */   
/*    */   private static class ListTypeReference
/*    */     extends TypeReference<List<Object>>
/*    */   {}
/*    */   
/*    */   private static class MapTypeReference
/*    */     extends TypeReference<Map<String, Object>>
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\JacksonJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */