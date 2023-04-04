package sg.edu.nus.iss.workshop21.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.workshop21.model.Customer;
import sg.edu.nus.iss.workshop21.model.Order;
import sg.edu.nus.iss.workshop21.repository.CustomerRepository;

@RestController
@RequestMapping(path="/api/customers", produces=MediaType.APPLICATION_JSON_VALUE)
public class CustomerRestController {

    @Autowired
    private CustomerRepository custRepo;
    
    @GetMapping()
    public ResponseEntity<String> getAllCustomers(@RequestParam(required = false) 
        String limit, @RequestParam(required = false) String offset) {

        if (Objects.isNull(limit)) {
            limit = "5";
        }

        if (Objects.isNull(offset)) {
            offset = "0";
        }

        List<Customer> customers = custRepo.getAllCustomers
            (Integer.parseInt(offset), Integer.parseInt(limit));

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Customer c : customers) {
            arrBuilder.add(c.toJSON());
        }

        JsonArray result = arrBuilder.build();
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @GetMapping(path="{customerId}")
    public ResponseEntity<String> getCustomerById(@PathVariable Integer customerId) {

        // JsonObject result = null;

        // try {
        // Customer customer = custRepo.findCustomerById(customerId);

        // JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        // objBuilder.add("customer", customer.toJSON());

        // result = objBuilder.build();

        // controller for sqlrowset and rs.next()
        // List<Customer> cust = custRepo.findCustomerById(customerId);
        // JsonArray result = null;
        
        // JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        // for (Customer c : cust) {
        //     arrBuilder.add(c.toJSON());
        // }
        // result = arrBuilder.build();

        // if (cust.isEmpty()) {
        // return ResponseEntity
        //         .status(HttpStatus.NOT_FOUND)
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .body(Json.createObjectBuilder().add("error message"
        //         , "record not found").build().toString());
        // }

        // return ResponseEntity
        //         .status(HttpStatus.OK)
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .body(result.get(0).toString());

        // controller for sqlrowset and rs.first()
        Optional<Customer> cust = custRepo.findCustomerById(customerId);

        if (cust.isEmpty()) {
            return ResponseEntity.status(404).body(
                Json.createObjectBuilder().add("error message"
                , "record not found").build().toString());
        }
        return ResponseEntity.ok(cust.get().toJSON().toString());
    }

    @GetMapping(path="{customerId}/orders")
    public ResponseEntity<String> getOrdersForCustomer(@PathVariable Integer customerId) {

        // Optional<Customer> cust = custRepo.findCustomerById(customerId);
        // Optional<Order> ord = custRepo.getCustomerOrders(customerId);

        // if (cust.isEmpty()) {
        //     return ResponseEntity.status(404).body(
        //         Json.createObjectBuilder().add("error message"
        //         , "record not found").build().toString());
        // }
        // return ResponseEntity.ok(ord.get().toJSON().toString());
        
        List<Order> orders = new ArrayList<Order>();
        JsonArray result = null;
        
        orders = custRepo.getCustomerOrders(customerId);
        Optional<Customer> cust = custRepo.findCustomerById(customerId);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Order o : orders) {
            arrBuilder.add(o.toJSON());
        }

        result = arrBuilder.build();

        if ((result.size() == 0) && (cust.isEmpty())) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder().add("error message"
                    , "record not found").build().toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }
}
