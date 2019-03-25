/*     */ package com.fasterxml.jackson.databind.ext;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*     */ import com.fasterxml.jackson.databind.ser.Serializers;
/*     */ import java.io.Serializable;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
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
/*     */ public class OptionalHandlerFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
/*     */   private static final String SERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
/*     */   private static final String DESERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
/*     */   private static final String SERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMSerializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer";
/*     */   private static final String DESERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer";
/*     */   private static final Class<?> CLASS_DOM_NODE;
/*     */   private static final Class<?> CLASS_DOM_DOCUMENT;
/*     */   private static final Java7Support _jdk7Helper;
/*     */   
/*     */   static
/*     */   {
/*  49 */     Class<?> doc = null;Class<?> node = null;
/*     */     try {
/*  51 */       node = Node.class;
/*  52 */       doc = Document.class;
/*     */     }
/*     */     catch (Exception e) {
/*  55 */       Logger.getLogger(OptionalHandlerFactory.class.getName()).log(Level.INFO, "Could not load DOM `Node` and/or `Document` classes: no DOM support");
/*     */     }
/*     */     
/*  58 */     CLASS_DOM_NODE = node;
/*  59 */     CLASS_DOM_DOCUMENT = doc;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */     Java7Support x = null;
/*     */     try {
/*  70 */       x = Java7Support.instance();
/*     */     } catch (Throwable t) {}
/*  72 */     _jdk7Helper = x;
/*     */   }
/*     */   
/*  75 */   public static final OptionalHandlerFactory instance = new OptionalHandlerFactory();
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
/*     */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc)
/*     */   {
/*  88 */     Class<?> rawType = type.getRawClass();
/*     */     
/*  90 */     if (_jdk7Helper != null) {
/*  91 */       JsonSerializer<?> ser = _jdk7Helper.getSerializerForJavaNioFilePath(rawType);
/*  92 */       if (ser != null) {
/*  93 */         return ser;
/*     */       }
/*     */     }
/*  96 */     if ((CLASS_DOM_NODE != null) && (CLASS_DOM_NODE.isAssignableFrom(rawType))) {
/*  97 */       return (JsonSerializer)instantiate("com.fasterxml.jackson.databind.ext.DOMSerializer");
/*     */     }
/*  99 */     String className = rawType.getName();
/*     */     String factoryName;
/* 101 */     if ((className.startsWith("javax.xml.")) || (hasSuperClassStartingWith(rawType, "javax.xml."))) {
/* 102 */       factoryName = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
/*     */     } else {
/* 104 */       return null;
/*     */     }
/*     */     String factoryName;
/* 107 */     Object ob = instantiate(factoryName);
/* 108 */     if (ob == null) {
/* 109 */       return null;
/*     */     }
/* 111 */     return ((Serializers)ob).findSerializer(config, type, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 118 */     Class<?> rawType = type.getRawClass();
/*     */     
/* 120 */     if (_jdk7Helper != null) {
/* 121 */       JsonDeserializer<?> deser = _jdk7Helper.getDeserializerForJavaNioFilePath(rawType);
/* 122 */       if (deser != null) {
/* 123 */         return deser;
/*     */       }
/*     */     }
/* 126 */     if ((CLASS_DOM_NODE != null) && (CLASS_DOM_NODE.isAssignableFrom(rawType))) {
/* 127 */       return (JsonDeserializer)instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer");
/*     */     }
/* 129 */     if ((CLASS_DOM_DOCUMENT != null) && (CLASS_DOM_DOCUMENT.isAssignableFrom(rawType))) {
/* 130 */       return (JsonDeserializer)instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer");
/*     */     }
/* 132 */     String className = rawType.getName();
/*     */     String factoryName;
/* 134 */     if ((className.startsWith("javax.xml.")) || (hasSuperClassStartingWith(rawType, "javax.xml.")))
/*     */     {
/* 136 */       factoryName = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
/*     */     } else
/* 138 */       return null;
/*     */     String factoryName;
/* 140 */     Object ob = instantiate(factoryName);
/* 141 */     if (ob == null) {
/* 142 */       return null;
/*     */     }
/* 144 */     return ((Deserializers)ob).findBeanDeserializer(type, config, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object instantiate(String className)
/*     */   {
/*     */     try
/*     */     {
/* 156 */       return Class.forName(className).newInstance();
/*     */     }
/*     */     catch (LinkageError e) {}catch (Exception e) {}
/*     */     
/* 160 */     return null;
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
/*     */   private boolean hasSuperClassStartingWith(Class<?> rawType, String prefix)
/*     */   {
/* 173 */     for (Class<?> supertype = rawType.getSuperclass(); supertype != null; supertype = supertype.getSuperclass()) {
/* 174 */       if (supertype == Object.class) {
/* 175 */         return false;
/*     */       }
/* 177 */       if (supertype.getName().startsWith(prefix)) {
/* 178 */         return true;
/*     */       }
/*     */     }
/* 181 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ext\OptionalHandlerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */