package by.it.dao;

import by.it.entities.Contact;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {

    private final JdbcTemplate jdbcTemplate;

    public ContactDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int save(Contact contact) {
        String sql = "INSERT INTO Contact (name, email, address, phone) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql, contact.getName(), contact.getEmail(), contact.getAddress(), contact.getPhone());
    }

    @Override
    public int update(Contact contact) {
        String sql = "UPDATE Contact SET name=?, email=?, address=?, phone=? WHERE contact_id=?";
        return jdbcTemplate.update(sql, contact.getName(), contact.getEmail(), contact.getAddress(), contact.getPhone(), contact.getId());
    }

    @Override
    public Contact getContact(Integer id) {
        String sql = "SELECT * FROM Contact WHERE contact_id="+id;
        ResultSetExtractor<Contact> extractor = new ResultSetExtractor<Contact>() {
            @Override
            public Contact extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()){
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    return new Contact(id, name, email, address, phone);
                }
                return null;
            }
        };
        return jdbcTemplate.query(sql, extractor);
    }

    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM Contact WHERE contact_id=" + id;
        return jdbcTemplate.update(sql);
    }

    @Override
    public List<Contact> getContactsList() {
        String sql = "SELECT * FROM Contact";
        RowMapper<Contact> rowMapper = new RowMapper<Contact>() {
            @Override
            public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("contact_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                return new Contact(id, name, email, address, phone);
            }
        };
        return jdbcTemplate.query(sql, rowMapper);
    }
}