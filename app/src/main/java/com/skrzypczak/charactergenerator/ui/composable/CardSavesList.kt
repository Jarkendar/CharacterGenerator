package com.skrzypczak.charactergenerator.ui.composable

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.compose.rememberImagePainter
import com.skrzypczak.charactergenerator.R
import com.skrzypczak.charactergenerator.database.CardModel
import com.skrzypczak.charactergenerator.ui.CardSavesViewModel
import com.skrzypczak.charactergenerator.ui.OnCardInteract

//@Preview(widthDp = 1080, heightDp = 400, uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(widthDp = 1080, heightDp = 400, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardSavesList(viewModel: CardSavesViewModel, onCardInteract: OnCardInteract) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flow = remember(viewModel.cardsList, lifecycleOwner) {
        viewModel.cardsList.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val cardList: List<CardModel> by flow.collectAsState(initial = emptyList())

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(
            items = cardList,
            itemContent = {
                CardSavesItem(cardModel = it, onCardInteract)
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardSavesItem(cardModel: CardModel, onCardInteract: OnCardInteract) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(1.dp, Color.Gray),
    ) {
        var expanded by remember { mutableStateOf(false) }

        Row(modifier = Modifier
            .padding(8.dp, 3.dp)
            .combinedClickable(
                onClick = { onCardInteract.onItemClick(cardModel) },
                onLongClick = { expanded = true }
            ),
        verticalAlignment = Alignment.CenterVertically) {
            CardImage(uri = cardModel.imageUri)
            Column(
                Modifier
                    .padding(dimensionResource(id = R.dimen.margin_default))
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Row {
                    Text(
                        text = cardModel.name,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .alignByBaseline()
                            .padding(end = dimensionResource(id = R.dimen.margin_default))
                    )
                    Text(
                        text = cardModel.race,
                        modifier = Modifier
                            .alignByBaseline()
                            .padding(0.dp, dimensionResource(id = R.dimen.margin_default))
                    )
                    Text(
                        text = cardModel.timeStamp.toString(), modifier = Modifier
                            .alignByBaseline()
                            .fillMaxWidth(), textAlign = TextAlign.End
                    )
                }
                Row(Modifier.padding(vertical = dimensionResource(id = R.dimen.margin_default))) {
                    CardAttribute(
                        imageRes = R.drawable.attr_strength_image,
                        value = cardModel.attribution.strength
                    )
                    CardAttribute(
                        imageRes = R.drawable.attr_wisdom_image,
                        value = cardModel.attribution.wisdom
                    )
                    CardAttribute(
                        imageRes = R.drawable.attr_agility_image,
                        value = cardModel.attribution.agility
                    )
                    CardAttribute(
                        imageRes = R.drawable.attr_spirit_image,
                        value = cardModel.attribution.spirit
                    )
                    CardAttribute(
                        imageRes = R.drawable.attr_wit_image,
                        value = cardModel.attribution.wit
                    )
                    CardAttribute(
                        imageRes = R.drawable.limit_inspiration_image,
                        value = cardModel.attribution.inspirationLimit
                    )
                    CardAttribute(
                        imageRes = R.drawable.limit_fear_image,
                        value = cardModel.attribution.fearLimit
                    )
                    CardAttribute(
                        imageRes = R.drawable.limit_damage_image,
                        value = cardModel.attribution.damageLimit
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(onClick = {
                onCardInteract.onRemoveClick(cardModel)
                expanded = false
            }) {
                Text(text = stringResource(id = R.string.remove))
            }
        }
    }
}

@Composable
fun CardAttribute(imageRes: Int, value: Int) {
    Row {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Attribute",
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.card_save_item_image))
                .align(Alignment.CenterVertically)
        )
        Text(
            text = value.toString(), modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = dimensionResource(id = R.dimen.margin_default), end = dimensionResource(id = R.dimen.margin_triple))
        )
    }
}

@Composable
fun CardImage(uri: Uri?) {
    Image(
        painter = rememberImagePainter(data = uri, builder = {
            allowHardware(false)
        }),
        contentDescription = "",
        modifier = Modifier.size(46.dp, 50.dp),
        contentScale = ContentScale.Crop,
    )
}

