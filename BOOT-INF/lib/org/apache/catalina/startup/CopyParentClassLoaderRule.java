/*    */ package org.apache.catalina.startup;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.catalina.Container;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.tomcat.util.digester.Digester;
/*    */ import org.apache.tomcat.util.digester.Rule;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class CopyParentClassLoaderRule
/*    */   extends Rule
/*    */ {
/*    */   public void begin(String namespace, String name, Attributes attributes)
/*    */     throws Exception
/*    */   {
/* 64 */     if (this.digester.getLogger().isDebugEnabled())
/* 65 */       this.digester.getLogger().debug("Copying parent class loader");
/* 66 */     Container child = (Container)this.digester.peek(0);
/* 67 */     Object parent = this.digester.peek(1);
/*    */     
/* 69 */     Method method = parent.getClass().getMethod("getParentClassLoader", new Class[0]);
/*    */     
/* 71 */     ClassLoader classLoader = (ClassLoader)method.invoke(parent, new Object[0]);
/* 72 */     child.setParentClassLoader(classLoader);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\CopyParentClassLoaderRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */