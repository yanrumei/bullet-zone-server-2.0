/*    */ package ch.qos.logback.core.spi;
/*    */ 
/*    */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
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
/*    */ public class CyclicBufferTracker<E>
/*    */   extends AbstractComponentTracker<CyclicBuffer<E>>
/*    */ {
/*    */   static final int DEFAULT_NUMBER_OF_BUFFERS = 64;
/*    */   static final int DEFAULT_BUFFER_SIZE = 256;
/* 30 */   int bufferSize = 256;
/*    */   
/*    */   public CyclicBufferTracker()
/*    */   {
/* 34 */     setMaxComponents(64);
/*    */   }
/*    */   
/*    */   public int getBufferSize() {
/* 38 */     return this.bufferSize;
/*    */   }
/*    */   
/*    */   public void setBufferSize(int bufferSize) {
/* 42 */     this.bufferSize = bufferSize;
/*    */   }
/*    */   
/*    */   protected void processPriorToRemoval(CyclicBuffer<E> component)
/*    */   {
/* 47 */     component.clear();
/*    */   }
/*    */   
/*    */   protected CyclicBuffer<E> buildComponent(String key)
/*    */   {
/* 52 */     return new CyclicBuffer(this.bufferSize);
/*    */   }
/*    */   
/*    */   protected boolean isComponentStale(CyclicBuffer<E> eCyclicBuffer)
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   List<String> liveKeysAsOrderedList()
/*    */   {
/* 62 */     return new ArrayList(this.liveMap.keySet());
/*    */   }
/*    */   
/*    */   List<String> lingererKeysAsOrderedList() {
/* 66 */     return new ArrayList(this.lingerersMap.keySet());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\spi\CyclicBufferTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */