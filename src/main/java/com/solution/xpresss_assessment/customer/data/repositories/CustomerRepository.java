package com.solution.xpresss_assessment.customer.data.repositories;

import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.customer.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findCustomerByUser(User user);
}
