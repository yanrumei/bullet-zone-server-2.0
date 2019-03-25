/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JOptCommandLinePropertySource
/*     */   extends CommandLinePropertySource<OptionSet>
/*     */ {
/*     */   public JOptCommandLinePropertySource(OptionSet options)
/*     */   {
/*  68 */     super(options);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JOptCommandLinePropertySource(String name, OptionSet options)
/*     */   {
/*  76 */     super(name, options);
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean containsOption(String name)
/*     */   {
/*  82 */     return ((OptionSet)this.source).has(name);
/*     */   }
/*     */   
/*     */   public String[] getPropertyNames()
/*     */   {
/*  87 */     List<String> names = new ArrayList();
/*  88 */     for (OptionSpec<?> spec : ((OptionSet)this.source).specs()) {
/*  89 */       List<String> aliases = spec.options();
/*  90 */       if (!aliases.isEmpty())
/*     */       {
/*  92 */         names.add(aliases.get(aliases.size() - 1));
/*     */       }
/*     */     }
/*  95 */     return StringUtils.toStringArray(names);
/*     */   }
/*     */   
/*     */   public List<String> getOptionValues(String name)
/*     */   {
/* 100 */     List<?> argValues = ((OptionSet)this.source).valuesOf(name);
/* 101 */     List<String> stringArgValues = new ArrayList();
/* 102 */     for (Object argValue : argValues) {
/* 103 */       stringArgValues.add(argValue.toString());
/*     */     }
/* 105 */     if (stringArgValues.isEmpty()) {
/* 106 */       return ((OptionSet)this.source).has(name) ? Collections.emptyList() : null;
/*     */     }
/* 108 */     return Collections.unmodifiableList(stringArgValues);
/*     */   }
/*     */   
/*     */   protected List<String> getNonOptionArgs()
/*     */   {
/* 113 */     List<?> argValues = ((OptionSet)this.source).nonOptionArguments();
/* 114 */     List<String> stringArgValues = new ArrayList();
/* 115 */     for (Object argValue : argValues) {
/* 116 */       stringArgValues.add(argValue.toString());
/*     */     }
/* 118 */     return stringArgValues.isEmpty() ? Collections.emptyList() : 
/* 119 */       Collections.unmodifiableList(stringArgValues);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\JOptCommandLinePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */