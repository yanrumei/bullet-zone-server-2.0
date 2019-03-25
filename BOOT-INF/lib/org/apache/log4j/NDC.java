/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.util.Stack;
/*    */ import org.slf4j.MDC;
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
/*    */ public class NDC
/*    */ {
/*    */   public static final String PREFIX = "NDC";
/*    */   
/*    */   public static void clear()
/*    */   {
/* 34 */     int depth = getDepth();
/* 35 */     for (int i = 0; i < depth; i++) {
/* 36 */       String key = "NDC" + i;
/* 37 */       MDC.remove(key);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Stack cloneStack()
/*    */   {
/* 43 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public static void inherit(Stack stack) {}
/*    */   
/*    */   public static String get()
/*    */   {
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public static int getDepth() {
/* 55 */     int i = 0;
/*    */     for (;;) {
/* 57 */       String val = MDC.get("NDC" + i);
/* 58 */       if (val == null) break;
/* 59 */       i++;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 64 */     return i;
/*    */   }
/*    */   
/*    */   public static String pop() {
/* 68 */     int next = getDepth();
/* 69 */     if (next == 0) {
/* 70 */       return "";
/*    */     }
/* 72 */     int last = next - 1;
/* 73 */     String key = "NDC" + last;
/* 74 */     String val = MDC.get(key);
/* 75 */     MDC.remove(key);
/* 76 */     return val;
/*    */   }
/*    */   
/*    */   public static String peek() {
/* 80 */     int next = getDepth();
/* 81 */     if (next == 0) {
/* 82 */       return "";
/*    */     }
/* 84 */     int last = next - 1;
/* 85 */     String key = "NDC" + last;
/* 86 */     String val = MDC.get(key);
/* 87 */     return val;
/*    */   }
/*    */   
/*    */   public static void push(String message) {
/* 91 */     int next = getDepth();
/* 92 */     MDC.put("NDC" + next, message);
/*    */   }
/*    */   
/*    */   public static void remove() {}
/*    */   
/*    */   public static void setMaxDepth(int maxDepth) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\log4j-over-slf4j-1.7.25.jar!\org\apache\log4j\NDC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */