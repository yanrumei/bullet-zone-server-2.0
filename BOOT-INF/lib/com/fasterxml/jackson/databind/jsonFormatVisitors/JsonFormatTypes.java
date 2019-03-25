/*    */ package com.fasterxml.jackson.databind.jsonFormatVisitors;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public enum JsonFormatTypes
/*    */ {
/* 10 */   STRING, 
/* 11 */   NUMBER, 
/* 12 */   INTEGER, 
/* 13 */   BOOLEAN, 
/* 14 */   OBJECT, 
/* 15 */   ARRAY, 
/* 16 */   NULL, 
/* 17 */   ANY;
/*    */   
/* 19 */   static { _byLCName = new HashMap();
/*    */     
/* 21 */     for (JsonFormatTypes t : values())
/* 22 */       _byLCName.put(t.name().toLowerCase(), t);
/*    */   }
/*    */   
/*    */   private static final Map<String, JsonFormatTypes> _byLCName;
/*    */   @JsonValue
/*    */   public String value() {
/* 28 */     return name().toLowerCase();
/*    */   }
/*    */   
/*    */   @JsonCreator
/*    */   public static JsonFormatTypes forValue(String s) {
/* 33 */     return (JsonFormatTypes)_byLCName.get(s);
/*    */   }
/*    */   
/*    */   private JsonFormatTypes() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsonFormatVisitors\JsonFormatTypes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */