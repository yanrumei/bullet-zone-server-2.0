/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HierarchicalUriComponents
/*     */   extends UriComponents
/*     */ {
/*     */   private static final char PATH_DELIMITER = '/';
/*     */   private static final String PATH_DELIMITER_STRING = "/";
/*     */   private final String userInfo;
/*     */   private final String host;
/*     */   private final String port;
/*     */   private final PathComponent path;
/*     */   private final MultiValueMap<String, String> queryParams;
/*     */   private final boolean encoded;
/*     */   
/*     */   HierarchicalUriComponents(String scheme, String userInfo, String host, String port, PathComponent path, MultiValueMap<String, String> queryParams, String fragment, boolean encoded, boolean verify)
/*     */   {
/*  85 */     super(scheme, fragment);
/*  86 */     this.userInfo = userInfo;
/*  87 */     this.host = host;
/*  88 */     this.port = port;
/*  89 */     this.path = (path != null ? path : NULL_PATH_COMPONENT);
/*  90 */     this.queryParams = CollectionUtils.unmodifiableMultiValueMap(queryParams != null ? queryParams : new LinkedMultiValueMap(0));
/*     */     
/*  92 */     this.encoded = encoded;
/*     */     
/*  94 */     if (verify) {
/*  95 */       verify();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSchemeSpecificPart()
/*     */   {
/* 104 */     return null;
/*     */   }
/*     */   
/*     */   public String getUserInfo()
/*     */   {
/* 109 */     return this.userInfo;
/*     */   }
/*     */   
/*     */   public String getHost()
/*     */   {
/* 114 */     return this.host;
/*     */   }
/*     */   
/*     */   public int getPort()
/*     */   {
/* 119 */     if (this.port == null) {
/* 120 */       return -1;
/*     */     }
/* 122 */     if (this.port.contains("{")) {
/* 123 */       throw new IllegalStateException("The port contains a URI variable but has not been expanded yet: " + this.port);
/*     */     }
/*     */     
/* 126 */     return Integer.parseInt(this.port);
/*     */   }
/*     */   
/*     */   public String getPath()
/*     */   {
/* 131 */     return this.path.getPath();
/*     */   }
/*     */   
/*     */   public List<String> getPathSegments()
/*     */   {
/* 136 */     return this.path.getPathSegments();
/*     */   }
/*     */   
/*     */   public String getQuery()
/*     */   {
/* 141 */     if (!this.queryParams.isEmpty()) {
/* 142 */       StringBuilder queryBuilder = new StringBuilder();
/* 143 */       for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
/* 144 */         name = (String)entry.getKey();
/* 145 */         List<String> values = (List)entry.getValue();
/* 146 */         if (CollectionUtils.isEmpty(values)) {
/* 147 */           if (queryBuilder.length() != 0) {
/* 148 */             queryBuilder.append('&');
/*     */           }
/* 150 */           queryBuilder.append(name);
/*     */         }
/*     */         else {
/* 153 */           for (Object value : values) {
/* 154 */             if (queryBuilder.length() != 0) {
/* 155 */               queryBuilder.append('&');
/*     */             }
/* 157 */             queryBuilder.append(name);
/*     */             
/* 159 */             if (value != null) {
/* 160 */               queryBuilder.append('=');
/* 161 */               queryBuilder.append(value.toString());
/*     */             }
/*     */           }
/*     */         } }
/*     */       String name;
/* 166 */       return queryBuilder.toString();
/*     */     }
/*     */     
/* 169 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultiValueMap<String, String> getQueryParams()
/*     */   {
/* 178 */     return this.queryParams;
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
/*     */   public HierarchicalUriComponents encode(String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 193 */     if (this.encoded) {
/* 194 */       return this;
/*     */     }
/* 196 */     Assert.hasLength(encoding, "Encoding must not be empty");
/* 197 */     String schemeTo = encodeUriComponent(getScheme(), encoding, Type.SCHEME);
/* 198 */     String userInfoTo = encodeUriComponent(this.userInfo, encoding, Type.USER_INFO);
/* 199 */     String hostTo = encodeUriComponent(this.host, encoding, getHostType());
/* 200 */     PathComponent pathTo = this.path.encode(encoding);
/* 201 */     MultiValueMap<String, String> paramsTo = encodeQueryParams(encoding);
/* 202 */     String fragmentTo = encodeUriComponent(getFragment(), encoding, Type.FRAGMENT);
/* 203 */     return new HierarchicalUriComponents(schemeTo, userInfoTo, hostTo, this.port, pathTo, paramsTo, fragmentTo, true, false);
/*     */   }
/*     */   
/*     */   private MultiValueMap<String, String> encodeQueryParams(String encoding) throws UnsupportedEncodingException
/*     */   {
/* 208 */     int size = this.queryParams.size();
/* 209 */     MultiValueMap<String, String> result = new LinkedMultiValueMap(size);
/* 210 */     for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
/* 211 */       String name = encodeUriComponent((String)entry.getKey(), encoding, Type.QUERY_PARAM);
/* 212 */       List<String> values = new ArrayList(((List)entry.getValue()).size());
/* 213 */       for (String value : (List)entry.getValue()) {
/* 214 */         values.add(encodeUriComponent(value, encoding, Type.QUERY_PARAM));
/*     */       }
/* 216 */       result.put(name, values);
/*     */     }
/* 218 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String encodeUriComponent(String source, String encoding, Type type)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 231 */     if (source == null) {
/* 232 */       return null;
/*     */     }
/* 234 */     Assert.hasLength(encoding, "Encoding must not be empty");
/* 235 */     byte[] bytes = encodeBytes(source.getBytes(encoding), type);
/* 236 */     return new String(bytes, "US-ASCII");
/*     */   }
/*     */   
/*     */   private static byte[] encodeBytes(byte[] source, Type type) {
/* 240 */     Assert.notNull(source, "Source must not be null");
/* 241 */     Assert.notNull(type, "Type must not be null");
/* 242 */     ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
/* 243 */     for (byte b : source) {
/* 244 */       if (b < 0) {
/* 245 */         b = (byte)(b + 256);
/*     */       }
/* 247 */       if (type.isAllowed(b)) {
/* 248 */         bos.write(b);
/*     */       }
/*     */       else {
/* 251 */         bos.write(37);
/* 252 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 253 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 254 */         bos.write(hex1);
/* 255 */         bos.write(hex2);
/*     */       }
/*     */     }
/* 258 */     return bos.toByteArray();
/*     */   }
/*     */   
/*     */   private Type getHostType() {
/* 262 */     return (this.host != null) && (this.host.startsWith("[")) ? Type.HOST_IPV6 : Type.HOST_IPV4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void verify()
/*     */   {
/* 274 */     if (!this.encoded) {
/* 275 */       return;
/*     */     }
/* 277 */     verifyUriComponent(getScheme(), Type.SCHEME);
/* 278 */     verifyUriComponent(this.userInfo, Type.USER_INFO);
/* 279 */     verifyUriComponent(this.host, getHostType());
/* 280 */     this.path.verify();
/* 281 */     for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
/* 282 */       verifyUriComponent((String)entry.getKey(), Type.QUERY_PARAM);
/* 283 */       for (String value : (List)entry.getValue()) {
/* 284 */         verifyUriComponent(value, Type.QUERY_PARAM);
/*     */       }
/*     */     }
/* 287 */     verifyUriComponent(getFragment(), Type.FRAGMENT);
/*     */   }
/*     */   
/*     */   private static void verifyUriComponent(String source, Type type) {
/* 291 */     if (source == null) {
/* 292 */       return;
/*     */     }
/* 294 */     int length = source.length();
/* 295 */     for (int i = 0; i < length; i++) {
/* 296 */       char ch = source.charAt(i);
/* 297 */       if (ch == '%') {
/* 298 */         if (i + 2 < length) {
/* 299 */           char hex1 = source.charAt(i + 1);
/* 300 */           char hex2 = source.charAt(i + 2);
/* 301 */           int u = Character.digit(hex1, 16);
/* 302 */           int l = Character.digit(hex2, 16);
/* 303 */           if ((u == -1) || (l == -1))
/*     */           {
/* 305 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*     */           }
/* 307 */           i += 2;
/*     */         }
/*     */         else
/*     */         {
/* 311 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*     */         }
/*     */       }
/* 314 */       else if (!type.isAllowed(ch))
/*     */       {
/* 316 */         throw new IllegalArgumentException("Invalid character '" + ch + "' for " + type.name() + " in \"" + source + "\"");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HierarchicalUriComponents expandInternal(UriComponents.UriTemplateVariables uriVariables)
/*     */   {
/* 326 */     Assert.state(!this.encoded, "Cannot expand an already encoded UriComponents object");
/*     */     
/* 328 */     String schemeTo = expandUriComponent(getScheme(), uriVariables);
/* 329 */     String userInfoTo = expandUriComponent(this.userInfo, uriVariables);
/* 330 */     String hostTo = expandUriComponent(this.host, uriVariables);
/* 331 */     String portTo = expandUriComponent(this.port, uriVariables);
/* 332 */     PathComponent pathTo = this.path.expand(uriVariables);
/* 333 */     MultiValueMap<String, String> paramsTo = expandQueryParams(uriVariables);
/* 334 */     String fragmentTo = expandUriComponent(getFragment(), uriVariables);
/*     */     
/* 336 */     return new HierarchicalUriComponents(schemeTo, userInfoTo, hostTo, portTo, pathTo, paramsTo, fragmentTo, false, false);
/*     */   }
/*     */   
/*     */   private MultiValueMap<String, String> expandQueryParams(UriComponents.UriTemplateVariables variables)
/*     */   {
/* 341 */     int size = this.queryParams.size();
/* 342 */     MultiValueMap<String, String> result = new LinkedMultiValueMap(size);
/* 343 */     variables = new QueryUriTemplateVariables(variables);
/* 344 */     for (Map.Entry<String, List<String>> entry : this.queryParams.entrySet()) {
/* 345 */       String name = expandUriComponent((String)entry.getKey(), variables);
/* 346 */       List<String> values = new ArrayList(((List)entry.getValue()).size());
/* 347 */       for (String value : (List)entry.getValue()) {
/* 348 */         values.add(expandUriComponent(value, variables));
/*     */       }
/* 350 */       result.put(name, values);
/*     */     }
/* 352 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponents normalize()
/*     */   {
/* 361 */     String normalizedPath = StringUtils.cleanPath(getPath());
/* 362 */     return new HierarchicalUriComponents(getScheme(), this.userInfo, this.host, this.port, new FullPathComponent(normalizedPath), this.queryParams, 
/*     */     
/* 364 */       getFragment(), this.encoded, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toUriString()
/*     */   {
/* 375 */     StringBuilder uriBuilder = new StringBuilder();
/* 376 */     if (getScheme() != null) {
/* 377 */       uriBuilder.append(getScheme());
/* 378 */       uriBuilder.append(':');
/*     */     }
/* 380 */     if ((this.userInfo != null) || (this.host != null)) {
/* 381 */       uriBuilder.append("//");
/* 382 */       if (this.userInfo != null) {
/* 383 */         uriBuilder.append(this.userInfo);
/* 384 */         uriBuilder.append('@');
/*     */       }
/* 386 */       if (this.host != null) {
/* 387 */         uriBuilder.append(this.host);
/*     */       }
/* 389 */       if (getPort() != -1) {
/* 390 */         uriBuilder.append(':');
/* 391 */         uriBuilder.append(this.port);
/*     */       }
/*     */     }
/* 394 */     String path = getPath();
/* 395 */     if (StringUtils.hasLength(path)) {
/* 396 */       if ((uriBuilder.length() != 0) && (path.charAt(0) != '/')) {
/* 397 */         uriBuilder.append('/');
/*     */       }
/* 399 */       uriBuilder.append(path);
/*     */     }
/* 401 */     String query = getQuery();
/* 402 */     if (query != null) {
/* 403 */       uriBuilder.append('?');
/* 404 */       uriBuilder.append(query);
/*     */     }
/* 406 */     if (getFragment() != null) {
/* 407 */       uriBuilder.append('#');
/* 408 */       uriBuilder.append(getFragment());
/*     */     }
/* 410 */     return uriBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public URI toUri()
/*     */   {
/*     */     try
/*     */     {
/* 419 */       if (this.encoded) {
/* 420 */         return new URI(toString());
/*     */       }
/*     */       
/* 423 */       String path = getPath();
/* 424 */       if ((StringUtils.hasLength(path)) && (path.charAt(0) != '/'))
/*     */       {
/* 426 */         if ((getScheme() != null) || (getUserInfo() != null) || (getHost() != null) || (getPort() != -1)) {
/* 427 */           path = '/' + path;
/*     */         }
/*     */       }
/* 430 */       return new URI(getScheme(), getUserInfo(), getHost(), getPort(), path, getQuery(), 
/* 431 */         getFragment());
/*     */     }
/*     */     catch (URISyntaxException ex)
/*     */     {
/* 435 */       throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void copyToUriComponentsBuilder(UriComponentsBuilder builder)
/*     */   {
/* 441 */     builder.scheme(getScheme());
/* 442 */     builder.userInfo(getUserInfo());
/* 443 */     builder.host(getHost());
/* 444 */     builder.port(getPort());
/* 445 */     builder.replacePath("");
/* 446 */     this.path.copyToUriComponentsBuilder(builder);
/* 447 */     builder.replaceQueryParams(getQueryParams());
/* 448 */     builder.fragment(getFragment());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 454 */     if (this == obj) {
/* 455 */       return true;
/*     */     }
/* 457 */     if (!(obj instanceof HierarchicalUriComponents)) {
/* 458 */       return false;
/*     */     }
/* 460 */     HierarchicalUriComponents other = (HierarchicalUriComponents)obj;
/* 461 */     return (ObjectUtils.nullSafeEquals(getScheme(), other.getScheme())) && 
/* 462 */       (ObjectUtils.nullSafeEquals(getUserInfo(), other.getUserInfo())) && 
/* 463 */       (ObjectUtils.nullSafeEquals(getHost(), other.getHost())) && 
/* 464 */       (getPort() == other.getPort()) && 
/* 465 */       (this.path.equals(other.path)) && 
/* 466 */       (this.queryParams.equals(other.queryParams)) && 
/* 467 */       (ObjectUtils.nullSafeEquals(getFragment(), other.getFragment()));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 472 */     int result = ObjectUtils.nullSafeHashCode(getScheme());
/* 473 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.userInfo);
/* 474 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.host);
/* 475 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.port);
/* 476 */     result = 31 * result + this.path.hashCode();
/* 477 */     result = 31 * result + this.queryParams.hashCode();
/* 478 */     result = 31 * result + ObjectUtils.nullSafeHashCode(getFragment());
/* 479 */     return result;
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
/*     */   static abstract enum Type
/*     */   {
/* 492 */     SCHEME, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 498 */     AUTHORITY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 504 */     USER_INFO, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 510 */     HOST_IPV4, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 516 */     HOST_IPV6, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 522 */     PORT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 528 */     PATH, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 534 */     PATH_SEGMENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 540 */     QUERY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 546 */     QUERY_PARAM, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 557 */     FRAGMENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 563 */     URI;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private Type() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract boolean isAllowed(int paramInt);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isAlpha(int c)
/*     */     {
/* 581 */       return ((c >= 97) && (c <= 122)) || ((c >= 65) && (c <= 90));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isDigit(int c)
/*     */     {
/* 589 */       return (c >= 48) && (c <= 57);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isGenericDelimiter(int c)
/*     */     {
/* 597 */       return (58 == c) || (47 == c) || (63 == c) || (35 == c) || (91 == c) || (93 == c) || (64 == c);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isSubDelimiter(int c)
/*     */     {
/* 605 */       return (33 == c) || (36 == c) || (38 == c) || (39 == c) || (40 == c) || (41 == c) || (42 == c) || (43 == c) || (44 == c) || (59 == c) || (61 == c);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isReserved(int c)
/*     */     {
/* 614 */       return (isGenericDelimiter(c)) || (isSubDelimiter(c));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isUnreserved(int c)
/*     */     {
/* 622 */       return (isAlpha(c)) || (isDigit(c)) || (45 == c) || (46 == c) || (95 == c) || (126 == c);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isPchar(int c)
/*     */     {
/* 630 */       return (isUnreserved(c)) || (isSubDelimiter(c)) || (58 == c) || (64 == c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static abstract interface PathComponent
/*     */     extends Serializable
/*     */   {
/*     */     public abstract String getPath();
/*     */     
/*     */ 
/*     */     public abstract List<String> getPathSegments();
/*     */     
/*     */ 
/*     */     public abstract PathComponent encode(String paramString)
/*     */       throws UnsupportedEncodingException;
/*     */     
/*     */ 
/*     */     public abstract void verify();
/*     */     
/*     */     public abstract PathComponent expand(UriComponents.UriTemplateVariables paramUriTemplateVariables);
/*     */     
/*     */     public abstract void copyToUriComponentsBuilder(UriComponentsBuilder paramUriComponentsBuilder);
/*     */   }
/*     */   
/*     */   static final class FullPathComponent
/*     */     implements HierarchicalUriComponents.PathComponent
/*     */   {
/*     */     private final String path;
/*     */     
/*     */     public FullPathComponent(String path)
/*     */     {
/* 662 */       this.path = path;
/*     */     }
/*     */     
/*     */     public String getPath()
/*     */     {
/* 667 */       return this.path;
/*     */     }
/*     */     
/*     */     public List<String> getPathSegments()
/*     */     {
/* 672 */       String[] segments = StringUtils.tokenizeToStringArray(getPath(), "/");
/* 673 */       if (segments == null) {
/* 674 */         return Collections.emptyList();
/*     */       }
/* 676 */       return Collections.unmodifiableList(Arrays.asList(segments));
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException
/*     */     {
/* 681 */       String encodedPath = HierarchicalUriComponents.encodeUriComponent(getPath(), encoding, HierarchicalUriComponents.Type.PATH);
/* 682 */       return new FullPathComponent(encodedPath);
/*     */     }
/*     */     
/*     */     public void verify()
/*     */     {
/* 687 */       HierarchicalUriComponents.verifyUriComponent(getPath(), HierarchicalUriComponents.Type.PATH);
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/*     */     {
/* 692 */       String expandedPath = UriComponents.expandUriComponent(getPath(), uriVariables);
/* 693 */       return new FullPathComponent(expandedPath);
/*     */     }
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder)
/*     */     {
/* 698 */       builder.path(getPath());
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 703 */       return (this == obj) || (((obj instanceof FullPathComponent)) && 
/* 704 */         (ObjectUtils.nullSafeEquals(getPath(), ((FullPathComponent)obj).getPath())));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 709 */       return ObjectUtils.nullSafeHashCode(getPath());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class PathSegmentComponent
/*     */     implements HierarchicalUriComponents.PathComponent
/*     */   {
/*     */     private final List<String> pathSegments;
/*     */     
/*     */ 
/*     */     public PathSegmentComponent(List<String> pathSegments)
/*     */     {
/* 722 */       Assert.notNull(pathSegments, "List must not be null");
/* 723 */       this.pathSegments = Collections.unmodifiableList(new ArrayList(pathSegments));
/*     */     }
/*     */     
/*     */     public String getPath()
/*     */     {
/* 728 */       StringBuilder pathBuilder = new StringBuilder();
/* 729 */       pathBuilder.append('/');
/* 730 */       for (Iterator<String> iterator = this.pathSegments.iterator(); iterator.hasNext();) {
/* 731 */         String pathSegment = (String)iterator.next();
/* 732 */         pathBuilder.append(pathSegment);
/* 733 */         if (iterator.hasNext()) {
/* 734 */           pathBuilder.append('/');
/*     */         }
/*     */       }
/* 737 */       return pathBuilder.toString();
/*     */     }
/*     */     
/*     */     public List<String> getPathSegments()
/*     */     {
/* 742 */       return this.pathSegments;
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException
/*     */     {
/* 747 */       List<String> pathSegments = getPathSegments();
/* 748 */       List<String> encodedPathSegments = new ArrayList(pathSegments.size());
/* 749 */       for (String pathSegment : pathSegments) {
/* 750 */         String encodedPathSegment = HierarchicalUriComponents.encodeUriComponent(pathSegment, encoding, HierarchicalUriComponents.Type.PATH_SEGMENT);
/* 751 */         encodedPathSegments.add(encodedPathSegment);
/*     */       }
/* 753 */       return new PathSegmentComponent(encodedPathSegments);
/*     */     }
/*     */     
/*     */     public void verify()
/*     */     {
/* 758 */       for (String pathSegment : getPathSegments()) {
/* 759 */         HierarchicalUriComponents.verifyUriComponent(pathSegment, HierarchicalUriComponents.Type.PATH_SEGMENT);
/*     */       }
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/*     */     {
/* 765 */       List<String> pathSegments = getPathSegments();
/* 766 */       List<String> expandedPathSegments = new ArrayList(pathSegments.size());
/* 767 */       for (String pathSegment : pathSegments) {
/* 768 */         String expandedPathSegment = UriComponents.expandUriComponent(pathSegment, uriVariables);
/* 769 */         expandedPathSegments.add(expandedPathSegment);
/*     */       }
/* 771 */       return new PathSegmentComponent(expandedPathSegments);
/*     */     }
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder)
/*     */     {
/* 776 */       builder.pathSegment((String[])getPathSegments().toArray(new String[getPathSegments().size()]));
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 781 */       return (this == obj) || (((obj instanceof PathSegmentComponent)) && 
/* 782 */         (getPathSegments().equals(((PathSegmentComponent)obj).getPathSegments())));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 787 */       return getPathSegments().hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class PathComponentComposite
/*     */     implements HierarchicalUriComponents.PathComponent
/*     */   {
/*     */     private final List<HierarchicalUriComponents.PathComponent> pathComponents;
/*     */     
/*     */ 
/*     */     public PathComponentComposite(List<HierarchicalUriComponents.PathComponent> pathComponents)
/*     */     {
/* 800 */       Assert.notNull(pathComponents, "PathComponent List must not be null");
/* 801 */       this.pathComponents = pathComponents;
/*     */     }
/*     */     
/*     */     public String getPath()
/*     */     {
/* 806 */       StringBuilder pathBuilder = new StringBuilder();
/* 807 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 808 */         pathBuilder.append(pathComponent.getPath());
/*     */       }
/* 810 */       return pathBuilder.toString();
/*     */     }
/*     */     
/*     */     public List<String> getPathSegments()
/*     */     {
/* 815 */       List<String> result = new ArrayList();
/* 816 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 817 */         result.addAll(pathComponent.getPathSegments());
/*     */       }
/* 819 */       return result;
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException
/*     */     {
/* 824 */       List<HierarchicalUriComponents.PathComponent> encodedComponents = new ArrayList(this.pathComponents.size());
/* 825 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 826 */         encodedComponents.add(pathComponent.encode(encoding));
/*     */       }
/* 828 */       return new PathComponentComposite(encodedComponents);
/*     */     }
/*     */     
/*     */     public void verify()
/*     */     {
/* 833 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 834 */         pathComponent.verify();
/*     */       }
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/*     */     {
/* 840 */       List<HierarchicalUriComponents.PathComponent> expandedComponents = new ArrayList(this.pathComponents.size());
/* 841 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 842 */         expandedComponents.add(pathComponent.expand(uriVariables));
/*     */       }
/* 844 */       return new PathComponentComposite(expandedComponents);
/*     */     }
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder)
/*     */     {
/* 849 */       for (HierarchicalUriComponents.PathComponent pathComponent : this.pathComponents) {
/* 850 */         pathComponent.copyToUriComponentsBuilder(builder);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 859 */   static final PathComponent NULL_PATH_COMPONENT = new PathComponent()
/*     */   {
/*     */     public String getPath() {
/* 862 */       return null;
/*     */     }
/*     */     
/*     */     public List<String> getPathSegments() {
/* 866 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent encode(String encoding) throws UnsupportedEncodingException {
/* 870 */       return this;
/*     */     }
/*     */     
/*     */     public void verify() {}
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent expand(UriComponents.UriTemplateVariables uriVariables)
/*     */     {
/* 877 */       return this;
/*     */     }
/*     */     
/*     */     public void copyToUriComponentsBuilder(UriComponentsBuilder builder) {}
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 884 */       return this == obj;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 888 */       return getClass().hashCode();
/*     */     }
/*     */   };
/*     */   
/*     */   private static class QueryUriTemplateVariables implements UriComponents.UriTemplateVariables
/*     */   {
/*     */     private final UriComponents.UriTemplateVariables delegate;
/*     */     
/*     */     public QueryUriTemplateVariables(UriComponents.UriTemplateVariables delegate)
/*     */     {
/* 898 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public Object getValue(String name)
/*     */     {
/* 903 */       Object value = this.delegate.getValue(name);
/* 904 */       if (ObjectUtils.isArray(value)) {
/* 905 */         value = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(value));
/*     */       }
/* 907 */       return value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\HierarchicalUriComponents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */