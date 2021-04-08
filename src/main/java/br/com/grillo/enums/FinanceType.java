package br.com.grillo.enums;

public enum FinanceType {
    REVENUE("REVENUE"),
    EXPENSE("EXPENSE");

    private final String value;

    FinanceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
