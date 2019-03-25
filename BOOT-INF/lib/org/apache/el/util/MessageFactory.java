/*    */ package org.apache.el.util;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
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
/*    */ public final class MessageFactory
/*    */ {
/* 29 */   static final ResourceBundle bundle = ResourceBundle.getBundle("org.apache.el.Messages");
/*    */   
/*    */ 
/*    */ 
/*    */   public static String get(String key)
/*    */   {
/*    */     try
/*    */     {
/* 37 */       return bundle.getString(key);
/*    */     } catch (MissingResourceException e) {}
/* 39 */     return key;
/*    */   }
/*    */   
/*    */   public static String get(String key, Object... args)
/*    */   {
/* 44 */     String value = get(key);
/*    */     
/* 46 */     MessageFormat mf = new MessageFormat(value);
/* 47 */     return mf.format(args, new StringBuffer(), null).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\e\\util\MessageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */