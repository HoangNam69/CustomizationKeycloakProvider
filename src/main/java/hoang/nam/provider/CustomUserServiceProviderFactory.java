/**
 * @ (#) CustomUserServiceProviderFactory.java 1.0 30-Dec-24
 * <p>
 * Copyright (c) 2024 IUH. All rights reserved.
 */
package hoang.nam.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

/**
 * @description:
 * @author: Le Hoang Nam
 * @date: 30-Dec-24
 * @version: 1.0
 */
public class CustomUserServiceProviderFactory implements UserStorageProviderFactory<CustomUserServiceProvider> {

    public static final String PROVIDER_ID = "custom-user-storage-database";

    @Override
    public CustomUserServiceProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        return new CustomUserServiceProvider(keycloakSession, componentModel);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void close() {
        UserStorageProviderFactory.super.close();
    }
}
