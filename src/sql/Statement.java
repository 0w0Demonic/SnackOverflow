package sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dto.DbObject;

final class Statement implements Query {
    private final PreparedStatement ps;
    
    Statement(PreparedStatement ps) {
        this.ps = Objects.requireNonNull(ps);
    }
    
    @Override
    public Query withArgs(Object ... args) {
        try {
            for (int i = 0; i < args.length; ++i) {
                // for some reason, these are 1-indexed.
                ps.setObject(i + 1, args[i]);
            }
            return this;
        } catch (SQLException e) {
            return FailedStatement.INSTANCE;
        }
    }
    
    @Override public void update() {
        try (ps) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends DbObject> List<T> toList(Mapper<T> mapper) {
        List<T> list = new ArrayList<>();
        
        // I HATE this.
        // 
        // At least I don't have to deal with SQLException ever again.
        try (ps; var rs = ps.executeQuery()) {
            try {
                while (rs.next()) {
                    list.add(mapper.map(rs));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
