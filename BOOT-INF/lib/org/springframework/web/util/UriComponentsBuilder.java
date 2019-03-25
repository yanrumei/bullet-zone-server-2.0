/*     */ package org.springframework.web.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpRequest;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class UriComponentsBuilder
/*     */   implements Cloneable
/*     */ {
/*  63 */   private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
/*     */   
/*     */ 
/*     */   private static final String SCHEME_PATTERN = "([^:/?#]+):";
/*     */   
/*     */   private static final String HTTP_PATTERN = "(?i)(http|https):";
/*     */   
/*     */   private static final String USERINFO_PATTERN = "([^@\\[/?#]*)";
/*     */   
/*     */   private static final String HOST_IPV4_PATTERN = "[^\\[/?#:]*";
/*     */   
/*     */   private static final String HOST_IPV6_PATTERN = "\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]";
/*     */   
/*     */   private static final String HOST_PATTERN = "(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)";
/*     */   
/*     */   private static final String PORT_PATTERN = "(\\d*(?:\\{[^/]+?\\})?)";
/*     */   
/*     */   private static final String PATH_PATTERN = "([^?#]*)";
/*     */   
/*     */   private static final String QUERY_PATTERN = "([^#]*)";
/*     */   
/*     */   private static final String LAST_PATTERN = "(.*)";
/*     */   
/*  86 */   private static final Pattern URI_PATTERN = Pattern.compile("^(([^:/?#]+):)?(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?([^#]*))?(#(.*))?");
/*     */   
/*     */ 
/*     */ 
/*  90 */   private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(?i)(http|https):(//(([^@\\[/?#]*)@)?(\\[[\\p{XDigit}\\:\\.]*[%\\p{Alnum}]*\\]|[^\\[/?#:]*)(:(\\d*(?:\\{[^/]+?\\})?))?)?([^?#]*)(\\?(.*))?");
/*     */   
/*     */ 
/*     */ 
/*  94 */   private static final Pattern FORWARDED_HOST_PATTERN = Pattern.compile("host=\"?([^;,\"]+)\"?");
/*     */   
/*  96 */   private static final Pattern FORWARDED_PROTO_PATTERN = Pattern.compile("proto=\"?([^;,\"]+)\"?");
/*     */   
/*     */ 
/*     */   private String scheme;
/*     */   
/*     */   private String ssp;
/*     */   
/*     */   private String userInfo;
/*     */   
/*     */   private String host;
/*     */   
/*     */   private String port;
/*     */   
/*     */   private CompositePathComponentBuilder pathBuilder;
/*     */   
/* 111 */   private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String fragment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UriComponentsBuilder()
/*     */   {
/* 123 */     this.pathBuilder = new CompositePathComponentBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected UriComponentsBuilder(UriComponentsBuilder other)
/*     */   {
/* 132 */     this.scheme = other.scheme;
/* 133 */     this.ssp = other.ssp;
/* 134 */     this.userInfo = other.userInfo;
/* 135 */     this.host = other.host;
/* 136 */     this.port = other.port;
/* 137 */     this.pathBuilder = other.pathBuilder.cloneBuilder();
/* 138 */     this.queryParams.putAll(other.queryParams);
/* 139 */     this.fragment = other.fragment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UriComponentsBuilder newInstance()
/*     */   {
/* 150 */     return new UriComponentsBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UriComponentsBuilder fromPath(String path)
/*     */   {
/* 159 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/* 160 */     builder.path(path);
/* 161 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UriComponentsBuilder fromUri(URI uri)
/*     */   {
/* 170 */     UriComponentsBuilder builder = new UriComponentsBuilder();
/* 171 */     builder.uri(uri);
/* 172 */     return builder;
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
/*     */   public static UriComponentsBuilder fromUriString(String uri)
/*     */   {
/* 190 */     Assert.notNull(uri, "URI must not be null");
/* 191 */     Matcher matcher = URI_PATTERN.matcher(uri);
/* 192 */     if (matcher.matches()) {
/* 193 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/* 194 */       String scheme = matcher.group(2);
/* 195 */       String userInfo = matcher.group(5);
/* 196 */       String host = matcher.group(6);
/* 197 */       String port = matcher.group(8);
/* 198 */       String path = matcher.group(9);
/* 199 */       String query = matcher.group(11);
/* 200 */       String fragment = matcher.group(13);
/* 201 */       boolean opaque = false;
/* 202 */       if (StringUtils.hasLength(scheme)) {
/* 203 */         String rest = uri.substring(scheme.length());
/* 204 */         if (!rest.startsWith(":/")) {
/* 205 */           opaque = true;
/*     */         }
/*     */       }
/* 208 */       builder.scheme(scheme);
/* 209 */       if (opaque) {
/* 210 */         String ssp = uri.substring(scheme.length()).substring(1);
/* 211 */         if (StringUtils.hasLength(fragment)) {
/* 212 */           ssp = ssp.substring(0, ssp.length() - (fragment.length() + 1));
/*     */         }
/* 214 */         builder.schemeSpecificPart(ssp);
/*     */       }
/*     */       else {
/* 217 */         builder.userInfo(userInfo);
/* 218 */         builder.host(host);
/* 219 */         if (StringUtils.hasLength(port)) {
/* 220 */           builder.port(port);
/*     */         }
/* 222 */         builder.path(path);
/* 223 */         builder.query(query);
/*     */       }
/* 225 */       if (StringUtils.hasText(fragment)) {
/* 226 */         builder.fragment(fragment);
/*     */       }
/* 228 */       return builder;
/*     */     }
/*     */     
/* 231 */     throw new IllegalArgumentException("[" + uri + "] is not a valid URI");
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
/*     */   public static UriComponentsBuilder fromHttpUrl(String httpUrl)
/*     */   {
/* 250 */     Assert.notNull(httpUrl, "HTTP URL must not be null");
/* 251 */     Matcher matcher = HTTP_URL_PATTERN.matcher(httpUrl);
/* 252 */     if (matcher.matches()) {
/* 253 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/* 254 */       String scheme = matcher.group(1);
/* 255 */       builder.scheme(scheme != null ? scheme.toLowerCase() : null);
/* 256 */       builder.userInfo(matcher.group(4));
/* 257 */       String host = matcher.group(5);
/* 258 */       if ((StringUtils.hasLength(scheme)) && (!StringUtils.hasLength(host))) {
/* 259 */         throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
/*     */       }
/* 261 */       builder.host(host);
/* 262 */       String port = matcher.group(7);
/* 263 */       if (StringUtils.hasLength(port)) {
/* 264 */         builder.port(port);
/*     */       }
/* 266 */       builder.path(matcher.group(8));
/* 267 */       builder.query(matcher.group(10));
/* 268 */       return builder;
/*     */     }
/*     */     
/* 271 */     throw new IllegalArgumentException("[" + httpUrl + "] is not a valid HTTP URL");
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
/*     */   public static UriComponentsBuilder fromHttpRequest(HttpRequest request)
/*     */   {
/* 286 */     return fromUri(request.getURI()).adaptFromForwardedHeaders(request.getHeaders());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UriComponentsBuilder fromOriginHeader(String origin)
/*     */   {
/* 294 */     Matcher matcher = URI_PATTERN.matcher(origin);
/* 295 */     if (matcher.matches()) {
/* 296 */       UriComponentsBuilder builder = new UriComponentsBuilder();
/* 297 */       String scheme = matcher.group(2);
/* 298 */       String host = matcher.group(6);
/* 299 */       String port = matcher.group(8);
/* 300 */       if (StringUtils.hasLength(scheme)) {
/* 301 */         builder.scheme(scheme);
/*     */       }
/* 303 */       builder.host(host);
/* 304 */       if (StringUtils.hasLength(port)) {
/* 305 */         builder.port(port);
/*     */       }
/* 307 */       return builder;
/*     */     }
/*     */     
/* 310 */     throw new IllegalArgumentException("[" + origin + "] is not a valid \"Origin\" header value");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponents build()
/*     */   {
/* 322 */     return build(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponents build(boolean encoded)
/*     */   {
/* 333 */     if (this.ssp != null) {
/* 334 */       return new OpaqueUriComponents(this.scheme, this.ssp, this.fragment);
/*     */     }
/*     */     
/* 337 */     return new HierarchicalUriComponents(this.scheme, this.userInfo, this.host, this.port, this.pathBuilder
/* 338 */       .build(), this.queryParams, this.fragment, encoded, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponents buildAndExpand(Map<String, ?> uriVariables)
/*     */   {
/* 350 */     return build(false).expand(uriVariables);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponents buildAndExpand(Object... uriVariableValues)
/*     */   {
/* 361 */     return build(false).expand(uriVariableValues);
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
/* 372 */     return build(false).encode().toUriString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder uri(URI uri)
/*     */   {
/* 384 */     Assert.notNull(uri, "URI must not be null");
/* 385 */     this.scheme = uri.getScheme();
/* 386 */     if (uri.isOpaque()) {
/* 387 */       this.ssp = uri.getRawSchemeSpecificPart();
/* 388 */       resetHierarchicalComponents();
/*     */     }
/*     */     else {
/* 391 */       if (uri.getRawUserInfo() != null) {
/* 392 */         this.userInfo = uri.getRawUserInfo();
/*     */       }
/* 394 */       if (uri.getHost() != null) {
/* 395 */         this.host = uri.getHost();
/*     */       }
/* 397 */       if (uri.getPort() != -1) {
/* 398 */         this.port = String.valueOf(uri.getPort());
/*     */       }
/* 400 */       if (StringUtils.hasLength(uri.getRawPath())) {
/* 401 */         this.pathBuilder = new CompositePathComponentBuilder(uri.getRawPath());
/*     */       }
/* 403 */       if (StringUtils.hasLength(uri.getRawQuery())) {
/* 404 */         this.queryParams.clear();
/* 405 */         query(uri.getRawQuery());
/*     */       }
/* 407 */       resetSchemeSpecificPart();
/*     */     }
/* 409 */     if (uri.getRawFragment() != null) {
/* 410 */       this.fragment = uri.getRawFragment();
/*     */     }
/* 412 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder scheme(String scheme)
/*     */   {
/* 422 */     this.scheme = scheme;
/* 423 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder uriComponents(UriComponents uriComponents)
/*     */   {
/* 432 */     Assert.notNull(uriComponents, "UriComponents must not be null");
/* 433 */     uriComponents.copyToUriComponentsBuilder(this);
/* 434 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder schemeSpecificPart(String ssp)
/*     */   {
/* 446 */     this.ssp = ssp;
/* 447 */     resetHierarchicalComponents();
/* 448 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder userInfo(String userInfo)
/*     */   {
/* 458 */     this.userInfo = userInfo;
/* 459 */     resetSchemeSpecificPart();
/* 460 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder host(String host)
/*     */   {
/* 470 */     this.host = host;
/* 471 */     resetSchemeSpecificPart();
/* 472 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder port(int port)
/*     */   {
/* 481 */     Assert.isTrue(port >= -1, "Port must be >= -1");
/* 482 */     this.port = String.valueOf(port);
/* 483 */     resetSchemeSpecificPart();
/* 484 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder port(String port)
/*     */   {
/* 495 */     this.port = port;
/* 496 */     resetSchemeSpecificPart();
/* 497 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder path(String path)
/*     */   {
/* 507 */     this.pathBuilder.addPath(path);
/* 508 */     resetSchemeSpecificPart();
/* 509 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder replacePath(String path)
/*     */   {
/* 518 */     this.pathBuilder = new CompositePathComponentBuilder(path);
/* 519 */     resetSchemeSpecificPart();
/* 520 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder pathSegment(String... pathSegments)
/*     */     throws IllegalArgumentException
/*     */   {
/* 531 */     this.pathBuilder.addPathSegments(pathSegments);
/* 532 */     resetSchemeSpecificPart();
/* 533 */     return this;
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
/*     */   public UriComponentsBuilder query(String query)
/*     */   {
/* 553 */     if (query != null) {
/* 554 */       Matcher matcher = QUERY_PARAM_PATTERN.matcher(query);
/* 555 */       while (matcher.find()) {
/* 556 */         String name = matcher.group(1);
/* 557 */         String eq = matcher.group(2);
/* 558 */         String value = matcher.group(3);
/* 559 */         queryParam(name, new Object[] { StringUtils.hasLength(eq) ? "" : value != null ? value : null });
/*     */       }
/*     */     }
/*     */     else {
/* 563 */       this.queryParams.clear();
/*     */     }
/* 565 */     resetSchemeSpecificPart();
/* 566 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder replaceQuery(String query)
/*     */   {
/* 575 */     this.queryParams.clear();
/* 576 */     query(query);
/* 577 */     resetSchemeSpecificPart();
/* 578 */     return this;
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
/*     */   public UriComponentsBuilder queryParam(String name, Object... values)
/*     */   {
/* 591 */     Assert.notNull(name, "Name must not be null");
/* 592 */     if (!ObjectUtils.isEmpty(values)) {
/* 593 */       for (Object value : values) {
/* 594 */         String valueAsString = value != null ? value.toString() : null;
/* 595 */         this.queryParams.add(name, valueAsString);
/*     */       }
/*     */       
/*     */     } else {
/* 599 */       this.queryParams.add(name, null);
/*     */     }
/* 601 */     resetSchemeSpecificPart();
/* 602 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder queryParams(MultiValueMap<String, String> params)
/*     */   {
/* 612 */     if (params != null) {
/* 613 */       this.queryParams.putAll(params);
/*     */     }
/* 615 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder replaceQueryParam(String name, Object... values)
/*     */   {
/* 626 */     Assert.notNull(name, "Name must not be null");
/* 627 */     this.queryParams.remove(name);
/* 628 */     if (!ObjectUtils.isEmpty(values)) {
/* 629 */       queryParam(name, values);
/*     */     }
/* 631 */     resetSchemeSpecificPart();
/* 632 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder replaceQueryParams(MultiValueMap<String, String> params)
/*     */   {
/* 642 */     this.queryParams.clear();
/* 643 */     if (params != null) {
/* 644 */       this.queryParams.putAll(params);
/*     */     }
/* 646 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder fragment(String fragment)
/*     */   {
/* 656 */     if (fragment != null) {
/* 657 */       Assert.hasLength(fragment, "Fragment must not be empty");
/* 658 */       this.fragment = fragment;
/*     */     }
/*     */     else {
/* 661 */       this.fragment = null;
/*     */     }
/* 663 */     return this;
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
/*     */   UriComponentsBuilder adaptFromForwardedHeaders(HttpHeaders headers)
/*     */   {
/* 676 */     String forwardedHeader = headers.getFirst("Forwarded");
/* 677 */     if (StringUtils.hasText(forwardedHeader)) {
/* 678 */       String forwardedToUse = StringUtils.tokenizeToStringArray(forwardedHeader, ",")[0];
/* 679 */       Matcher matcher = FORWARDED_PROTO_PATTERN.matcher(forwardedToUse);
/* 680 */       if (matcher.find()) {
/* 681 */         scheme(matcher.group(1).trim());
/* 682 */         port(null);
/*     */       }
/* 684 */       matcher = FORWARDED_HOST_PATTERN.matcher(forwardedToUse);
/* 685 */       if (matcher.find()) {
/* 686 */         adaptForwardedHost(matcher.group(1).trim());
/*     */       }
/*     */     }
/*     */     else {
/* 690 */       String protocolHeader = headers.getFirst("X-Forwarded-Proto");
/* 691 */       if (StringUtils.hasText(protocolHeader)) {
/* 692 */         scheme(StringUtils.tokenizeToStringArray(protocolHeader, ",")[0]);
/* 693 */         port(null);
/*     */       }
/*     */       
/* 696 */       String hostHeader = headers.getFirst("X-Forwarded-Host");
/* 697 */       if (StringUtils.hasText(hostHeader)) {
/* 698 */         adaptForwardedHost(StringUtils.tokenizeToStringArray(hostHeader, ",")[0]);
/*     */       }
/*     */       
/* 701 */       String portHeader = headers.getFirst("X-Forwarded-Port");
/* 702 */       if (StringUtils.hasText(portHeader)) {
/* 703 */         port(Integer.parseInt(StringUtils.tokenizeToStringArray(portHeader, ",")[0]));
/*     */       }
/*     */     }
/*     */     
/* 707 */     if ((this.scheme != null) && (((this.scheme.equals("http")) && ("80".equals(this.port))) || (
/* 708 */       (this.scheme.equals("https")) && ("443".equals(this.port))))) {
/* 709 */       port(null);
/*     */     }
/*     */     
/* 712 */     return this;
/*     */   }
/*     */   
/*     */   private void adaptForwardedHost(String hostToUse) {
/* 716 */     int portSeparatorIdx = hostToUse.lastIndexOf(":");
/* 717 */     if (portSeparatorIdx > hostToUse.lastIndexOf("]")) {
/* 718 */       host(hostToUse.substring(0, portSeparatorIdx));
/* 719 */       port(Integer.parseInt(hostToUse.substring(portSeparatorIdx + 1)));
/*     */     }
/*     */     else {
/* 722 */       host(hostToUse);
/* 723 */       port(null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetHierarchicalComponents() {
/* 728 */     this.userInfo = null;
/* 729 */     this.host = null;
/* 730 */     this.port = null;
/* 731 */     this.pathBuilder = new CompositePathComponentBuilder();
/* 732 */     this.queryParams.clear();
/*     */   }
/*     */   
/*     */   private void resetSchemeSpecificPart() {
/* 736 */     this.ssp = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 747 */     return cloneBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder cloneBuilder()
/*     */   {
/* 756 */     return new UriComponentsBuilder(this);
/*     */   }
/*     */   
/*     */ 
/*     */   private static abstract interface PathComponentBuilder
/*     */   {
/*     */     public abstract HierarchicalUriComponents.PathComponent build();
/*     */     
/*     */     public abstract PathComponentBuilder cloneBuilder();
/*     */   }
/*     */   
/*     */   private static class CompositePathComponentBuilder
/*     */     implements UriComponentsBuilder.PathComponentBuilder
/*     */   {
/* 770 */     private final LinkedList<UriComponentsBuilder.PathComponentBuilder> builders = new LinkedList();
/*     */     
/*     */     public CompositePathComponentBuilder() {}
/*     */     
/*     */     public CompositePathComponentBuilder(String path)
/*     */     {
/* 776 */       addPath(path);
/*     */     }
/*     */     
/*     */     public void addPathSegments(String... pathSegments) {
/* 780 */       if (!ObjectUtils.isEmpty(pathSegments)) {
/* 781 */         UriComponentsBuilder.PathSegmentComponentBuilder psBuilder = (UriComponentsBuilder.PathSegmentComponentBuilder)getLastBuilder(UriComponentsBuilder.PathSegmentComponentBuilder.class);
/* 782 */         UriComponentsBuilder.FullPathComponentBuilder fpBuilder = (UriComponentsBuilder.FullPathComponentBuilder)getLastBuilder(UriComponentsBuilder.FullPathComponentBuilder.class);
/* 783 */         if (psBuilder == null) {
/* 784 */           psBuilder = new UriComponentsBuilder.PathSegmentComponentBuilder(null);
/* 785 */           this.builders.add(psBuilder);
/* 786 */           if (fpBuilder != null) {
/* 787 */             fpBuilder.removeTrailingSlash();
/*     */           }
/*     */         }
/* 790 */         psBuilder.append(pathSegments);
/*     */       }
/*     */     }
/*     */     
/*     */     public void addPath(String path) {
/* 795 */       if (StringUtils.hasText(path)) {
/* 796 */         UriComponentsBuilder.PathSegmentComponentBuilder psBuilder = (UriComponentsBuilder.PathSegmentComponentBuilder)getLastBuilder(UriComponentsBuilder.PathSegmentComponentBuilder.class);
/* 797 */         UriComponentsBuilder.FullPathComponentBuilder fpBuilder = (UriComponentsBuilder.FullPathComponentBuilder)getLastBuilder(UriComponentsBuilder.FullPathComponentBuilder.class);
/* 798 */         if (psBuilder != null) {
/* 799 */           path = "/" + path;
/*     */         }
/* 801 */         if (fpBuilder == null) {
/* 802 */           fpBuilder = new UriComponentsBuilder.FullPathComponentBuilder(null);
/* 803 */           this.builders.add(fpBuilder);
/*     */         }
/* 805 */         fpBuilder.append(path);
/*     */       }
/*     */     }
/*     */     
/*     */     private <T> T getLastBuilder(Class<T> builderClass)
/*     */     {
/* 811 */       if (!this.builders.isEmpty()) {
/* 812 */         UriComponentsBuilder.PathComponentBuilder last = (UriComponentsBuilder.PathComponentBuilder)this.builders.getLast();
/* 813 */         if (builderClass.isInstance(last)) {
/* 814 */           return last;
/*     */         }
/*     */       }
/* 817 */       return null;
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent build()
/*     */     {
/* 822 */       int size = this.builders.size();
/* 823 */       List<HierarchicalUriComponents.PathComponent> components = new ArrayList(size);
/* 824 */       for (UriComponentsBuilder.PathComponentBuilder componentBuilder : this.builders) {
/* 825 */         HierarchicalUriComponents.PathComponent pathComponent = componentBuilder.build();
/* 826 */         if (pathComponent != null) {
/* 827 */           components.add(pathComponent);
/*     */         }
/*     */       }
/* 830 */       if (components.isEmpty()) {
/* 831 */         return HierarchicalUriComponents.NULL_PATH_COMPONENT;
/*     */       }
/* 833 */       if (components.size() == 1) {
/* 834 */         return (HierarchicalUriComponents.PathComponent)components.get(0);
/*     */       }
/* 836 */       return new HierarchicalUriComponents.PathComponentComposite(components);
/*     */     }
/*     */     
/*     */     public CompositePathComponentBuilder cloneBuilder()
/*     */     {
/* 841 */       CompositePathComponentBuilder compositeBuilder = new CompositePathComponentBuilder();
/* 842 */       for (UriComponentsBuilder.PathComponentBuilder builder : this.builders) {
/* 843 */         compositeBuilder.builders.add(builder.cloneBuilder());
/*     */       }
/* 845 */       return compositeBuilder;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FullPathComponentBuilder
/*     */     implements UriComponentsBuilder.PathComponentBuilder
/*     */   {
/* 852 */     private final StringBuilder path = new StringBuilder();
/*     */     
/*     */     public void append(String path) {
/* 855 */       this.path.append(path);
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent build()
/*     */     {
/* 860 */       if (this.path.length() == 0) {
/* 861 */         return null;
/*     */       }
/* 863 */       String path = this.path.toString();
/*     */       for (;;) {
/* 865 */         int index = path.indexOf("//");
/* 866 */         if (index == -1) {
/*     */           break;
/*     */         }
/* 869 */         path = path.substring(0, index) + path.substring(index + 1);
/*     */       }
/* 871 */       return new HierarchicalUriComponents.FullPathComponent(path);
/*     */     }
/*     */     
/*     */     public void removeTrailingSlash() {
/* 875 */       int index = this.path.length() - 1;
/* 876 */       if (this.path.charAt(index) == '/') {
/* 877 */         this.path.deleteCharAt(index);
/*     */       }
/*     */     }
/*     */     
/*     */     public FullPathComponentBuilder cloneBuilder()
/*     */     {
/* 883 */       FullPathComponentBuilder builder = new FullPathComponentBuilder();
/* 884 */       builder.append(this.path.toString());
/* 885 */       return builder;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PathSegmentComponentBuilder
/*     */     implements UriComponentsBuilder.PathComponentBuilder
/*     */   {
/* 892 */     private final List<String> pathSegments = new LinkedList();
/*     */     
/*     */     public void append(String... pathSegments) {
/* 895 */       for (String pathSegment : pathSegments) {
/* 896 */         if (StringUtils.hasText(pathSegment)) {
/* 897 */           this.pathSegments.add(pathSegment);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public HierarchicalUriComponents.PathComponent build()
/*     */     {
/* 904 */       return this.pathSegments.isEmpty() ? null : new HierarchicalUriComponents.PathSegmentComponent(this.pathSegments);
/*     */     }
/*     */     
/*     */ 
/*     */     public PathSegmentComponentBuilder cloneBuilder()
/*     */     {
/* 910 */       PathSegmentComponentBuilder builder = new PathSegmentComponentBuilder();
/* 911 */       builder.pathSegments.addAll(this.pathSegments);
/* 912 */       return builder;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\we\\util\UriComponentsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */