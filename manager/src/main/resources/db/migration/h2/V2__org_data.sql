INSERT INTO organization
    (org_name, display_name, contact_name, contact_email, contact_phone, created_timestamp, updated_timestamp)
    VALUES ('test-organization', 'Test Organization',
            'Contact Name', 'tests@example.com', '555-555-1234',
            CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO organization
    (org_name, display_name, contact_name, contact_email, contact_phone, created_timestamp, updated_timestamp)
    VALUES ('another-org', 'Another org for testing',
            'Contact Name', 'tests@example.com', '555-555-4321',
            CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO vault
    (name, description, org_name, created_timestamp, updated_timestamp)
    VALUES ('test-vault', 'A vault for tests', 'test-organization', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Programmatic user that can submit content to the test-vault
INSERT INTO preservation_user
    (username, display_name, enabled, user_type, created_timestamp, updated_timestamp)
    VALUES ('fedora-object-preserver', 'fedora-object-preserver', true, 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO api_key
    (key_hash, username, created_timestamp)
     VALUES ('7b3d979ca8330a94fa7e9e1b466d8b99e0bcdea1ec90596c0dcc8d7ef6b4300c', 'fedora-object-preserver', CURRENT_TIMESTAMP());

INSERT INTO user_organization_map
    (username, org_name, role, enabled, created_timestamp, updated_timestamp)
    VALUES ('fedora-object-preserver', 'test-organization', 1, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO vault_permission
    (vault, username, permission)
    VALUES ('test-vault', 'fedora-object-preserver', 1);
INSERT INTO vault_permission
    (vault, username, permission)
    VALUES ('test-vault', 'fedora-object-preserver', 2);

-- Admin interface application user -- may only be used for proxying requests of normal users
INSERT INTO preservation_user
    (username, display_name, enabled, user_type, created_timestamp, updated_timestamp)
    VALUES ('admin-app', 'admin-app', true, 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO api_key
    (key_hash, username, created_timestamp)
    VALUES ('dfe5a69381dba6bff0ac156d63b3fdb315bab647c6007f82a4465f3ef142999e', 'admin-app', CURRENT_TIMESTAMP());

-- A test user with org admin privileges
INSERT INTO preservation_user
    (username, display_name, external_id, enabled, user_type, created_timestamp, updated_timestamp)
    VALUES ('admin-1', 'Admin 1', 'admin-1@example.com', true, 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO user_organization_map
    (username, org_name, role, enabled, created_timestamp, updated_timestamp)
    VALUES ('admin-1', 'test-organization', 2, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO vault_permission
    (vault, username, permission)
    VALUES ('test-vault', 'admin-1', 1);
INSERT INTO vault_permission
    (vault, username, permission)
    VALUES ('test-vault', 'admin-1', 2);

-- A test user without org admin privileges
INSERT INTO preservation_user
    (username, display_name, external_id, enabled, user_type, created_timestamp, updated_timestamp)
    VALUES ('user-1', 'User 1', 'user-1@example.com', true, 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO user_organization_map
    (username, org_name, role, enabled, created_timestamp, updated_timestamp)
    VALUES ('user-1', 'test-organization', 1, true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO vault_permission
    (vault, username, permission)
    VALUES ('test-vault', 'user-1', 1);
INSERT INTO vault_permission
    (vault, username, permission)
    VALUES ('test-vault', 'user-1', 2);

-- A test service admin user
INSERT INTO preservation_user
    (username, display_name, external_id, enabled, user_type, created_timestamp, updated_timestamp)
    VALUES ('service-admin-1', 'Service Admin 1', 'service-admin-1@example.com', true, 4, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO api_key
    (key_hash, username, created_timestamp)
     VALUES ('bdc2d1a501f5ad1a9b4e03fb335a8bc5e75440346f3e90c8581c039163cb3486', 'service-admin-1', CURRENT_TIMESTAMP());
