package com.scit.xml.security;

import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.apache.commons.lang3.StringUtils;
import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final String USER_ID_XPATH = "/user/@id";
    private final String USER_PASSWORD_XPATH = "/user/password";
    private final String USER_ROLE_XPATH = "/user/role";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        String userXml = this.userRepository.findByUsername(username);

        if (StringUtils.isEmpty(userXml)) {
            throw new UsernameNotFoundException("No user found with username '" + username + "'");
        } else {
            Document document = new XmlWrapper(userXml).getDocument();

            String id = XmlExtractorUtil.extractStringAndValidateNotBlank(document, USER_ID_XPATH);
            String password = XmlExtractorUtil.extractStringAndValidateNotBlank(document, USER_PASSWORD_XPATH);
            String role = XmlExtractorUtil.extractStringAndValidateNotBlank(document, USER_ROLE_XPATH);
            List<GrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(role));

            return new JwtUser(username, this.passwordEncoder.encode(password), true, true, true, true, grantedAuthorities, id);
        }
    }
}
