package com.santidev.accountbook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A Transaction.
 */
@Document(collection = "transaction")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";

    @Id
    private String id;

    @Field("id_user_account")
    private String idUserAccount;

    @Field("type")
    private String type;

    @Field("amount")
    private BigDecimal amount;

    @Field("effective_date")
    private LocalDate effectiveDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", idUserAccount='" + getIdUserAccount() + "'" +
            ", type='" + getType() + "'" +
            ", amount=" + getAmount() +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            "}";
    }
}
