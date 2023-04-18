package space.maxus.dnevnik.data.hibernate;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;
import java.util.UUID;

public class UUIDArrayType implements UserType<UUID[]> {

    @Override
    public int getSqlType() {
        return SqlTypes.ARRAY;
    }

    @Override
    public Class<UUID[]> returnedClass() {
        return UUID[].class;
    }

    @Override
    public boolean equals(UUID[] x, UUID[] y) {
        return Arrays.equals(x, y);
    }

    @Override
    public int hashCode(UUID[] x) {
        return Arrays.hashCode(x);
    }

    @Override
    public UUID[] nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        if (rs.wasNull()) {
            return new UUID[0];
        }
        if(rs.getArray(position) == null){
            return new UUID[0];
        }

        Array array = rs.getArray(position);
        return (UUID[]) array.getArray();
    }

    @Override
    public UUID[] deepCopy(UUID[] value) {
        return value;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, UUID[] value, int index, SharedSessionContractImplementor session) throws SQLException {
        Connection connection = st.getConnection();
        if (value == null) {
            st.setNull(index, Types.ARRAY);
        } else {
            Array array = connection.createArrayOf("uuid", value);
            st.setArray(index, array);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(UUID[] value) {
        return this.deepCopy(value);
    }

    @Override
    public UUID[] assemble(Serializable cached, Object owner) {
        return this.deepCopy((UUID[]) cached);
    }

    @Override
    public UUID[] replace(UUID[] detached, UUID[] managed, Object owner) {
        return detached;
    }
}
