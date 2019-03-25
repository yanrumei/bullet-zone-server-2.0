/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.FlashMapManager;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public abstract class AbstractFlashMapManager
/*     */   implements FlashMapManager
/*     */ {
/*  46 */   private static final Object DEFAULT_FLASH_MAPS_MUTEX = new Object();
/*     */   
/*     */ 
/*  49 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  51 */   private int flashMapTimeout = 180;
/*     */   
/*  53 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFlashMapTimeout(int flashMapTimeout)
/*     */   {
/*  62 */     this.flashMapTimeout = flashMapTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getFlashMapTimeout()
/*     */   {
/*  69 */     return this.flashMapTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/*  76 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  77 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public UrlPathHelper getUrlPathHelper()
/*     */   {
/*  84 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */   public final FlashMap retrieveAndUpdate(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/*  90 */     List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
/*  91 */     if (CollectionUtils.isEmpty(allFlashMaps)) {
/*  92 */       return null;
/*     */     }
/*     */     
/*  95 */     if (this.logger.isDebugEnabled()) {
/*  96 */       this.logger.debug("Retrieved FlashMap(s): " + allFlashMaps);
/*     */     }
/*  98 */     List<FlashMap> mapsToRemove = getExpiredFlashMaps(allFlashMaps);
/*  99 */     FlashMap match = getMatchingFlashMap(allFlashMaps, request);
/* 100 */     if (match != null) {
/* 101 */       mapsToRemove.add(match);
/*     */     }
/*     */     
/* 104 */     if (!mapsToRemove.isEmpty()) {
/* 105 */       if (this.logger.isDebugEnabled()) {
/* 106 */         this.logger.debug("Removing FlashMap(s): " + mapsToRemove);
/*     */       }
/* 108 */       Object mutex = getFlashMapsMutex(request);
/* 109 */       if (mutex != null) {
/* 110 */         synchronized (mutex) {
/* 111 */           allFlashMaps = retrieveFlashMaps(request);
/* 112 */           if (allFlashMaps != null) {
/* 113 */             allFlashMaps.removeAll(mapsToRemove);
/* 114 */             updateFlashMaps(allFlashMaps, request, response);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 119 */         allFlashMaps.removeAll(mapsToRemove);
/* 120 */         updateFlashMaps(allFlashMaps, request, response);
/*     */       }
/*     */     }
/*     */     
/* 124 */     return match;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<FlashMap> getExpiredFlashMaps(List<FlashMap> allMaps)
/*     */   {
/* 131 */     List<FlashMap> result = new LinkedList();
/* 132 */     for (FlashMap map : allMaps) {
/* 133 */       if (map.isExpired()) {
/* 134 */         result.add(map);
/*     */       }
/*     */     }
/* 137 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private FlashMap getMatchingFlashMap(List<FlashMap> allMaps, HttpServletRequest request)
/*     */   {
/* 145 */     List<FlashMap> result = new LinkedList();
/* 146 */     for (FlashMap flashMap : allMaps) {
/* 147 */       if (isFlashMapForRequest(flashMap, request)) {
/* 148 */         result.add(flashMap);
/*     */       }
/*     */     }
/* 151 */     if (!result.isEmpty()) {
/* 152 */       Collections.sort(result);
/* 153 */       if (this.logger.isDebugEnabled()) {
/* 154 */         this.logger.debug("Found matching FlashMap(s): " + result);
/*     */       }
/* 156 */       return (FlashMap)result.get(0);
/*     */     }
/* 158 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isFlashMapForRequest(FlashMap flashMap, HttpServletRequest request)
/*     */   {
/* 166 */     String expectedPath = flashMap.getTargetRequestPath();
/* 167 */     if (expectedPath != null) {
/* 168 */       String requestUri = getUrlPathHelper().getOriginatingRequestUri(request);
/* 169 */       if ((!requestUri.equals(expectedPath)) && (!requestUri.equals(expectedPath + "/"))) {
/* 170 */         return false;
/*     */       }
/*     */     }
/* 173 */     MultiValueMap<String, String> actualParams = getOriginatingRequestParams(request);
/* 174 */     MultiValueMap<String, String> expectedParams = flashMap.getTargetRequestParams();
/* 175 */     for (String expectedName : expectedParams.keySet()) {
/* 176 */       actualValues = (List)actualParams.get(expectedName);
/* 177 */       if (actualValues == null) {
/* 178 */         return false;
/*     */       }
/* 180 */       for (String expectedValue : (List)expectedParams.get(expectedName)) {
/* 181 */         if (!actualValues.contains(expectedValue))
/* 182 */           return false;
/*     */       }
/*     */     }
/*     */     List<String> actualValues;
/* 186 */     return true;
/*     */   }
/*     */   
/*     */   private MultiValueMap<String, String> getOriginatingRequestParams(HttpServletRequest request) {
/* 190 */     String query = getUrlPathHelper().getOriginatingQueryString(request);
/* 191 */     return ServletUriComponentsBuilder.fromPath("/").query(query).build().getQueryParams();
/*     */   }
/*     */   
/*     */   public final void saveOutputFlashMap(FlashMap flashMap, HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 196 */     if (CollectionUtils.isEmpty(flashMap)) {
/* 197 */       return;
/*     */     }
/*     */     
/* 200 */     String path = decodeAndNormalizePath(flashMap.getTargetRequestPath(), request);
/* 201 */     flashMap.setTargetRequestPath(path);
/*     */     
/* 203 */     if (this.logger.isDebugEnabled()) {
/* 204 */       this.logger.debug("Saving FlashMap=" + flashMap);
/*     */     }
/* 206 */     flashMap.startExpirationPeriod(getFlashMapTimeout());
/*     */     
/* 208 */     Object mutex = getFlashMapsMutex(request);
/* 209 */     if (mutex != null) {
/* 210 */       synchronized (mutex) {
/* 211 */         List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
/* 212 */         allFlashMaps = allFlashMaps != null ? allFlashMaps : new CopyOnWriteArrayList();
/* 213 */         allFlashMaps.add(flashMap);
/* 214 */         updateFlashMaps(allFlashMaps, request, response);
/*     */       }
/*     */     }
/*     */     else {
/* 218 */       List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
/* 219 */       allFlashMaps = allFlashMaps != null ? allFlashMaps : new LinkedList();
/* 220 */       allFlashMaps.add(flashMap);
/* 221 */       updateFlashMaps(allFlashMaps, request, response);
/*     */     }
/*     */   }
/*     */   
/*     */   private String decodeAndNormalizePath(String path, HttpServletRequest request) {
/* 226 */     if (path != null) {
/* 227 */       path = getUrlPathHelper().decodeRequestString(request, path);
/* 228 */       if (path.charAt(0) != '/') {
/* 229 */         String requestUri = getUrlPathHelper().getRequestUri(request);
/* 230 */         path = requestUri.substring(0, requestUri.lastIndexOf('/') + 1) + path;
/* 231 */         path = StringUtils.cleanPath(path);
/*     */       }
/*     */     }
/* 234 */     return path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract List<FlashMap> retrieveFlashMaps(HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void updateFlashMaps(List<FlashMap> paramList, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getFlashMapsMutex(HttpServletRequest request)
/*     */   {
/* 264 */     return DEFAULT_FLASH_MAPS_MUTEX;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\support\AbstractFlashMapManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */