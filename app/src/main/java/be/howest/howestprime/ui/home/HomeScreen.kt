package be.howest.howestprime.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.howestprime.R

@Composable
fun HomeScreen(
    uiState: HomeUIState,
    onOrderSnackClick: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onSubscribeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("home_screen")
    ) {
        HeroImage()

        OrderASnack(onOrderSnackClick = onOrderSnackClick)

        IntroTexts()

        if (!uiState.isSubscribed) {
            SubscribeToNewsletter(
                emailAddress = uiState.emailAddress,
                onEmailChanged = onEmailChanged,
                onSubscribeClick = onSubscribeClick,
                modifier = Modifier.testTag("subscribe")
            )
        }
    }
}

@Composable
private fun HeroImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.howestprime),
        modifier = Modifier.aspectRatio(16f / 9f),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
private fun OrderASnack(
    onOrderSnackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onOrderSnackClick() }
            .testTag("order_snack_btn")
    ) {
        Text(
            text = stringResource(R.string.order_snack_label),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 4.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun IntroText(textID: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(textID),
        modifier = modifier
            .fillMaxSize(),
        fontSize = 18f.sp,
        lineHeight = 28.sp
    )
}

@Composable
private fun IntroTexts(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16f.dp)) {
        IntroText(textID = R.string.intro_text_header, modifier = modifier)
        IntroText(textID = R.string.intro_text_body, modifier = modifier)
        IntroText(textID = R.string.intro_text_footer, modifier = modifier)
    }
}

@Composable
private fun SubscribeToNewsletter(
    emailAddress: String,
    onEmailChanged: (String) -> Unit,
    onSubscribeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.subscribe_title),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10f.dp, vertical = 5f.dp ),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 20f.sp,
            textAlign = TextAlign.Left
        )

        TextField(
            value = emailAddress,
            onValueChange = { onEmailChanged(it) },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10f.dp, vertical = 5f.dp ),
        )

        TextButton(
            enabled = emailAddress.isNotBlank(),
            onClick = { onSubscribeClick() }
        ) {
            Text(
                text = stringResource(R.string.subscribe_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10f.dp, vertical = 5f.dp ),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeUIState("", false),
        onOrderSnackClick = {},
        onEmailChanged = {},
        onSubscribeClick = {}
    )
}
