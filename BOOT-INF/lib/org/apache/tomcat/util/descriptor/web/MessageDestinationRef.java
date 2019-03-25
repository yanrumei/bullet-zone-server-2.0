/*     */ package org.apache.tomcat.util.descriptor.web;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageDestinationRef
/*     */   extends ResourceBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private String link = null;
/*     */   
/*     */   public String getLink() {
/*  41 */     return this.link;
/*     */   }
/*     */   
/*     */   public void setLink(String link) {
/*  45 */     this.link = link;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private String usage = null;
/*     */   
/*     */   public String getUsage() {
/*  55 */     return this.usage;
/*     */   }
/*     */   
/*     */   public void setUsage(String usage) {
/*  59 */     this.usage = usage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  71 */     StringBuilder sb = new StringBuilder("MessageDestination[");
/*  72 */     sb.append("name=");
/*  73 */     sb.append(getName());
/*  74 */     if (this.link != null) {
/*  75 */       sb.append(", link=");
/*  76 */       sb.append(this.link);
/*     */     }
/*  78 */     if (getType() != null) {
/*  79 */       sb.append(", type=");
/*  80 */       sb.append(getType());
/*     */     }
/*  82 */     if (this.usage != null) {
/*  83 */       sb.append(", usage=");
/*  84 */       sb.append(this.usage);
/*     */     }
/*  86 */     if (getDescription() != null) {
/*  87 */       sb.append(", description=");
/*  88 */       sb.append(getDescription());
/*     */     }
/*  90 */     sb.append("]");
/*  91 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  97 */     int prime = 31;
/*  98 */     int result = super.hashCode();
/*  99 */     result = 31 * result + (this.link == null ? 0 : this.link.hashCode());
/* 100 */     result = 31 * result + (this.usage == null ? 0 : this.usage.hashCode());
/* 101 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 107 */     if (this == obj) {
/* 108 */       return true;
/*     */     }
/* 110 */     if (!super.equals(obj)) {
/* 111 */       return false;
/*     */     }
/* 113 */     if (getClass() != obj.getClass()) {
/* 114 */       return false;
/*     */     }
/* 116 */     MessageDestinationRef other = (MessageDestinationRef)obj;
/* 117 */     if (this.link == null) {
/* 118 */       if (other.link != null) {
/* 119 */         return false;
/*     */       }
/* 121 */     } else if (!this.link.equals(other.link)) {
/* 122 */       return false;
/*     */     }
/* 124 */     if (this.usage == null) {
/* 125 */       if (other.usage != null) {
/* 126 */         return false;
/*     */       }
/* 128 */     } else if (!this.usage.equals(other.usage)) {
/* 129 */       return false;
/*     */     }
/* 131 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\MessageDestinationRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */