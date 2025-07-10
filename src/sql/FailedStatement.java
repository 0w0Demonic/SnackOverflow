package sql;

import java.util.ArrayList;
import java.util.List;

import dto.DbObject;

/**
 * Implementation of Query used for hiding SQLException into a
 * concrete type that does nothing.
 */
final class FailedStatement implements Query
{
    /** Singleton instance. */
    static final FailedStatement INSTANCE = new FailedStatement();

    private FailedStatement() { } // unreachable
    
    /**
     * Does nothing.
     */
    @Override
    public void update() {
        // nothing
    }

    /**
     * Does nothing.
     */
    @Override
    public Query withArgs(Object ... args) {
        return this;
    }
    
    /**
     * Returns nothing (empty list of queried items).
     */
    @Override
    public <T extends DbObject> List<T> toList(Mapper<T> mapper) {
        return new ArrayList<>();
    }
}
