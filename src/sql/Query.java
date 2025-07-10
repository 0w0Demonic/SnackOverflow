package sql;

import java.sql.PreparedStatement;
import java.util.List;

import dto.DbObject;

public sealed interface Query
        permits Statement, FailedStatement
{
    public static Query from(PreparedStatement ps) {
        if (ps == null) {
            return new Statement(ps);
        }
        return FailedStatement.INSTANCE;
    }
    
    public Query withArgs(Object ... args);
    
    public <T extends DbObject> List<T> toList(Mapper<T> mapper);
    
    public void update();
}
