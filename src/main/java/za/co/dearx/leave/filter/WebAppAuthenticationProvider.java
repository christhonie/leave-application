package za.co.dearx.leave.filter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class WebAppAuthenticationProvider extends ContainerBasedAuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(WebAppAuthenticationProvider.class.getName());

    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";

    @Override
    public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {
        logger.info("++ WebAppAuthenticationProvider.extractAuthenticatedUser()....");

        if (HTTP_METHOD_OPTIONS.equals(request.getMethod())) {
            //When the OPTION verb is used we need to fake that we are logged in
            return new AuthenticationResult("Anonymous", true);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            logger.debug("++ authentication == null...return unsuccessful for " + request.getContextPath());
            return AuthenticationResult.unsuccessful();
        }

        logger.debug("++ authentication IS NOT NULL");

        String name = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            name = ((UserDetails) principal).getUsername();
        } else {
            name = principal.toString();
        }

        if (name == null || name.isEmpty()) {
            return AuthenticationResult.unsuccessful();
        }

        logger.debug("++ name = " + name);
        AuthenticationResult authenticationResult = new AuthenticationResult(name, true);
        if (principal instanceof UserDetails) {
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            authenticationResult.setGroups(getUserGroups(authorities));
        } else {
            authenticationResult.setGroups(getUserGroups(authentication.getAuthorities()));
        }
        return authenticationResult;
    }

    private List<String> getUserGroups(Collection<? extends GrantedAuthority> authorities) {
        logger.info("++ WebAppAuthenticationProvider.getUserGroups()....");
        List<String> groupIds;

        groupIds =
            authorities
                .stream()
                .map(res -> res.getAuthority())
                .map(res -> res.substring(5)) // Strip "ROLE_"
                .collect(Collectors.toList());

        logger.debug("++ groupIds = " + groupIds.toString());

        return groupIds;
    }
}
