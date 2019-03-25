/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HPackHuffman
/*     */ {
/*     */   protected static final StringManager sm;
/*     */   private static final HuffmanCode[] HUFFMAN_CODES;
/*     */   private static final int[] DECODING_TABLE;
/*     */   private static final int LOW_TERMINAL_BIT = 32768;
/*     */   private static final int HIGH_TERMINAL_BIT = Integer.MIN_VALUE;
/*     */   private static final int LOW_MASK = 32767;
/*     */   
/*     */   static
/*     */   {
/*  28 */     sm = StringManager.getManager(HPackHuffman.class);
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
/*  47 */     HuffmanCode[] codes = new HuffmanCode['ā'];
/*     */     
/*  49 */     codes[0] = new HuffmanCode(8184, 13);
/*  50 */     codes[1] = new HuffmanCode(8388568, 23);
/*  51 */     codes[2] = new HuffmanCode(268435426, 28);
/*  52 */     codes[3] = new HuffmanCode(268435427, 28);
/*  53 */     codes[4] = new HuffmanCode(268435428, 28);
/*  54 */     codes[5] = new HuffmanCode(268435429, 28);
/*  55 */     codes[6] = new HuffmanCode(268435430, 28);
/*  56 */     codes[7] = new HuffmanCode(268435431, 28);
/*  57 */     codes[8] = new HuffmanCode(268435432, 28);
/*  58 */     codes[9] = new HuffmanCode(16777194, 24);
/*  59 */     codes[10] = new HuffmanCode(1073741820, 30);
/*  60 */     codes[11] = new HuffmanCode(268435433, 28);
/*  61 */     codes[12] = new HuffmanCode(268435434, 28);
/*  62 */     codes[13] = new HuffmanCode(1073741821, 30);
/*  63 */     codes[14] = new HuffmanCode(268435435, 28);
/*  64 */     codes[15] = new HuffmanCode(268435436, 28);
/*  65 */     codes[16] = new HuffmanCode(268435437, 28);
/*  66 */     codes[17] = new HuffmanCode(268435438, 28);
/*  67 */     codes[18] = new HuffmanCode(268435439, 28);
/*  68 */     codes[19] = new HuffmanCode(268435440, 28);
/*  69 */     codes[20] = new HuffmanCode(268435441, 28);
/*  70 */     codes[21] = new HuffmanCode(268435442, 28);
/*  71 */     codes[22] = new HuffmanCode(1073741822, 30);
/*  72 */     codes[23] = new HuffmanCode(268435443, 28);
/*  73 */     codes[24] = new HuffmanCode(268435444, 28);
/*  74 */     codes[25] = new HuffmanCode(268435445, 28);
/*  75 */     codes[26] = new HuffmanCode(268435446, 28);
/*  76 */     codes[27] = new HuffmanCode(268435447, 28);
/*  77 */     codes[28] = new HuffmanCode(268435448, 28);
/*  78 */     codes[29] = new HuffmanCode(268435449, 28);
/*  79 */     codes[30] = new HuffmanCode(268435450, 28);
/*  80 */     codes[31] = new HuffmanCode(268435451, 28);
/*  81 */     codes[32] = new HuffmanCode(20, 6);
/*  82 */     codes[33] = new HuffmanCode(1016, 10);
/*  83 */     codes[34] = new HuffmanCode(1017, 10);
/*  84 */     codes[35] = new HuffmanCode(4090, 12);
/*  85 */     codes[36] = new HuffmanCode(8185, 13);
/*  86 */     codes[37] = new HuffmanCode(21, 6);
/*  87 */     codes[38] = new HuffmanCode(248, 8);
/*  88 */     codes[39] = new HuffmanCode(2042, 11);
/*  89 */     codes[40] = new HuffmanCode(1018, 10);
/*  90 */     codes[41] = new HuffmanCode(1019, 10);
/*  91 */     codes[42] = new HuffmanCode(249, 8);
/*  92 */     codes[43] = new HuffmanCode(2043, 11);
/*  93 */     codes[44] = new HuffmanCode(250, 8);
/*  94 */     codes[45] = new HuffmanCode(22, 6);
/*  95 */     codes[46] = new HuffmanCode(23, 6);
/*  96 */     codes[47] = new HuffmanCode(24, 6);
/*  97 */     codes[48] = new HuffmanCode(0, 5);
/*  98 */     codes[49] = new HuffmanCode(1, 5);
/*  99 */     codes[50] = new HuffmanCode(2, 5);
/* 100 */     codes[51] = new HuffmanCode(25, 6);
/* 101 */     codes[52] = new HuffmanCode(26, 6);
/* 102 */     codes[53] = new HuffmanCode(27, 6);
/* 103 */     codes[54] = new HuffmanCode(28, 6);
/* 104 */     codes[55] = new HuffmanCode(29, 6);
/* 105 */     codes[56] = new HuffmanCode(30, 6);
/* 106 */     codes[57] = new HuffmanCode(31, 6);
/* 107 */     codes[58] = new HuffmanCode(92, 7);
/* 108 */     codes[59] = new HuffmanCode(251, 8);
/* 109 */     codes[60] = new HuffmanCode(32764, 15);
/* 110 */     codes[61] = new HuffmanCode(32, 6);
/* 111 */     codes[62] = new HuffmanCode(4091, 12);
/* 112 */     codes[63] = new HuffmanCode(1020, 10);
/* 113 */     codes[64] = new HuffmanCode(8186, 13);
/* 114 */     codes[65] = new HuffmanCode(33, 6);
/* 115 */     codes[66] = new HuffmanCode(93, 7);
/* 116 */     codes[67] = new HuffmanCode(94, 7);
/* 117 */     codes[68] = new HuffmanCode(95, 7);
/* 118 */     codes[69] = new HuffmanCode(96, 7);
/* 119 */     codes[70] = new HuffmanCode(97, 7);
/* 120 */     codes[71] = new HuffmanCode(98, 7);
/* 121 */     codes[72] = new HuffmanCode(99, 7);
/* 122 */     codes[73] = new HuffmanCode(100, 7);
/* 123 */     codes[74] = new HuffmanCode(101, 7);
/* 124 */     codes[75] = new HuffmanCode(102, 7);
/* 125 */     codes[76] = new HuffmanCode(103, 7);
/* 126 */     codes[77] = new HuffmanCode(104, 7);
/* 127 */     codes[78] = new HuffmanCode(105, 7);
/* 128 */     codes[79] = new HuffmanCode(106, 7);
/* 129 */     codes[80] = new HuffmanCode(107, 7);
/* 130 */     codes[81] = new HuffmanCode(108, 7);
/* 131 */     codes[82] = new HuffmanCode(109, 7);
/* 132 */     codes[83] = new HuffmanCode(110, 7);
/* 133 */     codes[84] = new HuffmanCode(111, 7);
/* 134 */     codes[85] = new HuffmanCode(112, 7);
/* 135 */     codes[86] = new HuffmanCode(113, 7);
/* 136 */     codes[87] = new HuffmanCode(114, 7);
/* 137 */     codes[88] = new HuffmanCode(252, 8);
/* 138 */     codes[89] = new HuffmanCode(115, 7);
/* 139 */     codes[90] = new HuffmanCode(253, 8);
/* 140 */     codes[91] = new HuffmanCode(8187, 13);
/* 141 */     codes[92] = new HuffmanCode(524272, 19);
/* 142 */     codes[93] = new HuffmanCode(8188, 13);
/* 143 */     codes[94] = new HuffmanCode(16380, 14);
/* 144 */     codes[95] = new HuffmanCode(34, 6);
/* 145 */     codes[96] = new HuffmanCode(32765, 15);
/* 146 */     codes[97] = new HuffmanCode(3, 5);
/* 147 */     codes[98] = new HuffmanCode(35, 6);
/* 148 */     codes[99] = new HuffmanCode(4, 5);
/* 149 */     codes[100] = new HuffmanCode(36, 6);
/* 150 */     codes[101] = new HuffmanCode(5, 5);
/* 151 */     codes[102] = new HuffmanCode(37, 6);
/* 152 */     codes[103] = new HuffmanCode(38, 6);
/* 153 */     codes[104] = new HuffmanCode(39, 6);
/* 154 */     codes[105] = new HuffmanCode(6, 5);
/* 155 */     codes[106] = new HuffmanCode(116, 7);
/* 156 */     codes[107] = new HuffmanCode(117, 7);
/* 157 */     codes[108] = new HuffmanCode(40, 6);
/* 158 */     codes[109] = new HuffmanCode(41, 6);
/* 159 */     codes[110] = new HuffmanCode(42, 6);
/* 160 */     codes[111] = new HuffmanCode(7, 5);
/* 161 */     codes[112] = new HuffmanCode(43, 6);
/* 162 */     codes[113] = new HuffmanCode(118, 7);
/* 163 */     codes[114] = new HuffmanCode(44, 6);
/* 164 */     codes[115] = new HuffmanCode(8, 5);
/* 165 */     codes[116] = new HuffmanCode(9, 5);
/* 166 */     codes[117] = new HuffmanCode(45, 6);
/* 167 */     codes[118] = new HuffmanCode(119, 7);
/* 168 */     codes[119] = new HuffmanCode(120, 7);
/* 169 */     codes[120] = new HuffmanCode(121, 7);
/* 170 */     codes[121] = new HuffmanCode(122, 7);
/* 171 */     codes[122] = new HuffmanCode(123, 7);
/* 172 */     codes[123] = new HuffmanCode(32766, 15);
/* 173 */     codes[124] = new HuffmanCode(2044, 11);
/* 174 */     codes[125] = new HuffmanCode(16381, 14);
/* 175 */     codes[126] = new HuffmanCode(8189, 13);
/* 176 */     codes[127] = new HuffmanCode(268435452, 28);
/* 177 */     codes[''] = new HuffmanCode(1048550, 20);
/* 178 */     codes[''] = new HuffmanCode(4194258, 22);
/* 179 */     codes[''] = new HuffmanCode(1048551, 20);
/* 180 */     codes[''] = new HuffmanCode(1048552, 20);
/* 181 */     codes[''] = new HuffmanCode(4194259, 22);
/* 182 */     codes[''] = new HuffmanCode(4194260, 22);
/* 183 */     codes[''] = new HuffmanCode(4194261, 22);
/* 184 */     codes[''] = new HuffmanCode(8388569, 23);
/* 185 */     codes[''] = new HuffmanCode(4194262, 22);
/* 186 */     codes[''] = new HuffmanCode(8388570, 23);
/* 187 */     codes[''] = new HuffmanCode(8388571, 23);
/* 188 */     codes[''] = new HuffmanCode(8388572, 23);
/* 189 */     codes[''] = new HuffmanCode(8388573, 23);
/* 190 */     codes[''] = new HuffmanCode(8388574, 23);
/* 191 */     codes[''] = new HuffmanCode(16777195, 24);
/* 192 */     codes[''] = new HuffmanCode(8388575, 23);
/* 193 */     codes[''] = new HuffmanCode(16777196, 24);
/* 194 */     codes[''] = new HuffmanCode(16777197, 24);
/* 195 */     codes[''] = new HuffmanCode(4194263, 22);
/* 196 */     codes[''] = new HuffmanCode(8388576, 23);
/* 197 */     codes[''] = new HuffmanCode(16777198, 24);
/* 198 */     codes[''] = new HuffmanCode(8388577, 23);
/* 199 */     codes[''] = new HuffmanCode(8388578, 23);
/* 200 */     codes[''] = new HuffmanCode(8388579, 23);
/* 201 */     codes[''] = new HuffmanCode(8388580, 23);
/* 202 */     codes[''] = new HuffmanCode(2097116, 21);
/* 203 */     codes[''] = new HuffmanCode(4194264, 22);
/* 204 */     codes[''] = new HuffmanCode(8388581, 23);
/* 205 */     codes[''] = new HuffmanCode(4194265, 22);
/* 206 */     codes[''] = new HuffmanCode(8388582, 23);
/* 207 */     codes[''] = new HuffmanCode(8388583, 23);
/* 208 */     codes[''] = new HuffmanCode(16777199, 24);
/* 209 */     codes[' '] = new HuffmanCode(4194266, 22);
/* 210 */     codes['¡'] = new HuffmanCode(2097117, 21);
/* 211 */     codes['¢'] = new HuffmanCode(1048553, 20);
/* 212 */     codes['£'] = new HuffmanCode(4194267, 22);
/* 213 */     codes['¤'] = new HuffmanCode(4194268, 22);
/* 214 */     codes['¥'] = new HuffmanCode(8388584, 23);
/* 215 */     codes['¦'] = new HuffmanCode(8388585, 23);
/* 216 */     codes['§'] = new HuffmanCode(2097118, 21);
/* 217 */     codes['¨'] = new HuffmanCode(8388586, 23);
/* 218 */     codes['©'] = new HuffmanCode(4194269, 22);
/* 219 */     codes['ª'] = new HuffmanCode(4194270, 22);
/* 220 */     codes['«'] = new HuffmanCode(16777200, 24);
/* 221 */     codes['¬'] = new HuffmanCode(2097119, 21);
/* 222 */     codes['­'] = new HuffmanCode(4194271, 22);
/* 223 */     codes['®'] = new HuffmanCode(8388587, 23);
/* 224 */     codes['¯'] = new HuffmanCode(8388588, 23);
/* 225 */     codes['°'] = new HuffmanCode(2097120, 21);
/* 226 */     codes['±'] = new HuffmanCode(2097121, 21);
/* 227 */     codes['²'] = new HuffmanCode(4194272, 22);
/* 228 */     codes['³'] = new HuffmanCode(2097122, 21);
/* 229 */     codes['´'] = new HuffmanCode(8388589, 23);
/* 230 */     codes['µ'] = new HuffmanCode(4194273, 22);
/* 231 */     codes['¶'] = new HuffmanCode(8388590, 23);
/* 232 */     codes['·'] = new HuffmanCode(8388591, 23);
/* 233 */     codes['¸'] = new HuffmanCode(1048554, 20);
/* 234 */     codes['¹'] = new HuffmanCode(4194274, 22);
/* 235 */     codes['º'] = new HuffmanCode(4194275, 22);
/* 236 */     codes['»'] = new HuffmanCode(4194276, 22);
/* 237 */     codes['¼'] = new HuffmanCode(8388592, 23);
/* 238 */     codes['½'] = new HuffmanCode(4194277, 22);
/* 239 */     codes['¾'] = new HuffmanCode(4194278, 22);
/* 240 */     codes['¿'] = new HuffmanCode(8388593, 23);
/* 241 */     codes['À'] = new HuffmanCode(67108832, 26);
/* 242 */     codes['Á'] = new HuffmanCode(67108833, 26);
/* 243 */     codes['Â'] = new HuffmanCode(1048555, 20);
/* 244 */     codes['Ã'] = new HuffmanCode(524273, 19);
/* 245 */     codes['Ä'] = new HuffmanCode(4194279, 22);
/* 246 */     codes['Å'] = new HuffmanCode(8388594, 23);
/* 247 */     codes['Æ'] = new HuffmanCode(4194280, 22);
/* 248 */     codes['Ç'] = new HuffmanCode(33554412, 25);
/* 249 */     codes['È'] = new HuffmanCode(67108834, 26);
/* 250 */     codes['É'] = new HuffmanCode(67108835, 26);
/* 251 */     codes['Ê'] = new HuffmanCode(67108836, 26);
/* 252 */     codes['Ë'] = new HuffmanCode(134217694, 27);
/* 253 */     codes['Ì'] = new HuffmanCode(134217695, 27);
/* 254 */     codes['Í'] = new HuffmanCode(67108837, 26);
/* 255 */     codes['Î'] = new HuffmanCode(16777201, 24);
/* 256 */     codes['Ï'] = new HuffmanCode(33554413, 25);
/* 257 */     codes['Ð'] = new HuffmanCode(524274, 19);
/* 258 */     codes['Ñ'] = new HuffmanCode(2097123, 21);
/* 259 */     codes['Ò'] = new HuffmanCode(67108838, 26);
/* 260 */     codes['Ó'] = new HuffmanCode(134217696, 27);
/* 261 */     codes['Ô'] = new HuffmanCode(134217697, 27);
/* 262 */     codes['Õ'] = new HuffmanCode(67108839, 26);
/* 263 */     codes['Ö'] = new HuffmanCode(134217698, 27);
/* 264 */     codes['×'] = new HuffmanCode(16777202, 24);
/* 265 */     codes['Ø'] = new HuffmanCode(2097124, 21);
/* 266 */     codes['Ù'] = new HuffmanCode(2097125, 21);
/* 267 */     codes['Ú'] = new HuffmanCode(67108840, 26);
/* 268 */     codes['Û'] = new HuffmanCode(67108841, 26);
/* 269 */     codes['Ü'] = new HuffmanCode(268435453, 28);
/* 270 */     codes['Ý'] = new HuffmanCode(134217699, 27);
/* 271 */     codes['Þ'] = new HuffmanCode(134217700, 27);
/* 272 */     codes['ß'] = new HuffmanCode(134217701, 27);
/* 273 */     codes['à'] = new HuffmanCode(1048556, 20);
/* 274 */     codes['á'] = new HuffmanCode(16777203, 24);
/* 275 */     codes['â'] = new HuffmanCode(1048557, 20);
/* 276 */     codes['ã'] = new HuffmanCode(2097126, 21);
/* 277 */     codes['ä'] = new HuffmanCode(4194281, 22);
/* 278 */     codes['å'] = new HuffmanCode(2097127, 21);
/* 279 */     codes['æ'] = new HuffmanCode(2097128, 21);
/* 280 */     codes['ç'] = new HuffmanCode(8388595, 23);
/* 281 */     codes['è'] = new HuffmanCode(4194282, 22);
/* 282 */     codes['é'] = new HuffmanCode(4194283, 22);
/* 283 */     codes['ê'] = new HuffmanCode(33554414, 25);
/* 284 */     codes['ë'] = new HuffmanCode(33554415, 25);
/* 285 */     codes['ì'] = new HuffmanCode(16777204, 24);
/* 286 */     codes['í'] = new HuffmanCode(16777205, 24);
/* 287 */     codes['î'] = new HuffmanCode(67108842, 26);
/* 288 */     codes['ï'] = new HuffmanCode(8388596, 23);
/* 289 */     codes['ð'] = new HuffmanCode(67108843, 26);
/* 290 */     codes['ñ'] = new HuffmanCode(134217702, 27);
/* 291 */     codes['ò'] = new HuffmanCode(67108844, 26);
/* 292 */     codes['ó'] = new HuffmanCode(67108845, 26);
/* 293 */     codes['ô'] = new HuffmanCode(134217703, 27);
/* 294 */     codes['õ'] = new HuffmanCode(134217704, 27);
/* 295 */     codes['ö'] = new HuffmanCode(134217705, 27);
/* 296 */     codes['÷'] = new HuffmanCode(134217706, 27);
/* 297 */     codes['ø'] = new HuffmanCode(134217707, 27);
/* 298 */     codes['ù'] = new HuffmanCode(268435454, 28);
/* 299 */     codes['ú'] = new HuffmanCode(134217708, 27);
/* 300 */     codes['û'] = new HuffmanCode(134217709, 27);
/* 301 */     codes['ü'] = new HuffmanCode(134217710, 27);
/* 302 */     codes['ý'] = new HuffmanCode(134217711, 27);
/* 303 */     codes['þ'] = new HuffmanCode(134217712, 27);
/* 304 */     codes['ÿ'] = new HuffmanCode(67108846, 26);
/* 305 */     codes['Ā'] = new HuffmanCode(1073741823, 30);
/* 306 */     HUFFMAN_CODES = codes;
/*     */     
/*     */ 
/* 309 */     int[] codingTree = new int['Ā'];
/*     */     
/* 311 */     int pos = 0;
/* 312 */     int allocated = 1;
/*     */     
/*     */ 
/* 315 */     HuffmanCode[] currentCode = new HuffmanCode['Ā'];
/* 316 */     currentCode[0] = new HuffmanCode(0, 0);
/*     */     
/* 318 */     Set<HuffmanCode> allCodes = new HashSet();
/* 319 */     allCodes.addAll(Arrays.asList(HUFFMAN_CODES));
/*     */     
/* 321 */     while (!allCodes.isEmpty()) {
/* 322 */       int length = currentCode[pos].length;
/* 323 */       int code = currentCode[pos].value;
/*     */       
/* 325 */       int newLength = length + 1;
/* 326 */       HuffmanCode high = new HuffmanCode(code << 1 | 0x1, newLength);
/* 327 */       HuffmanCode low = new HuffmanCode(code << 1, newLength);
/* 328 */       int newVal = 0;
/* 329 */       boolean highTerminal = allCodes.remove(high);
/* 330 */       if (highTerminal)
/*     */       {
/* 332 */         int i = 0;
/* 333 */         for (i = 0; i < codes.length; i++) {
/* 334 */           if (codes[i].equals(high)) {
/*     */             break;
/*     */           }
/*     */         }
/* 338 */         newVal = 0x8000 | i;
/*     */       } else {
/* 340 */         int highPos = allocated++;
/* 341 */         currentCode[highPos] = high;
/* 342 */         newVal = highPos;
/*     */       }
/* 344 */       newVal <<= 16;
/* 345 */       boolean lowTerminal = allCodes.remove(low);
/* 346 */       if (lowTerminal)
/*     */       {
/* 348 */         int i = 0;
/* 349 */         for (i = 0; i < codes.length; i++) {
/* 350 */           if (codes[i].equals(low)) {
/*     */             break;
/*     */           }
/*     */         }
/* 354 */         newVal |= 0x8000 | i;
/*     */       } else {
/* 356 */         int lowPos = allocated++;
/* 357 */         currentCode[lowPos] = low;
/* 358 */         newVal |= lowPos;
/*     */       }
/* 360 */       codingTree[pos] = newVal;
/* 361 */       pos++;
/*     */     }
/* 363 */     DECODING_TABLE = codingTree;
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
/*     */   public static void decode(ByteBuffer data, int length, StringBuilder target)
/*     */     throws HpackException
/*     */   {
/* 379 */     assert (data.remaining() >= length);
/* 380 */     int treePos = 0;
/* 381 */     boolean eosBits = true;
/* 382 */     int eosBitCount = 0;
/* 383 */     for (int i = 0; i < length; i++) {
/* 384 */       byte b = data.get();
/* 385 */       int bitPos = 7;
/* 386 */       while (bitPos >= 0) {
/* 387 */         int val = DECODING_TABLE[treePos];
/* 388 */         if ((1 << bitPos & b) == 0)
/*     */         {
/* 390 */           if ((val & 0x8000) == 0) {
/* 391 */             treePos = val & 0x7FFF;
/* 392 */             eosBits = false;
/* 393 */             eosBitCount = 0;
/*     */           } else {
/* 395 */             target.append((char)(val & 0x7FFF));
/* 396 */             treePos = 0;
/* 397 */             eosBits = true;
/*     */           }
/*     */         } else {
/* 400 */           if (eosBits) {
/* 401 */             eosBitCount++;
/*     */           }
/*     */           
/* 404 */           if ((val & 0x80000000) == 0) {
/* 405 */             treePos = val >> 16 & 0x7FFF;
/*     */           } else {
/* 407 */             target.append((char)(val >> 16 & 0x7FFF));
/* 408 */             treePos = 0;
/* 409 */             eosBits = true;
/*     */           }
/*     */         }
/* 412 */         bitPos--;
/*     */       }
/*     */     }
/* 415 */     if (eosBitCount > 7) {
/* 416 */       throw new HpackException(sm.getString("hpackhuffman.stringLiteralTooMuchPadding"));
/*     */     }
/*     */     
/* 419 */     if (!eosBits) {
/* 420 */       throw new HpackException(sm.getString("hpackhuffman.huffmanEncodedHpackValueDidNotEndWithEOS"));
/*     */     }
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
/*     */   public static boolean encode(ByteBuffer buffer, String toEncode, boolean forceLowercase)
/*     */   {
/* 437 */     if (buffer.remaining() <= toEncode.length()) {
/* 438 */       return false;
/*     */     }
/* 440 */     int start = buffer.position();
/*     */     
/*     */ 
/*     */ 
/* 444 */     int length = 0;
/* 445 */     for (int i = 0; i < toEncode.length(); i++) {
/* 446 */       char c = toEncode.charAt(i);
/* 447 */       if (c > 'ÿ') {
/* 448 */         throw new IllegalArgumentException(sm.getString("hpack.invalidCharacter", new Object[] {
/* 449 */           Character.toString(c), Integer.valueOf(c) }));
/*     */       }
/* 451 */       if (forceLowercase) {
/* 452 */         c = Hpack.toLower(c);
/*     */       }
/* 454 */       HuffmanCode code = HUFFMAN_CODES[c];
/* 455 */       length += code.length;
/*     */     }
/* 457 */     int byteLength = length / 8 + (length % 8 == 0 ? 0 : 1);
/*     */     
/* 459 */     buffer.put((byte)Byte.MIN_VALUE);
/* 460 */     Hpack.encodeInteger(buffer, byteLength, 7);
/*     */     
/*     */ 
/* 463 */     int bytePos = 0;
/* 464 */     byte currentBufferByte = 0;
/* 465 */     for (int i = 0; i < toEncode.length(); i++) {
/* 466 */       char c = toEncode.charAt(i);
/* 467 */       if (forceLowercase) {
/* 468 */         c = Hpack.toLower(c);
/*     */       }
/* 470 */       HuffmanCode code = HUFFMAN_CODES[c];
/* 471 */       if (code.length + bytePos <= 8)
/*     */       {
/* 473 */         currentBufferByte = (byte)(currentBufferByte | (code.value & 0xFF) << 8 - (code.length + bytePos));
/* 474 */         bytePos += code.length;
/*     */       }
/*     */       else {
/* 477 */         int val = code.value;
/* 478 */         int rem = code.length;
/* 479 */         while (rem > 0) {
/* 480 */           if (!buffer.hasRemaining()) {
/* 481 */             buffer.position(start);
/* 482 */             return false;
/*     */           }
/* 484 */           int remainingInByte = 8 - bytePos;
/* 485 */           if (rem > remainingInByte) {
/* 486 */             currentBufferByte = (byte)(currentBufferByte | val >> rem - remainingInByte);
/*     */           } else {
/* 488 */             currentBufferByte = (byte)(currentBufferByte | val << remainingInByte - rem);
/*     */           }
/* 490 */           if (rem > remainingInByte) {
/* 491 */             buffer.put(currentBufferByte);
/* 492 */             currentBufferByte = 0;
/* 493 */             bytePos = 0;
/*     */           } else {
/* 495 */             bytePos = rem;
/*     */           }
/* 497 */           rem -= remainingInByte;
/*     */         }
/*     */       }
/* 500 */       if (bytePos == 8) {
/* 501 */         if (!buffer.hasRemaining()) {
/* 502 */           buffer.position(start);
/* 503 */           return false;
/*     */         }
/* 505 */         buffer.put(currentBufferByte);
/* 506 */         currentBufferByte = 0;
/* 507 */         bytePos = 0;
/*     */       }
/* 509 */       if (buffer.position() - start > toEncode.length())
/*     */       {
/*     */ 
/* 512 */         buffer.position(start);
/* 513 */         return false;
/*     */       }
/*     */     }
/* 516 */     if (bytePos > 0)
/*     */     {
/* 518 */       if (!buffer.hasRemaining()) {
/* 519 */         buffer.position(start);
/* 520 */         return false;
/*     */       }
/* 522 */       buffer.put((byte)(currentBufferByte | 255 >> bytePos));
/*     */     }
/* 524 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class HuffmanCode
/*     */   {
/*     */     int value;
/*     */     
/*     */     int length;
/*     */     
/*     */ 
/*     */     public HuffmanCode(int value, int length)
/*     */     {
/* 538 */       this.value = value;
/* 539 */       this.length = length;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 543 */       return this.value;
/*     */     }
/*     */     
/*     */     public int getLength() {
/* 547 */       return this.length;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/* 554 */       if (this == o) return true;
/* 555 */       if ((o == null) || (getClass() != o.getClass())) { return false;
/*     */       }
/* 557 */       HuffmanCode that = (HuffmanCode)o;
/*     */       
/* 559 */       if (this.length != that.length) return false;
/* 560 */       if (this.value != that.value) { return false;
/*     */       }
/* 562 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 567 */       int result = this.value;
/* 568 */       result = 31 * result + this.length;
/* 569 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 574 */       return "HuffmanCode{value=" + this.value + ", length=" + this.length + '}';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\HPackHuffman.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */