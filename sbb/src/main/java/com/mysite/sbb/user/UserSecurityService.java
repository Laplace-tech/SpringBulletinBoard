package com.mysite.sbb.user;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      SiteUser siteUser = findUserByUsername(username);
      List<GrantedAuthority> authorities = List.of(getUserRole(username));
      
      return new User(siteUser.getUserName(), siteUser.getPassword(), authorities);
    }

    private SiteUser findUserByUsername(String username) {
    	return this.userRepository.findByUserName(username)
    			.orElseThrow(() -> new UsernameNotFoundException(createErrorMessage(username)));
    }
    
    private String createErrorMessage(String username) {
        return String.format("%s를 찾을 수 없습니다..!", username);
    }
    
    private GrantedAuthority getUserRole(String username) {
        return new SimpleGrantedAuthority("admin".equals(username) ? 
                UserRole.ADMIN.getValue() : UserRole.USER.getValue());
    }
}
