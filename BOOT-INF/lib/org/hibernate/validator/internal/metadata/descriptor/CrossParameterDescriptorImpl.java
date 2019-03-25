/*    */ package org.hibernate.validator.internal.metadata.descriptor;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.validation.metadata.CrossParameterDescriptor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CrossParameterDescriptorImpl
/*    */   extends ElementDescriptorImpl
/*    */   implements CrossParameterDescriptor
/*    */ {
/*    */   public CrossParameterDescriptorImpl(Set<ConstraintDescriptorImpl<?>> constraintDescriptors, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*    */   {
/* 21 */     super(Object[].class, constraintDescriptors, defaultGroupSequenceRedefined, defaultGroupSequence);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\CrossParameterDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */