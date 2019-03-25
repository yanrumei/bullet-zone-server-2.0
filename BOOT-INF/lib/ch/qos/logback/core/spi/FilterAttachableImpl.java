/*    */ package ch.qos.logback.core.spi;
/*    */ 
/*    */ import ch.qos.logback.core.filter.Filter;
/*    */ import ch.qos.logback.core.util.COWArrayList;
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ public final class FilterAttachableImpl<E>
/*    */   implements FilterAttachable<E>
/*    */ {
/* 29 */   COWArrayList<Filter<E>> filterList = new COWArrayList(new Filter[0]);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addFilter(Filter<E> newFilter)
/*    */   {
/* 36 */     this.filterList.add(newFilter);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void clearAllFilters()
/*    */   {
/* 43 */     this.filterList.clear();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FilterReply getFilterChainDecision(E event)
/*    */   {
/* 53 */     Filter<E>[] filterArrray = (Filter[])this.filterList.asTypedArray();
/* 54 */     int len = filterArrray.length;
/*    */     
/* 56 */     for (int i = 0; i < len; i++) {
/* 57 */       FilterReply r = filterArrray[i].decide(event);
/* 58 */       if ((r == FilterReply.DENY) || (r == FilterReply.ACCEPT)) {
/* 59 */         return r;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 64 */     return FilterReply.NEUTRAL;
/*    */   }
/*    */   
/*    */   public List<Filter<E>> getCopyOfAttachedFiltersList() {
/* 68 */     return new ArrayList(this.filterList);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\spi\FilterAttachableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */