package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.Token.ID;

public abstract interface Scanner
{
  public abstract boolean checkToken(Token.ID... paramVarArgs);
  
  public abstract Token peekToken();
  
  public abstract Token getToken();
}


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\scanner\Scanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */