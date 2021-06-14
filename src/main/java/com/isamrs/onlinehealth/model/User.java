package com.isamrs.onlinehealth.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "users")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(
            name = "user_id",
            unique = true,
            nullable = false
    )
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    protected Long id;
    @Column(
            name = "username",
            unique = true,
            nullable = false
    )
    protected String username;
    @Column(
            name = "email",
            unique = true,
            nullable = false
    )
    @Email
    protected String email;
    @Column(
            name = "password",
            nullable = false
    )
    @NotNull
    @NotBlank
    protected String password;
    @Column(
            name = "first_name",
            nullable = false
    )
    protected String firstName;
    @Column(
            name = "last_name",
            nullable = false
    )
    protected String lastName;
    @Column(
            name = "address",
            nullable = false
    )
    protected String address;
    @Column(
            name = "city",
            nullable = false
    )
    protected String city;
    @Column(
            name = "country",
            nullable = false
    )
    protected String country;
    @Column(
            name = "phone_number",
            nullable = false
    )
    protected String phoneNumber;
    @Column(
            name = "user_type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    protected USER_TYPE userType;
    @Column(
            name = "password_changed"
    )
    private boolean passwordChanged = false;
    @Column(
            name = "deleted"
    )
    private Boolean deleted = false;

    @Column(name = "enabled")
    private boolean enabled = false;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User(Long id, String username, String email, String password, String firstName, String lastName, String address, String city, String country, String phoneNumber, USER_TYPE user, boolean passwordChanged) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.userType = user;
        this.passwordChanged = passwordChanged;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public USER_TYPE getUser() {
        return userType;
    }

    public void setUser(USER_TYPE userType) {
        this.userType = userType;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String state) {
        this.country = state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public String toString() {
        return username.toString();
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
