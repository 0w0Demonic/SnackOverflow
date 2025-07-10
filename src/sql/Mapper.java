package sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import dto.DbObject;

/**
 * Lambda type that is used to retrieve a new object from the given
 * ResultSet. Users must catch the SQLException manually.
 * 
 * @param <T> the type to return
 */
@FunctionalInterface
public interface Mapper<T extends DbObject>
{
    /**
     * Converts the current ResultSet entry into the given T.
     * 
     * @param rs the ResultSet to be used
     * @return a new instance of T
     */
    public T map(ResultSet rs) throws SQLException;
}
