package net.moddity.droidnubekit.responsemodels;

/**
 * Created by jaume on 14/7/15.
 */
public class DNKUser {
    private String userRecordName;
    private String firstName;
    private String lastName;

    public String getUserRecordName() {
        return userRecordName;
    }

    public void setUserRecordName(String userRecordName) {
        this.userRecordName = userRecordName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
