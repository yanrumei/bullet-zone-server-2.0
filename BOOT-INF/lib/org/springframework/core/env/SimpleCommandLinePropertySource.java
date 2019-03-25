/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleCommandLinePropertySource
/*     */   extends CommandLinePropertySource<CommandLineArgs>
/*     */ {
/*     */   public SimpleCommandLinePropertySource(String... args)
/*     */   {
/*  87 */     super(new SimpleCommandLineArgsParser().parse(args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleCommandLinePropertySource(String name, String[] args)
/*     */   {
/*  95 */     super(name, new SimpleCommandLineArgsParser().parse(args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getPropertyNames()
/*     */   {
/* 103 */     return (String[])((CommandLineArgs)this.source).getOptionNames().toArray(new String[((CommandLineArgs)this.source).getOptionNames().size()]);
/*     */   }
/*     */   
/*     */   protected boolean containsOption(String name)
/*     */   {
/* 108 */     return ((CommandLineArgs)this.source).containsOption(name);
/*     */   }
/*     */   
/*     */   protected List<String> getOptionValues(String name)
/*     */   {
/* 113 */     return ((CommandLineArgs)this.source).getOptionValues(name);
/*     */   }
/*     */   
/*     */   protected List<String> getNonOptionArgs()
/*     */   {
/* 118 */     return ((CommandLineArgs)this.source).getNonOptionArgs();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\SimpleCommandLinePropertySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */