/*    */ package org.springframework.http.converter.json;
/*    */ 
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import org.springframework.util.Base64Utils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GsonBuilderUtils
/*    */ {
/*    */   public static GsonBuilder gsonBuilderWithBase64EncodedByteArrays()
/*    */   {
/* 55 */     GsonBuilder builder = new GsonBuilder();
/* 56 */     builder.registerTypeHierarchyAdapter(byte[].class, new Base64TypeAdapter(null));
/* 57 */     return builder;
/*    */   }
/*    */   
/*    */   private static class Base64TypeAdapter
/*    */     implements JsonSerializer<byte[]>, JsonDeserializer<byte[]>
/*    */   {
/*    */     public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context)
/*    */     {
/* 65 */       return new JsonPrimitive(Base64Utils.encodeToString(src));
/*    */     }
/*    */     
/*    */     public byte[] deserialize(JsonElement json, Type type, JsonDeserializationContext cxt)
/*    */     {
/* 70 */       return Base64Utils.decodeFromString(json.getAsString());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\converter\json\GsonBuilderUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */