/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.deser.CreatorProperty;
/*    */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiator.Base;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonLocationInstantiator
/*    */   extends ValueInstantiator.Base
/*    */ {
/*    */   public JsonLocationInstantiator()
/*    */   {
/* 22 */     super(JsonLocation.class);
/*    */   }
/*    */   
/*    */   public boolean canCreateFromObjectWith() {
/* 26 */     return true;
/*    */   }
/*    */   
/*    */   public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
/* 30 */     JavaType intType = config.constructType(Integer.TYPE);
/* 31 */     JavaType longType = config.constructType(Long.TYPE);
/* 32 */     return new SettableBeanProperty[] { creatorProp("sourceRef", config.constructType(Object.class), 0), creatorProp("byteOffset", longType, 1), creatorProp("charOffset", longType, 2), creatorProp("lineNr", intType, 3), creatorProp("columnNr", intType, 4) };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static CreatorProperty creatorProp(String name, JavaType type, int index)
/*    */   {
/* 42 */     return new CreatorProperty(PropertyName.construct(name), type, null, null, null, null, index, null, PropertyMetadata.STD_REQUIRED);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
/*    */   {
/* 48 */     return new JsonLocation(args[0], _long(args[1]), _long(args[2]), _int(args[3]), _int(args[4]));
/*    */   }
/*    */   
/*    */   private static final long _long(Object o)
/*    */   {
/* 53 */     return o == null ? 0L : ((Number)o).longValue();
/*    */   }
/*    */   
/*    */   private static final int _int(Object o) {
/* 57 */     return o == null ? 0 : ((Number)o).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\JsonLocationInstantiator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */