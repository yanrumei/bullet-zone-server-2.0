/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanParameterInfo;
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
/*     */ public class OperationInfo
/*     */   extends FeatureInfo
/*     */ {
/*     */   static final long serialVersionUID = 4418342922072614875L;
/*  49 */   protected String impact = "UNKNOWN";
/*  50 */   protected String role = "operation";
/*  51 */   protected final ReadWriteLock parametersLock = new ReentrantReadWriteLock();
/*  52 */   protected ParameterInfo[] parameters = new ParameterInfo[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getImpact()
/*     */   {
/*  63 */     return this.impact;
/*     */   }
/*     */   
/*     */   public void setImpact(String impact) {
/*  67 */     if (impact == null) {
/*  68 */       this.impact = null;
/*     */     } else {
/*  70 */       this.impact = impact.toUpperCase(Locale.ENGLISH);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRole()
/*     */   {
/*  79 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(String role) {
/*  83 */     this.role = role;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getReturnType()
/*     */   {
/*  92 */     if (this.type == null) {
/*  93 */       this.type = "void";
/*     */     }
/*  95 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setReturnType(String returnType) {
/*  99 */     this.type = returnType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ParameterInfo[] getSignature()
/*     */   {
/* 106 */     Lock readLock = this.parametersLock.readLock();
/* 107 */     readLock.lock();
/*     */     try {
/* 109 */       return this.parameters;
/*     */     } finally {
/* 111 */       readLock.unlock();
/*     */     }
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
/*     */   public void addParameter(ParameterInfo parameter)
/*     */   {
/* 125 */     Lock writeLock = this.parametersLock.writeLock();
/* 126 */     writeLock.lock();
/*     */     try {
/* 128 */       ParameterInfo[] results = new ParameterInfo[this.parameters.length + 1];
/* 129 */       System.arraycopy(this.parameters, 0, results, 0, this.parameters.length);
/* 130 */       results[this.parameters.length] = parameter;
/* 131 */       this.parameters = results;
/* 132 */       this.info = null;
/*     */     } finally {
/* 134 */       writeLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   MBeanOperationInfo createOperationInfo()
/*     */   {
/* 147 */     if (this.info == null)
/*     */     {
/* 149 */       int impact = 3;
/* 150 */       if ("ACTION".equals(getImpact())) {
/* 151 */         impact = 1;
/* 152 */       } else if ("ACTION_INFO".equals(getImpact())) {
/* 153 */         impact = 2;
/* 154 */       } else if ("INFO".equals(getImpact())) {
/* 155 */         impact = 0;
/*     */       }
/*     */       
/*     */ 
/* 159 */       this.info = new MBeanOperationInfo(getName(), getDescription(), getMBeanParameterInfo(), getReturnType(), impact);
/*     */     }
/* 161 */     return (MBeanOperationInfo)this.info;
/*     */   }
/*     */   
/*     */   protected MBeanParameterInfo[] getMBeanParameterInfo() {
/* 165 */     ParameterInfo[] params = getSignature();
/* 166 */     MBeanParameterInfo[] parameters = new MBeanParameterInfo[params.length];
/*     */     
/* 168 */     for (int i = 0; i < params.length; i++)
/* 169 */       parameters[i] = params[i].createParameterInfo();
/* 170 */     return parameters;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\OperationInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */