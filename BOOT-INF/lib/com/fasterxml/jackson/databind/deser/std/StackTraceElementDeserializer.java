/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class StackTraceElementDeserializer
/*    */   extends StdScalarDeserializer<StackTraceElement>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public StackTraceElementDeserializer()
/*    */   {
/* 16 */     super(StackTraceElement.class);
/*    */   }
/*    */   
/*    */   public StackTraceElement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
/*    */   {
/* 21 */     JsonToken t = p.getCurrentToken();
/*    */     
/* 23 */     if (t == JsonToken.START_OBJECT) {
/* 24 */       String className = "";String methodName = "";String fileName = "";
/*    */       
/* 26 */       String moduleName = null;String moduleVersion = null;
/* 27 */       int lineNumber = -1;
/*    */       
/* 29 */       while ((t = p.nextValue()) != JsonToken.END_OBJECT) {
/* 30 */         String propName = p.getCurrentName();
/*    */         
/* 32 */         if ("className".equals(propName)) {
/* 33 */           className = p.getText();
/* 34 */         } else if ("fileName".equals(propName)) {
/* 35 */           fileName = p.getText();
/* 36 */         } else if ("lineNumber".equals(propName)) {
/* 37 */           if (t.isNumeric()) {
/* 38 */             lineNumber = p.getIntValue();
/*    */           } else {
/* 40 */             lineNumber = _parseIntPrimitive(p, ctxt);
/*    */           }
/* 42 */         } else if ("methodName".equals(propName)) {
/* 43 */           methodName = p.getText();
/* 44 */         } else if (!"nativeMethod".equals(propName))
/*    */         {
/* 46 */           if ("moduleName".equals(propName)) {
/* 47 */             moduleName = p.getText();
/* 48 */           } else if ("moduleVersion".equals(propName)) {
/* 49 */             moduleVersion = p.getText();
/*    */           } else
/* 51 */             handleUnknownProperty(p, ctxt, this._valueClass, propName);
/*    */         }
/*    */       }
/* 54 */       return constructValue(ctxt, className, methodName, fileName, lineNumber, moduleName, moduleVersion);
/*    */     }
/* 56 */     if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 57 */       p.nextToken();
/* 58 */       StackTraceElement value = deserialize(p, ctxt);
/* 59 */       if (p.nextToken() != JsonToken.END_ARRAY) {
/* 60 */         handleMissingEndArrayForSingle(p, ctxt);
/*    */       }
/* 62 */       return value;
/*    */     }
/* 64 */     return (StackTraceElement)ctxt.handleUnexpectedToken(this._valueClass, p);
/*    */   }
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
/*    */   protected StackTraceElement constructValue(DeserializationContext ctxt, String className, String methodName, String fileName, int lineNumber, String moduleName, String moduleVersion)
/*    */   {
/* 78 */     return new StackTraceElement(className, methodName, fileName, lineNumber);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StackTraceElementDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */