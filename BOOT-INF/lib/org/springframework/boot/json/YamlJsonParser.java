/*    */ package org.springframework.boot.json;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.yaml.snakeyaml.Yaml;
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
/*    */ public class YamlJsonParser
/*    */   implements JsonParser
/*    */ {
/*    */   public Map<String, Object> parseMap(String json)
/*    */   {
/* 36 */     if (json != null) {
/* 37 */       json = json.trim();
/* 38 */       if (json.startsWith("{")) {
/* 39 */         return (Map)new Yaml().loadAs(json, Map.class);
/*    */       }
/*    */     }
/* 42 */     throw new IllegalArgumentException("Cannot parse JSON");
/*    */   }
/*    */   
/*    */ 
/*    */   public List<Object> parseList(String json)
/*    */   {
/* 48 */     if (json != null) {
/* 49 */       json = json.trim();
/* 50 */       if (json.startsWith("[")) {
/* 51 */         return (List)new Yaml().loadAs(json, List.class);
/*    */       }
/*    */     }
/* 54 */     throw new IllegalArgumentException("Cannot parse JSON");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\YamlJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */