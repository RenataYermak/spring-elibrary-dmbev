package org.example.service.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityUserDetails extends UserDetails {

    Long getId();
}
