/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import java.beans.IntrospectionException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.yaml.snakeyaml.constructor.Constructor;
/*    */ import org.yaml.snakeyaml.constructor.Constructor.ConstructMapping;
/*    */ import org.yaml.snakeyaml.introspector.Property;
/*    */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*    */ import org.yaml.snakeyaml.nodes.NodeId;
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
/*    */ public class YamlJavaBeanPropertyConstructor
/*    */   extends Constructor
/*    */ {
/* 36 */   private final Map<Class<?>, Map<String, Property>> properties = new HashMap();
/*    */   
/* 38 */   private final PropertyUtils propertyUtils = new PropertyUtils();
/*    */   
/*    */   public YamlJavaBeanPropertyConstructor(Class<?> theRoot) {
/* 41 */     super(theRoot);
/* 42 */     this.yamlClassConstructors.put(NodeId.mapping, new CustomPropertyConstructMapping());
/*    */   }
/*    */   
/*    */ 
/*    */   public YamlJavaBeanPropertyConstructor(Class<?> theRoot, Map<Class<?>, Map<String, String>> propertyAliases)
/*    */   {
/* 48 */     this(theRoot);
/* 49 */     for (Iterator localIterator1 = propertyAliases.keySet().iterator(); localIterator1.hasNext();) { key = (Class)localIterator1.next();
/* 50 */       map = (Map)propertyAliases.get(key);
/* 51 */       if (map != null) {
/* 52 */         for (String alias : map.keySet()) {
/* 53 */           addPropertyAlias(alias, key, (String)map.get(alias));
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */     Class<?> key;
/*    */     
/*    */     Map<String, String> map;
/*    */   }
/*    */   
/*    */ 
/*    */   protected final void addPropertyAlias(String alias, Class<?> type, String name)
/*    */   {
/* 67 */     Map<String, Property> typeMap = (Map)this.properties.get(type);
/* 68 */     if (typeMap == null) {
/* 69 */       typeMap = new HashMap();
/* 70 */       this.properties.put(type, typeMap);
/*    */     }
/*    */     try {
/* 73 */       typeMap.put(alias, this.propertyUtils.getProperty(type, name));
/*    */     }
/*    */     catch (IntrospectionException ex) {
/* 76 */       throw new RuntimeException(ex);
/*    */     }
/*    */   }
/*    */   
/*    */   class CustomPropertyConstructMapping extends Constructor.ConstructMapping
/*    */   {
/*    */     CustomPropertyConstructMapping() {
/* 83 */       super();
/*    */     }
/*    */     
/*    */     protected Property getProperty(Class<?> type, String name)
/*    */       throws IntrospectionException
/*    */     {
/* 89 */       Map<String, Property> forType = (Map)YamlJavaBeanPropertyConstructor.this.properties.get(type);
/* 90 */       Property property = forType == null ? null : (Property)forType.get(name);
/* 91 */       return property == null ? super.getProperty(type, name) : property;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\YamlJavaBeanPropertyConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */