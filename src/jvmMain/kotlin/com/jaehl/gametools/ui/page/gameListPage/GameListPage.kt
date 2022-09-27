package com.jaehl.gametools.ui.page.gameListPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jaehl.gametools.data.model.Game
import com.jaehl.gametools.ui.R
import com.jaehl.gametools.ui.component.AppBar

@Composable
fun GameListPage(
    viewModel: GameListViewModel,
    onGoBackClicked: () -> Unit,
    //onSelectGameClick: (Game) -> Unit,
    onEditGameClick: (Game?) -> Unit
) {

    Box {
        Column(modifier = Modifier) {
            AppBar(
                title = "Games",
                returnButton = false,
                onBackClick = {
                    onGoBackClicked()
                }
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Button(onClick = {
                    onEditGameClick(null)
                }) {
                    Text("Create New")
                }
                GameList(viewModel, viewModel.games, onEditGameClick)
            }
        }
    }
}

@Composable
fun GameList(
    viewModel: GameListViewModel,
    list: List<Game>,
    onEditGameClick: (Game?) -> Unit
) {
    LazyColumn {
        itemsIndexed(list) { index, game ->
            GameRow(viewModel, index, game, onEditGameClick)
        }
    }
}

@Composable
fun GameRow(
    viewModel: GameListViewModel,
    index : Int,
    game : Game,
    onEditGameClick: (Game?) -> Unit
) {
    Row (
        modifier = Modifier
            .clickable {
                viewModel.onGameClick(game)
            }
            .background(if(index.mod(2) == 0) R.Color.rowBackgroundEven else R.Color.rowBackgroundOdd),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            game.name,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        )
        IconButton(content = {
            Icon(Icons.Outlined.Edit, "back", tint = Color.Black)
        }, onClick = {
            onEditGameClick(game)
        })
    }
}