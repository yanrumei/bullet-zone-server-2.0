/*     */ package org.springframework.beans.support;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PagedListHolder<E>
/*     */   implements Serializable
/*     */ {
/*     */   public static final int DEFAULT_PAGE_SIZE = 10;
/*     */   public static final int DEFAULT_MAX_LINKED_PAGES = 10;
/*     */   private List<E> source;
/*     */   private Date refreshDate;
/*     */   private SortDefinition sort;
/*     */   private SortDefinition sortUsed;
/*  66 */   private int pageSize = 10;
/*     */   
/*  68 */   private int page = 0;
/*     */   
/*     */   private boolean newPageSet;
/*     */   
/*  72 */   private int maxLinkedPages = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PagedListHolder()
/*     */   {
/*  81 */     this(new ArrayList(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PagedListHolder(List<E> source)
/*     */   {
/*  91 */     this(source, new MutableSortDefinition(true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PagedListHolder(List<E> source, SortDefinition sort)
/*     */   {
/* 100 */     setSource(source);
/* 101 */     setSort(sort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSource(List<E> source)
/*     */   {
/* 109 */     Assert.notNull(source, "Source List must not be null");
/* 110 */     this.source = source;
/* 111 */     this.refreshDate = new Date();
/* 112 */     this.sortUsed = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<E> getSource()
/*     */   {
/* 119 */     return this.source;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Date getRefreshDate()
/*     */   {
/* 126 */     return this.refreshDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSort(SortDefinition sort)
/*     */   {
/* 135 */     this.sort = sort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SortDefinition getSort()
/*     */   {
/* 142 */     return this.sort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPageSize(int pageSize)
/*     */   {
/* 151 */     if (pageSize != this.pageSize) {
/* 152 */       this.pageSize = pageSize;
/* 153 */       if (!this.newPageSet) {
/* 154 */         this.page = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 163 */     return this.pageSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPage(int page)
/*     */   {
/* 171 */     this.page = page;
/* 172 */     this.newPageSet = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPage()
/*     */   {
/* 180 */     this.newPageSet = false;
/* 181 */     if (this.page >= getPageCount()) {
/* 182 */       this.page = (getPageCount() - 1);
/*     */     }
/* 184 */     return this.page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaxLinkedPages(int maxLinkedPages)
/*     */   {
/* 191 */     this.maxLinkedPages = maxLinkedPages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxLinkedPages()
/*     */   {
/* 198 */     return this.maxLinkedPages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPageCount()
/*     */   {
/* 206 */     float nrOfPages = getNrOfElements() / getPageSize();
/* 207 */     return (int)((nrOfPages > (int)nrOfPages) || (nrOfPages == 0.0D) ? nrOfPages + 1.0F : nrOfPages);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isFirstPage()
/*     */   {
/* 214 */     return getPage() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isLastPage()
/*     */   {
/* 221 */     return getPage() == getPageCount() - 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void previousPage()
/*     */   {
/* 229 */     if (!isFirstPage()) {
/* 230 */       this.page -= 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void nextPage()
/*     */   {
/* 239 */     if (!isLastPage()) {
/* 240 */       this.page += 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getNrOfElements()
/*     */   {
/* 248 */     return getSource().size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getFirstElementOnPage()
/*     */   {
/* 256 */     return getPageSize() * getPage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLastElementOnPage()
/*     */   {
/* 264 */     int endIndex = getPageSize() * (getPage() + 1);
/* 265 */     int size = getNrOfElements();
/* 266 */     return (endIndex > size ? size : endIndex) - 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<E> getPageList()
/*     */   {
/* 273 */     return getSource().subList(getFirstElementOnPage(), getLastElementOnPage() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getFirstLinkedPage()
/*     */   {
/* 280 */     return Math.max(0, getPage() - getMaxLinkedPages() / 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getLastLinkedPage()
/*     */   {
/* 287 */     return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resort()
/*     */   {
/* 298 */     SortDefinition sort = getSort();
/* 299 */     if ((sort != null) && (!sort.equals(this.sortUsed))) {
/* 300 */       this.sortUsed = copySortDefinition(sort);
/* 301 */       doSort(getSource(), sort);
/* 302 */       setPage(0);
/*     */     }
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
/*     */   protected SortDefinition copySortDefinition(SortDefinition sort)
/*     */   {
/* 319 */     return new MutableSortDefinition(sort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doSort(List<E> source, SortDefinition sort)
/*     */   {
/* 330 */     PropertyComparator.sort(source, sort);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\support\PagedListHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */