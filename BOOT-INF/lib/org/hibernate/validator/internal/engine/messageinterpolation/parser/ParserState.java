package org.hibernate.validator.internal.engine.messageinterpolation.parser;

public abstract interface ParserState
{
  public abstract void terminate(TokenCollector paramTokenCollector)
    throws MessageDescriptorFormatException;
  
  public abstract void handleNonMetaCharacter(char paramChar, TokenCollector paramTokenCollector)
    throws MessageDescriptorFormatException;
  
  public abstract void handleBeginTerm(char paramChar, TokenCollector paramTokenCollector)
    throws MessageDescriptorFormatException;
  
  public abstract void handleEndTerm(char paramChar, TokenCollector paramTokenCollector)
    throws MessageDescriptorFormatException;
  
  public abstract void handleEscapeCharacter(char paramChar, TokenCollector paramTokenCollector)
    throws MessageDescriptorFormatException;
  
  public abstract void handleELDesignator(char paramChar, TokenCollector paramTokenCollector)
    throws MessageDescriptorFormatException;
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\ParserState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */