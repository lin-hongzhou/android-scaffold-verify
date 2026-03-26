package com.lhz.feature.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lhz.common.greet
import com.lhz.model.User
import com.lhz.ui.AppCard

@Composable
fun UserScreen(modifier: Modifier = Modifier) {

    // Test: core:model
    val users = listOf(
        User(1, "Alice", "alice@example.com"),
        User(2, "Bob", "bob@example.com"),
        User(3, "Charlie", "charlie@example.com")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Scaffold Test",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            // Test: core:common extension function
            text = "User".greet(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        users.forEach { user ->
            // Test: core:ui AppCard component
            AppCard(
                title = user.name,
                subtitle = user.email
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "All modules connected!",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
