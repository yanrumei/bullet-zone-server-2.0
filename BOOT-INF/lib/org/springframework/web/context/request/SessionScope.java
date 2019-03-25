/*    */ package org.springframework.web.context.request;
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
/*    */ public class SessionScope
/*    */   extends AbstractRequestAttributesScope
/*    */ {
/*    */   private final int scope;
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
/*    */   public SessionScope()
/*    */   {
/* 58 */     this.scope = 1;
/*    */   }
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
/*    */   public SessionScope(boolean globalSession)
/*    */   {
/* 75 */     this.scope = (globalSession ? 2 : 1);
/*    */   }
/*    */   
/*    */ 
/*    */   protected int getScope()
/*    */   {
/* 81 */     return this.scope;
/*    */   }
/*    */   
/*    */   public String getConversationId()
/*    */   {
/* 86 */     return RequestContextHolder.currentRequestAttributes().getSessionId();
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public Object get(String name, org.springframework.beans.factory.ObjectFactory<?> objectFactory)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: invokestatic 4	org/springframework/web/context/request/RequestContextHolder:currentRequestAttributes	()Lorg/springframework/web/context/request/RequestAttributes;
/*    */     //   3: invokeinterface 6 1 0
/*    */     //   8: astore_3
/*    */     //   9: aload_3
/*    */     //   10: dup
/*    */     //   11: astore 4
/*    */     //   13: monitorenter
/*    */     //   14: aload_0
/*    */     //   15: aload_1
/*    */     //   16: aload_2
/*    */     //   17: invokespecial 7	org/springframework/web/context/request/AbstractRequestAttributesScope:get	(Ljava/lang/String;Lorg/springframework/beans/factory/ObjectFactory;)Ljava/lang/Object;
/*    */     //   20: aload 4
/*    */     //   22: monitorexit
/*    */     //   23: areturn
/*    */     //   24: astore 5
/*    */     //   26: aload 4
/*    */     //   28: monitorexit
/*    */     //   29: aload 5
/*    */     //   31: athrow
/*    */     // Line number table:
/*    */     //   Java source line #91	-> byte code offset #0
/*    */     //   Java source line #92	-> byte code offset #9
/*    */     //   Java source line #93	-> byte code offset #14
/*    */     //   Java source line #94	-> byte code offset #24
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	32	0	this	SessionScope
/*    */     //   0	32	1	name	String
/*    */     //   0	32	2	objectFactory	org.springframework.beans.factory.ObjectFactory<?>
/*    */     //   8	2	3	mutex	Object
/*    */     //   11	16	4	Ljava/lang/Object;	Object
/*    */     //   24	6	5	localObject1	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   14	23	24	finally
/*    */     //   24	29	24	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public Object remove(String name)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: invokestatic 4	org/springframework/web/context/request/RequestContextHolder:currentRequestAttributes	()Lorg/springframework/web/context/request/RequestAttributes;
/*    */     //   3: invokeinterface 6 1 0
/*    */     //   8: astore_2
/*    */     //   9: aload_2
/*    */     //   10: dup
/*    */     //   11: astore_3
/*    */     //   12: monitorenter
/*    */     //   13: aload_0
/*    */     //   14: aload_1
/*    */     //   15: invokespecial 8	org/springframework/web/context/request/AbstractRequestAttributesScope:remove	(Ljava/lang/String;)Ljava/lang/Object;
/*    */     //   18: aload_3
/*    */     //   19: monitorexit
/*    */     //   20: areturn
/*    */     //   21: astore 4
/*    */     //   23: aload_3
/*    */     //   24: monitorexit
/*    */     //   25: aload 4
/*    */     //   27: athrow
/*    */     // Line number table:
/*    */     //   Java source line #99	-> byte code offset #0
/*    */     //   Java source line #100	-> byte code offset #9
/*    */     //   Java source line #101	-> byte code offset #13
/*    */     //   Java source line #102	-> byte code offset #21
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	28	0	this	SessionScope
/*    */     //   0	28	1	name	String
/*    */     //   8	2	2	mutex	Object
/*    */     //   11	13	3	Ljava/lang/Object;	Object
/*    */     //   21	5	4	localObject1	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   13	20	21	finally
/*    */     //   21	25	21	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\context\request\SessionScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */