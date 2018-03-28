package tn.rnu.fmt.zoozfmt.Admin;

/**
 * Created by sana on 25/03/2018.
 */

public class Admin {
    private String eFirstname;
    private String eLastname;
    private String ePassword;
    private String eEmail;
    private String role;

    public Admin(String eFirstname, String eLastname, String ePassword, String eEmail) {
        this.eFirstname = eFirstname;
        this.eLastname = eLastname;
        this.ePassword = ePassword;
        this.eEmail = eEmail;
        this.role = "Admin";
    }

    public String geteFirstname() {
        return eFirstname;
    }

    public String geteLastname() {
        return eLastname;
    }

    public String getePassword() {
        return ePassword;
    }

    public String geteEmail() {
        return eEmail;
    }

    public String getRole() {
        return role;
    }

    public void seteFirstname(String eFirstname) {
        this.eFirstname = eFirstname;
    }

    public void seteLastname(String eLastname) {
        this.eLastname = eLastname;
    }

    public void setePassword(String ePassword) {
        this.ePassword = ePassword;
    }

    public void seteEmail(String eEmail) {
        this.eEmail = eEmail;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
