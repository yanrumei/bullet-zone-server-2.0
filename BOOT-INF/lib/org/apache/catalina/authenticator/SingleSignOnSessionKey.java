/*     */ package org.apache.catalina.authenticator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Manager;
/*     */ import org.apache.catalina.Session;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SingleSignOnSessionKey
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String sessionId;
/*     */   private final String contextName;
/*     */   private final String hostName;
/*     */   
/*     */   public SingleSignOnSessionKey(Session session)
/*     */   {
/*  40 */     this.sessionId = session.getId();
/*  41 */     Context context = session.getManager().getContext();
/*  42 */     this.contextName = context.getName();
/*  43 */     this.hostName = context.getParent().getName();
/*     */   }
/*     */   
/*     */   public String getSessionId() {
/*  47 */     return this.sessionId;
/*     */   }
/*     */   
/*     */   public String getContextName() {
/*  51 */     return this.contextName;
/*     */   }
/*     */   
/*     */   public String getHostName() {
/*  55 */     return this.hostName;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  60 */     int prime = 31;
/*  61 */     int result = 1;
/*     */     
/*  63 */     result = 31 * result + (this.sessionId == null ? 0 : this.sessionId.hashCode());
/*     */     
/*  65 */     result = 31 * result + (this.contextName == null ? 0 : this.contextName.hashCode());
/*     */     
/*  67 */     result = 31 * result + (this.hostName == null ? 0 : this.hostName.hashCode());
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/*  73 */     if (this == obj) {
/*  74 */       return true;
/*     */     }
/*  76 */     if (obj == null) {
/*  77 */       return false;
/*     */     }
/*  79 */     if (getClass() != obj.getClass()) {
/*  80 */       return false;
/*     */     }
/*  82 */     SingleSignOnSessionKey other = (SingleSignOnSessionKey)obj;
/*  83 */     if (this.sessionId == null) {
/*  84 */       if (other.sessionId != null) {
/*  85 */         return false;
/*     */       }
/*  87 */     } else if (!this.sessionId.equals(other.sessionId)) {
/*  88 */       return false;
/*     */     }
/*  90 */     if (this.contextName == null) {
/*  91 */       if (other.contextName != null) {
/*  92 */         return false;
/*     */       }
/*  94 */     } else if (!this.contextName.equals(other.contextName)) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (this.hostName == null) {
/*  98 */       if (other.hostName != null) {
/*  99 */         return false;
/*     */       }
/* 101 */     } else if (!this.hostName.equals(other.hostName)) {
/* 102 */       return false;
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     StringBuilder sb = new StringBuilder(128);
/* 113 */     sb.append("Host: [");
/* 114 */     sb.append(this.hostName);
/* 115 */     sb.append("], Context: [");
/* 116 */     sb.append(this.contextName);
/* 117 */     sb.append("], SessionID: [");
/* 118 */     sb.append(this.sessionId);
/* 119 */     sb.append("]");
/* 120 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\SingleSignOnSessionKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */