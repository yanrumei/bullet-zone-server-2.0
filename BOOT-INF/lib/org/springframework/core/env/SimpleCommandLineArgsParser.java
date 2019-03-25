/*    */ package org.springframework.core.env;
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
/*    */ class SimpleCommandLineArgsParser
/*    */ {
/*    */   public CommandLineArgs parse(String... args)
/*    */   {
/* 61 */     CommandLineArgs commandLineArgs = new CommandLineArgs();
/* 62 */     for (String arg : args) {
/* 63 */       if (arg.startsWith("--")) {
/* 64 */         String optionText = arg.substring(2, arg.length());
/*    */         
/* 66 */         String optionValue = null;
/* 67 */         String optionName; if (optionText.contains("=")) {
/* 68 */           String optionName = optionText.substring(0, optionText.indexOf("="));
/* 69 */           optionValue = optionText.substring(optionText.indexOf("=") + 1, optionText.length());
/*    */         }
/*    */         else {
/* 72 */           optionName = optionText;
/*    */         }
/* 74 */         if ((optionName.isEmpty()) || ((optionValue != null) && (optionValue.isEmpty()))) {
/* 75 */           throw new IllegalArgumentException("Invalid argument syntax: " + arg);
/*    */         }
/* 77 */         commandLineArgs.addOptionArg(optionName, optionValue);
/*    */       }
/*    */       else {
/* 80 */         commandLineArgs.addNonOptionArg(arg);
/*    */       }
/*    */     }
/* 83 */     return commandLineArgs;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\env\SimpleCommandLineArgsParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */