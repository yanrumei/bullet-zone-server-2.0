/*    */ package org.springframework.boot.json;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.json.JSONArray;
/*    */ import org.json.JSONObject;
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
/*    */ public class JsonJsonParser
/*    */   implements JsonParser
/*    */ {
/*    */   public Map<String, Object> parseMap(String json)
/*    */   {
/* 38 */     Map<String, Object> map = new LinkedHashMap();
/* 39 */     putAll(map, new JSONObject(json));
/* 40 */     return map;
/*    */   }
/*    */   
/*    */   private void putAll(Map<String, Object> map, JSONObject object) {
/* 44 */     for (Object key : object.keySet()) {
/* 45 */       String name = key.toString();
/* 46 */       Object value = object.get(name);
/* 47 */       if ((value instanceof JSONObject)) {
/* 48 */         Map<String, Object> nested = new LinkedHashMap();
/* 49 */         putAll(nested, (JSONObject)value);
/* 50 */         value = nested;
/*    */       }
/* 52 */       if ((value instanceof JSONArray)) {
/* 53 */         List<Object> nested = new ArrayList();
/* 54 */         addAll(nested, (JSONArray)value);
/* 55 */         value = nested;
/*    */       }
/* 57 */       map.put(name, value);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addAll(List<Object> list, JSONArray array) {
/* 62 */     for (int i = 0; i < array.length(); i++) {
/* 63 */       Object value = array.get(i);
/* 64 */       if ((value instanceof JSONObject)) {
/* 65 */         Map<String, Object> nested = new LinkedHashMap();
/* 66 */         putAll(nested, (JSONObject)value);
/* 67 */         value = nested;
/*    */       }
/* 69 */       if ((value instanceof JSONArray)) {
/* 70 */         List<Object> nested = new ArrayList();
/* 71 */         addAll(nested, (JSONArray)value);
/* 72 */         value = nested;
/*    */       }
/* 74 */       list.add(value);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<Object> parseList(String json)
/*    */   {
/* 80 */     List<Object> nested = new ArrayList();
/* 81 */     addAll(nested, new JSONArray(json));
/* 82 */     return nested;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\JsonJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */