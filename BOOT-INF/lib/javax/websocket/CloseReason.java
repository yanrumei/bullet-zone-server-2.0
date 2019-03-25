/*     */ package javax.websocket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CloseReason
/*     */ {
/*     */   private final CloseCode closeCode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String reasonPhrase;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CloseReason(CloseCode closeCode, String reasonPhrase)
/*     */   {
/*  25 */     this.closeCode = closeCode;
/*  26 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */   
/*     */   public CloseCode getCloseCode() {
/*  30 */     return this.closeCode;
/*     */   }
/*     */   
/*     */   public String getReasonPhrase() {
/*  34 */     return this.reasonPhrase;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  39 */     return "CloseReason: code [" + this.closeCode.getCode() + "], reason [" + this.reasonPhrase + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum CloseCodes
/*     */     implements CloseReason.CloseCode
/*     */   {
/*  49 */     NORMAL_CLOSURE(1000), 
/*  50 */     GOING_AWAY(1001), 
/*  51 */     PROTOCOL_ERROR(1002), 
/*  52 */     CANNOT_ACCEPT(1003), 
/*  53 */     RESERVED(1004), 
/*  54 */     NO_STATUS_CODE(1005), 
/*  55 */     CLOSED_ABNORMALLY(1006), 
/*  56 */     NOT_CONSISTENT(1007), 
/*  57 */     VIOLATED_POLICY(1008), 
/*  58 */     TOO_BIG(1009), 
/*  59 */     NO_EXTENSION(1010), 
/*  60 */     UNEXPECTED_CONDITION(1011), 
/*  61 */     SERVICE_RESTART(1012), 
/*  62 */     TRY_AGAIN_LATER(1013), 
/*  63 */     TLS_HANDSHAKE_FAILURE(1015);
/*     */     
/*     */     private int code;
/*     */     
/*     */     private CloseCodes(int code) {
/*  68 */       this.code = code;
/*     */     }
/*     */     
/*     */     public static CloseReason.CloseCode getCloseCode(int code) {
/*  72 */       if ((code > 2999) && (code < 5000)) {
/*  73 */         new CloseReason.CloseCode()
/*     */         {
/*     */           public int getCode() {
/*  76 */             return this.val$code;
/*     */           }
/*     */         };
/*     */       }
/*  80 */       switch (code) {
/*     */       case 1000: 
/*  82 */         return NORMAL_CLOSURE;
/*     */       case 1001: 
/*  84 */         return GOING_AWAY;
/*     */       case 1002: 
/*  86 */         return PROTOCOL_ERROR;
/*     */       case 1003: 
/*  88 */         return CANNOT_ACCEPT;
/*     */       case 1004: 
/*  90 */         return RESERVED;
/*     */       case 1005: 
/*  92 */         return NO_STATUS_CODE;
/*     */       case 1006: 
/*  94 */         return CLOSED_ABNORMALLY;
/*     */       case 1007: 
/*  96 */         return NOT_CONSISTENT;
/*     */       case 1008: 
/*  98 */         return VIOLATED_POLICY;
/*     */       case 1009: 
/* 100 */         return TOO_BIG;
/*     */       case 1010: 
/* 102 */         return NO_EXTENSION;
/*     */       case 1011: 
/* 104 */         return UNEXPECTED_CONDITION;
/*     */       case 1012: 
/* 106 */         return SERVICE_RESTART;
/*     */       case 1013: 
/* 108 */         return TRY_AGAIN_LATER;
/*     */       case 1015: 
/* 110 */         return TLS_HANDSHAKE_FAILURE;
/*     */       }
/* 112 */       throw new IllegalArgumentException("Invalid close code: [" + code + "]");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int getCode()
/*     */     {
/* 119 */       return this.code;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface CloseCode
/*     */   {
/*     */     public abstract int getCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\javax\websocket\CloseReason.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */