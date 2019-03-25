/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletRequestWrapper;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.FormHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpPutFormContentFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*  63 */   private FormHttpMessageConverter formConverter = new AllEncompassingFormHttpMessageConverter();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFormConverter(FormHttpMessageConverter converter)
/*     */   {
/*  71 */     Assert.notNull(converter, "FormHttpMessageConverter is required.");
/*  72 */     this.formConverter = converter;
/*     */   }
/*     */   
/*     */   public FormHttpMessageConverter getFormConverter() {
/*  76 */     return this.formConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/*  85 */     this.formConverter.setCharset(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
/*     */     throws ServletException, IOException
/*     */   {
/*  93 */     if ((("PUT".equals(request.getMethod())) || ("PATCH".equals(request.getMethod()))) && (isFormContentType(request))) {
/*  94 */       HttpInputMessage inputMessage = new ServletServerHttpRequest(request)
/*     */       {
/*     */         public InputStream getBody() throws IOException {
/*  97 */           return request.getInputStream();
/*     */         }
/*  99 */       };
/* 100 */       MultiValueMap<String, String> formParameters = this.formConverter.read(null, inputMessage);
/* 101 */       if (!formParameters.isEmpty()) {
/* 102 */         HttpServletRequest wrapper = new HttpPutFormContentRequestWrapper(request, formParameters);
/* 103 */         filterChain.doFilter(wrapper, response);
/* 104 */         return;
/*     */       }
/*     */     }
/*     */     
/* 108 */     filterChain.doFilter(request, response);
/*     */   }
/*     */   
/*     */   private boolean isFormContentType(HttpServletRequest request) {
/* 112 */     String contentType = request.getContentType();
/* 113 */     if (contentType != null) {
/*     */       try {
/* 115 */         MediaType mediaType = MediaType.parseMediaType(contentType);
/* 116 */         return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
/*     */       }
/*     */       catch (IllegalArgumentException ex) {
/* 119 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 123 */     return false;
/*     */   }
/*     */   
/*     */   private static class HttpPutFormContentRequestWrapper
/*     */     extends HttpServletRequestWrapper
/*     */   {
/*     */     private MultiValueMap<String, String> formParameters;
/*     */     
/*     */     public HttpPutFormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters)
/*     */     {
/* 133 */       super();
/* 134 */       this.formParameters = (parameters != null ? parameters : new LinkedMultiValueMap());
/*     */     }
/*     */     
/*     */     public String getParameter(String name)
/*     */     {
/* 139 */       String queryStringValue = super.getParameter(name);
/* 140 */       String formValue = (String)this.formParameters.getFirst(name);
/* 141 */       return queryStringValue != null ? queryStringValue : formValue;
/*     */     }
/*     */     
/*     */     public Map<String, String[]> getParameterMap()
/*     */     {
/* 146 */       Map<String, String[]> result = new LinkedHashMap();
/* 147 */       Enumeration<String> names = getParameterNames();
/* 148 */       while (names.hasMoreElements()) {
/* 149 */         String name = (String)names.nextElement();
/* 150 */         result.put(name, getParameterValues(name));
/*     */       }
/* 152 */       return result;
/*     */     }
/*     */     
/*     */     public Enumeration<String> getParameterNames()
/*     */     {
/* 157 */       Set<String> names = new LinkedHashSet();
/* 158 */       names.addAll(Collections.list(super.getParameterNames()));
/* 159 */       names.addAll(this.formParameters.keySet());
/* 160 */       return Collections.enumeration(names);
/*     */     }
/*     */     
/*     */     public String[] getParameterValues(String name)
/*     */     {
/* 165 */       String[] parameterValues = super.getParameterValues(name);
/* 166 */       List<String> formParam = (List)this.formParameters.get(name);
/* 167 */       if (formParam == null) {
/* 168 */         return parameterValues;
/*     */       }
/* 170 */       if ((parameterValues == null) || (getQueryString() == null)) {
/* 171 */         return (String[])formParam.toArray(new String[formParam.size()]);
/*     */       }
/*     */       
/* 174 */       List<String> result = new ArrayList(parameterValues.length + formParam.size());
/* 175 */       result.addAll(Arrays.asList(parameterValues));
/* 176 */       result.addAll(formParam);
/* 177 */       return (String[])result.toArray(new String[result.size()]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\filter\HttpPutFormContentFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */