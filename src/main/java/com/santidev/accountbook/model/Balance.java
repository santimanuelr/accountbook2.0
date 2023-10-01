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

/**
 * A Balance.
 */
@Document(collection = "balance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("account_id")
    private String accountId;

    @Field("total")
    private BigDecimal total;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Balance)) {
            return false;
        }
        return id != null && id.equals(((Balance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Balance{" +
            "id=" + getId() +
            ", accountId='" + getAccountId() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
