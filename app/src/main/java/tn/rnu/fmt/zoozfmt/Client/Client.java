package tn.rnu.fmt.zoozfmt.Client;

/**
 * Created by sana on 20/03/2018.
 */

public class Client {
    private String eFirstname;
    private String eLastname;
    private String ePassword;
    private String eEmail;
    private int eNbChildren;
    private String eBirthday;
    private Boolean cMenopause;
    private String role;

    public Client(String eFirstname, String eLastname, String ePassword, String eEmail, int eNbChildren, String eBirthday, Boolean cMenopause) {
        this.eFirstname = eFirstname;
        this.eLastname = eLastname;
        this.ePassword = ePassword;
        this.eEmail = eEmail;
        this.eNbChildren = eNbChildren;
        this.eBirthday = eBirthday;
        this.cMenopause = cMenopause;
        this.role = "client";
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

    public void seteNbChildren(int eNbChildren) {
        this.eNbChildren = eNbChildren;
    }

    public void seteBirthday(String eBirthday) {
        this.eBirthday = eBirthday;
    }

    public void setcMenopause(Boolean cMenopause) {
        this.cMenopause = cMenopause;
    }

    public void setRole(String role) {
        this.role = role;
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

    public int geteNbChildren() {
        return eNbChildren;
    }

    public String geteBirthday() {
        return eBirthday;
    }

    public Boolean getcMenopause() {
        return cMenopause;
    }

    public String getRole() {
        return role;
    }
}
