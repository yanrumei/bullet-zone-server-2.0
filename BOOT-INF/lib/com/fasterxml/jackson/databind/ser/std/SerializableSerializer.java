/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable.Base;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class SerializableSerializer
/*     */   extends StdSerializer<JsonSerializable>
/*     */ {
/*  32 */   public static final SerializableSerializer instance = new SerializableSerializer();
/*     */   
/*     */ 
/*  35 */   private static final AtomicReference<ObjectMapper> _mapperReference = new AtomicReference();
/*     */   
/*  37 */   protected SerializableSerializer() { super(JsonSerializable.class); }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider serializers, JsonSerializable value)
/*     */   {
/*  41 */     if ((value instanceof JsonSerializable.Base)) {
/*  42 */       return ((JsonSerializable.Base)value).isEmpty(serializers);
/*     */     }
/*  44 */     return false;
/*     */   }
/*     */   
/*     */   public void serialize(JsonSerializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException
/*     */   {
/*  49 */     value.serialize(gen, serializers);
/*     */   }
/*     */   
/*     */   public final void serializeWithType(JsonSerializable value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/*  55 */     value.serializeWithType(gen, serializers, typeSer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/*  63 */     ObjectNode objectNode = createObjectNode();
/*  64 */     String schemaType = "any";
/*  65 */     String objectProperties = null;
/*  66 */     String itemDefinition = null;
/*  67 */     if (typeHint != null) {
/*  68 */       Class<?> rawClass = TypeFactory.rawClass(typeHint);
/*  69 */       if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
/*  70 */         JsonSerializableSchema schemaInfo = (JsonSerializableSchema)rawClass.getAnnotation(JsonSerializableSchema.class);
/*  71 */         schemaType = schemaInfo.schemaType();
/*  72 */         if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
/*  73 */           objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
/*     */         }
/*  75 */         if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
/*  76 */           itemDefinition = schemaInfo.schemaItemDefinition();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  83 */     objectNode.put("type", schemaType);
/*  84 */     if (objectProperties != null) {
/*     */       try {
/*  86 */         objectNode.set("properties", _getObjectMapper().readTree(objectProperties));
/*     */       } catch (IOException e) {
/*  88 */         provider.reportMappingProblem("Failed to parse @JsonSerializableSchema.schemaObjectPropertiesDefinition value", new Object[0]);
/*     */       }
/*     */     }
/*  91 */     if (itemDefinition != null) {
/*     */       try {
/*  93 */         objectNode.set("items", _getObjectMapper().readTree(itemDefinition));
/*     */       } catch (IOException e) {
/*  95 */         provider.reportMappingProblem("Failed to parse @JsonSerializableSchema.schemaItemDefinition value", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 100 */     return objectNode;
/*     */   }
/*     */   
/*     */   private static final synchronized ObjectMapper _getObjectMapper()
/*     */   {
/* 105 */     ObjectMapper mapper = (ObjectMapper)_mapperReference.get();
/* 106 */     if (mapper == null) {
/* 107 */       mapper = new ObjectMapper();
/* 108 */       _mapperReference.set(mapper);
/*     */     }
/* 110 */     return mapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 117 */     visitor.expectAnyFormat(typeHint);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\SerializableSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */