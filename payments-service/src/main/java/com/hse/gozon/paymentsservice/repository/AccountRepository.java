package com.hse.gozon.paymentsservice.repository;

import com.hse.gozon.paymentsservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("""
            UPDATE Account a
            SET a.balance = a.balance + :amount
            WHERE a.id = :accountId
            """)
    void deposit(@Param("accountId") Integer accountId, @Param("amount")BigDecimal amount);

    @Query("""
            UPDATE Account a
            SET a.balance = a.balance - :amount
            WHERE a.id = :accountId AND a.balance >= :amount
            """)
    int withDraw(@Param("accountId") Integer accountId, @Param("amount") BigDecimal amount);
}
