CREATE TABLE PUBLIC.FLAT_ENTITY (
                                    ID UUID NOT NULL,
                                    NAME CHARACTER VARYING(255),
                                    ROOM_NUMBER INT,
                                    COST INT,
                                    DISTRICT CHARACTER VARYING(255),
                                    LINK CHARACTER VARYING(1000),
                                    LAST_UPDATED_AT TIMESTAMP,
                                    CONSTRAINT CONSTRAINT_F PRIMARY KEY (ID),
                                    CONSTRAINT UK_2JSK4EAKD0RMVYBO409WGWXUW UNIQUE (NAME)
);