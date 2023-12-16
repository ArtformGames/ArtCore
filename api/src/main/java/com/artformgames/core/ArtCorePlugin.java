package com.artformgames.core;

import com.artformgames.core.user.manager.UserManager;
import org.jetbrains.annotations.NotNull;

interface ArtCorePlugin {

    @NotNull UserManager<?> getUserManager();




}
