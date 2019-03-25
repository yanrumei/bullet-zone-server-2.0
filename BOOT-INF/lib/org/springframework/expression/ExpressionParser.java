package org.springframework.expression;

public abstract interface ExpressionParser
{
  public abstract Expression parseExpression(String paramString)
    throws ParseException;
  
  public abstract Expression parseExpression(String paramString, ParserContext paramParserContext)
    throws ParseException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\ExpressionParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */