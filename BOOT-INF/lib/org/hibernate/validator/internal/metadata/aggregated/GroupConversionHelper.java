/*    */ package org.hibernate.validator.internal.metadata.aggregated;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import javax.validation.GroupSequence;
/*    */ import javax.validation.metadata.GroupConversionDescriptor;
/*    */ import org.hibernate.validator.internal.metadata.descriptor.GroupConversionDescriptorImpl;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public class GroupConversionHelper
/*    */ {
/* 28 */   private static final Log log = ;
/*    */   private final Map<Class<?>, Class<?>> groupConversions;
/*    */   
/*    */   public GroupConversionHelper(Map<Class<?>, Class<?>> groupConversions) {
/* 32 */     this.groupConversions = Collections.unmodifiableMap(groupConversions);
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
/*    */   public Class<?> convertGroup(Class<?> from)
/*    */   {
/* 46 */     Class<?> to = (Class)this.groupConversions.get(from);
/* 47 */     return to != null ? to : from;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Set<GroupConversionDescriptor> asDescriptors()
/*    */   {
/* 58 */     Set<GroupConversionDescriptor> descriptors = CollectionHelper.newHashSet(this.groupConversions.size());
/*    */     
/* 60 */     for (Map.Entry<Class<?>, Class<?>> conversion : this.groupConversions.entrySet()) {
/* 61 */       descriptors.add(new GroupConversionDescriptorImpl(
/*    */       
/* 63 */         (Class)conversion.getKey(), 
/* 64 */         (Class)conversion.getValue()));
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 69 */     return Collections.unmodifiableSet(descriptors);
/*    */   }
/*    */   
/*    */   public void validateGroupConversions(boolean isCascaded, String location)
/*    */   {
/* 74 */     if ((!isCascaded) && (!this.groupConversions.isEmpty())) {
/* 75 */       throw log.getGroupConversionOnNonCascadingElementException(location);
/*    */     }
/*    */     
/*    */ 
/* 79 */     for (Class<?> oneGroup : this.groupConversions.keySet()) {
/* 80 */       if (isGroupSequence(oneGroup)) {
/* 81 */         throw log.getGroupConversionForSequenceException(oneGroup);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean isGroupSequence(Class<?> oneGroup) {
/* 87 */     return oneGroup.isAnnotationPresent(GroupSequence.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\GroupConversionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */