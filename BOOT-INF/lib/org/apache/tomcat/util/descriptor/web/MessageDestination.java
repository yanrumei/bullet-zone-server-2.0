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
/*     */ public class MessageDestination
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
/*  38 */   private String displayName = null;
/*     */   
/*     */   public String getDisplayName() {
/*  41 */     return this.displayName;
/*     */   }
/*     */   
/*     */   public void setDisplayName(String displayName) {
/*  45 */     this.displayName = displayName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private String largeIcon = null;
/*     */   
/*     */   public String getLargeIcon() {
/*  55 */     return this.largeIcon;
/*     */   }
/*     */   
/*     */   public void setLargeIcon(String largeIcon) {
/*  59 */     this.largeIcon = largeIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private String smallIcon = null;
/*     */   
/*     */   public String getSmallIcon() {
/*  69 */     return this.smallIcon;
/*     */   }
/*     */   
/*     */   public void setSmallIcon(String smallIcon) {
/*  73 */     this.smallIcon = smallIcon;
/*     */   }
/*     */   
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
/*  86 */     StringBuilder sb = new StringBuilder("MessageDestination[");
/*  87 */     sb.append("name=");
/*  88 */     sb.append(getName());
/*  89 */     if (this.displayName != null) {
/*  90 */       sb.append(", displayName=");
/*  91 */       sb.append(this.displayName);
/*     */     }
/*  93 */     if (this.largeIcon != null) {
/*  94 */       sb.append(", largeIcon=");
/*  95 */       sb.append(this.largeIcon);
/*     */     }
/*  97 */     if (this.smallIcon != null) {
/*  98 */       sb.append(", smallIcon=");
/*  99 */       sb.append(this.smallIcon);
/*     */     }
/* 101 */     if (getDescription() != null) {
/* 102 */       sb.append(", description=");
/* 103 */       sb.append(getDescription());
/*     */     }
/* 105 */     sb.append("]");
/* 106 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 112 */     int prime = 31;
/* 113 */     int result = super.hashCode();
/*     */     
/* 115 */     result = 31 * result + (this.displayName == null ? 0 : this.displayName.hashCode());
/*     */     
/* 117 */     result = 31 * result + (this.largeIcon == null ? 0 : this.largeIcon.hashCode());
/*     */     
/* 119 */     result = 31 * result + (this.smallIcon == null ? 0 : this.smallIcon.hashCode());
/* 120 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 126 */     if (this == obj) {
/* 127 */       return true;
/*     */     }
/* 129 */     if (!super.equals(obj)) {
/* 130 */       return false;
/*     */     }
/* 132 */     if (getClass() != obj.getClass()) {
/* 133 */       return false;
/*     */     }
/* 135 */     MessageDestination other = (MessageDestination)obj;
/* 136 */     if (this.displayName == null) {
/* 137 */       if (other.displayName != null) {
/* 138 */         return false;
/*     */       }
/* 140 */     } else if (!this.displayName.equals(other.displayName)) {
/* 141 */       return false;
/*     */     }
/* 143 */     if (this.largeIcon == null) {
/* 144 */       if (other.largeIcon != null) {
/* 145 */         return false;
/*     */       }
/* 147 */     } else if (!this.largeIcon.equals(other.largeIcon)) {
/* 148 */       return false;
/*     */     }
/* 150 */     if (this.smallIcon == null) {
/* 151 */       if (other.smallIcon != null) {
/* 152 */         return false;
/*     */       }
/* 154 */     } else if (!this.smallIcon.equals(other.smallIcon)) {
/* 155 */       return false;
/*     */     }
/* 157 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\descriptor\web\MessageDestination.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */