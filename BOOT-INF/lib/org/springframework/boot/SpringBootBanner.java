/*    */ package org.springframework.boot;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.springframework.boot.ansi.AnsiColor;
/*    */ import org.springframework.boot.ansi.AnsiOutput;
/*    */ import org.springframework.boot.ansi.AnsiStyle;
/*    */ import org.springframework.core.env.Environment;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class SpringBootBanner
/*    */   implements Banner
/*    */ {
/* 33 */   private static final String[] BANNER = { "", "  .   ____          _            __ _ _", " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\", "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\", " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )", "  '  |____| .__|_| |_|_| |_\\__, | / / / /", " =========|_|==============|___/=/_/_/_/" };
/*    */   
/*    */ 
/*    */ 
/*    */   private static final String SPRING_BOOT = " :: Spring Boot :: ";
/*    */   
/*    */ 
/*    */ 
/*    */   private static final int STRAP_LINE_SIZE = 42;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream)
/*    */   {
/* 48 */     for (String line : BANNER) {
/* 49 */       printStream.println(line);
/*    */     }
/* 51 */     String version = SpringBootVersion.getVersion();
/* 52 */     version = " (v" + version + ")";
/* 53 */     String padding = "";
/*    */     
/* 55 */     while (padding.length() < 42 - (version.length() + " :: Spring Boot :: ".length())) {
/* 56 */       padding = padding + " ";
/*    */     }
/*    */     
/* 59 */     printStream.println(AnsiOutput.toString(new Object[] { AnsiColor.GREEN, " :: Spring Boot :: ", AnsiColor.DEFAULT, padding, AnsiStyle.FAINT, version }));
/*    */     
/* 61 */     printStream.println();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\SpringBootBanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */