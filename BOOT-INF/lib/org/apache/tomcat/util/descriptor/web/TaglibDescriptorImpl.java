/*    */ package org.apache.tomcat.util.descriptor.web;
/*    */ 
/*    */ import javax.servlet.descriptor.TaglibDescriptor;
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
/*    */ public class TaglibDescriptorImpl
/*    */   implements TaglibDescriptor
/*    */ {
/*    */   private final String location;
/*    */   private final String uri;
/*    */   
/*    */   public TaglibDescriptorImpl(String location, String uri)
/*    */   {
/* 28 */     this.location = location;
/* 29 */     this.uri = uri;
/*    */   }
/*    */   
/*    */   public String getTaglibLocation()
/*    */   {
/* 34 */     return this.location;
/*    */   }
/*    */   
/*    */   public String getTaglibURI()
/*    */   {
/* 39 */     return this.uri;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 44 */     int prime = 31;
/* 45 */     int result = 1;
/*    */     
/* 47 */     result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
/* 48 */     result = 31 * result + (this.uri == null ? 0 : this.uri.hashCode());
/* 49 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 54 */     if (this == obj) {
/* 55 */       return true;
/*    */     }
/* 57 */     if (!(obj instanceof TaglibDescriptorImpl)) {
/* 58 */       return false;
/*    */     }
/* 60 */     TaglibDescriptorImpl other = (TaglibDescriptorImpl)obj;
/* 61 */     if (this.location == null) {
/* 62 */       if (other.location != null) {
/* 63 */         return false;
/*    */       }
/* 65 */     } else if (!this.location.equals(other.location)) {
/* 66 */       return false;
/*    */     }
/* 68 */     if (this.uri == null) {
/* 69 */       if (other.uri != null) {
/* 70 */         return false;
/*    */       }
/* 72 */     } else if (!this.uri.equals(other.uri)) {
/* 73 */       return false;
/*    */     }
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\TaglibDescriptorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */