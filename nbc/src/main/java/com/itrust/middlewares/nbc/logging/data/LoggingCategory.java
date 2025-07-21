package com.itrust.middlewares.nbc.logging.data;

public enum LoggingCategory {

    authentication ("Authentication"),
    account ("Account"),
    onboarding ("Onboarding"),
    fundTransfer ("Fund Transfer"),
    stocks("Stocks"),
    units("Units"),
    utility("Utility");

    private final String name;

    LoggingCategory(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
