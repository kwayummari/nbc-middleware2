package com.itrust.middlewares.nbc.logging.data;

public enum LoggingType {

    login ("Login"),
    changePassword ("Change Password"),
    accountOpen ("Account Open"),
    accountStatus ("Account Status"),
    accountBalance ("Account Balance"),
    accountEnquiry ("Account Enquiry"),
    onboardingQuestionnaire ("Onboarding Questionnaire"),
    onboardingBiometric ("Onboarding Biometric"),
    fundTransfer ("Fund Transfer"),
    stocksBuy("Stocks Buy"),
    stocksSell("Stocks Sell"),
    unitsBuy("Units Buy"),
    unitsSell("Units Sell"),
    utility("Utility");


    private final String name;

    LoggingType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
