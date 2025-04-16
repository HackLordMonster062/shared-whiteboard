package org.hacklord.sharedcanvas.ui.whiteboard_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import org.hacklord.sharedcanvas.domain.data.User
import org.hacklord.sharedcanvas.ui.authScreens.components.FormField
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardEvent
import org.hacklord.sharedcanvas.ui.whiteboard_screen.WhiteboardState
import org.jetbrains.compose.resources.painterResource
import sharedcanvas.composeapp.generated.resources.AddUserTitle
import sharedcanvas.composeapp.generated.resources.CreateBoardBG
import sharedcanvas.composeapp.generated.resources.Res

@Composable
fun AddUserPanel(
    state: WhiteboardState,
    onEvent: (event: WhiteboardEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Popup(
        onDismissRequest = { onEvent(WhiteboardEvent.ToggleAddUserMenu) },
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,

            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onEvent(WhiteboardEvent.ToggleAddUserMenu) }
        ) {
            Image(
                painter = painterResource(Res.drawable.CreateBoardBG),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(.8f)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(.8f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row {
                    Image(
                        painter = painterResource(Res.drawable.AddUserTitle),
                        contentDescription = null,
                        modifier = Modifier
                            .width(200.dp)
                            .padding(10.dp)
                    )

                    //Spacer(modifier = Modifier.weight(.5f))

                    FormField(
                        value = state.currUsernameToAdd,
                        onChange = { newValue ->
                            onEvent(WhiteboardEvent.ChangeUsernameToAdd(newValue))
                        },
                        hint = "Username",
                        modifier = Modifier
                            .width(200.dp)
                            .padding(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(.7f)
                ) {
                    for (user: User in state.users) {
                        item {
                            UserListItem(
                                user = user,
                                onClick = {
                                    onEvent(WhiteboardEvent.AddUser(user.id))
                                },
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}