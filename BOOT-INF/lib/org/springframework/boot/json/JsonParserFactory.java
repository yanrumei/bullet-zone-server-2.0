/*    */ package org.springframework.boot.json;
/*    */ 
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public abstract class JsonParserFactory
/*    */ {
/*    */   public static JsonParser getJsonParser()
/*    */   {
/* 41 */     if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
/* 42 */       return new JacksonJsonParser();
/*    */     }
/* 44 */     if (ClassUtils.isPresent("com.google.gson.Gson", null)) {
/* 45 */       return new GsonJsonParser();
/*    */     }
/* 47 */     if (ClassUtils.isPresent("org.yaml.snakeyaml.Yaml", null)) {
/* 48 */       return new YamlJsonParser();
/*    */     }
/* 50 */     if (ClassUtils.isPresent("org.json.simple.JSONObject", null)) {
/* 51 */       return new JsonSimpleJsonParser();
/*    */     }
/* 53 */     if (ClassUtils.isPresent("org.json.JSONObject", null)) {
/* 54 */       return new JsonJsonParser();
/*    */     }
/* 56 */     return new BasicJsonParser();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\JsonParserFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */