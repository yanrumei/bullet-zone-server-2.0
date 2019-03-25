/*     */ package org.apache.catalina.startup;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.tomcat.util.IntrospectionUtils;
/*     */ import org.apache.tomcat.util.digester.Digester;
/*     */ import org.apache.tomcat.util.digester.Rule;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LifecycleListenerRule
/*     */   extends Rule
/*     */ {
/*     */   private final String attributeName;
/*     */   private final String listenerClass;
/*     */   
/*     */   public LifecycleListenerRule(String listenerClass, String attributeName)
/*     */   {
/*  59 */     this.listenerClass = listenerClass;
/*  60 */     this.attributeName = attributeName;
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
/*  95 */     Container c = (Container)this.digester.peek();
/*  96 */     Container p = null;
/*  97 */     Object obj = this.digester.peek(1);
/*  98 */     if ((obj instanceof Container)) {
/*  99 */       p = (Container)obj;
/*     */     }
/*     */     
/* 102 */     String className = null;
/*     */     
/*     */ 
/* 105 */     if (this.attributeName != null) {
/* 106 */       String value = attributes.getValue(this.attributeName);
/* 107 */       if (value != null) {
/* 108 */         className = value;
/*     */       }
/*     */     }
/*     */     
/* 112 */     if ((p != null) && (className == null))
/*     */     {
/* 114 */       String configClass = (String)IntrospectionUtils.getProperty(p, this.attributeName);
/* 115 */       if ((configClass != null) && (configClass.length() > 0)) {
/* 116 */         className = configClass;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 121 */     if (className == null) {
/* 122 */       className = this.listenerClass;
/*     */     }
/*     */     
/*     */ 
/* 126 */     Class<?> clazz = Class.forName(className);
/* 127 */     LifecycleListener listener = (LifecycleListener)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */     
/*     */ 
/* 130 */     c.addLifecycleListener(listener);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\LifecycleListenerRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */