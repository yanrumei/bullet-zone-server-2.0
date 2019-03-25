/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Joiner.MapJoiner;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableListMultimap;
/*     */ import com.google.common.collect.ImmutableListMultimap.Builder;
/*     */ import com.google.common.collect.ImmutableMultiset;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ @Immutable
/*     */ public final class MediaType
/*     */ {
/*     */   private static final String CHARSET_ATTRIBUTE = "charset";
/*  81 */   private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of("charset", Ascii.toLowerCase(Charsets.UTF_8.name()));
/*     */   
/*     */ 
/*     */ 
/*  85 */   private static final CharMatcher TOKEN_MATCHER = CharMatcher.ascii()
/*  86 */     .and(CharMatcher.javaIsoControl().negate())
/*  87 */     .and(CharMatcher.isNot(' '))
/*  88 */     .and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
/*  89 */   private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
/*     */   
/*     */   private static final String APPLICATION_TYPE = "application";
/*     */   
/*     */   private static final String AUDIO_TYPE = "audio";
/*     */   
/*     */   private static final String IMAGE_TYPE = "image";
/*     */   
/*     */   private static final String TEXT_TYPE = "text";
/*     */   private static final String VIDEO_TYPE = "video";
/*     */   private static final String WILDCARD = "*";
/* 105 */   private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
/*     */   
/*     */   private static MediaType createConstant(String type, String subtype)
/*     */   {
/* 109 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, ImmutableListMultimap.of()));
/* 110 */     mediaType.parsedCharset = Optional.absent();
/* 111 */     return mediaType;
/*     */   }
/*     */   
/*     */   private static MediaType createConstantUtf8(String type, String subtype) {
/* 115 */     MediaType mediaType = addKnownType(new MediaType(type, subtype, UTF_8_CONSTANT_PARAMETERS));
/* 116 */     mediaType.parsedCharset = Optional.of(Charsets.UTF_8);
/* 117 */     return mediaType;
/*     */   }
/*     */   
/*     */   private static MediaType addKnownType(MediaType mediaType) {
/* 121 */     KNOWN_TYPES.put(mediaType, mediaType);
/* 122 */     return mediaType;
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
/* 135 */   public static final MediaType ANY_TYPE = createConstant("*", "*");
/* 136 */   public static final MediaType ANY_TEXT_TYPE = createConstant("text", "*");
/* 137 */   public static final MediaType ANY_IMAGE_TYPE = createConstant("image", "*");
/* 138 */   public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", "*");
/* 139 */   public static final MediaType ANY_VIDEO_TYPE = createConstant("video", "*");
/* 140 */   public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", "*");
/*     */   
/*     */ 
/*     */ 
/* 144 */   public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
/* 145 */   public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
/* 146 */   public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
/* 147 */   public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
/* 148 */   public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
/* 149 */   public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */   public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 162 */   public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
/* 163 */   public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
/* 164 */   public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 170 */   public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */   public static final MediaType VTT_UTF_8 = createConstantUtf8("text", "vtt");
/*     */   
/*     */ 
/* 180 */   public static final MediaType BMP = createConstant("image", "bmp");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */   public static final MediaType CRW = createConstant("image", "x-canon-crw");
/* 191 */   public static final MediaType GIF = createConstant("image", "gif");
/* 192 */   public static final MediaType ICO = createConstant("image", "vnd.microsoft.icon");
/* 193 */   public static final MediaType JPEG = createConstant("image", "jpeg");
/* 194 */   public static final MediaType PNG = createConstant("image", "png");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */   public static final MediaType PSD = createConstant("image", "vnd.adobe.photoshop");
/* 213 */   public static final MediaType SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
/* 214 */   public static final MediaType TIFF = createConstant("image", "tiff");
/* 215 */   public static final MediaType WEBP = createConstant("image", "webp");
/*     */   
/*     */ 
/* 218 */   public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
/* 219 */   public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
/* 220 */   public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
/* 221 */   public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 229 */   public static final MediaType L24_AUDIO = createConstant("audio", "l24");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 237 */   public static final MediaType BASIC_AUDIO = createConstant("audio", "basic");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 245 */   public static final MediaType AAC_AUDIO = createConstant("audio", "aac");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 253 */   public static final MediaType VORBIS_AUDIO = createConstant("audio", "vorbis");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */   public static final MediaType WMA_AUDIO = createConstant("audio", "x-ms-wma");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 271 */   public static final MediaType WAX_AUDIO = createConstant("audio", "x-ms-wax");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 279 */   public static final MediaType VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 287 */   public static final MediaType VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
/*     */   
/*     */ 
/* 290 */   public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
/* 291 */   public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
/* 292 */   public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
/* 293 */   public static final MediaType QUICKTIME = createConstant("video", "quicktime");
/* 294 */   public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
/* 295 */   public static final MediaType WMV = createConstant("video", "x-ms-wmv");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 304 */   public static final MediaType FLV_VIDEO = createConstant("video", "x-flv");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 313 */   public static final MediaType THREE_GPP_VIDEO = createConstant("video", "3gpp");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 322 */   public static final MediaType THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 330 */   public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
/* 331 */   public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
/* 332 */   public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 339 */   public static final MediaType DART_UTF_8 = createConstantUtf8("application", "dart");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 347 */   public static final MediaType APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 357 */   public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 367 */   public static final MediaType EPUB = createConstant("application", "epub+zip");
/*     */   
/* 369 */   public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 377 */   public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 388 */   public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
/*     */   
/* 390 */   public static final MediaType GZIP = createConstant("application", "x-gzip");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 397 */   public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
/* 398 */   public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 406 */   public static final MediaType MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
/* 407 */   public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
/* 408 */   public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
/* 409 */   public static final MediaType MBOX = createConstant("application", "mbox");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 418 */   public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
/* 419 */   public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
/*     */   
/* 421 */   public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
/* 422 */   public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 431 */   public static final MediaType NACL_APPLICATION = createConstant("application", "x-nacl");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 441 */   public static final MediaType NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
/*     */   
/* 443 */   public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
/*     */   
/* 445 */   public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
/*     */   
/* 447 */   public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
/*     */   
/*     */ 
/* 450 */   public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
/*     */   
/*     */ 
/* 453 */   public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
/*     */   
/* 455 */   public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
/*     */   
/* 457 */   public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
/*     */   
/* 459 */   public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
/*     */   
/* 461 */   public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
/* 462 */   public static final MediaType PDF = createConstant("application", "pdf");
/* 463 */   public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 469 */   public static final MediaType PROTOBUF = createConstant("application", "protobuf");
/*     */   
/* 471 */   public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
/* 472 */   public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 482 */   public static final MediaType SFNT = createConstant("application", "font-sfnt");
/*     */   
/* 484 */   public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
/* 485 */   public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 497 */   public static final MediaType SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
/* 498 */   public static final MediaType TAR = createConstant("application", "x-tar");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 507 */   public static final MediaType WOFF = createConstant("application", "font-woff");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 514 */   public static final MediaType WOFF2 = createConstant("application", "font-woff2");
/* 515 */   public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 522 */   public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
/* 523 */   public static final MediaType ZIP = createConstant("application", "zip");
/*     */   
/*     */   private final String type;
/*     */   
/*     */   private final String subtype;
/*     */   private final ImmutableListMultimap<String, String> parameters;
/*     */   @LazyInit
/*     */   private String toString;
/*     */   @LazyInit
/*     */   private int hashCode;
/*     */   @LazyInit
/*     */   private Optional<Charset> parsedCharset;
/*     */   
/*     */   private MediaType(String type, String subtype, ImmutableListMultimap<String, String> parameters)
/*     */   {
/* 538 */     this.type = type;
/* 539 */     this.subtype = subtype;
/* 540 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public String type()
/*     */   {
/* 545 */     return this.type;
/*     */   }
/*     */   
/*     */   public String subtype()
/*     */   {
/* 550 */     return this.subtype;
/*     */   }
/*     */   
/*     */   public ImmutableListMultimap<String, String> parameters()
/*     */   {
/* 555 */     return this.parameters;
/*     */   }
/*     */   
/*     */   private Map<String, ImmutableMultiset<String>> parametersAsMap() {
/* 559 */     Maps.transformValues(this.parameters
/* 560 */       .asMap(), new Function()
/*     */       {
/*     */         public ImmutableMultiset<String> apply(Collection<String> input)
/*     */         {
/* 564 */           return ImmutableMultiset.copyOf(input);
/*     */         }
/*     */       });
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
/*     */   public Optional<Charset> charset()
/*     */   {
/* 579 */     Optional<Charset> local = this.parsedCharset;
/* 580 */     if (local == null) {
/* 581 */       String value = null;
/* 582 */       local = Optional.absent();
/* 583 */       for (UnmodifiableIterator localUnmodifiableIterator = this.parameters.get("charset").iterator(); localUnmodifiableIterator.hasNext();) { String currentValue = (String)localUnmodifiableIterator.next();
/* 584 */         if (value == null) {
/* 585 */           value = currentValue;
/* 586 */           local = Optional.of(Charset.forName(value));
/* 587 */         } else if (!value.equals(currentValue)) {
/* 588 */           throw new IllegalStateException("Multiple charset values defined: " + value + ", " + currentValue);
/*     */         }
/*     */       }
/*     */       
/* 592 */       this.parsedCharset = local;
/*     */     }
/* 594 */     return local;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType withoutParameters()
/*     */   {
/* 602 */     return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType withParameters(Multimap<String, String> parameters)
/*     */   {
/* 611 */     return create(this.type, this.subtype, parameters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MediaType withParameter(String attribute, String value)
/*     */   {
/* 623 */     Preconditions.checkNotNull(attribute);
/* 624 */     Preconditions.checkNotNull(value);
/* 625 */     String normalizedAttribute = normalizeToken(attribute);
/* 626 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 627 */     for (UnmodifiableIterator localUnmodifiableIterator = this.parameters.entries().iterator(); localUnmodifiableIterator.hasNext();) { Map.Entry<String, String> entry = (Map.Entry)localUnmodifiableIterator.next();
/* 628 */       String key = (String)entry.getKey();
/* 629 */       if (!normalizedAttribute.equals(key)) {
/* 630 */         builder.put(key, entry.getValue());
/*     */       }
/*     */     }
/* 633 */     builder.put(normalizedAttribute, normalizeParameterValue(normalizedAttribute, value));
/* 634 */     MediaType mediaType = new MediaType(this.type, this.subtype, builder.build());
/*     */     
/* 636 */     if (!normalizedAttribute.equals("charset")) {
/* 637 */       mediaType.parsedCharset = this.parsedCharset;
/*     */     }
/*     */     
/* 640 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
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
/*     */   public MediaType withCharset(Charset charset)
/*     */   {
/* 653 */     Preconditions.checkNotNull(charset);
/* 654 */     MediaType withCharset = withParameter("charset", charset.name());
/*     */     
/* 656 */     withCharset.parsedCharset = Optional.of(charset);
/* 657 */     return withCharset;
/*     */   }
/*     */   
/*     */   public boolean hasWildcard()
/*     */   {
/* 662 */     return ("*".equals(this.type)) || ("*".equals(this.subtype));
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
/*     */   public boolean is(MediaType mediaTypeRange)
/*     */   {
/* 692 */     return ((mediaTypeRange.type.equals("*")) || (mediaTypeRange.type.equals(this.type))) && 
/* 693 */       ((mediaTypeRange.subtype.equals("*")) || (mediaTypeRange.subtype.equals(this.subtype))) && 
/* 694 */       (this.parameters.entries().containsAll(mediaTypeRange.parameters.entries()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MediaType create(String type, String subtype)
/*     */   {
/* 704 */     MediaType mediaType = create(type, subtype, ImmutableListMultimap.of());
/* 705 */     mediaType.parsedCharset = Optional.absent();
/* 706 */     return mediaType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static MediaType createApplicationType(String subtype)
/*     */   {
/* 715 */     return create("application", subtype);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static MediaType createAudioType(String subtype)
/*     */   {
/* 724 */     return create("audio", subtype);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static MediaType createImageType(String subtype)
/*     */   {
/* 733 */     return create("image", subtype);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static MediaType createTextType(String subtype)
/*     */   {
/* 742 */     return create("text", subtype);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static MediaType createVideoType(String subtype)
/*     */   {
/* 751 */     return create("video", subtype);
/*     */   }
/*     */   
/*     */   private static MediaType create(String type, String subtype, Multimap<String, String> parameters)
/*     */   {
/* 756 */     Preconditions.checkNotNull(type);
/* 757 */     Preconditions.checkNotNull(subtype);
/* 758 */     Preconditions.checkNotNull(parameters);
/* 759 */     String normalizedType = normalizeToken(type);
/* 760 */     String normalizedSubtype = normalizeToken(subtype);
/* 761 */     Preconditions.checkArgument(
/* 762 */       (!"*".equals(normalizedType)) || ("*".equals(normalizedSubtype)), "A wildcard type cannot be used with a non-wildcard subtype");
/*     */     
/* 764 */     ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
/* 765 */     for (Map.Entry<String, String> entry : parameters.entries()) {
/* 766 */       String attribute = normalizeToken((String)entry.getKey());
/* 767 */       builder.put(attribute, normalizeParameterValue(attribute, (String)entry.getValue()));
/*     */     }
/* 769 */     MediaType mediaType = new MediaType(normalizedType, normalizedSubtype, builder.build());
/*     */     
/* 771 */     return (MediaType)MoreObjects.firstNonNull(KNOWN_TYPES.get(mediaType), mediaType);
/*     */   }
/*     */   
/*     */   private static String normalizeToken(String token) {
/* 775 */     Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(token));
/* 776 */     return Ascii.toLowerCase(token);
/*     */   }
/*     */   
/*     */   private static String normalizeParameterValue(String attribute, String value) {
/* 780 */     return "charset".equals(attribute) ? Ascii.toLowerCase(value) : value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MediaType parse(String input)
/*     */   {
/* 789 */     Preconditions.checkNotNull(input);
/* 790 */     Tokenizer tokenizer = new Tokenizer(input);
/*     */     try {
/* 792 */       String type = tokenizer.consumeToken(TOKEN_MATCHER);
/* 793 */       tokenizer.consumeCharacter('/');
/* 794 */       String subtype = tokenizer.consumeToken(TOKEN_MATCHER);
/* 795 */       ImmutableListMultimap.Builder<String, String> parameters = ImmutableListMultimap.builder();
/* 796 */       while (tokenizer.hasMore()) {
/* 797 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 798 */         tokenizer.consumeCharacter(';');
/* 799 */         tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
/* 800 */         String attribute = tokenizer.consumeToken(TOKEN_MATCHER);
/* 801 */         tokenizer.consumeCharacter('=');
/*     */         String value;
/* 803 */         if ('"' == tokenizer.previewChar()) {
/* 804 */           tokenizer.consumeCharacter('"');
/* 805 */           StringBuilder valueBuilder = new StringBuilder();
/* 806 */           while ('"' != tokenizer.previewChar()) {
/* 807 */             if ('\\' == tokenizer.previewChar()) {
/* 808 */               tokenizer.consumeCharacter('\\');
/* 809 */               valueBuilder.append(tokenizer.consumeCharacter(CharMatcher.ascii()));
/*     */             } else {
/* 811 */               valueBuilder.append(tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
/*     */             }
/*     */           }
/* 814 */           String value = valueBuilder.toString();
/* 815 */           tokenizer.consumeCharacter('"');
/*     */         } else {
/* 817 */           value = tokenizer.consumeToken(TOKEN_MATCHER);
/*     */         }
/* 819 */         parameters.put(attribute, value);
/*     */       }
/* 821 */       return create(type, subtype, parameters.build());
/*     */     } catch (IllegalStateException e) {
/* 823 */       throw new IllegalArgumentException("Could not parse '" + input + "'", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Tokenizer {
/*     */     final String input;
/* 829 */     int position = 0;
/*     */     
/*     */     Tokenizer(String input) {
/* 832 */       this.input = input;
/*     */     }
/*     */     
/*     */     String consumeTokenIfPresent(CharMatcher matcher) {
/* 836 */       Preconditions.checkState(hasMore());
/* 837 */       int startPosition = this.position;
/* 838 */       this.position = matcher.negate().indexIn(this.input, startPosition);
/* 839 */       return hasMore() ? this.input.substring(startPosition, this.position) : this.input.substring(startPosition);
/*     */     }
/*     */     
/*     */     String consumeToken(CharMatcher matcher) {
/* 843 */       int startPosition = this.position;
/* 844 */       String token = consumeTokenIfPresent(matcher);
/* 845 */       Preconditions.checkState(this.position != startPosition);
/* 846 */       return token;
/*     */     }
/*     */     
/*     */     char consumeCharacter(CharMatcher matcher) {
/* 850 */       Preconditions.checkState(hasMore());
/* 851 */       char c = previewChar();
/* 852 */       Preconditions.checkState(matcher.matches(c));
/* 853 */       this.position += 1;
/* 854 */       return c;
/*     */     }
/*     */     
/*     */     char consumeCharacter(char c) {
/* 858 */       Preconditions.checkState(hasMore());
/* 859 */       Preconditions.checkState(previewChar() == c);
/* 860 */       this.position += 1;
/* 861 */       return c;
/*     */     }
/*     */     
/*     */     char previewChar() {
/* 865 */       Preconditions.checkState(hasMore());
/* 866 */       return this.input.charAt(this.position);
/*     */     }
/*     */     
/*     */     boolean hasMore() {
/* 870 */       return (this.position >= 0) && (this.position < this.input.length());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object obj)
/*     */   {
/* 876 */     if (obj == this)
/* 877 */       return true;
/* 878 */     if ((obj instanceof MediaType)) {
/* 879 */       MediaType that = (MediaType)obj;
/* 880 */       if ((this.type.equals(that.type)) && 
/* 881 */         (this.subtype.equals(that.subtype))) {}
/* 880 */       return 
/*     */       
/*     */ 
/* 883 */         parametersAsMap().equals(that.parametersAsMap());
/*     */     }
/* 885 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 892 */     int h = this.hashCode;
/* 893 */     if (h == 0) {
/* 894 */       h = Objects.hashCode(new Object[] { this.type, this.subtype, parametersAsMap() });
/* 895 */       this.hashCode = h;
/*     */     }
/* 897 */     return h;
/*     */   }
/*     */   
/* 900 */   private static final Joiner.MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 909 */     String result = this.toString;
/* 910 */     if (result == null) {
/* 911 */       result = computeToString();
/* 912 */       this.toString = result;
/*     */     }
/* 914 */     return result;
/*     */   }
/*     */   
/*     */   private String computeToString() {
/* 918 */     StringBuilder builder = new StringBuilder().append(this.type).append('/').append(this.subtype);
/* 919 */     if (!this.parameters.isEmpty()) {
/* 920 */       builder.append("; ");
/*     */       
/* 922 */       Multimap<String, String> quotedParameters = Multimaps.transformValues(this.parameters, new Function()
/*     */       {
/*     */ 
/*     */         public String apply(String value)
/*     */         {
/*     */ 
/* 927 */           return MediaType.TOKEN_MATCHER.matchesAllOf(value) ? value : MediaType.escapeAndQuote(value);
/*     */         }
/* 929 */       });
/* 930 */       PARAMETER_JOINER.appendTo(builder, quotedParameters.entries());
/*     */     }
/* 932 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String escapeAndQuote(String value) {
/* 936 */     StringBuilder escaped = new StringBuilder(value.length() + 16).append('"');
/* 937 */     for (int i = 0; i < value.length(); i++) {
/* 938 */       char ch = value.charAt(i);
/* 939 */       if ((ch == '\r') || (ch == '\\') || (ch == '"')) {
/* 940 */         escaped.append('\\');
/*     */       }
/* 942 */       escaped.append(ch);
/*     */     }
/* 944 */     return '"';
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\net\MediaType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */