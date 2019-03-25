/*    */ package org.apache.tomcat.websocket;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.websocket.Extension.Parameter;
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ public class TransformationFactory
/*    */ {
/* 27 */   private static final StringManager sm = StringManager.getManager(TransformationFactory.class);
/*    */   
/* 29 */   private static final TransformationFactory factory = new TransformationFactory();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static TransformationFactory getInstance()
/*    */   {
/* 36 */     return factory;
/*    */   }
/*    */   
/*    */   public Transformation create(String name, List<List<Extension.Parameter>> preferences, boolean isServer)
/*    */   {
/* 41 */     if ("permessage-deflate".equals(name)) {
/* 42 */       return PerMessageDeflate.negotiate(preferences, isServer);
/*    */     }
/* 44 */     if (Constants.ALLOW_UNSUPPORTED_EXTENSIONS) {
/* 45 */       return null;
/*    */     }
/*    */     
/* 48 */     throw new IllegalArgumentException(sm.getString("transformerFactory.unsupportedExtension", new Object[] { name }));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\TransformationFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */