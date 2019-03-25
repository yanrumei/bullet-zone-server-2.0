/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.EnumValues;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ @JacksonStdImpl
/*     */ public class EnumSerializer
/*     */   extends StdScalarSerializer<Enum<?>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final EnumValues _values;
/*     */   protected final Boolean _serializeAsIndex;
/*     */   
/*     */   @Deprecated
/*     */   public EnumSerializer(EnumValues v)
/*     */   {
/*  61 */     this(v, null);
/*     */   }
/*     */   
/*     */   public EnumSerializer(EnumValues v, Boolean serializeAsIndex)
/*     */   {
/*  66 */     super(v.getEnumClass(), false);
/*  67 */     this._values = v;
/*  68 */     this._serializeAsIndex = serializeAsIndex;
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
/*     */   public static EnumSerializer construct(Class<?> enumClass, SerializationConfig config, BeanDescription beanDesc, JsonFormat.Value format)
/*     */   {
/*  85 */     EnumValues v = EnumValues.constructFromName(config, enumClass);
/*  86 */     Boolean serializeAsIndex = _isShapeWrittenUsingIndex(enumClass, format, true, null);
/*  87 */     return new EnumSerializer(v, serializeAsIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  99 */     if (property != null) {
/* 100 */       JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/*     */       
/* 102 */       if (format != null) {
/* 103 */         Boolean serializeAsIndex = _isShapeWrittenUsingIndex(property.getType().getRawClass(), format, false, this._serializeAsIndex);
/*     */         
/* 105 */         if (serializeAsIndex != this._serializeAsIndex) {
/* 106 */           return new EnumSerializer(this._values, serializeAsIndex);
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnumValues getEnumValues()
/*     */   {
/* 119 */     return this._values;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(Enum<?> en, JsonGenerator gen, SerializerProvider serializers)
/*     */     throws IOException
/*     */   {
/* 132 */     if (_serializeAsIndex(serializers)) {
/* 133 */       gen.writeNumber(en.ordinal());
/* 134 */       return;
/*     */     }
/*     */     
/* 137 */     if (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 138 */       gen.writeString(en.toString());
/* 139 */       return;
/*     */     }
/* 141 */     gen.writeString(this._values.serializedValueFor(en));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 153 */     if (_serializeAsIndex(provider)) {
/* 154 */       return createSchemaNode("integer", true);
/*     */     }
/* 156 */     ObjectNode objectNode = createSchemaNode("string", true);
/* 157 */     ArrayNode enumNode; if (typeHint != null) {
/* 158 */       JavaType type = provider.constructType(typeHint);
/* 159 */       if (type.isEnumType()) {
/* 160 */         enumNode = objectNode.putArray("enum");
/* 161 */         for (SerializableString value : this._values.values()) {
/* 162 */           enumNode.add(value.getValue());
/*     */         }
/*     */       }
/*     */     }
/* 166 */     return objectNode;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 173 */     SerializerProvider serializers = visitor.getProvider();
/* 174 */     if (_serializeAsIndex(serializers)) {
/* 175 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.INT);
/* 176 */       return;
/*     */     }
/* 178 */     JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(typeHint);
/* 179 */     if (stringVisitor != null) {
/* 180 */       Set<String> enums = new LinkedHashSet();
/*     */       
/*     */ 
/* 183 */       if ((serializers != null) && (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)))
/*     */       {
/* 185 */         for (Enum<?> e : this._values.enums()) {
/* 186 */           enums.add(e.toString());
/*     */         }
/*     */         
/*     */       } else {
/* 190 */         for (SerializableString value : this._values.values()) {
/* 191 */           enums.add(value.getValue());
/*     */         }
/*     */       }
/* 194 */       stringVisitor.enumTypes(enums);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _serializeAsIndex(SerializerProvider serializers)
/*     */   {
/* 206 */     if (this._serializeAsIndex != null) {
/* 207 */       return this._serializeAsIndex.booleanValue();
/*     */     }
/* 209 */     return serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static Boolean _isShapeWrittenUsingIndex(Class<?> enumClass, JsonFormat.Value format, boolean fromClass, Boolean defaultValue)
/*     */   {
/* 220 */     JsonFormat.Shape shape = format == null ? null : format.getShape();
/* 221 */     if (shape == null) {
/* 222 */       return defaultValue;
/*     */     }
/*     */     
/* 225 */     if ((shape == JsonFormat.Shape.ANY) || (shape == JsonFormat.Shape.SCALAR)) {
/* 226 */       return defaultValue;
/*     */     }
/*     */     
/* 229 */     if ((shape == JsonFormat.Shape.STRING) || (shape == JsonFormat.Shape.NATURAL)) {
/* 230 */       return Boolean.FALSE;
/*     */     }
/*     */     
/* 233 */     if ((shape.isNumeric()) || (shape == JsonFormat.Shape.ARRAY)) {
/* 234 */       return Boolean.TRUE;
/*     */     }
/*     */     
/* 237 */     throw new IllegalArgumentException(String.format("Unsupported serialization shape (%s) for Enum %s, not supported as %s annotation", new Object[] { shape, enumClass.getName(), fromClass ? "class" : "property" }));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\EnumSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */