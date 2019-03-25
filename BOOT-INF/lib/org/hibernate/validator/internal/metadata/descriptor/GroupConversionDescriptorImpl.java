/*    */ package org.hibernate.validator.internal.metadata.descriptor;
/*    */ 
/*    */ import javax.validation.metadata.GroupConversionDescriptor;
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
/*    */ public class GroupConversionDescriptorImpl
/*    */   implements GroupConversionDescriptor
/*    */ {
/*    */   private final Class<?> from;
/*    */   private final Class<?> to;
/*    */   
/*    */   public GroupConversionDescriptorImpl(Class<?> from, Class<?> to)
/*    */   {
/* 22 */     this.from = from;
/* 23 */     this.to = to;
/*    */   }
/*    */   
/*    */   public Class<?> getFrom()
/*    */   {
/* 28 */     return this.from;
/*    */   }
/*    */   
/*    */   public Class<?> getTo()
/*    */   {
/* 33 */     return this.to;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 38 */     int prime = 31;
/* 39 */     int result = 1;
/* 40 */     result = 31 * result + (this.from == null ? 0 : this.from.hashCode());
/* 41 */     result = 31 * result + (this.to == null ? 0 : this.to.hashCode());
/* 42 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 47 */     if (this == obj) {
/* 48 */       return true;
/*    */     }
/* 50 */     if (obj == null) {
/* 51 */       return false;
/*    */     }
/* 53 */     if (getClass() != obj.getClass()) {
/* 54 */       return false;
/*    */     }
/* 56 */     GroupConversionDescriptorImpl other = (GroupConversionDescriptorImpl)obj;
/* 57 */     if (this.from == null) {
/* 58 */       if (other.from != null) {
/* 59 */         return false;
/*    */       }
/*    */     }
/* 62 */     else if (!this.from.equals(other.from)) {
/* 63 */       return false;
/*    */     }
/* 65 */     if (this.to == null) {
/* 66 */       if (other.to != null) {
/* 67 */         return false;
/*    */       }
/*    */     }
/* 70 */     else if (!this.to.equals(other.to)) {
/* 71 */       return false;
/*    */     }
/* 73 */     return true;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 78 */     return "GroupConversionDescriptorImpl [from=" + this.from + ", to=" + this.to + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\GroupConversionDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */