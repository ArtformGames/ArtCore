package com.artformgames.core.user.handler;

import com.artformgames.core.user.User;

public abstract class AbstractUserHandler implements UserHandler {

    protected final User user;

    protected AbstractUserHandler(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

}
