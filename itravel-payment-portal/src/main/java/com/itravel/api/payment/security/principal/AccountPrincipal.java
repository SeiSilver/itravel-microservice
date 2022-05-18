package com.itravel.api.payment.security.principal;

import com.itravel.api.payment.model.entitydto.AccountEntity;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountPrincipal implements OAuth2User, UserDetails, Serializable {

  private String accessToken;
  private transient AccountEntity accountVerified;
  private String username;
  private Collection<? extends GrantedAuthority> authorities;
  private transient Map<String, Object> attributes;

  public static AccountPrincipal build(AccountEntity account) {

    List<GrantedAuthority> authorities = account.getRoles().stream().map(role ->
        new SimpleGrantedAuthority(role.getRoleName().name())
    ).collect(Collectors.toList());

    return AccountPrincipal.builder()
        .username(account.getEmail())
        .accountVerified(account)
        .authorities(authorities)
        .build();
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return username;
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

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getName() {
    return getUsername();
  }
}
