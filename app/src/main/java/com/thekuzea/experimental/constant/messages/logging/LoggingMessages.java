package com.thekuzea.experimental.constant.messages.logging;

public interface LoggingMessages {

    String USER_NOT_FOUND_BY_USERNAME = "User with username {} not found.";

    String USER_NOT_FOUND_BY_ID = "User with id {} not found.";

    String USER_ALREADY_EXISTS_LOG = "User with username {} already exists.";

    String SAVED_NEW_USER = "User with [{}] username was saved.";

    String UPDATED_USER = "User with [{}] ID was updated.";

    String DELETED_USER = "User with [{}] username was deleted.";

    String ROLE_NOT_FOUND_BY_NAME = "Role with name {} not found.";

    String ROLE_ALREADY_EXISTS_LOG = "Role with name {} already exists.";

    String SAVED_NEW_ROLE = "Role with [{}] name was saved.";

    String DELETED_ROLE = "Role with [{}] name was deleted.";

    String UNABLE_TO_GET_TOKEN = "Unable to get JWT Token.";

    String TOKEN_HAS_EXPIRED = "JWT Token has expired.";

    String TOKEN_FORMAT_WARNING = "JWT Token does not begin with Bearer String.";

    String PUBLICATION_NOT_FOUND_BY_TOPIC = "Publication with topic {} not found.";

    String PUBLICATION_ALREADY_EXISTS_LOG = "Publication with topic {} already exists.";

    String DELETED_PUBLICATION = "Publication with [{}] topic was deleted.";
}
