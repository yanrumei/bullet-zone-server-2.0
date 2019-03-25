/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*    */ class GroupConversionBuilder
/*    */ {
/*    */   private final ClassLoadingHelper classLoadingHelper;
/*    */   
/*    */   GroupConversionBuilder(ClassLoadingHelper classLoadingHelper)
/*    */   {
/* 24 */     this.classLoadingHelper = classLoadingHelper;
/*    */   }
/*    */   
/*    */   Map<Class<?>, Class<?>> buildGroupConversionMap(List<GroupConversionType> groupConversionTypes, String defaultPackage)
/*    */   {
/* 29 */     Map<Class<?>, Class<?>> groupConversionMap = CollectionHelper.newHashMap();
/* 30 */     for (GroupConversionType groupConversionType : groupConversionTypes) {
/* 31 */       Class<?> fromClass = this.classLoadingHelper.loadClass(groupConversionType.getFrom(), defaultPackage);
/* 32 */       Class<?> toClass = this.classLoadingHelper.loadClass(groupConversionType.getTo(), defaultPackage);
/* 33 */       groupConversionMap.put(fromClass, toClass);
/*    */     }
/*    */     
/* 36 */     return groupConversionMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\GroupConversionBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */