/*    */ package org.hibernate.validator.internal.metadata.descriptor;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.validation.metadata.GroupConversionDescriptor;
/*    */ import javax.validation.metadata.ReturnValueDescriptor;
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
/*    */ public class ReturnValueDescriptorImpl
/*    */   extends ElementDescriptorImpl
/*    */   implements ReturnValueDescriptor
/*    */ {
/*    */   private final boolean cascaded;
/*    */   private final Set<GroupConversionDescriptor> groupConversions;
/*    */   
/*    */   public ReturnValueDescriptorImpl(Type returnType, Set<ConstraintDescriptorImpl<?>> returnValueConstraints, boolean cascaded, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence, Set<GroupConversionDescriptor> groupConversions)
/*    */   {
/* 30 */     super(returnType, returnValueConstraints, defaultGroupSequenceRedefined, defaultGroupSequence);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */     this.cascaded = cascaded;
/* 38 */     this.groupConversions = Collections.unmodifiableSet(groupConversions);
/*    */   }
/*    */   
/*    */   public boolean isCascaded()
/*    */   {
/* 43 */     return this.cascaded;
/*    */   }
/*    */   
/*    */   public Set<GroupConversionDescriptor> getGroupConversions()
/*    */   {
/* 48 */     return this.groupConversions;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 53 */     StringBuilder sb = new StringBuilder();
/* 54 */     sb.append("ReturnValueDescriptorImpl");
/* 55 */     sb.append("{cascaded=").append(this.cascaded);
/* 56 */     sb.append('}');
/* 57 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\ReturnValueDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */