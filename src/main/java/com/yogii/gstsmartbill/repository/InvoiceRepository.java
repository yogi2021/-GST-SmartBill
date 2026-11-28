package com.yogii.gstsmartbill.repository;

import com.yogii.gstsmartbill.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByTenant_Id(Long tenantId);

    Optional<Invoice> findByIdAndTenant_Id(Long id, Long tenantId);

    boolean existsByInvoiceNumberAndTenant_Id(
            String invoiceNumber,
            Long tenantId
    );
}