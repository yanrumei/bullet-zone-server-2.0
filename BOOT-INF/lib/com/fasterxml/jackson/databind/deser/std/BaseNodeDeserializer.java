/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class BaseNodeDeserializer<T extends JsonNode>
/*     */   extends StdDeserializer<T>
/*     */ {
/*     */   public BaseNodeDeserializer(Class<T> vc)
/*     */   {
/* 140 */     super(vc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 151 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 159 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected void _reportProblem(JsonParser p, String msg)
/*     */     throws JsonMappingException
/*     */   {
/* 169 */     throw JsonMappingException.from(p, msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue)
/*     */     throws JsonProcessingException
/*     */   {
/* 192 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)) {
/* 193 */       ctxt.reportMappingException("Duplicate field '%s' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", new Object[] { fieldName });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ObjectNode deserializeObject(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 207 */     ObjectNode node = nodeFactory.objectNode();
/*     */     String key;
/* 209 */     if (p.isExpectedStartObjectToken()) {
/* 210 */       key = p.nextFieldName();
/*     */     } else {
/* 212 */       JsonToken t = p.getCurrentToken();
/* 213 */       if (t == JsonToken.END_OBJECT) {
/* 214 */         return node;
/*     */       }
/* 216 */       if (t != JsonToken.FIELD_NAME)
/* 217 */         return (ObjectNode)ctxt.handleUnexpectedToken(handledType(), p);
/*     */     }
/* 219 */     for (String key = p.getCurrentName(); 
/*     */         
/* 221 */         key != null; key = p.nextFieldName())
/*     */     {
/* 223 */       JsonToken t = p.nextToken();
/* 224 */       if (t == null)
/* 225 */         throw ctxt.mappingException("Unexpected end-of-input when binding data into ObjectNode");
/*     */       JsonNode value;
/* 227 */       switch (t.id()) {
/*     */       case 1: 
/* 229 */         value = deserializeObject(p, ctxt, nodeFactory);
/* 230 */         break;
/*     */       case 3: 
/* 232 */         value = deserializeArray(p, ctxt, nodeFactory);
/* 233 */         break;
/*     */       case 12: 
/* 235 */         value = _fromEmbedded(p, ctxt, nodeFactory);
/* 236 */         break;
/*     */       case 6: 
/* 238 */         value = nodeFactory.textNode(p.getText());
/* 239 */         break;
/*     */       case 7: 
/* 241 */         value = _fromInt(p, ctxt, nodeFactory);
/* 242 */         break;
/*     */       case 9: 
/* 244 */         value = nodeFactory.booleanNode(true);
/* 245 */         break;
/*     */       case 10: 
/* 247 */         value = nodeFactory.booleanNode(false);
/* 248 */         break;
/*     */       case 11: 
/* 250 */         value = nodeFactory.nullNode();
/* 251 */         break;
/*     */       case 2: case 4: case 5: case 8: default: 
/* 253 */         value = deserializeAny(p, ctxt, nodeFactory);
/*     */       }
/* 255 */       JsonNode old = node.replace(key, value);
/* 256 */       if (old != null) {
/* 257 */         _handleDuplicateField(p, ctxt, nodeFactory, key, node, old, value);
/*     */       }
/*     */     }
/*     */     
/* 261 */     return node;
/*     */   }
/*     */   
/*     */   protected final ArrayNode deserializeArray(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 267 */     ArrayNode node = nodeFactory.arrayNode();
/*     */     for (;;) {
/* 269 */       JsonToken t = p.nextToken();
/* 270 */       switch (t.id()) {
/*     */       case 1: 
/* 272 */         node.add(deserializeObject(p, ctxt, nodeFactory));
/* 273 */         break;
/*     */       case 3: 
/* 275 */         node.add(deserializeArray(p, ctxt, nodeFactory));
/* 276 */         break;
/*     */       case 4: 
/* 278 */         return node;
/*     */       case 12: 
/* 280 */         node.add(_fromEmbedded(p, ctxt, nodeFactory));
/* 281 */         break;
/*     */       case 6: 
/* 283 */         node.add(nodeFactory.textNode(p.getText()));
/* 284 */         break;
/*     */       case 7: 
/* 286 */         node.add(_fromInt(p, ctxt, nodeFactory));
/* 287 */         break;
/*     */       case 9: 
/* 289 */         node.add(nodeFactory.booleanNode(true));
/* 290 */         break;
/*     */       case 10: 
/* 292 */         node.add(nodeFactory.booleanNode(false));
/* 293 */         break;
/*     */       case 11: 
/* 295 */         node.add(nodeFactory.nullNode());
/* 296 */         break;
/*     */       case 2: case 5: case 8: default: 
/* 298 */         node.add(deserializeAny(p, ctxt, nodeFactory));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   protected final JsonNode deserializeAny(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 307 */     switch (p.getCurrentTokenId()) {
/*     */     case 1: 
/*     */     case 2: 
/*     */     case 5: 
/* 311 */       return deserializeObject(p, ctxt, nodeFactory);
/*     */     case 3: 
/* 313 */       return deserializeArray(p, ctxt, nodeFactory);
/*     */     case 12: 
/* 315 */       return _fromEmbedded(p, ctxt, nodeFactory);
/*     */     case 6: 
/* 317 */       return nodeFactory.textNode(p.getText());
/*     */     case 7: 
/* 319 */       return _fromInt(p, ctxt, nodeFactory);
/*     */     case 8: 
/* 321 */       return _fromFloat(p, ctxt, nodeFactory);
/*     */     case 9: 
/* 323 */       return nodeFactory.booleanNode(true);
/*     */     case 10: 
/* 325 */       return nodeFactory.booleanNode(false);
/*     */     case 11: 
/* 327 */       return nodeFactory.nullNode();
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 336 */     return (JsonNode)ctxt.handleUnexpectedToken(handledType(), p);
/*     */   }
/*     */   
/*     */ 
/*     */   protected final JsonNode _fromInt(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 343 */     int feats = ctxt.getDeserializationFeatures();
/* 344 */     JsonParser.NumberType nt; JsonParser.NumberType nt; if ((feats & F_MASK_INT_COERCIONS) != 0) { JsonParser.NumberType nt;
/* 345 */       if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
/* 346 */         nt = JsonParser.NumberType.BIG_INTEGER; } else { JsonParser.NumberType nt;
/* 347 */         if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
/* 348 */           nt = JsonParser.NumberType.LONG;
/*     */         } else
/* 350 */           nt = p.getNumberType();
/*     */       }
/*     */     } else {
/* 353 */       nt = p.getNumberType();
/*     */     }
/* 355 */     if (nt == JsonParser.NumberType.INT) {
/* 356 */       return nodeFactory.numberNode(p.getIntValue());
/*     */     }
/* 358 */     if (nt == JsonParser.NumberType.LONG) {
/* 359 */       return nodeFactory.numberNode(p.getLongValue());
/*     */     }
/* 361 */     return nodeFactory.numberNode(p.getBigIntegerValue());
/*     */   }
/*     */   
/*     */   protected final JsonNode _fromFloat(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 367 */     JsonParser.NumberType nt = p.getNumberType();
/* 368 */     if (nt == JsonParser.NumberType.BIG_DECIMAL) {
/* 369 */       return nodeFactory.numberNode(p.getDecimalValue());
/*     */     }
/* 371 */     if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 377 */       double d = p.getDoubleValue();
/* 378 */       if ((Double.isInfinite(d)) || (Double.isNaN(d))) {
/* 379 */         return nodeFactory.numberNode(d);
/*     */       }
/* 381 */       return nodeFactory.numberNode(p.getDecimalValue());
/*     */     }
/* 383 */     if (nt == JsonParser.NumberType.FLOAT) {
/* 384 */       return nodeFactory.numberNode(p.getFloatValue());
/*     */     }
/* 386 */     return nodeFactory.numberNode(p.getDoubleValue());
/*     */   }
/*     */   
/*     */   protected final JsonNode _fromEmbedded(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory)
/*     */     throws IOException
/*     */   {
/* 392 */     Object ob = p.getEmbeddedObject();
/* 393 */     if (ob == null) {
/* 394 */       return nodeFactory.nullNode();
/*     */     }
/* 396 */     Class<?> type = ob.getClass();
/* 397 */     if (type == byte[].class) {
/* 398 */       return nodeFactory.binaryNode((byte[])ob);
/*     */     }
/*     */     
/* 401 */     if ((ob instanceof RawValue)) {
/* 402 */       return nodeFactory.rawValueNode((RawValue)ob);
/*     */     }
/* 404 */     if ((ob instanceof JsonNode))
/*     */     {
/* 406 */       return (JsonNode)ob;
/*     */     }
/*     */     
/* 409 */     return nodeFactory.pojoNode(ob);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\BaseNodeDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */