/*     */ package org.springframework.http.server;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.Principal;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.InvalidMediaTypeException;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedCaseInsensitiveMap;
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
/*     */ public class ServletServerHttpRequest
/*     */   implements ServerHttpRequest
/*     */ {
/*     */   protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
/*     */   protected static final String FORM_CHARSET = "UTF-8";
/*     */   private final HttpServletRequest servletRequest;
/*     */   private HttpHeaders headers;
/*     */   private ServerHttpAsyncRequestControl asyncRequestControl;
/*     */   
/*     */   public ServletServerHttpRequest(HttpServletRequest servletRequest)
/*     */   {
/*  72 */     Assert.notNull(servletRequest, "HttpServletRequest must not be null");
/*  73 */     this.servletRequest = servletRequest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpServletRequest getServletRequest()
/*     */   {
/*  81 */     return this.servletRequest;
/*     */   }
/*     */   
/*     */   public HttpMethod getMethod()
/*     */   {
/*  86 */     return HttpMethod.resolve(this.servletRequest.getMethod());
/*     */   }
/*     */   
/*     */   public URI getURI()
/*     */   {
/*     */     try {
/*  92 */       StringBuffer url = this.servletRequest.getRequestURL();
/*  93 */       String query = this.servletRequest.getQueryString();
/*  94 */       if (StringUtils.hasText(query)) {
/*  95 */         url.append('?').append(query);
/*     */       }
/*  97 */       return new URI(url.toString());
/*     */     }
/*     */     catch (URISyntaxException ex) {
/* 100 */       throw new IllegalStateException("Could not get HttpServletRequest URI: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public HttpHeaders getHeaders()
/*     */   {
/* 106 */     if (this.headers == null) {
/* 107 */       this.headers = new HttpHeaders();
/*     */       
/* 109 */       for (Enumeration<?> headerNames = this.servletRequest.getHeaderNames(); headerNames.hasMoreElements();) {
/* 110 */         String headerName = (String)headerNames.nextElement();
/* 111 */         Enumeration<?> headerValues = this.servletRequest.getHeaders(headerName);
/* 112 */         while (headerValues.hasMoreElements()) {
/* 113 */           String headerValue = (String)headerValues.nextElement();
/* 114 */           this.headers.add(headerName, headerValue);
/*     */         }
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 120 */         MediaType contentType = this.headers.getContentType();
/* 121 */         if (contentType == null) {
/* 122 */           String requestContentType = this.servletRequest.getContentType();
/* 123 */           if (StringUtils.hasLength(requestContentType)) {
/* 124 */             contentType = MediaType.parseMediaType(requestContentType);
/* 125 */             this.headers.setContentType(contentType);
/*     */           }
/*     */         }
/* 128 */         if ((contentType != null) && (contentType.getCharset() == null)) {
/* 129 */           String requestEncoding = this.servletRequest.getCharacterEncoding();
/* 130 */           if (StringUtils.hasLength(requestEncoding)) {
/* 131 */             Charset charSet = Charset.forName(requestEncoding);
/* 132 */             Map<String, String> params = new LinkedCaseInsensitiveMap();
/* 133 */             params.putAll(contentType.getParameters());
/* 134 */             params.put("charset", charSet.toString());
/* 135 */             MediaType newContentType = new MediaType(contentType.getType(), contentType.getSubtype(), params);
/* 136 */             this.headers.setContentType(newContentType);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (InvalidMediaTypeException localInvalidMediaTypeException) {}
/*     */       
/*     */ 
/*     */ 
/* 144 */       if (this.headers.getContentLength() < 0L) {
/* 145 */         int requestContentLength = this.servletRequest.getContentLength();
/* 146 */         if (requestContentLength != -1) {
/* 147 */           this.headers.setContentLength(requestContentLength);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 152 */     return this.headers;
/*     */   }
/*     */   
/*     */   public Principal getPrincipal()
/*     */   {
/* 157 */     return this.servletRequest.getUserPrincipal();
/*     */   }
/*     */   
/*     */   public InetSocketAddress getLocalAddress()
/*     */   {
/* 162 */     return new InetSocketAddress(this.servletRequest.getLocalName(), this.servletRequest.getLocalPort());
/*     */   }
/*     */   
/*     */   public InetSocketAddress getRemoteAddress()
/*     */   {
/* 167 */     return new InetSocketAddress(this.servletRequest.getRemoteHost(), this.servletRequest.getRemotePort());
/*     */   }
/*     */   
/*     */   public InputStream getBody() throws IOException
/*     */   {
/* 172 */     if (isFormPost(this.servletRequest)) {
/* 173 */       return getBodyFromServletRequestParameters(this.servletRequest);
/*     */     }
/*     */     
/* 176 */     return this.servletRequest.getInputStream();
/*     */   }
/*     */   
/*     */ 
/*     */   public ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse response)
/*     */   {
/* 182 */     if (this.asyncRequestControl == null) {
/* 183 */       if (!ServletServerHttpResponse.class.isInstance(response)) {
/* 184 */         throw new IllegalArgumentException("Response must be a ServletServerHttpResponse: " + response.getClass());
/*     */       }
/* 186 */       ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse)response;
/* 187 */       this.asyncRequestControl = new ServletServerHttpAsyncRequestControl(this, servletServerResponse);
/*     */     }
/* 189 */     return this.asyncRequestControl;
/*     */   }
/*     */   
/*     */   private static boolean isFormPost(HttpServletRequest request)
/*     */   {
/* 194 */     String contentType = request.getContentType();
/* 195 */     return (contentType != null) && (contentType.contains("application/x-www-form-urlencoded")) && 
/* 196 */       (HttpMethod.POST.matches(request.getMethod()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static InputStream getBodyFromServletRequestParameters(HttpServletRequest request)
/*     */     throws IOException
/*     */   {
/* 206 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
/* 207 */     Writer writer = new OutputStreamWriter(bos, "UTF-8");
/*     */     
/* 209 */     Map<String, String[]> form = request.getParameterMap();
/* 210 */     for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();) {
/* 211 */       String name = (String)nameIterator.next();
/* 212 */       List<String> values = Arrays.asList((Object[])form.get(name));
/* 213 */       for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext();) {
/* 214 */         String value = (String)valueIterator.next();
/* 215 */         writer.write(URLEncoder.encode(name, "UTF-8"));
/* 216 */         if (value != null) {
/* 217 */           writer.write(61);
/* 218 */           writer.write(URLEncoder.encode(value, "UTF-8"));
/* 219 */           if (valueIterator.hasNext()) {
/* 220 */             writer.write(38);
/*     */           }
/*     */         }
/*     */       }
/* 224 */       if (nameIterator.hasNext()) {
/* 225 */         writer.append('&');
/*     */       }
/*     */     }
/* 228 */     writer.flush();
/*     */     
/* 230 */     return new ByteArrayInputStream(bos.toByteArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\http\server\ServletServerHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */