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
/*     */ public class ResourceRef
/*     */   extends Reference
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String DEFAULT_FACTORY = "org.apache.naming.factory.ResourceFactory";
/*     */   public static final String DESCRIPTION = "description";
/*     */   public static final String SCOPE = "scope";
/*     */   public static final String AUTH = "auth";
/*     */   public static final String SINGLETON = "singleton";
/*     */   
/*     */   public ResourceRef(String resourceClass, String description, String scope, String auth, boolean singleton)
/*     */   {
/*  85 */     this(resourceClass, description, scope, auth, singleton, null, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceRef(String resourceClass, String description, String scope, String auth, boolean singleton, String factory, String factoryLocation)
/*     */   {
/* 105 */     super(resourceClass, factory, factoryLocation);
/* 106 */     StringRefAddr refAddr = null;
/* 107 */     if (description != null) {
/* 108 */       refAddr = new StringRefAddr("description", description);
/* 109 */       add(refAddr);
/*     */     }
/* 111 */     if (scope != null) {
/* 112 */       refAddr = new StringRefAddr("scope", scope);
/* 113 */       add(refAddr);
/*     */     }
/* 115 */     if (auth != null) {
/* 116 */       refAddr = new StringRefAddr("auth", auth);
/* 117 */       add(refAddr);
/*     */     }
/*     */     
/* 120 */     refAddr = new StringRefAddr("singleton", Boolean.toString(singleton));
/* 121 */     add(refAddr);
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
/* 137 */     String factory = super.getFactoryClassName();
/* 138 */     if (factory != null) {
/* 139 */       return factory;
/*     */     }
/* 141 */     factory = System.getProperty("java.naming.factory.object");
/* 142 */     if (factory != null) {
/* 143 */       return null;
/*     */     }
/* 145 */     return "org.apache.naming.factory.ResourceFactory";
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
/* 160 */     StringBuilder sb = new StringBuilder("ResourceRef[");
/* 161 */     sb.append("className=");
/* 162 */     sb.append(getClassName());
/* 163 */     sb.append(",factoryClassLocation=");
/* 164 */     sb.append(getFactoryClassLocation());
/* 165 */     sb.append(",factoryClassName=");
/* 166 */     sb.append(getFactoryClassName());
/* 167 */     Enumeration<RefAddr> refAddrs = getAll();
/* 168 */     while (refAddrs.hasMoreElements()) {
/* 169 */       RefAddr refAddr = (RefAddr)refAddrs.nextElement();
/* 170 */       sb.append(",{type=");
/* 171 */       sb.append(refAddr.getType());
/* 172 */       sb.append(",content=");
/* 173 */       sb.append(refAddr.getContent());
/* 174 */       sb.append("}");
/*     */     }
/* 176 */     sb.append("]");
/* 177 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\naming\ResourceRef.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */