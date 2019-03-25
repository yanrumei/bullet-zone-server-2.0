/*     */ package org.yaml.snakeyaml.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Resolver
/*     */ {
/*  32 */   public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */   public static final Pattern FLOAT = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
/*     */   
/*  41 */   public static final Pattern INT = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
/*     */   
/*  43 */   public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
/*  44 */   public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
/*  45 */   public static final Pattern EMPTY = Pattern.compile("^$");
/*  46 */   public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
/*     */   
/*  48 */   public static final Pattern VALUE = Pattern.compile("^(?:=)$");
/*  49 */   public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
/*     */   
/*  51 */   protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap();
/*     */   
/*     */   protected void addImplicitResolvers() {
/*  54 */     addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */     addImplicitResolver(Tag.INT, INT, "-+0123456789");
/*  61 */     addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
/*  62 */     addImplicitResolver(Tag.MERGE, MERGE, "<");
/*  63 */     addImplicitResolver(Tag.NULL, NULL, "~nN\000");
/*  64 */     addImplicitResolver(Tag.NULL, EMPTY, null);
/*  65 */     addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  70 */     addImplicitResolver(Tag.YAML, YAML, "!&*");
/*     */   }
/*     */   
/*     */   public Resolver() {
/*  74 */     addImplicitResolvers();
/*     */   }
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/*  78 */     if (first == null) {
/*  79 */       List<ResolverTuple> curr = (List)this.yamlImplicitResolvers.get(null);
/*  80 */       if (curr == null) {
/*  81 */         curr = new ArrayList();
/*  82 */         this.yamlImplicitResolvers.put(null, curr);
/*     */       }
/*  84 */       curr.add(new ResolverTuple(tag, regexp));
/*     */     } else {
/*  86 */       char[] chrs = first.toCharArray();
/*  87 */       int i = 0; for (int j = chrs.length; i < j; i++) {
/*  88 */         Character theC = Character.valueOf(chrs[i]);
/*  89 */         if (theC.charValue() == 0)
/*     */         {
/*  91 */           theC = null;
/*     */         }
/*  93 */         List<ResolverTuple> curr = (List)this.yamlImplicitResolvers.get(theC);
/*  94 */         if (curr == null) {
/*  95 */           curr = new ArrayList();
/*  96 */           this.yamlImplicitResolvers.put(theC, curr);
/*     */         }
/*  98 */         curr.add(new ResolverTuple(tag, regexp));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Tag resolve(NodeId kind, String value, boolean implicit) {
/* 104 */     if ((kind == NodeId.scalar) && (implicit)) {
/* 105 */       List<ResolverTuple> resolvers = null;
/* 106 */       if (value.length() == 0) {
/* 107 */         resolvers = (List)this.yamlImplicitResolvers.get(Character.valueOf('\000'));
/*     */       } else {
/* 109 */         resolvers = (List)this.yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
/*     */       }
/* 111 */       if (resolvers != null) {
/* 112 */         for (ResolverTuple v : resolvers) {
/* 113 */           Tag tag = v.getTag();
/* 114 */           Pattern regexp = v.getRegexp();
/* 115 */           if (regexp.matcher(value).matches()) {
/* 116 */             return tag;
/*     */           }
/*     */         }
/*     */       }
/* 120 */       if (this.yamlImplicitResolvers.containsKey(null)) {
/* 121 */         for (ResolverTuple v : (List)this.yamlImplicitResolvers.get(null)) {
/* 122 */           Tag tag = v.getTag();
/* 123 */           Pattern regexp = v.getRegexp();
/* 124 */           if (regexp.matcher(value).matches()) {
/* 125 */             return tag;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 130 */     switch (kind) {
/*     */     case scalar: 
/* 132 */       return Tag.STR;
/*     */     case sequence: 
/* 134 */       return Tag.SEQ;
/*     */     }
/* 136 */     return Tag.MAP;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\resolver\Resolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */