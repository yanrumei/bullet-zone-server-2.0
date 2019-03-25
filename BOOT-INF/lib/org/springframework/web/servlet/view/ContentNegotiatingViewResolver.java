/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.SmartView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
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
/*     */ public class ContentNegotiatingViewResolver
/*     */   extends WebApplicationObjectSupport
/*     */   implements ViewResolver, Ordered, InitializingBean
/*     */ {
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*  92 */   private final ContentNegotiationManagerFactoryBean cnmFactoryBean = new ContentNegotiationManagerFactoryBean();
/*     */   
/*  94 */   private boolean useNotAcceptableStatusCode = false;
/*     */   
/*     */   private List<View> defaultViews;
/*     */   
/*     */   private List<ViewResolver> viewResolvers;
/*     */   
/* 100 */   private int order = Integer.MIN_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 110 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContentNegotiationManager getContentNegotiationManager()
/*     */   {
/* 118 */     return this.contentNegotiationManager;
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
/*     */   public void setUseNotAcceptableStatusCode(boolean useNotAcceptableStatusCode)
/*     */   {
/* 131 */     this.useNotAcceptableStatusCode = useNotAcceptableStatusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isUseNotAcceptableStatusCode()
/*     */   {
/* 138 */     return this.useNotAcceptableStatusCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultViews(List<View> defaultViews)
/*     */   {
/* 146 */     this.defaultViews = defaultViews;
/*     */   }
/*     */   
/*     */   public List<View> getDefaultViews() {
/* 150 */     return Collections.unmodifiableList(this.defaultViews);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewResolvers(List<ViewResolver> viewResolvers)
/*     */   {
/* 158 */     this.viewResolvers = viewResolvers;
/*     */   }
/*     */   
/*     */   public List<ViewResolver> getViewResolvers() {
/* 162 */     return Collections.unmodifiableList(this.viewResolvers);
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 166 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 171 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void initServletContext(ServletContext servletContext)
/*     */   {
/* 178 */     Collection<ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), ViewResolver.class).values();
/* 179 */     if (this.viewResolvers == null) {
/* 180 */       this.viewResolvers = new ArrayList(matchingBeans.size());
/* 181 */       for (ViewResolver viewResolver : matchingBeans) {
/* 182 */         if (this != viewResolver) {
/* 183 */           this.viewResolvers.add(viewResolver);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 188 */       for (int i = 0; i < this.viewResolvers.size(); i++) {
/* 189 */         if (!matchingBeans.contains(this.viewResolvers.get(i)))
/*     */         {
/*     */ 
/* 192 */           String name = ((ViewResolver)this.viewResolvers.get(i)).getClass().getName() + i;
/* 193 */           getApplicationContext().getAutowireCapableBeanFactory().initializeBean(this.viewResolvers.get(i), name);
/*     */         }
/*     */       }
/*     */     }
/* 197 */     if (this.viewResolvers.isEmpty()) {
/* 198 */       this.logger.warn("Did not find any ViewResolvers to delegate to; please configure them using the 'viewResolvers' property on the ContentNegotiatingViewResolver");
/*     */     }
/*     */     
/* 201 */     AnnotationAwareOrderComparator.sort(this.viewResolvers);
/* 202 */     this.cnmFactoryBean.setServletContext(servletContext);
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */   {
/* 207 */     if (this.contentNegotiationManager == null) {
/* 208 */       this.cnmFactoryBean.afterPropertiesSet();
/* 209 */       this.contentNegotiationManager = this.cnmFactoryBean.getObject();
/*     */     }
/*     */   }
/*     */   
/*     */   public View resolveViewName(String viewName, Locale locale)
/*     */     throws Exception
/*     */   {
/* 216 */     RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
/* 217 */     Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
/* 218 */     List<MediaType> requestedMediaTypes = getMediaTypes(((ServletRequestAttributes)attrs).getRequest());
/* 219 */     if (requestedMediaTypes != null) {
/* 220 */       List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
/* 221 */       View bestView = getBestView(candidateViews, requestedMediaTypes, attrs);
/* 222 */       if (bestView != null) {
/* 223 */         return bestView;
/*     */       }
/*     */     }
/* 226 */     if (this.useNotAcceptableStatusCode) {
/* 227 */       if (this.logger.isDebugEnabled()) {
/* 228 */         this.logger.debug("No acceptable view found; returning 406 (Not Acceptable) status code");
/*     */       }
/* 230 */       return NOT_ACCEPTABLE_VIEW;
/*     */     }
/*     */     
/* 233 */     this.logger.debug("No acceptable view found; returning null");
/* 234 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<MediaType> getMediaTypes(HttpServletRequest request)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       ServletWebRequest webRequest = new ServletWebRequest(request);
/*     */       
/* 247 */       List<MediaType> acceptableMediaTypes = this.contentNegotiationManager.resolveMediaTypes(webRequest);
/*     */       
/* 249 */       acceptableMediaTypes = !acceptableMediaTypes.isEmpty() ? acceptableMediaTypes : Collections.singletonList(MediaType.ALL);
/*     */       
/* 251 */       List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request);
/* 252 */       Set<MediaType> compatibleMediaTypes = new LinkedHashSet();
/* 253 */       for (Iterator localIterator1 = acceptableMediaTypes.iterator(); localIterator1.hasNext();) { acceptable = (MediaType)localIterator1.next();
/* 254 */         for (MediaType producible : producibleMediaTypes) {
/* 255 */           if (acceptable.isCompatibleWith(producible))
/* 256 */             compatibleMediaTypes.add(getMostSpecificMediaType(acceptable, producible));
/*     */         }
/*     */       }
/*     */       MediaType acceptable;
/* 260 */       Object selectedMediaTypes = new ArrayList(compatibleMediaTypes);
/* 261 */       MediaType.sortBySpecificityAndQuality((List)selectedMediaTypes);
/* 262 */       if (this.logger.isDebugEnabled()) {
/* 263 */         this.logger.debug("Requested media types are " + selectedMediaTypes + " based on Accept header types and producible media types " + producibleMediaTypes + ")");
/*     */       }
/*     */       
/* 266 */       return (List<MediaType>)selectedMediaTypes;
/*     */     }
/*     */     catch (HttpMediaTypeNotAcceptableException ex) {}
/* 269 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<MediaType> getProducibleMediaTypes(HttpServletRequest request)
/*     */   {
/* 276 */     Set<MediaType> mediaTypes = (Set)request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/* 277 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 278 */       return new ArrayList(mediaTypes);
/*     */     }
/*     */     
/* 281 */     return Collections.singletonList(MediaType.ALL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType)
/*     */   {
/* 290 */     produceType = produceType.copyQualityValue(acceptType);
/* 291 */     return MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceType) < 0 ? acceptType : produceType;
/*     */   }
/*     */   
/*     */   private List<View> getCandidateViews(String viewName, Locale locale, List<MediaType> requestedMediaTypes)
/*     */     throws Exception
/*     */   {
/* 297 */     List<View> candidateViews = new ArrayList();
/* 298 */     for (Iterator localIterator1 = this.viewResolvers.iterator(); localIterator1.hasNext();) { viewResolver = (ViewResolver)localIterator1.next();
/* 299 */       view = viewResolver.resolveViewName(viewName, locale);
/* 300 */       if (view != null) {
/* 301 */         candidateViews.add(view);
/*     */       }
/* 303 */       for (MediaType requestedMediaType : requestedMediaTypes) {
/* 304 */         List<String> extensions = this.contentNegotiationManager.resolveFileExtensions(requestedMediaType);
/* 305 */         for (String extension : extensions) {
/* 306 */           String viewNameWithExtension = viewName + '.' + extension;
/* 307 */           view = viewResolver.resolveViewName(viewNameWithExtension, locale);
/* 308 */           if (view != null)
/* 309 */             candidateViews.add(view);
/*     */         }
/*     */       } }
/*     */     ViewResolver viewResolver;
/*     */     View view;
/* 314 */     if (!CollectionUtils.isEmpty(this.defaultViews)) {
/* 315 */       candidateViews.addAll(this.defaultViews);
/*     */     }
/* 317 */     return candidateViews;
/*     */   }
/*     */   
/*     */   private View getBestView(List<View> candidateViews, List<MediaType> requestedMediaTypes, RequestAttributes attrs) {
/* 321 */     for (View candidateView : candidateViews)
/* 322 */       if ((candidateView instanceof SmartView)) {
/* 323 */         smartView = (SmartView)candidateView;
/* 324 */         if (smartView.isRedirectView()) {
/* 325 */           if (this.logger.isDebugEnabled()) {
/* 326 */             this.logger.debug("Returning redirect view [" + candidateView + "]");
/*     */           }
/* 328 */           return candidateView;
/*     */         }
/*     */       }
/*     */     SmartView smartView;
/* 332 */     for (??? = requestedMediaTypes.iterator(); ???.hasNext();) { mediaType = (MediaType)???.next();
/* 333 */       for (View candidateView : candidateViews)
/* 334 */         if (StringUtils.hasText(candidateView.getContentType())) {
/* 335 */           MediaType candidateContentType = MediaType.parseMediaType(candidateView.getContentType());
/* 336 */           if (mediaType.isCompatibleWith(candidateContentType)) {
/* 337 */             if (this.logger.isDebugEnabled()) {
/* 338 */               this.logger.debug("Returning [" + candidateView + "] based on requested media type '" + mediaType + "'");
/*     */             }
/*     */             
/* 341 */             attrs.setAttribute(View.SELECTED_CONTENT_TYPE, mediaType, 0);
/* 342 */             return candidateView;
/*     */           }
/*     */         }
/*     */     }
/*     */     MediaType mediaType;
/* 347 */     return null;
/*     */   }
/*     */   
/*     */ 
/* 351 */   private static final View NOT_ACCEPTABLE_VIEW = new View()
/*     */   {
/*     */     public String getContentType()
/*     */     {
/* 355 */       return null;
/*     */     }
/*     */     
/*     */     public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/*     */     {
/* 360 */       response.setStatus(406);
/*     */     }
/*     */   };
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\ContentNegotiatingViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */