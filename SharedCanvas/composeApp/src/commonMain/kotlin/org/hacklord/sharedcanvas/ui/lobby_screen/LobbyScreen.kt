package org.hacklord.sharedcanvas.ui.lobby_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import org.hacklord.sharedcanvas.ui.Route
import org.hacklord.sharedcanvas.ui.UiEvent
import org.hacklord.sharedcanvas.ui.lobby_screen.components.BoardListItem
import org.hacklord.sharedcanvas.ui.lobby_screen.create_board_screen.CreateBoardPanel
import org.hacklord.sharedcanvas.ui.lobby_screen.create_board_screen.CreateBoardState
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.AddBoard
import sharedcanvas.composeapp.generated.resources.ExitIcon
import sharedcanvas.composeapp.generated.resources.Logo
import sharedcanvas.composeapp.generated.resources.Res
import kotlin.math.min

@Composable
fun LobbyScreen(
    state: LobbyState,
    createBoardState: CreateBoardState,
    onEvent: (event: LobbyEvent) -> Unit,
    onNavigate: (newRoute: Route) -> Unit,
    uiEventFlow: Flow<UiEvent>,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(key1 = true) {
        uiEventFlow.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                else -> {}
            }
        }
    }

    Row {
        Image(
            painter = painterResource(Res.drawable.ExitIcon),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .padding(20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onEvent(LobbyEvent.Logout)
                    onNavigate(Route.Login)
                }
        )

        Spacer(
            Modifier
                .weight(1f)
        )

        Image(
            painter = painterResource(Res.drawable.AddBoard),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .padding(20.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onEvent(LobbyEvent.CreateWhiteboardToggle)
                }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.Logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(500.dp)
        )

        LazyColumn {
            for (i in 0..state.boards.size step 3) {
                item {
                    Row {
                        for (board in state.boards.subList(i, min(i + 3, state.boards.size))) {
                            BoardListItem(
                                board,
                                onClick = {
                                    onEvent(LobbyEvent.EnterWhiteboard(board.id))
                                },
                                modifier = Modifier
                                    .width(200.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (createBoardState.isOpen) {
        CreateBoardPanel(
            createBoardState,
            onEvent,
            onNavigate,
            Modifier
                .fillMaxSize(.5f)
        )
    }
}