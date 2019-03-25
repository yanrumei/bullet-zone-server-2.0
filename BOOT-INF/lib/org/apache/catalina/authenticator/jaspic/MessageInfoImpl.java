/*    */ package org.apache.catalina.authenticator.jaspic;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.security.auth.message.MessageInfo;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import javax.servlet.http.HttpServletResponse;
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
/*    */ public class MessageInfoImpl
/*    */   implements MessageInfo
/*    */ {
/* 29 */   protected static final StringManager sm = StringManager.getManager(MessageInfoImpl.class);
/*    */   
/*    */   public static final String IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";
/*    */   
/* 33 */   private final Map<String, Object> map = new HashMap();
/*    */   private HttpServletRequest request;
/*    */   private HttpServletResponse response;
/*    */   
/*    */   public MessageInfoImpl() {}
/*    */   
/*    */   public MessageInfoImpl(HttpServletRequest request, HttpServletResponse response, boolean authMandatory)
/*    */   {
/* 41 */     this.request = request;
/* 42 */     this.response = response;
/* 43 */     this.map.put("javax.security.auth.message.MessagePolicy.isMandatory", Boolean.toString(authMandatory));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Map getMap()
/*    */   {
/* 50 */     return this.map;
/*    */   }
/*    */   
/*    */   public Object getRequestMessage()
/*    */   {
/* 55 */     return this.request;
/*    */   }
/*    */   
/*    */   public Object getResponseMessage()
/*    */   {
/* 60 */     return this.response;
/*    */   }
/*    */   
/*    */   public void setRequestMessage(Object request)
/*    */   {
/* 65 */     if (!(request instanceof HttpServletRequest)) {
/* 66 */       throw new IllegalArgumentException(sm.getString("authenticator.jaspic.badRequestType", new Object[] {request
/* 67 */         .getClass().getName() }));
/*    */     }
/* 69 */     this.request = ((HttpServletRequest)request);
/*    */   }
/*    */   
/*    */   public void setResponseMessage(Object response)
/*    */   {
/* 74 */     if (!(response instanceof HttpServletResponse)) {
/* 75 */       throw new IllegalArgumentException(sm.getString("authenticator.jaspic.badResponseType", new Object[] {response
/* 76 */         .getClass().getName() }));
/*    */     }
/* 78 */     this.response = ((HttpServletResponse)response);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\authenticator\jaspic\MessageInfoImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */