/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ClassPackagingData;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Marker;
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
/*    */ public class Util
/*    */ {
/* 29 */   static Map<String, ClassPackagingData> cache = new HashMap();
/*    */   
/*    */   public static boolean match(Marker marker, Marker[] markerArray) {
/* 32 */     if (markerArray == null) {
/* 33 */       throw new IllegalArgumentException("markerArray should not be null");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 38 */     int size = markerArray.length;
/* 39 */     for (int i = 0; i < size; i++)
/*    */     {
/*    */ 
/* 42 */       if (marker.contains(markerArray[i])) {
/* 43 */         return true;
/*    */       }
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\pattern\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */