/*    */ package org.springframework.boot.json;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.reflect.TypeToken;
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
/*    */ 
/*    */ 
/*    */ public class GsonJsonParser
/*    */   implements JsonParser
/*    */ {
/* 36 */   private static final TypeToken<?> MAP_TYPE = new MapTypeToken(null);
/*    */   
/* 38 */   private static final TypeToken<?> LIST_TYPE = new ListTypeToken(null);
/*    */   
/* 40 */   private Gson gson = new GsonBuilder().create();
/*    */   
/*    */   public Map<String, Object> parseMap(String json)
/*    */   {
/* 44 */     if (json != null) {
/* 45 */       json = json.trim();
/* 46 */       if (json.startsWith("{")) {
/* 47 */         return (Map)this.gson.fromJson(json, MAP_TYPE.getType());
/*    */       }
/*    */     }
/* 50 */     throw new IllegalArgumentException("Cannot parse JSON");
/*    */   }
/*    */   
/*    */   public List<Object> parseList(String json)
/*    */   {
/* 55 */     if (json != null) {
/* 56 */       json = json.trim();
/* 57 */       if (json.startsWith("[")) {
/* 58 */         return (List)this.gson.fromJson(json, LIST_TYPE.getType());
/*    */       }
/*    */     }
/* 61 */     throw new IllegalArgumentException("Cannot parse JSON");
/*    */   }
/*    */   
/*    */   private static final class ListTypeToken
/*    */     extends TypeToken<List<Object>>
/*    */   {}
/*    */   
/*    */   private static final class MapTypeToken
/*    */     extends TypeToken<Map<String, Object>>
/*    */   {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\GsonJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */