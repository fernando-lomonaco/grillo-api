package br.com.grillo.enums;

public enum RoleType {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}