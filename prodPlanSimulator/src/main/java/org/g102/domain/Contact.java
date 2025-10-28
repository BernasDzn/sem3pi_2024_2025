package org.g102.domain;

import java.util.List;

public class Contact {

    private int mobileNumber, fixedNumber;
    private String primaryEmail, secondaryEmail;

    public static final List<Character> mobileOperators = List.of('1', '2', '3', '6');

    public Contact(int mobileNumber, int fixedNumber, String primaryEmail, String secondaryEmail) {
        this.mobileNumber = mobileNumber;
        this.fixedNumber = fixedNumber;
        this.primaryEmail = primaryEmail;
        this.secondaryEmail = secondaryEmail;
    }

    public Contact(int mobileNumber, String primaryEmail) {
        this.mobileNumber = mobileNumber;
        this.primaryEmail = primaryEmail;
    }

    public int getMobileNumber() { return mobileNumber; }

    public int getFixedNumber() { return fixedNumber; }

    public String getPrimaryEmail() { return primaryEmail; }

    public String getSecondaryEmail() { return secondaryEmail; }

    public void setMobileNumber(String mobileNumber) {
        if (checkMobileNumber(mobileNumber)) {
            this.mobileNumber = Integer.parseInt(mobileNumber);
        }
    }

    public void setFixedNumber(int fixedNumber) { this.fixedNumber = fixedNumber; }

    public void setPrimaryEmail(String primaryEmail) { this.primaryEmail = primaryEmail; }

    public void setSecondaryEmail(String secondaryEmail) { this.secondaryEmail = secondaryEmail; }

    private boolean checkMobileNumber(String mobileNumber) {
        if(mobileNumber.length() != 9){
            System.out.println("Mobile number must have 9 digits");
            return false;
        }
        if (mobileOperators.contains(mobileNumber.charAt(1))) {
            System.out.println("Mobile number must start with 6, 7, 8 or 9");
            return false;
        }
        return true;
    }

}
