package be.howest.howestprime.ui.snacks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import be.howest.howestprime.R
import be.howest.howestprime.domain.model.Snack
import be.howest.howestprime.ui.theme.HowestMain
import be.howest.howestprime.ui.theme.HowestPromo
import coil3.compose.AsyncImage

@Composable
fun SnacksScreen(
    uiState: SnacksUIState,
    onAddToCart: (Snack) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SnacksHeader()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.snacks) { snack ->
                SnackCard(snack = snack, onAddClick = { onAddToCart(snack) })
            }
        }
    }
}

@Composable
private fun SnacksHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.snacks_title),
            modifier = Modifier.padding(vertical = 24.dp),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun SnackCard(
    snack: Snack,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = snack.imageUrl.ifBlank { null },
                contentDescription = snack.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit,
                error = painterResource(R.drawable.default_snack),
                placeholder = painterResource(R.drawable.default_snack)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(snack.name, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            if (snack.promoPrice != null) {
                Text(stringResource(R.string.snack_promo_prefix) + snack.promoPrice, color = HowestPromo, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            } else {
                Text(stringResource(R.string.snack_price_prefix) + snack.originalPrice, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(containerColor = HowestMain),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Text(stringResource(R.string.snack_add_button), fontSize = 12.sp)
            }
        }
    }
}
