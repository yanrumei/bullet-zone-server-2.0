/*     */ package org.springframework.boot.context.embedded;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public final class MimeMappings
/*     */   implements Iterable<Mapping>
/*     */ {
/*     */   public static final MimeMappings DEFAULT;
/*     */   private final Map<String, Mapping> map;
/*     */   
/*     */   static
/*     */   {
/*  42 */     MimeMappings mappings = new MimeMappings();
/*  43 */     mappings.add("abs", "audio/x-mpeg");
/*  44 */     mappings.add("ai", "application/postscript");
/*  45 */     mappings.add("aif", "audio/x-aiff");
/*  46 */     mappings.add("aifc", "audio/x-aiff");
/*  47 */     mappings.add("aiff", "audio/x-aiff");
/*  48 */     mappings.add("aim", "application/x-aim");
/*  49 */     mappings.add("art", "image/x-jg");
/*  50 */     mappings.add("asf", "video/x-ms-asf");
/*  51 */     mappings.add("asx", "video/x-ms-asf");
/*  52 */     mappings.add("au", "audio/basic");
/*  53 */     mappings.add("avi", "video/x-msvideo");
/*  54 */     mappings.add("avx", "video/x-rad-screenplay");
/*  55 */     mappings.add("bcpio", "application/x-bcpio");
/*  56 */     mappings.add("bin", "application/octet-stream");
/*  57 */     mappings.add("bmp", "image/bmp");
/*  58 */     mappings.add("body", "text/html");
/*  59 */     mappings.add("cdf", "application/x-cdf");
/*  60 */     mappings.add("cer", "application/pkix-cert");
/*  61 */     mappings.add("class", "application/java");
/*  62 */     mappings.add("cpio", "application/x-cpio");
/*  63 */     mappings.add("csh", "application/x-csh");
/*  64 */     mappings.add("css", "text/css");
/*  65 */     mappings.add("dib", "image/bmp");
/*  66 */     mappings.add("doc", "application/msword");
/*  67 */     mappings.add("dtd", "application/xml-dtd");
/*  68 */     mappings.add("dv", "video/x-dv");
/*  69 */     mappings.add("dvi", "application/x-dvi");
/*  70 */     mappings.add("eot", "application/vnd.ms-fontobject");
/*  71 */     mappings.add("eps", "application/postscript");
/*  72 */     mappings.add("etx", "text/x-setext");
/*  73 */     mappings.add("exe", "application/octet-stream");
/*  74 */     mappings.add("gif", "image/gif");
/*  75 */     mappings.add("gtar", "application/x-gtar");
/*  76 */     mappings.add("gz", "application/x-gzip");
/*  77 */     mappings.add("hdf", "application/x-hdf");
/*  78 */     mappings.add("hqx", "application/mac-binhex40");
/*  79 */     mappings.add("htc", "text/x-component");
/*  80 */     mappings.add("htm", "text/html");
/*  81 */     mappings.add("html", "text/html");
/*  82 */     mappings.add("ief", "image/ief");
/*  83 */     mappings.add("jad", "text/vnd.sun.j2me.app-descriptor");
/*  84 */     mappings.add("jar", "application/java-archive");
/*  85 */     mappings.add("java", "text/x-java-source");
/*  86 */     mappings.add("jnlp", "application/x-java-jnlp-file");
/*  87 */     mappings.add("jpe", "image/jpeg");
/*  88 */     mappings.add("jpeg", "image/jpeg");
/*  89 */     mappings.add("jpg", "image/jpeg");
/*  90 */     mappings.add("js", "application/javascript");
/*  91 */     mappings.add("jsf", "text/plain");
/*  92 */     mappings.add("json", "application/json");
/*  93 */     mappings.add("jspf", "text/plain");
/*  94 */     mappings.add("kar", "audio/midi");
/*  95 */     mappings.add("latex", "application/x-latex");
/*  96 */     mappings.add("m3u", "audio/x-mpegurl");
/*  97 */     mappings.add("mac", "image/x-macpaint");
/*  98 */     mappings.add("man", "text/troff");
/*  99 */     mappings.add("mathml", "application/mathml+xml");
/* 100 */     mappings.add("me", "text/troff");
/* 101 */     mappings.add("mid", "audio/midi");
/* 102 */     mappings.add("midi", "audio/midi");
/* 103 */     mappings.add("mif", "application/x-mif");
/* 104 */     mappings.add("mov", "video/quicktime");
/* 105 */     mappings.add("movie", "video/x-sgi-movie");
/* 106 */     mappings.add("mp1", "audio/mpeg");
/* 107 */     mappings.add("mp2", "audio/mpeg");
/* 108 */     mappings.add("mp3", "audio/mpeg");
/* 109 */     mappings.add("mp4", "video/mp4");
/* 110 */     mappings.add("mpa", "audio/mpeg");
/* 111 */     mappings.add("mpe", "video/mpeg");
/* 112 */     mappings.add("mpeg", "video/mpeg");
/* 113 */     mappings.add("mpega", "audio/x-mpeg");
/* 114 */     mappings.add("mpg", "video/mpeg");
/* 115 */     mappings.add("mpv2", "video/mpeg2");
/* 116 */     mappings.add("ms", "application/x-wais-source");
/* 117 */     mappings.add("nc", "application/x-netcdf");
/* 118 */     mappings.add("oda", "application/oda");
/* 119 */     mappings.add("odb", "application/vnd.oasis.opendocument.database");
/* 120 */     mappings.add("odc", "application/vnd.oasis.opendocument.chart");
/* 121 */     mappings.add("odf", "application/vnd.oasis.opendocument.formula");
/* 122 */     mappings.add("odg", "application/vnd.oasis.opendocument.graphics");
/* 123 */     mappings.add("odi", "application/vnd.oasis.opendocument.image");
/* 124 */     mappings.add("odm", "application/vnd.oasis.opendocument.text-master");
/* 125 */     mappings.add("odp", "application/vnd.oasis.opendocument.presentation");
/* 126 */     mappings.add("ods", "application/vnd.oasis.opendocument.spreadsheet");
/* 127 */     mappings.add("odt", "application/vnd.oasis.opendocument.text");
/* 128 */     mappings.add("otg", "application/vnd.oasis.opendocument.graphics-template");
/* 129 */     mappings.add("oth", "application/vnd.oasis.opendocument.text-web");
/* 130 */     mappings.add("otp", "application/vnd.oasis.opendocument.presentation-template");
/* 131 */     mappings.add("ots", "application/vnd.oasis.opendocument.spreadsheet-template ");
/* 132 */     mappings.add("ott", "application/vnd.oasis.opendocument.text-template");
/* 133 */     mappings.add("ogx", "application/ogg");
/* 134 */     mappings.add("ogv", "video/ogg");
/* 135 */     mappings.add("oga", "audio/ogg");
/* 136 */     mappings.add("ogg", "audio/ogg");
/* 137 */     mappings.add("otf", "application/x-font-opentype");
/* 138 */     mappings.add("spx", "audio/ogg");
/* 139 */     mappings.add("flac", "audio/flac");
/* 140 */     mappings.add("anx", "application/annodex");
/* 141 */     mappings.add("axa", "audio/annodex");
/* 142 */     mappings.add("axv", "video/annodex");
/* 143 */     mappings.add("xspf", "application/xspf+xml");
/* 144 */     mappings.add("pbm", "image/x-portable-bitmap");
/* 145 */     mappings.add("pct", "image/pict");
/* 146 */     mappings.add("pdf", "application/pdf");
/* 147 */     mappings.add("pgm", "image/x-portable-graymap");
/* 148 */     mappings.add("pic", "image/pict");
/* 149 */     mappings.add("pict", "image/pict");
/* 150 */     mappings.add("pls", "audio/x-scpls");
/* 151 */     mappings.add("png", "image/png");
/* 152 */     mappings.add("pnm", "image/x-portable-anymap");
/* 153 */     mappings.add("pnt", "image/x-macpaint");
/* 154 */     mappings.add("ppm", "image/x-portable-pixmap");
/* 155 */     mappings.add("ppt", "application/vnd.ms-powerpoint");
/* 156 */     mappings.add("pps", "application/vnd.ms-powerpoint");
/* 157 */     mappings.add("ps", "application/postscript");
/* 158 */     mappings.add("psd", "image/vnd.adobe.photoshop");
/* 159 */     mappings.add("qt", "video/quicktime");
/* 160 */     mappings.add("qti", "image/x-quicktime");
/* 161 */     mappings.add("qtif", "image/x-quicktime");
/* 162 */     mappings.add("ras", "image/x-cmu-raster");
/* 163 */     mappings.add("rdf", "application/rdf+xml");
/* 164 */     mappings.add("rgb", "image/x-rgb");
/* 165 */     mappings.add("rm", "application/vnd.rn-realmedia");
/* 166 */     mappings.add("roff", "text/troff");
/* 167 */     mappings.add("rtf", "application/rtf");
/* 168 */     mappings.add("rtx", "text/richtext");
/* 169 */     mappings.add("sfnt", "application/font-sfnt");
/* 170 */     mappings.add("sh", "application/x-sh");
/* 171 */     mappings.add("shar", "application/x-shar");
/* 172 */     mappings.add("sit", "application/x-stuffit");
/* 173 */     mappings.add("snd", "audio/basic");
/* 174 */     mappings.add("src", "application/x-wais-source");
/* 175 */     mappings.add("sv4cpio", "application/x-sv4cpio");
/* 176 */     mappings.add("sv4crc", "application/x-sv4crc");
/* 177 */     mappings.add("svg", "image/svg+xml");
/* 178 */     mappings.add("svgz", "image/svg+xml");
/* 179 */     mappings.add("swf", "application/x-shockwave-flash");
/* 180 */     mappings.add("t", "text/troff");
/* 181 */     mappings.add("tar", "application/x-tar");
/* 182 */     mappings.add("tcl", "application/x-tcl");
/* 183 */     mappings.add("tex", "application/x-tex");
/* 184 */     mappings.add("texi", "application/x-texinfo");
/* 185 */     mappings.add("texinfo", "application/x-texinfo");
/* 186 */     mappings.add("tif", "image/tiff");
/* 187 */     mappings.add("tiff", "image/tiff");
/* 188 */     mappings.add("tr", "text/troff");
/* 189 */     mappings.add("tsv", "text/tab-separated-values");
/* 190 */     mappings.add("ttf", "application/x-font-ttf");
/* 191 */     mappings.add("txt", "text/plain");
/* 192 */     mappings.add("ulw", "audio/basic");
/* 193 */     mappings.add("ustar", "application/x-ustar");
/* 194 */     mappings.add("vxml", "application/voicexml+xml");
/* 195 */     mappings.add("xbm", "image/x-xbitmap");
/* 196 */     mappings.add("xht", "application/xhtml+xml");
/* 197 */     mappings.add("xhtml", "application/xhtml+xml");
/* 198 */     mappings.add("xls", "application/vnd.ms-excel");
/* 199 */     mappings.add("xml", "application/xml");
/* 200 */     mappings.add("xpm", "image/x-xpixmap");
/* 201 */     mappings.add("xsl", "application/xml");
/* 202 */     mappings.add("xslt", "application/xslt+xml");
/* 203 */     mappings.add("xul", "application/vnd.mozilla.xul+xml");
/* 204 */     mappings.add("xwd", "image/x-xwindowdump");
/* 205 */     mappings.add("vsd", "application/vnd.visio");
/* 206 */     mappings.add("wav", "audio/x-wav");
/* 207 */     mappings.add("wbmp", "image/vnd.wap.wbmp");
/* 208 */     mappings.add("wml", "text/vnd.wap.wml");
/* 209 */     mappings.add("wmlc", "application/vnd.wap.wmlc");
/* 210 */     mappings.add("wmls", "text/vnd.wap.wmlsc");
/* 211 */     mappings.add("wmlscriptc", "application/vnd.wap.wmlscriptc");
/* 212 */     mappings.add("wmv", "video/x-ms-wmv");
/* 213 */     mappings.add("woff", "application/font-woff");
/* 214 */     mappings.add("woff2", "application/font-woff2");
/* 215 */     mappings.add("wrl", "model/vrml");
/* 216 */     mappings.add("wspolicy", "application/wspolicy+xml");
/* 217 */     mappings.add("z", "application/x-compress");
/* 218 */     mappings.add("zip", "application/zip");
/* 219 */     DEFAULT = unmodifiableMappings(mappings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeMappings()
/*     */   {
/* 228 */     this.map = new LinkedHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeMappings(MimeMappings mappings)
/*     */   {
/* 236 */     this(mappings, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MimeMappings(Map<String, String> mappings)
/*     */   {
/* 245 */     Assert.notNull(mappings, "Mappings must not be null");
/* 246 */     this.map = new LinkedHashMap();
/* 247 */     for (Map.Entry<String, String> entry : mappings.entrySet()) {
/* 248 */       add((String)entry.getKey(), (String)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MimeMappings(MimeMappings mappings, boolean mutable)
/*     */   {
/* 258 */     Assert.notNull(mappings, "Mappings must not be null");
/*     */     
/*     */ 
/* 261 */     this.map = (mutable ? new LinkedHashMap(mappings.map) : Collections.unmodifiableMap(mappings.map));
/*     */   }
/*     */   
/*     */   public Iterator<Mapping> iterator()
/*     */   {
/* 266 */     return getAll().iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Mapping> getAll()
/*     */   {
/* 274 */     return this.map.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String add(String extension, String mimeType)
/*     */   {
/* 284 */     Mapping previous = (Mapping)this.map.put(extension, new Mapping(extension, mimeType));
/* 285 */     return previous == null ? null : previous.getMimeType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String get(String extension)
/*     */   {
/* 294 */     Mapping mapping = (Mapping)this.map.get(extension);
/* 295 */     return mapping == null ? null : mapping.getMimeType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String remove(String extension)
/*     */   {
/* 304 */     Mapping previous = (Mapping)this.map.remove(extension);
/* 305 */     return previous == null ? null : previous.getMimeType();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 310 */     return this.map.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 315 */     if (obj == null) {
/* 316 */       return false;
/*     */     }
/* 318 */     if (obj == this) {
/* 319 */       return true;
/*     */     }
/* 321 */     if ((obj instanceof MimeMappings)) {
/* 322 */       MimeMappings other = (MimeMappings)obj;
/* 323 */       return this.map.equals(other.map);
/*     */     }
/* 325 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MimeMappings unmodifiableMappings(MimeMappings mappings)
/*     */   {
/* 335 */     return new MimeMappings(mappings, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public static final class Mapping
/*     */   {
/*     */     private final String extension;
/*     */     
/*     */     private final String mimeType;
/*     */     
/*     */ 
/*     */     public Mapping(String extension, String mimeType)
/*     */     {
/* 348 */       Assert.notNull(extension, "Extension must not be null");
/* 349 */       Assert.notNull(mimeType, "MimeType must not be null");
/* 350 */       this.extension = extension;
/* 351 */       this.mimeType = mimeType;
/*     */     }
/*     */     
/*     */     public String getExtension() {
/* 355 */       return this.extension;
/*     */     }
/*     */     
/*     */     public String getMimeType() {
/* 359 */       return this.mimeType;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 364 */       return this.extension.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 369 */       if (obj == null) {
/* 370 */         return false;
/*     */       }
/* 372 */       if (obj == this) {
/* 373 */         return true;
/*     */       }
/* 375 */       if ((obj instanceof Mapping)) {
/* 376 */         Mapping other = (Mapping)obj;
/* 377 */         return (this.extension.equals(other.extension)) && 
/* 378 */           (this.mimeType.equals(other.mimeType));
/*     */       }
/* 380 */       return false;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 385 */       return "Mapping [extension=" + this.extension + ", mimeType=" + this.mimeType + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\MimeMappings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */