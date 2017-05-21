package tech.caols.infinitely.repositories;

import tech.caols.infinitely.datamodels.Token;
import tech.caols.infinitely.db.Repository;

import java.util.List;

public class TokenRepository extends Repository<Token, Long> {

    public TokenRepository() {
        super(Token.class);
    }

    public Token findTokenByUserId(long userId) {
        List<Token> tokenList = super.query("Select a From Token a Where a.userId = ? Order By a.id desc Limit 1",
                new String[]{"tech.caols.infinitely.datamodels."}, userId);
        if (tokenList.size() > 0) {
            return tokenList.get(0);
        }
        return null;
    }

}
