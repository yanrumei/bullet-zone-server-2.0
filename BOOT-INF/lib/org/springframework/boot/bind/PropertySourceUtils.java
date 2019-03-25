/*    */ package org.springframework.boot.bind;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.env.EnumerablePropertySource;
/*    */ import org.springframework.core.env.PropertySource;
/*    */ import org.springframework.core.env.PropertySources;
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
/*    */ public abstract class PropertySourceUtils
/*    */ {
/*    */   public static Map<String, Object> getSubProperties(PropertySources propertySources, String keyPrefix)
/*    */   {
/* 46 */     return getSubProperties(propertySources, null, keyPrefix);
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
/*    */ 
/*    */   public static Map<String, Object> getSubProperties(PropertySources propertySources, String rootPrefix, String keyPrefix)
/*    */   {
/* 61 */     RelaxedNames keyPrefixes = new RelaxedNames(keyPrefix);
/* 62 */     Map<String, Object> subProperties = new LinkedHashMap();
/* 63 */     for (PropertySource<?> source : propertySources) {
/* 64 */       if ((source instanceof EnumerablePropertySource)) {
/* 65 */         for (String name : ((EnumerablePropertySource)source)
/* 66 */           .getPropertyNames()) {
/* 67 */           String key = getSubKey(name, rootPrefix, keyPrefixes);
/*    */           
/* 69 */           if ((key != null) && (!subProperties.containsKey(key))) {
/* 70 */             subProperties.put(key, source.getProperty(name));
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 75 */     return Collections.unmodifiableMap(subProperties);
/*    */   }
/*    */   
/*    */   private static String getSubKey(String name, String rootPrefixes, RelaxedNames keyPrefix)
/*    */   {
/* 80 */     rootPrefixes = rootPrefixes == null ? "" : rootPrefixes;
/* 81 */     for (Iterator localIterator1 = new RelaxedNames(rootPrefixes).iterator(); localIterator1.hasNext();) { rootPrefix = (String)localIterator1.next();
/* 82 */       for (String candidateKeyPrefix : keyPrefix) {
/* 83 */         if (name.startsWith(rootPrefix + candidateKeyPrefix))
/* 84 */           return name.substring((rootPrefix + candidateKeyPrefix).length());
/*    */       }
/*    */     }
/*    */     String rootPrefix;
/* 88 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\bind\PropertySourceUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */