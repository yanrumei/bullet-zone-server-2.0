/*     */ package org.springframework.boot.ansi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.env.PropertySource;
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
/*     */ public class AnsiPropertySource
/*     */   extends PropertySource<AnsiElement>
/*     */ {
/*     */   private static final Iterable<MappedEnum<?>> MAPPED_ENUMS;
/*     */   private final boolean encode;
/*     */   
/*     */   static
/*     */   {
/*  44 */     List<MappedEnum<?>> enums = new ArrayList();
/*  45 */     enums.add(new MappedEnum("AnsiStyle.", AnsiStyle.class));
/*  46 */     enums.add(new MappedEnum("AnsiColor.", AnsiColor.class));
/*  47 */     enums.add(new MappedEnum("AnsiBackground.", AnsiBackground.class));
/*     */     
/*  49 */     enums.add(new MappedEnum("Ansi.", AnsiStyle.class));
/*  50 */     enums.add(new MappedEnum("Ansi.", AnsiColor.class));
/*  51 */     enums.add(new MappedEnum("Ansi.BG_", AnsiBackground.class));
/*  52 */     MAPPED_ENUMS = Collections.unmodifiableList(enums);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnsiPropertySource(String name, boolean encode)
/*     */   {
/*  63 */     super(name);
/*  64 */     this.encode = encode;
/*     */   }
/*     */   
/*     */   public Object getProperty(String name)
/*     */   {
/*  69 */     if (StringUtils.hasLength(name)) {
/*  70 */       for (MappedEnum<?> mappedEnum : MAPPED_ENUMS) {
/*  71 */         if (name.startsWith(mappedEnum.getPrefix())) {
/*  72 */           enumName = name.substring(mappedEnum.getPrefix().length());
/*  73 */           for (Enum<?> ansiEnum : mappedEnum.getEnums())
/*  74 */             if (ansiEnum.name().equals(enumName)) {
/*  75 */               if (this.encode) {
/*  76 */                 return AnsiOutput.encode((AnsiElement)ansiEnum);
/*     */               }
/*  78 */               return ansiEnum;
/*     */             }
/*     */         }
/*     */       }
/*     */     }
/*     */     String enumName;
/*  84 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MappedEnum<E extends Enum<E>>
/*     */   {
/*     */     private final String prefix;
/*     */     
/*     */     private final Set<E> enums;
/*     */     
/*     */ 
/*     */     MappedEnum(String prefix, Class<E> enumType)
/*     */     {
/*  97 */       this.prefix = prefix;
/*  98 */       this.enums = EnumSet.allOf(enumType);
/*     */     }
/*     */     
/*     */     public String getPrefix()
/*     */     {
/* 103 */       return this.prefix;
/*     */     }
/*     */     
/*     */     public Set<E> getEnums() {
/* 107 */       return this.enums;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ansi\AnsiPropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */