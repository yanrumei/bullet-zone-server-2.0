/*    */ package org.hibernate.validator.internal.metadata.descriptor;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.validation.metadata.GroupConversionDescriptor;
/*    */ import javax.validation.metadata.PropertyDescriptor;
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
/*    */ public class PropertyDescriptorImpl
/*    */   extends ElementDescriptorImpl
/*    */   implements PropertyDescriptor
/*    */ {
/*    */   private final boolean cascaded;
/*    */   private final String property;
/*    */   private final Set<GroupConversionDescriptor> groupConversions;
/*    */   
/*    */   public PropertyDescriptorImpl(Type returnType, String propertyName, Set<ConstraintDescriptorImpl<?>> constraints, boolean cascaded, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence, Set<GroupConversionDescriptor> groupConversions)
/*    */   {
/* 33 */     super(returnType, constraints, defaultGroupSequenceRedefined, defaultGroupSequence);
/*    */     
/* 35 */     this.property = propertyName;
/* 36 */     this.cascaded = cascaded;
/* 37 */     this.groupConversions = Collections.unmodifiableSet(groupConversions);
/*    */   }
/*    */   
/*    */   public boolean isCascaded()
/*    */   {
/* 42 */     return this.cascaded;
/*    */   }
/*    */   
/*    */   public Set<GroupConversionDescriptor> getGroupConversions()
/*    */   {
/* 47 */     return this.groupConversions;
/*    */   }
/*    */   
/*    */   public String getPropertyName()
/*    */   {
/* 52 */     return this.property;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 57 */     StringBuilder sb = new StringBuilder();
/* 58 */     sb.append("PropertyDescriptorImpl");
/* 59 */     sb.append("{property=").append(this.property);
/* 60 */     sb.append(", cascaded='").append(this.cascaded).append('\'');
/* 61 */     sb.append('}');
/* 62 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\PropertyDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */