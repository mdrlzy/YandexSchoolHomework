package com.mdrlzy.budgetwise.core.domain

/**
 * Holds configuration fields required at runtime, typically provided at build time.
 *
 * @property bearerToken The authentication token used for authorized API requests.
 */
data class BuildConfigFields(
    val bearerToken: String,
)
