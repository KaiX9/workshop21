package sg.edu.nus.iss.workshop21.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop21.model.Customer;
import sg.edu.nus.iss.workshop21.model.Order;

import static sg.edu.nus.iss.workshop21.repository.DBQueries.*;

@Repository
public class CustomerRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public List<Customer> getAllCustomers(Integer offset, Integer limit) {
        List<Customer> csts = new ArrayList<Customer>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ALL_CUSTOMERS, limit, offset);

        while (rs.next()) {
            csts.add(Customer.create(rs));
        }

        return csts;
    }

    // Fetch customer by Id
    // public Customer findCustomerById(Integer id) {

    //     List<Customer> customer = jdbcTemplate.query(SELECT_CUSTOMER_BY_ID
    //         , new CustomerRowMapper(), new Object[] {id});

    //     return customer.get(0);
    // }

    // Fetch customer by Id via queryForRowSet & rs.next()
    // public List<Customer> findCustomerById(Integer id) {

    //     List<Customer> cust = new ArrayList<Customer>();

    //     SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_CUSTOMER_BY_ID, id);

    //     while (rs.next()) {
    //         cust.add(Customer.create(rs));
    //     }

    //     return cust;
    // }

    // Fetch customer by Id via queryForRowSet & row.first()

    public Optional<Customer> findCustomerById(Integer id) {
        
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_CUSTOMER_BY_ID, id);
        if (rs.first()) {
            return Optional.of(Customer.create(rs));
        }
        return Optional.empty();
    }

    // Fetch orders for customer 

    public List<Order> getCustomerOrders(Integer id) {

        List<Order> orders = new ArrayList<Order>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ORDER_FOR_CUSTOMER, new Object[] {id});

        while (rs.next()) {
            orders.add(Order.create(rs));
        }

        return orders;
    }

    // Fetch customer order via row.first()
    // public Optional<Order> getCustomerOrders(Integer id) {

    //     SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ORDER_FOR_CUSTOMER, id);
    //     if (rs.first()) {
    //         return Optional.of(Order.create(rs));
    //     }
    //     return Optional.empty();
    // }
}
