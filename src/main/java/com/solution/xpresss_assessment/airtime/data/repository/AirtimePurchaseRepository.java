package com.solution.xpresss_assessment.airtime.data.repository;


import com.solution.xpresss_assessment.airtime.data.models.AirtimePurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirtimePurchaseRepository extends JpaRepository<AirtimePurchase, String> {
}
