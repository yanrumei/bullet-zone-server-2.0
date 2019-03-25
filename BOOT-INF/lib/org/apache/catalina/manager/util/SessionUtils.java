/*     */ package org.apache.catalina.manager.util;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.security.auth.Subject;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.catalina.Session;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
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
/*     */ public class SessionUtils
/*     */ {
/*     */   private static final String STRUTS_LOCALE_KEY = "org.apache.struts.action.LOCALE";
/*     */   private static final String JSTL_LOCALE_KEY = "javax.servlet.jsp.jstl.fmt.locale";
/*     */   private static final String SPRING_LOCALE_KEY = "org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE";
/*  59 */   private static final String[] LOCALE_TEST_ATTRIBUTES = { "org.apache.struts.action.LOCALE", "org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", "javax.servlet.jsp.jstl.fmt.locale", "Locale", "java.util.Locale" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private static final String[] USER_TEST_ATTRIBUTES = { "Login", "User", "userName", "UserName", "Utilisateur", "SPRING_SECURITY_LAST_USERNAME" };
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
/*  78 */   public static Locale guessLocaleFromSession(Session in_session) { return guessLocaleFromSession(in_session.getSession()); }
/*     */   
/*     */   public static Locale guessLocaleFromSession(HttpSession in_session) {
/*  81 */     if (null == in_session) {
/*  82 */       return null;
/*     */     }
/*     */     try {
/*  85 */       Locale locale = null;
/*     */       
/*     */ 
/*  88 */       for (int i = 0; i < LOCALE_TEST_ATTRIBUTES.length; i++) {
/*  89 */         Object obj = in_session.getAttribute(LOCALE_TEST_ATTRIBUTES[i]);
/*  90 */         if ((obj instanceof Locale)) {
/*  91 */           locale = (Locale)obj;
/*  92 */           break;
/*     */         }
/*  94 */         obj = in_session.getAttribute(LOCALE_TEST_ATTRIBUTES[i].toLowerCase(Locale.ENGLISH));
/*  95 */         if ((obj instanceof Locale)) {
/*  96 */           locale = (Locale)obj;
/*  97 */           break;
/*     */         }
/*  99 */         obj = in_session.getAttribute(LOCALE_TEST_ATTRIBUTES[i].toUpperCase(Locale.ENGLISH));
/* 100 */         if ((obj instanceof Locale)) {
/* 101 */           locale = (Locale)obj;
/* 102 */           break;
/*     */         }
/*     */       }
/*     */       
/* 106 */       if (null != locale) {
/* 107 */         return locale;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 112 */       List<Object> tapestryArray = new ArrayList();
/* 113 */       for (Enumeration<String> enumeration = in_session.getAttributeNames(); enumeration.hasMoreElements();) {
/* 114 */         String name = (String)enumeration.nextElement();
/* 115 */         if ((name.indexOf("tapestry") > -1) && (name.indexOf("engine") > -1) && (null != in_session.getAttribute(name))) {
/* 116 */           tapestryArray.add(in_session.getAttribute(name));
/*     */         }
/*     */       }
/* 119 */       if (tapestryArray.size() == 1)
/*     */       {
/* 121 */         Object probableEngine = tapestryArray.get(0);
/* 122 */         if (null != probableEngine) {
/*     */           try {
/* 124 */             Method readMethod = probableEngine.getClass().getMethod("getLocale", (Class[])null);
/*     */             
/* 126 */             Object possibleLocale = readMethod.invoke(probableEngine, (Object[])null);
/* 127 */             if ((possibleLocale instanceof Locale)) {
/* 128 */               locale = (Locale)possibleLocale;
/*     */             }
/*     */           }
/*     */           catch (Exception e) {
/* 132 */             Throwable t = ExceptionUtils.unwrapInvocationTargetException(e);
/* 133 */             ExceptionUtils.handleThrowable(t);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 139 */       if (null != locale) {
/* 140 */         return locale;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 145 */       List<Object> localeArray = new ArrayList();
/* 146 */       for (Enumeration<String> enumeration = in_session.getAttributeNames(); enumeration.hasMoreElements();) {
/* 147 */         String name = (String)enumeration.nextElement();
/* 148 */         Object obj = in_session.getAttribute(name);
/* 149 */         if ((obj instanceof Locale)) {
/* 150 */           localeArray.add(obj);
/*     */         }
/*     */       }
/* 153 */       if (localeArray.size() == 1) {}
/* 154 */       return (Locale)localeArray.get(0);
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/*     */     
/*     */ 
/*     */ 
/* 160 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object guessUserFromSession(Session in_session)
/*     */   {
/* 170 */     if (null == in_session) {
/* 171 */       return null;
/*     */     }
/* 173 */     if (in_session.getPrincipal() != null) {
/* 174 */       return in_session.getPrincipal().getName();
/*     */     }
/* 176 */     HttpSession httpSession = in_session.getSession();
/* 177 */     if (httpSession == null) {
/* 178 */       return null;
/*     */     }
/*     */     try {
/* 181 */       Object user = null;
/*     */       
/* 183 */       for (int i = 0; i < USER_TEST_ATTRIBUTES.length; i++) {
/* 184 */         Object obj = httpSession.getAttribute(USER_TEST_ATTRIBUTES[i]);
/* 185 */         if (null != obj) {
/* 186 */           user = obj;
/* 187 */           break;
/*     */         }
/* 189 */         obj = httpSession.getAttribute(USER_TEST_ATTRIBUTES[i].toLowerCase(Locale.ENGLISH));
/* 190 */         if (null != obj) {
/* 191 */           user = obj;
/* 192 */           break;
/*     */         }
/* 194 */         obj = httpSession.getAttribute(USER_TEST_ATTRIBUTES[i].toUpperCase(Locale.ENGLISH));
/* 195 */         if (null != obj) {
/* 196 */           user = obj;
/* 197 */           break;
/*     */         }
/*     */       }
/*     */       
/* 201 */       if (null != user) {
/* 202 */         return user;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 207 */       List<Object> principalArray = new ArrayList();
/* 208 */       for (Enumeration<String> enumeration = httpSession.getAttributeNames(); enumeration.hasMoreElements();) {
/* 209 */         String name = (String)enumeration.nextElement();
/* 210 */         Object obj = httpSession.getAttribute(name);
/* 211 */         if (((obj instanceof Principal)) || ((obj instanceof Subject))) {
/* 212 */           principalArray.add(obj);
/*     */         }
/*     */       }
/* 215 */       if (principalArray.size() == 1) {
/* 216 */         user = principalArray.get(0);
/*     */       }
/*     */       
/* 219 */       if (null != user) {
/* 220 */         return user;
/*     */       }
/*     */       
/* 223 */       return user;
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/* 226 */     return null;
/*     */   }
/*     */   
/*     */   public static long getUsedTimeForSession(Session in_session)
/*     */   {
/*     */     try
/*     */     {
/* 233 */       return in_session.getThisAccessedTime() - in_session.getCreationTime();
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/*     */     
/* 237 */     return -1L;
/*     */   }
/*     */   
/*     */   public static long getTTLForSession(Session in_session)
/*     */   {
/*     */     try {
/* 243 */       return 1000 * in_session.getMaxInactiveInterval() - (System.currentTimeMillis() - in_session.getThisAccessedTime());
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/*     */     
/* 247 */     return -1L;
/*     */   }
/*     */   
/*     */   public static long getInactiveTimeForSession(Session in_session)
/*     */   {
/*     */     try {
/* 253 */       return System.currentTimeMillis() - in_session.getThisAccessedTime();
/*     */     }
/*     */     catch (IllegalStateException ise) {}
/*     */     
/* 257 */     return -1L;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\manage\\util\SessionUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */