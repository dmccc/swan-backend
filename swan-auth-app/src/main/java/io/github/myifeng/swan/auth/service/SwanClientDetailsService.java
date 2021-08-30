package io.github.myifeng.swan.auth.service;

import io.github.myifeng.swan.auth.dao.ClientRepository;
import io.github.myifeng.swan.auth.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
public class SwanClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new InvalidClientException("invalid clientId:" + clientId));

        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(clientId);
        details.setClientSecret(client.getClientSecret());
        details.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        details.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
        details.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
        details.setScope(client.getScope());
        return details;
    }
}
