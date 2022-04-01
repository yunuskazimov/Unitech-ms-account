package az.unibank.unitechmsaccount.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Status {
    ACTIVE, INACTIVE;

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
