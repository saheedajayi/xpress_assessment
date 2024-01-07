package com.solution.xpresss_assessment.data.repositories;

import com.solution.xpresss_assessment.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
