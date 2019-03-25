/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ class ExitCodeGenerators
/*     */   implements Iterable<ExitCodeGenerator>
/*     */ {
/*  37 */   private List<ExitCodeGenerator> generators = new ArrayList();
/*     */   
/*     */   public void addAll(Throwable exception, ExitCodeExceptionMapper... mappers) {
/*  40 */     Assert.notNull(exception, "Exception must not be null");
/*  41 */     Assert.notNull(mappers, "Mappers must not be null");
/*  42 */     addAll(exception, Arrays.asList(mappers));
/*     */   }
/*     */   
/*     */   public void addAll(Throwable exception, Iterable<? extends ExitCodeExceptionMapper> mappers)
/*     */   {
/*  47 */     Assert.notNull(exception, "Exception must not be null");
/*  48 */     Assert.notNull(mappers, "Mappers must not be null");
/*  49 */     for (ExitCodeExceptionMapper mapper : mappers) {
/*  50 */       add(exception, mapper);
/*     */     }
/*     */   }
/*     */   
/*     */   public void add(Throwable exception, ExitCodeExceptionMapper mapper) {
/*  55 */     Assert.notNull(exception, "Exception must not be null");
/*  56 */     Assert.notNull(mapper, "Mapper must not be null");
/*  57 */     add(new MappedExitCodeGenerator(exception, mapper));
/*     */   }
/*     */   
/*     */   public void addAll(ExitCodeGenerator... generators) {
/*  61 */     Assert.notNull(generators, "Generators must not be null");
/*  62 */     addAll(Arrays.asList(generators));
/*     */   }
/*     */   
/*     */   public void addAll(Iterable<? extends ExitCodeGenerator> generators) {
/*  66 */     Assert.notNull(generators, "Generators must not be null");
/*  67 */     for (ExitCodeGenerator generator : generators) {
/*  68 */       add(generator);
/*     */     }
/*     */   }
/*     */   
/*     */   public void add(ExitCodeGenerator generator) {
/*  73 */     Assert.notNull(generator, "Generator must not be null");
/*  74 */     this.generators.add(generator);
/*     */   }
/*     */   
/*     */   public Iterator<ExitCodeGenerator> iterator()
/*     */   {
/*  79 */     return this.generators.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getExitCode()
/*     */   {
/*  87 */     int exitCode = 0;
/*  88 */     for (ExitCodeGenerator generator : this.generators) {
/*     */       try {
/*  90 */         int value = generator.getExitCode();
/*  91 */         if (((value > 0) && (value > exitCode)) || ((value < 0) && (value < exitCode))) {
/*  92 */           exitCode = value;
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/*  96 */         exitCode = exitCode == 0 ? 1 : exitCode;
/*  97 */         ex.printStackTrace();
/*     */       }
/*     */     }
/* 100 */     return exitCode;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MappedExitCodeGenerator
/*     */     implements ExitCodeGenerator
/*     */   {
/*     */     private final Throwable exception;
/*     */     
/*     */     private final ExitCodeExceptionMapper mapper;
/*     */     
/*     */     MappedExitCodeGenerator(Throwable exception, ExitCodeExceptionMapper mapper)
/*     */     {
/* 113 */       this.exception = exception;
/* 114 */       this.mapper = mapper;
/*     */     }
/*     */     
/*     */     public int getExitCode()
/*     */     {
/* 119 */       return this.mapper.getExitCode(this.exception);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ExitCodeGenerators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */