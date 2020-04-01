package com.aoher.security.model;

import com.aoher.entities.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@SuppressWarnings("serial")
public class CustomUserPrincipal implements UserDetails {
    private Employee employee;

    public CustomUserPrincipal(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String getUsername() {
        return employee.getId().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
