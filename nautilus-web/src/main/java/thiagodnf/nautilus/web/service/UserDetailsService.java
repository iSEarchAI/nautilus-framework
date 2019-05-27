package thiagodnf.nautilus.web.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.model.UserDetails;
import thiagodnf.nautilus.web.repository.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("The username was not found. Please verify the e-mail");
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		if (grantedAuthorities.isEmpty()) {
			throw new RuntimeException("The permissions were not found. Please verify them with the admin");
		}

		return new UserDetails(user, grantedAuthorities);
	}
}
