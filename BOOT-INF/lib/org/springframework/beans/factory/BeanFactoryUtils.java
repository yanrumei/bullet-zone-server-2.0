/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public abstract class BeanFactoryUtils
/*     */ {
/*     */   public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
/*     */   
/*     */   public static boolean isFactoryDereference(String name)
/*     */   {
/*  61 */     return (name != null) && (name.startsWith("&"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String transformedBeanName(String name)
/*     */   {
/*  72 */     Assert.notNull(name, "'name' must not be null");
/*  73 */     String beanName = name;
/*  74 */     while (beanName.startsWith("&")) {
/*  75 */       beanName = beanName.substring("&".length());
/*     */     }
/*  77 */     return beanName;
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
/*     */   public static boolean isGeneratedBeanName(String name)
/*     */   {
/*  90 */     return (name != null) && (name.contains("#"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String originalBeanName(String name)
/*     */   {
/* 101 */     Assert.notNull(name, "'name' must not be null");
/* 102 */     int separatorIndex = name.indexOf("#");
/* 103 */     return separatorIndex != -1 ? name.substring(0, separatorIndex) : name;
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
/*     */   public static int countBeansIncludingAncestors(ListableBeanFactory lbf)
/*     */   {
/* 116 */     return beanNamesIncludingAncestors(lbf).length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf)
/*     */   {
/* 126 */     return beanNamesForTypeIncludingAncestors(lbf, Object.class);
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
/*     */ 
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, ResolvableType type)
/*     */   {
/* 143 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 144 */     String[] result = lbf.getBeanNamesForType(type);
/* 145 */     if ((lbf instanceof HierarchicalBeanFactory)) {
/* 146 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 147 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory)) {
/* 148 */         String[] parentResult = beanNamesForTypeIncludingAncestors(
/* 149 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type);
/* 150 */         List<String> resultList = new ArrayList();
/* 151 */         resultList.addAll(Arrays.asList(result));
/* 152 */         for (String beanName : parentResult) {
/* 153 */           if ((!resultList.contains(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 154 */             resultList.add(beanName);
/*     */           }
/*     */         }
/* 157 */         result = StringUtils.toStringArray(resultList);
/*     */       }
/*     */     }
/* 160 */     return result;
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
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type)
/*     */   {
/* 176 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 177 */     String[] result = lbf.getBeanNamesForType(type);
/* 178 */     if ((lbf instanceof HierarchicalBeanFactory)) {
/* 179 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 180 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory)) {
/* 181 */         String[] parentResult = beanNamesForTypeIncludingAncestors(
/* 182 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type);
/* 183 */         List<String> resultList = new ArrayList();
/* 184 */         resultList.addAll(Arrays.asList(result));
/* 185 */         for (String beanName : parentResult) {
/* 186 */           if ((!resultList.contains(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 187 */             resultList.add(beanName);
/*     */           }
/*     */         }
/* 190 */         result = StringUtils.toStringArray(resultList);
/*     */       }
/*     */     }
/* 193 */     return result;
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
/*     */   public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type, boolean includeNonSingletons, boolean allowEagerInit)
/*     */   {
/* 219 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 220 */     String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
/* 221 */     if ((lbf instanceof HierarchicalBeanFactory)) {
/* 222 */       HierarchicalBeanFactory hbf = (HierarchicalBeanFactory)lbf;
/* 223 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory)) {
/* 224 */         String[] parentResult = beanNamesForTypeIncludingAncestors(
/* 225 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 226 */         List<String> resultList = new ArrayList();
/* 227 */         resultList.addAll(Arrays.asList(result));
/* 228 */         for (String beanName : parentResult) {
/* 229 */           if ((!resultList.contains(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 230 */             resultList.add(beanName);
/*     */           }
/*     */         }
/* 233 */         result = StringUtils.toStringArray(resultList);
/*     */       }
/*     */     }
/* 236 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
/*     */     throws BeansException
/*     */   {
/* 259 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 260 */     Map<String, T> result = new LinkedHashMap(4);
/* 261 */     result.putAll(lbf.getBeansOfType(type));
/* 262 */     HierarchicalBeanFactory hbf; if ((lbf instanceof HierarchicalBeanFactory)) {
/* 263 */       hbf = (HierarchicalBeanFactory)lbf;
/* 264 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory)) {
/* 265 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors(
/* 266 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type);
/* 267 */         for (Map.Entry<String, T> entry : parentResult.entrySet()) {
/* 268 */           String beanName = (String)entry.getKey();
/* 269 */           if ((!result.containsKey(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 270 */             result.put(beanName, entry.getValue());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 275 */     return result;
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
/*     */   public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*     */     throws BeansException
/*     */   {
/* 308 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 309 */     Map<String, T> result = new LinkedHashMap(4);
/* 310 */     result.putAll(lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit));
/* 311 */     HierarchicalBeanFactory hbf; if ((lbf instanceof HierarchicalBeanFactory)) {
/* 312 */       hbf = (HierarchicalBeanFactory)lbf;
/* 313 */       if ((hbf.getParentBeanFactory() instanceof ListableBeanFactory)) {
/* 314 */         Map<String, T> parentResult = beansOfTypeIncludingAncestors(
/* 315 */           (ListableBeanFactory)hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
/* 316 */         for (Map.Entry<String, T> entry : parentResult.entrySet()) {
/* 317 */           String beanName = (String)entry.getKey();
/* 318 */           if ((!result.containsKey(beanName)) && (!hbf.containsLocalBean(beanName))) {
/* 319 */             result.put(beanName, entry.getValue());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 324 */     return result;
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
/*     */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type)
/*     */     throws BeansException
/*     */   {
/* 353 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type);
/* 354 */     return (T)uniqueBean(type, beansOfType);
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
/*     */   public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*     */     throws BeansException
/*     */   {
/* 390 */     Map<String, T> beansOfType = beansOfTypeIncludingAncestors(lbf, type, includeNonSingletons, allowEagerInit);
/* 391 */     return (T)uniqueBean(type, beansOfType);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type)
/*     */     throws BeansException
/*     */   {
/* 411 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 412 */     Map<String, T> beansOfType = lbf.getBeansOfType(type);
/* 413 */     return (T)uniqueBean(type, beansOfType);
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
/*     */   public static <T> T beanOfType(ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
/*     */     throws BeansException
/*     */   {
/* 444 */     Assert.notNull(lbf, "ListableBeanFactory must not be null");
/* 445 */     Map<String, T> beansOfType = lbf.getBeansOfType(type, includeNonSingletons, allowEagerInit);
/* 446 */     return (T)uniqueBean(type, beansOfType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans)
/*     */   {
/* 458 */     int nrFound = matchingBeans.size();
/* 459 */     if (nrFound == 1) {
/* 460 */       return (T)matchingBeans.values().iterator().next();
/*     */     }
/* 462 */     if (nrFound > 1) {
/* 463 */       throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
/*     */     }
/*     */     
/* 466 */     throw new NoSuchBeanDefinitionException(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\BeanFactoryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */