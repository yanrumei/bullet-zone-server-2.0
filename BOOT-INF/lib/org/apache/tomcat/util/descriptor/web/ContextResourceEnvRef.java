/*    */ package org.apache.tomcat.util.descriptor.web;
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
/*    */ public class ContextResourceEnvRef
/*    */   extends ResourceBase
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
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
/* 38 */   private boolean override = true;
/*    */   
/*    */   public boolean getOverride() {
/* 41 */     return this.override;
/*    */   }
/*    */   
/*    */   public void setOverride(boolean override) {
/* 45 */     this.override = override;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 57 */     StringBuilder sb = new StringBuilder("ContextResourceEnvRef[");
/* 58 */     sb.append("name=");
/* 59 */     sb.append(getName());
/* 60 */     if (getType() != null) {
/* 61 */       sb.append(", type=");
/* 62 */       sb.append(getType());
/*    */     }
/* 64 */     sb.append(", override=");
/* 65 */     sb.append(this.override);
/* 66 */     sb.append("]");
/* 67 */     return sb.toString();
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 73 */     int prime = 31;
/* 74 */     int result = super.hashCode();
/* 75 */     result = 31 * result + (this.override ? 1231 : 1237);
/* 76 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 82 */     if (this == obj) {
/* 83 */       return true;
/*    */     }
/* 85 */     if (!super.equals(obj)) {
/* 86 */       return false;
/*    */     }
/* 88 */     if (getClass() != obj.getClass()) {
/* 89 */       return false;
/*    */     }
/* 91 */     ContextResourceEnvRef other = (ContextResourceEnvRef)obj;
/* 92 */     if (this.override != other.override) {
/* 93 */       return false;
/*    */     }
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\ContextResourceEnvRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */