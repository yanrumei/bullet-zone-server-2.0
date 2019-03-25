/*     */ package org.apache.naming;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HandlerRef
/*     */   extends Reference
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.HandlerFactory";
/*     */   public static final String HANDLER_NAME = "handlername";
/*     */   public static final String HANDLER_CLASS = "handlerclass";
/*     */   public static final String HANDLER_LOCALPART = "handlerlocalpart";
/*     */   public static final String HANDLER_NAMESPACE = "handlernamespace";
/*     */   public static final String HANDLER_PARAMNAME = "handlerparamname";
/*     */   public static final String HANDLER_PARAMVALUE = "handlerparamvalue";
/*     */   public static final String HANDLER_SOAPROLE = "handlersoaprole";
/*     */   public static final String HANDLER_PORTNAME = "handlerportname";
/*     */   
/*     */   public HandlerRef(String refname, String handlerClass)
/*     */   {
/*  99 */     this(refname, handlerClass, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public HandlerRef(String refname, String handlerClass, String factory, String factoryLocation)
/*     */   {
/* 105 */     super(refname, factory, factoryLocation);
/* 106 */     StringRefAddr refAddr = null;
/* 107 */     if (refname != null) {
/* 108 */       refAddr = new StringRefAddr("handlername", refname);
/* 109 */       add(refAddr);
/*     */     }
/* 111 */     if (handlerClass != null) {
/* 112 */       refAddr = new StringRefAddr("handlerclass", handlerClass);
/* 113 */       add(refAddr);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFactoryClassName()
/*     */   {
/* 130 */     String factory = super.getFactoryClassName();
/* 131 */     if (factory != null) {
/* 132 */       return factory;
/*     */     }
/* 134 */     factory = System.getProperty("java.naming.factory.object");
/* 135 */     if (factory != null) {
/* 136 */       return null;
/*     */     }
/* 138 */     return "org.apache.naming.factory.HandlerFactory";
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
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 153 */     StringBuilder sb = new StringBuilder("HandlerRef[");
/* 154 */     sb.append("className=");
/* 155 */     sb.append(getClassName());
/* 156 */     sb.append(",factoryClassLocation=");
/* 157 */     sb.append(getFactoryClassLocation());
/* 158 */     sb.append(",factoryClassName=");
/* 159 */     sb.append(getFactoryClassName());
/* 160 */     Enumeration<RefAddr> refAddrs = getAll();
/* 161 */     while (refAddrs.hasMoreElements()) {
/* 162 */       RefAddr refAddr = (RefAddr)refAddrs.nextElement();
/* 163 */       sb.append(",{type=");
/* 164 */       sb.append(refAddr.getType());
/* 165 */       sb.append(",content=");
/* 166 */       sb.append(refAddr.getContent());
/* 167 */       sb.append("}");
/*     */     }
/* 169 */     sb.append("]");
/* 170 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\HandlerRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */