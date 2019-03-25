/*    */ package org.springframework.boot.json;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.json.simple.parser.JSONParser;
/*    */ import org.json.simple.parser.ParseException;
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
/*    */ public class JsonSimpleJsonParser
/*    */   implements JsonParser
/*    */ {
/*    */   public Map<String, Object> parseMap(String json)
/*    */   {
/*    */     try
/*    */     {
/* 39 */       return (Map)new JSONParser().parse(json);
/*    */     }
/*    */     catch (ParseException ex) {
/* 42 */       throw new IllegalArgumentException("Cannot parse JSON", ex);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<Object> parseList(String json)
/*    */   {
/*    */     try
/*    */     {
/* 50 */       return (List)new JSONParser().parse(json);
/*    */     }
/*    */     catch (ParseException ex) {
/* 53 */       throw new IllegalArgumentException("Cannot parse JSON", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\json\JsonSimpleJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */