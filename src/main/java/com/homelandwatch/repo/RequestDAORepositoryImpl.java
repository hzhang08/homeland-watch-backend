package com.homelandwatch.repo;

import com.homelandwatch.model.RequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RequestDAORepositoryImpl implements RequestDAORepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<RequestDAO> rowMapper = new RowMapper<RequestDAO>() {
        @Override
        public RequestDAO mapRow(ResultSet resultSet, int i) throws SQLException {
            RequestDAO requestDAO = new RequestDAO();
            requestDAO.setRequestId(resultSet.getInt("id"));
            requestDAO.setRequestType(resultSet.getString("type"));
            requestDAO.setRequestStartTime(resultSet.getLong("start_time"));
            requestDAO.setRequestEndTime(resultSet.getLong("end_time"));
            requestDAO.setElderlyId(resultSet.getInt("elderly_id"));
            requestDAO.setRequestStatus(RequestDAO.RequestStatus.valueOf(resultSet.getString("STATUS")));
            return requestDAO;
        }
    };

    @Override
    public List<RequestDAO> findAll() {
        return jdbcTemplate.query("select * from REQUEST where STATUS='Open' limit 3;",rowMapper);
    }

    @Override
    public void save(RequestDAO requestDAO) {
        jdbcTemplate.update(
                "insert into REQUEST(TYPE,ELDERLY_ID,ELDERLY_NAME,STATUS) values( :requestType,:elderlyId,:elderlyName,:requestStatus);",
                new BeanPropertySqlParameterSource(requestDAO));
    }

    @Override
    public RequestDAO findById(long id) {
        Map<String,Object> params = Collections.singletonMap("requestId",id);
        return jdbcTemplate.queryForObject("select * from REQUEST where id = :requestId",params,rowMapper);
    }

    @Override
    public void deleteById(long id) {
        Map<String,Object> params = Collections.singletonMap("requestId",id);
        jdbcTemplate.update("delete from REQUEST where id = :requestId",params);
    }

    @Override
    public List<RequestDAO> listAllElderlyRequests(int elderlyId) {
        Map<String,Object> params = Collections.singletonMap("elderlyId",elderlyId);
        return jdbcTemplate.query("select * from REQUEST where ELDERLY_ID= :elderlyId limit 5",params,rowMapper);
    }

    @Override
    public List<RequestDAO> listMyAcceptedRequets(int volunteerId) {
        Map<String,Object> params = Collections.singletonMap("volunteerId",volunteerId);
        return jdbcTemplate.query("select * from REQUEST where VOLUNTEER_ID= :volunteerId limit 2",params,rowMapper);
    }

    @Override
    public void accept(int requestId, int volunteerId) {
        Map<String,Object> params = new HashMap<>();
        params.put("requestId",requestId);
        params.put("volunteerId",volunteerId);
        jdbcTemplate.update("update REQUEST set VOLUNTEER_ID= :volunteerId,STATUS='Accepted' where id = :requestId",params);
    }
}
