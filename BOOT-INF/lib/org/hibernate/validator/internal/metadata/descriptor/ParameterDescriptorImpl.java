/*    */ package org.hibernate.validator.internal.metadata.descriptor;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.validation.metadata.GroupConversionDescriptor;
/*    */ import javax.validation.metadata.ParameterDescriptor;
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
/*    */ public class ParameterDescriptorImpl
/*    */   extends ElementDescriptorImpl
/*    */   implements ParameterDescriptor
/*    */ {
/*    */   private final int index;
/*    */   private final String name;
/*    */   private final boolean cascaded;
/*    */   private final Set<GroupConversionDescriptor> groupConversions;
/*    */   
/*    */   public ParameterDescriptorImpl(Type type, int index, String name, Set<ConstraintDescriptorImpl<?>> constraints, boolean isCascaded, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence, Set<GroupConversionDescriptor> groupConversions)
/*    */   {
/* 36 */     super(type, constraints, defaultGroupSequenceRedefined, defaultGroupSequence);
/* 37 */     this.index = index;
/* 38 */     this.name = name;
/* 39 */     this.cascaded = isCascaded;
/* 40 */     this.groupConversions = Collections.unmodifiableSet(groupConversions);
/*    */   }
/*    */   
/*    */   public boolean isCascaded()
/*    */   {
/* 45 */     return this.cascaded;
/*    */   }
/*    */   
/*    */   public Set<GroupConversionDescriptor> getGroupConversions()
/*    */   {
/* 50 */     return this.groupConversions;
/*    */   }
/*    */   
/*    */   public int getIndex()
/*    */   {
/* 55 */     return this.index;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 60 */     return this.name;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     StringBuilder sb = new StringBuilder();
/* 66 */     sb.append("ParameterDescriptorImpl");
/* 67 */     sb.append("{cascaded=").append(this.cascaded);
/* 68 */     sb.append(", index=").append(this.index);
/* 69 */     sb.append(", name=").append(this.name);
/* 70 */     sb.append('}');
/* 71 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\ParameterDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */