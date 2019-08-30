package com.wmapp.dipinjections.customscope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * Custom scope for dagger
 */
@MustBeDocumented
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class ApplicationScope