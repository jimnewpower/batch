package com.example.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EmployeeRowMapper implements RowMapper<Employee> {

    private static final Logger log = LoggerFactory.getLogger(EmployeeRowMapper.class);

    @Override
        public Employee mapRow(java.sql.ResultSet resultSet, int rowNum) throws java.sql.SQLException {
            int id = resultSet.getInt("id");
            String employeeId = resultSet.getString("employee_id");
            String first = resultSet.getString("first_name");
            String last = resultSet.getString("last_name");
            String number = resultSet.getString("employee_number");

            log.info("id: " + id);
            log.info("employeeId: " + employeeId);
            log.info("first: " + first);
            log.info("last: " + last);
            log.info("number: " + number);

            return new Employee(id, employeeId, first, last, number);
        }
}
