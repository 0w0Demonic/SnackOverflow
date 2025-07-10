package sql;

import java.util.ArrayList;
import java.util.List;

import dto.DbObject;

final class FailedStatement implements Query
{
    /** Singleton instance. */
    static final FailedStatement INSTANCE = new FailedStatement();

    private FailedStatement() { }
    
    @Override
    public void update() {
        // nothing
    }

    @Override
    public Query withArgs(Object ... args) {
        return this;
    }
    
    @Override
    public <T extends DbObject> List<T> toList(Mapper<T> mapper) {
        return new ArrayList<>();
    }
}
