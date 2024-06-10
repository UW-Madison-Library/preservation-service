-- -----------------------------------------------
-- OCFL tables. These tables are used by ocfl-java
-- -----------------------------------------------

CREATE TABLE IF NOT EXISTS ocfl_object_details (
  object_id varchar(1024) PRIMARY KEY NOT NULL,
  version_id varchar(255) NOT NULL,
  object_root_path varchar(2048) NOT NULL,
  revision_id varchar(255),
  inventory_digest varchar(255) NOT NULL,
  digest_algorithm varchar(255) NOT NULL,
  inventory bytea,
  update_timestamp timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS ocfl_object_lock (
  object_id varchar(1024) PRIMARY KEY,
  acquired_timestamp TIMESTAMP NOT NULL
);

-- ------------------------------
-- Enum reference tables
-- ------------------------------
CREATE TABLE data_store (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO data_store (id, name) VALUES
    (1, 'ibm_cos'),
    (2, 'glacier');

CREATE TABLE event_outcome (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO event_outcome (id, name) VALUES
    (1, 'success'),
    (2, 'fail'),
    (3, 'warning'),
    (4, 'approved'),
    (5, 'rejected'),
    (6, 'not_executed');

CREATE TABLE event_type (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO event_type (id, name) VALUES
    (1, 'receive_bag'),
    (2, 'virus_scan_bag'),
    (3, 'validate_bag'),
    (4, 'identify_obj'),
    (5, 'identify_file_format'),
    (6, 'review_obj'),
    (7, 'review_batch'),
    (8, 'write_obj_local'),
    (9, 'create_obj'),
    (10, 'update_obj'),
    (11, 'update_obj_metadata'),
    (12, 'replicate_obj_version'),
    (13, 'complete_obj_ingest'),
    (14, 'complete_batch_ingest'),
    (15, 'delete_bag'),
    (16, 'validate_obj_local'),
    (17, 'validate_obj_version_local'),
    (18, 'validate_obj_version_remote'),
    (19, 'restore_obj_version'),
    (20, 'prepare_dip'),
    (21, 'delete_obj');

CREATE TABLE format_registry (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO format_registry (id, name) VALUES
    (1, 'mime'),
    (2, 'pronom');

CREATE TABLE ingest_batch_state (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO ingest_batch_state (id, name) VALUES
    (1, 'received'),
    (2, 'analyzing'),
    (3, 'analysis_failed'),
    (4, 'pending_review'),
    (5, 'pending_ingestion'),
    (6, 'pending_rejection'),
    (7, 'ingesting'),
    (8, 'replicating'),
    (9, 'ingest_failed'),
    (10, 'replication_failed'),
    (11, 'complete'),
    (12, 'rejecting'),
    (13, 'rejected'),
    (14, 'deleted');

CREATE TABLE ingest_object_state (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO ingest_object_state (id, name) VALUES
    (1, 'analyzing'),
    (2, 'analysis_failed'),
    (3, 'pending_review'),
    (4, 'pending_ingestion'),
    (5, 'pending_rejection'),
    (6, 'ingesting'),
    (7, 'ingested'),
    (8, 'no_change'),
    (9, 'ingest_failed'),
    (10, 'replicating'),
    (11, 'replicated'),
    (12, 'replication_failed'),
    (13, 'complete'),
    (14, 'rejecting'),
    (15, 'rejected'),
    (16, 'deleted');

CREATE TABLE job_state (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO job_state (id, name) VALUES
    (1, 'pending'),
    (2, 'executing'),
    (3, 'failed'),
    (4, 'complete'),
    (5, 'cancelled');

CREATE TABLE job_type (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO job_type (id, name) VALUES
    (1, 'retrieve_objects'),
    (2, 'replicate'),
    (3, 'restore'),
    (4, 'finalize_restore'),
    (5, 'validate_local'),
    (6, 'process_batch'),
    (7, 'validate_remote'),
    (8, 'delete_dip'),
    (9, 'cleanup_sips');

CREATE TABLE log_level (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO log_level (id, name) VALUES
    (1, 'info'),
    (2, 'warn'),
    (3, 'error');

CREATE TABLE org_role (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO org_role (id, name) VALUES
    (1, 'standard'),
    (2, 'admin');

CREATE TABLE permission (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO permission (id, name) VALUES
    (1, 'read'),
    (2, 'write');

CREATE TABLE preservation_user_type (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO preservation_user_type (id, name) VALUES
    (1, 'programmatic_user'),
    (2, 'proxying_user'),
    (3, 'general_user'),
    (4, 'service_admin');

CREATE TABLE storage_problem_type (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO storage_problem_type (id, name) VALUES
    (1, 'missing'),
    (2, 'corrupt'),
    (3, 'none');

CREATE TABLE preservation_object_state (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO preservation_object_state (id, name) VALUES
    (1, 'active'),
    (2, 'deleted');

-- ------------------------------
-- Org and user
-- ------------------------------

CREATE TABLE organization (
    org_name VARCHAR_IGNORECASE(64) PRIMARY KEY,
    display_name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(64) NOT NULL,
    contact_email VARCHAR(64) NOT NULL,
    contact_phone VARCHAR(64) NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX org_name_unique_idx ON organization(org_name);

CREATE TABLE preservation_user (
    username VARCHAR_IGNORECASE(64) PRIMARY KEY,
    external_id VARCHAR(255),
    display_name VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    user_type SMALLINT NOT NULL REFERENCES preservation_user_type(id),
    created_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX external_id_unique_idx ON preservation_user(external_id);

CREATE TABLE user_organization_map (
    user_org_id SERIAL PRIMARY KEY,
    username VARCHAR_IGNORECASE(64) NOT NULL REFERENCES preservation_user(username),
    org_name VARCHAR_IGNORECASE(64) NOT NULL REFERENCES organization(org_name),
    role SMALLINT NOT NULL REFERENCES org_role(id),
    enabled BOOLEAN NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX user_org_map_idx ON user_organization_map(username, org_name);

CREATE INDEX user_org_map_org_idx ON user_organization_map(org_name);

CREATE TABLE api_key (
     key_hash VARCHAR(64) PRIMARY KEY NOT NULL,
     username VARCHAR(64) REFERENCES preservation_user(username),
     created_timestamp TIMESTAMP NOT NULL
);

CREATE TABLE vault (
    name VARCHAR_IGNORECASE(128) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    org_name VARCHAR_IGNORECASE(64) NOT NULL REFERENCES organization(org_name),
    objects INT NOT NULL DEFAULT 0,
    storage_mb BIGINT NOT NULL DEFAULT 0,
    created_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX vault_name_unique_idx ON vault(name);

CREATE INDEX vault_org_idx ON vault(org_name);

CREATE TABLE vault_permission (
    vault_permission_id SERIAL PRIMARY KEY,
    vault VARCHAR_IGNORECASE(128) NOT NULL REFERENCES vault(name),
    username VARCHAR_IGNORECASE(64) NOT NULL REFERENCES preservation_user(username),
    permission SMALLINT NOT NULL REFERENCES permission(id)
);
CREATE UNIQUE INDEX vault_permission_idx ON vault_permission (vault, username, permission);

CREATE INDEX vault_permission_vault_idx ON vault_permission (vault);
CREATE INDEX vault_permission_vault_user_idx ON vault_permission (vault, username);

-- ------------------------------
-- Ingest batch
-- ------------------------------

CREATE TABLE ingest_batch (
    ingest_id SERIAL PRIMARY KEY,
    org_name VARCHAR_IGNORECASE(64) NOT NULL REFERENCES organization(org_name),
    vault VARCHAR_IGNORECASE(128) NOT NULL REFERENCES vault(name),
    created_by VARCHAR_IGNORECASE(64) NOT NULL REFERENCES preservation_user(username),
    reviewed_by VARCHAR_IGNORECASE(64) REFERENCES preservation_user(username),
    state SMALLINT NOT NULL REFERENCES ingest_batch_state(id),
    original_filename VARCHAR(1024),
    file_path VARCHAR(1024),
    file_size BIGINT,
    has_analysis_errors BOOLEAN NOT NULL,
    has_analysis_warnings BOOLEAN NOT NULL,
    received_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    record_version INTEGER NOT NULL
);

CREATE INDEX ingest_batch_org_idx ON ingest_batch (org_name);
CREATE INDEX ingest_batch_vault_idx ON ingest_batch (vault);
CREATE INDEX ingest_batch_state_idx ON ingest_batch (state);
CREATE INDEX ingest_batch_updated_idx ON ingest_batch (updated_timestamp);
CREATE INDEX ingest_batch_path_idx ON ingest_batch (file_path);

CREATE TABLE ingest_batch_object (
    ingest_object_id SERIAL PRIMARY KEY,
    ingest_id INTEGER NOT NULL REFERENCES ingest_batch(ingest_id),
    external_object_id VARCHAR(255) NOT NULL,
    object_root_path VARCHAR(1024) NOT NULL,
    state SMALLINT NOT NULL REFERENCES ingest_object_state(id),
    reviewed_by VARCHAR_IGNORECASE(64) REFERENCES preservation_user(username),
    has_analysis_errors BOOLEAN NOT NULL,
    has_analysis_warnings BOOLEAN NOT NULL,
    record_version INTEGER NOT NULL
);
CREATE UNIQUE INDEX ingest_batch_object_idx ON ingest_batch_object (ingest_id, external_object_id);

CREATE TABLE ingest_batch_object_file (
    ingest_file_id SERIAL PRIMARY KEY,
    ingest_object_id INTEGER NOT NULL REFERENCES ingest_batch_object(ingest_object_id),
    file_path VARCHAR(1024) NOT NULL,
    sha256_digest CHAR(64),
    file_size BIGINT
);
CREATE UNIQUE INDEX ingest_batch_object_file_idx ON ingest_batch_object_file (ingest_object_id, file_path);

CREATE TABLE file_format (
    file_format_id SERIAL PRIMARY KEY,
    registry SMALLINT NOT NULL REFERENCES format_registry(id),
    format VARCHAR_IGNORECASE(255) NOT NULL
);
CREATE UNIQUE INDEX file_format_idx ON file_format (registry, format);

CREATE TABLE file_encoding (
    file_encoding_id SERIAL PRIMARY KEY,
    encoding VARCHAR_IGNORECASE(32) NOT NULL
);
CREATE UNIQUE INDEX file_encoding_idx ON file_encoding (encoding);

CREATE TABLE meta_source (
    meta_source_id SERIAL PRIMARY KEY,
    source VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX meta_source_idx ON meta_source (source);

CREATE TABLE ingest_file_format (
    ingest_file_format_id SERIAL PRIMARY KEY,
    ingest_file_id INTEGER NOT NULL REFERENCES ingest_batch_object_file(ingest_file_id),
    file_format_id INTEGER NOT NULL REFERENCES file_format(file_format_id),
    meta_source_id INTEGER NOT NULL REFERENCES meta_source(meta_source_id)
);

CREATE TABLE ingest_file_encoding (
    ingest_file_encoding_id SERIAL PRIMARY KEY,
    ingest_file_id INTEGER NOT NULL REFERENCES ingest_batch_object_file(ingest_file_id),
    file_encoding_id INTEGER NOT NULL REFERENCES file_encoding(file_encoding_id),
    meta_source_id INTEGER NOT NULL REFERENCES meta_source(meta_source_id)
);

CREATE TABLE ingest_file_validity (
    ingest_file_validity_id SERIAL PRIMARY KEY,
    ingest_file_id INTEGER NOT NULL REFERENCES ingest_batch_object_file(ingest_file_id),
    valid BOOLEAN,
    well_formed BOOLEAN,
    meta_source_id INTEGER NOT NULL REFERENCES meta_source(meta_source_id)
);

-- ------------------------------
-- Preservation object
-- ------------------------------

CREATE TABLE preservation_object (
    object_id UUID PRIMARY KEY,
    vault VARCHAR_IGNORECASE(128) NOT NULL REFERENCES vault(name),
    external_object_id VARCHAR(255) NOT NULL,
    state SMALLINT NOT NULL REFERENCES preservation_object_state(id),
    last_shallow_check_timestamp TIMESTAMP,
    last_deep_check_timestamp TIMESTAMP,
    created_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    record_version INTEGER NOT NULL
);
CREATE UNIQUE INDEX preservation_object_idx ON preservation_object (vault, external_object_id);
CREATE INDEX preservation_object_vault_idx ON preservation_object (vault);

CREATE TABLE preservation_object_version (
    object_version_id SERIAL PRIMARY KEY,
    object_id UUID NOT NULL REFERENCES preservation_object(object_id),
    version INTEGER,
    initial_persistence_version VARCHAR(32),
    persistence_version VARCHAR(32),
    ingest_id INTEGER NOT NULL REFERENCES ingest_batch(ingest_id),
    created_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX preservation_object_version_idx ON preservation_object_version (object_id, version);

ALTER TABLE preservation_object
    ADD COLUMN head_object_version_id INTEGER REFERENCES preservation_object_version(object_version_id);

ALTER TABLE ingest_batch_object
    ADD COLUMN head_object_version_id INTEGER REFERENCES preservation_object_version(object_version_id);

CREATE TABLE preservation_object_file (
    object_file_id SERIAL PRIMARY KEY,
    object_id UUID NOT NULL REFERENCES preservation_object(object_id),
    sha256_digest CHAR(64) NOT NULL,
    file_size BIGINT NOT NULL,
    created_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX file_object_id_sha256_idx ON preservation_object_file (object_id, sha256_digest);

CREATE TABLE preservation_object_version_file (
    object_version_file_id SERIAL PRIMARY KEY,
    object_file_id INTEGER NOT NULL REFERENCES preservation_object_file(object_file_id),
    object_version_id INTEGER NOT NULL REFERENCES preservation_object_version(object_version_id),
    file_path VARCHAR(1024) NOT NULL,
    created_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX version_file_file_id_idx ON preservation_object_version_file (object_version_id);

CREATE TABLE preservation_object_file_format (
    object_file_format_id SERIAL PRIMARY KEY,
    object_file_id INTEGER NOT NULL REFERENCES preservation_object_file(object_file_id) ON DELETE CASCADE,
    file_format_id INTEGER NOT NULL REFERENCES file_format(file_format_id),
    meta_source_id INTEGER NOT NULL REFERENCES meta_source(meta_source_id)
);
CREATE INDEX file_format_file_id_idx ON preservation_object_file_format (object_file_id);

CREATE TABLE preservation_object_file_encoding (
    object_file_encoding_id SERIAL PRIMARY KEY,
    object_file_id INTEGER NOT NULL REFERENCES preservation_object_file(object_file_id) ON DELETE CASCADE,
    file_encoding_id INTEGER NOT NULL REFERENCES file_encoding(file_encoding_id),
    meta_source_id INTEGER NOT NULL REFERENCES meta_source(meta_source_id)
);
CREATE INDEX file_encoding_file_id_idx ON preservation_object_file_encoding (object_file_id);

CREATE TABLE preservation_object_file_validity (
    object_file_validity_id SERIAL PRIMARY KEY,
    object_file_id INTEGER NOT NULL REFERENCES preservation_object_file(object_file_id) ON DELETE CASCADE,
    valid BOOLEAN,
    well_formed BOOLEAN,
    meta_source_id INTEGER NOT NULL REFERENCES meta_source(meta_source_id)
);
CREATE INDEX file_validity_file_id_idx ON preservation_object_file_validity (object_file_id);

CREATE TABLE preservation_object_version_location (
    object_version_location_id SERIAL PRIMARY KEY,
    object_id UUID NOT NULL REFERENCES preservation_object(object_id),
    persistence_version VARCHAR(32),
    datastore SMALLINT NOT NULL REFERENCES data_store(id),
    datastore_key VARCHAR(255) NOT NULL,
    sha256_digest CHAR(64) NOT NULL,
    written_timestamp TIMESTAMP NOT NULL,
    last_check_timestamp TIMESTAMP
);
CREATE INDEX version_location_version_id_idx ON preservation_object_version_location (object_id, persistence_version);
CREATE UNIQUE INDEX version_location_unique_idx ON preservation_object_version_location (object_id, persistence_version, datastore);

CREATE TABLE storage_problem (
    storage_problem_id SERIAL PRIMARY KEY,
    object_id UUID NOT NULL REFERENCES preservation_object(object_id),
    datastore SMALLINT NOT NULL REFERENCES data_store(id),
    persistence_version VARCHAR(32),
    problem SMALLINT NOT NULL REFERENCES storage_problem_type(id),
    reported_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX storage_problem_object_store_idx ON storage_problem (object_id, datastore);
CREATE INDEX storage_problem_object_idx ON storage_problem (object_id);

-- ------------------------------
-- Logging
-- ------------------------------

CREATE TABLE ingest_event (
    ingest_event_id SERIAL PRIMARY KEY,
    external_event_id UUID NOT NULL,
    ingest_id INTEGER NOT NULL REFERENCES ingest_batch(ingest_id),
    external_object_id VARCHAR(255),
    username VARCHAR_IGNORECASE(64) REFERENCES preservation_user(username),
    agent VARCHAR(128),
    type SMALLINT NOT NULL REFERENCES event_type(id),
    outcome SMALLINT NOT NULL REFERENCES event_outcome(id),
    event_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX ingest_event_idx ON ingest_event (ingest_id);

CREATE TABLE ingest_event_log (
    ingest_event_log_id SERIAL PRIMARY KEY,
    ingest_event_id INTEGER NOT NULL REFERENCES ingest_event(ingest_event_id),
    level SMALLINT NOT NULL REFERENCES log_level(id),
    message VARCHAR(2048) NOT NULL,
    created_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX ingest_event_log_event_idx ON ingest_event_log (ingest_event_id);

CREATE TABLE preservation_event (
    event_id SERIAL PRIMARY KEY,
    external_event_id UUID NOT NULL,
    object_id UUID NOT NULL REFERENCES preservation_object(object_id) ON DELETE CASCADE,
    username VARCHAR_IGNORECASE(64) REFERENCES preservation_user(username),
    agent VARCHAR(128),
    type SMALLINT NOT NULL REFERENCES event_type(id),
    outcome SMALLINT NOT NULL REFERENCES event_outcome(id),
    event_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX preservation_event_object_idx ON preservation_event (object_id);

CREATE TABLE preservation_event_log (
    event_log_id SERIAL PRIMARY KEY,
    event_id INTEGER NOT NULL REFERENCES preservation_event(event_id) ON DELETE CASCADE,
    level SMALLINT NOT NULL REFERENCES log_level(id),
    message VARCHAR(2048) NOT NULL,
    created_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX preservation_event_log_event_idx ON preservation_event_log (event_id);

-- ------------------------------
-- Jobs
-- ------------------------------

CREATE TABLE job (
    job_id SERIAL PRIMARY KEY,
    org_name VARCHAR_IGNORECASE(64) REFERENCES organization(org_name),
    type SMALLINT NOT NULL REFERENCES job_type(id),
    state SMALLINT NOT NULL REFERENCES job_state(id),
    received_timestamp TIMESTAMP NOT NULL,
    updated_timestamp TIMESTAMP NOT NULL,
    next_attempt_timestamp TIMESTAMP,
    background BOOLEAN NOT NULL,
    record_version INTEGER NOT NULL
);

CREATE TABLE job_dependency (
    job_dependency_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    depends_on_job_id INTEGER NOT NULL REFERENCES job(job_id)
);
CREATE UNIQUE INDEX job_dependency_idx ON job_dependency (job_id, depends_on_job_id);

CREATE TABLE retrieve_job_details (
    retrieve_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    object_version_id INTEGER NOT NULL REFERENCES preservation_object_version(object_version_id)
);
CREATE INDEX retrieve_job_details_idx ON retrieve_job_details (job_id);

CREATE TABLE replicate_job_details (
    replicate_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    object_id UUID NOT NULL REFERENCES preservation_object(object_id),
    persistence_version VARCHAR(32),
    source SMALLINT NOT NULL REFERENCES data_store(id),
    destination SMALLINT NOT NULL REFERENCES data_store(id),
    ingest_id INTEGER REFERENCES ingest_batch(ingest_id),
    external_object_id VARCHAR(255)
);
CREATE UNIQUE INDEX replicate_job_details_idx ON replicate_job_details (job_id);

CREATE TABLE restore_job_details (
    restore_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    object_version_location_id INTEGER NOT NULL REFERENCES preservation_object_version_location(object_version_location_id)
);
CREATE UNIQUE INDEX restore_job_details_idx ON restore_job_details (job_id);

CREATE TABLE finalize_restore_job_details (
    finalize_restore_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    object_id UUID NOT NULL REFERENCES preservation_object(object_id)
);
CREATE UNIQUE INDEX finalize_restore_job_details_job_idx ON finalize_restore_job_details (job_id);

CREATE TABLE validate_local_job_details (
    validate_local_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    object_id UUID NOT NULL REFERENCES preservation_object(object_id),
    content_fixity_check BOOLEAN NOT NULL
);
CREATE UNIQUE INDEX validate_local_job_details_job_idx ON validate_local_job_details (job_id);

CREATE TABLE validate_remote_job_details (
    validate_remote_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    object_version_location_id INTEGER NOT NULL REFERENCES preservation_object_version_location(object_version_location_id)
);
CREATE UNIQUE INDEX validate_remote_job_details_job_idx ON validate_remote_job_details (job_id);

CREATE TABLE process_batch_job_details (
    process_batch_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    ingest_id INTEGER NOT NULL REFERENCES ingest_batch(ingest_id)
);
CREATE UNIQUE INDEX process_batch_job_details_job_idx ON process_batch_job_details (job_id);

CREATE TABLE job_log (
    job_log_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    level SMALLINT NOT NULL REFERENCES log_level(id),
    message VARCHAR(2048) NOT NULL,
    created_timestamp TIMESTAMP NOT NULL
);
CREATE INDEX job_log_idx ON job_log (job_id);

CREATE TABLE retrieve_request (
    retrieve_request_id SERIAL PRIMARY KEY,
    username VARCHAR_IGNORECASE(64) NOT NULL REFERENCES preservation_user(username),
    vault VARCHAR_IGNORECASE(128) NOT NULL REFERENCES vault(name),
    external_object_ids TEXT,
    all_versions BOOLEAN NOT NULL,
    deleted BOOLEAN NOT NULL,
    created_timestamp TIMESTAMP NOT NULL,
    deleted_timestamp TIMESTAMP
);

CREATE TABLE retrieve_request_job (
    retrieve_request_job_id SERIAL PRIMARY KEY,
    retrieve_request_id INTEGER NOT NULL REFERENCES retrieve_request(retrieve_request_id),
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    last_downloaded_timestamp TIMESTAMP
);
CREATE INDEX retrieve_request_job_request_idx ON retrieve_request_job (retrieve_request_id);
CREATE UNIQUE INDEX retrieve_request_job_job_idx ON retrieve_request_job (job_id);

CREATE TABLE delete_dip_job_details (
    delete_dip_job_details_id SERIAL PRIMARY KEY,
    job_id INTEGER NOT NULL REFERENCES job(job_id),
    retrieve_request_id INTEGER NOT NULL REFERENCES retrieve_request(retrieve_request_id)
);
CREATE UNIQUE INDEX delete_dip_job_details_idx ON delete_dip_job_details (job_id);
