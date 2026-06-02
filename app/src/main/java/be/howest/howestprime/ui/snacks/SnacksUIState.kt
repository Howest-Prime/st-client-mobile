package be.howest.howestprime.ui.snacks

import be.howest.howestprime.domain.model.Snack

data class SnacksUIState(
    val snacks: List<Snack> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
