package sql;

import java.sql.PreparedStatement;
import java.util.List;

import dto.DbObject;

/**
 * Specifies a type that wraps around a PreparedStatement.
 * This is used for eliminating SQLException (annoying af).
 * Very cool. Very good. Please applaud.
 */
public sealed interface Query
        permits Statement, FailedStatement
{
    /**
     * Static factory method that either returns Statement
     * or FailedStatement.
     * 
     * @param ps
     * @return
     */
    public static Query from(PreparedStatement ps) {
        if (ps == null) {
            return new Statement(ps);
        }
        return FailedStatement.INSTANCE;
    }
    
    /**
     * Fills the PreparedStatement with arguments.
     * 
     * @param args
     * @return
     */
    public Query withArgs(Object ... args);
    
    /**
     * Flattens the ResultSet into a list that is actually reasonably
     * useable.
     * 
     * @param <T> the type contained inside of the list
     * @param mapper the given lambda function
     * @return a new list
     */
    public <T extends DbObject> List<T> toList(Mapper<T> mapper);
    
    /**
     * Used for UPDATE-statements. {@code db.query("UPDATE ...").update()}
     */
    public void update();
}
