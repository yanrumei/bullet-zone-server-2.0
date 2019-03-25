/*     */ package org.apache.tomcat.util.digester;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class ObjectCreateRule
/*     */   extends Rule
/*     */ {
/*     */   public ObjectCreateRule(String className)
/*     */   {
/*  44 */     this(className, (String)null);
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
/*     */   public ObjectCreateRule(String className, String attributeName)
/*     */   {
/*  60 */     this.className = className;
/*  61 */     this.attributeName = attributeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   protected String attributeName = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   protected String className = null;
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
/*     */   public void begin(String namespace, String name, Attributes attributes)
/*     */     throws Exception
/*     */   {
/*  98 */     String realClassName = this.className;
/*  99 */     if (this.attributeName != null) {
/* 100 */       String value = attributes.getValue(this.attributeName);
/* 101 */       if (value != null) {
/* 102 */         realClassName = value;
/*     */       }
/*     */     }
/* 105 */     if (this.digester.log.isDebugEnabled()) {
/* 106 */       this.digester.log.debug("[ObjectCreateRule]{" + this.digester.match + "}New " + realClassName);
/*     */     }
/*     */     
/*     */ 
/* 110 */     if (realClassName == null) {
/* 111 */       throw new NullPointerException("No class name specified for " + namespace + " " + name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 116 */     Class<?> clazz = this.digester.getClassLoader().loadClass(realClassName);
/* 117 */     Object instance = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 118 */     this.digester.push(instance);
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
/*     */   public void end(String namespace, String name)
/*     */     throws Exception
/*     */   {
/* 134 */     Object top = this.digester.pop();
/* 135 */     if (this.digester.log.isDebugEnabled()) {
/* 136 */       this.digester.log.debug("[ObjectCreateRule]{" + this.digester.match + "} Pop " + top
/* 137 */         .getClass().getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 149 */     StringBuilder sb = new StringBuilder("ObjectCreateRule[");
/* 150 */     sb.append("className=");
/* 151 */     sb.append(this.className);
/* 152 */     sb.append(", attributeName=");
/* 153 */     sb.append(this.attributeName);
/* 154 */     sb.append("]");
/* 155 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\digester\ObjectCreateRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */