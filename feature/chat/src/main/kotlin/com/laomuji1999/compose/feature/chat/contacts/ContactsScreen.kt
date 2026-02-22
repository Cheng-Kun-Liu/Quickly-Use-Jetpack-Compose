package com.laomuji1999.compose.feature.chat.contacts

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.laomuji1999.compose.core.logic.model.entity.ContactInfoEntity
import com.laomuji1999.compose.core.ui.extension.clickableDebounce
import com.laomuji1999.compose.core.ui.extension.ifCondition
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow
import com.laomuji1999.compose.core.ui.we.widget.row.WeRowType
import com.laomuji1999.compose.core.ui.we.widget.title.WeTitle
import com.laomuji1999.compose.feature.chat.AiChatTopBar
import com.laomuji1999.compose.feature.chat.contacts.ContactsScreenUiState.ContactInfo
import com.laomuji1999.compose.res.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel(), onContactClick: (ContactInfoEntity) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.onAction(ContactsScreenAction.UpdateContactList)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        AiChatTopBar(
            title = stringResource(id = R.string.string_ai_chat_screen_navigation_message),
            onMenuClick = {})
        ContactsScreenUi(
            contactInfoList = uiState.contactInfoList, onContactClick = onContactClick
        )
    }
}

@Composable
private fun ContactsScreenUi(
    contactInfoList: List<ContactInfo>,
    onContactClick: (ContactInfoEntity) -> Unit,
) {
    val state = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = state
        ) {
            contactInfoList.forEach {
                item {
                    WeTitle(
                        modifier = Modifier,
                        title = it.category,
                    )
                }
                item {
                    WeOutline(weOutlineType = WeOutlineType.Full)
                }
                itemsIndexed(
                    items = it.contactList,
                    key = { _, item -> item.account }) { index, item ->
                    Column {
                        WeContactItem(
                            avatar = item.avatarUri,
                            text = item.nickname,
                            onClick = {
                                onContactClick(item)
                            },
                        )
                        if (index < it.contactList.lastIndex) {
                            WeOutline(
                                weOutlineType = WeOutlineType.Custom(
                                    start = WeTheme.dimens.rowIconSize + WeTheme.dimens.rowPaddingHorizontal * 2,
                                    height = WeTheme.dimens.outlineHeight,
                                ),
                            )
                        }
                    }
                }
                item {
                    WeOutline(weOutlineType = WeOutlineType.Full)
                }
            }
        }
        CategoryView(
            modifier = Modifier.align(Alignment.CenterEnd),
            state = state,
            contactInfoList = contactInfoList
        )
    }
}

@Composable
private fun CategoryView(
    modifier: Modifier, state: LazyListState, contactInfoList: List<ContactInfo>
) {
    val coroutineScope = rememberCoroutineScope()
    var itemHeight by remember { mutableFloatStateOf(0f) }
    var currentIndex by remember { mutableIntStateOf(-1) }
    Box(modifier = modifier) {
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            Column(
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        itemHeight = layoutCoordinates.size.height / contactInfoList.size.toFloat()
                    }
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(onVerticalDrag = { change, _ ->
                            val newIndex = (change.position.y / itemHeight).roundToInt()
                                .coerceIn(contactInfoList.indices)
                            if (newIndex != currentIndex) {
                                currentIndex = newIndex
                                coroutineScope.launch {
                                    state.scrollToItem(
                                        getCategoryIndex(
                                            contactInfoList = contactInfoList, index = newIndex
                                        )
                                    )
                                }
                            }
                        }, onDragEnd = {
                            currentIndex = -1
                        })
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                for ((index, it) in contactInfoList.withIndex()) {
                    Box(
                        modifier = Modifier
                            .clickableDebounce(indication = null) {
                                coroutineScope.launch {
                                    state.scrollToItem(
                                        getCategoryIndex(
                                            contactInfoList = contactInfoList, index = index
                                        )
                                    )
                                }
                            }
                            .ifCondition(condition = index == currentIndex, onTrue = {
                                background(
                                    color = WeTheme.colorScheme.categoryBackground,
                                    shape = CircleShape
                                )
                            })
                            .size(
                                WeTheme.dimens.categorySize
                            )
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = it.category,
                            textAlign = TextAlign.Center,
                            style = WeTheme.typography.micro,
                            color = if (index == currentIndex) WeTheme.colorScheme.categoryTextColor else WeTheme.colorScheme.fontColorLight,
                        )
                    }
                }
            }
        }
        if (currentIndex >= 0) {
            CategoryIndicator(
                category = contactInfoList[currentIndex].category,
                index = currentIndex,
                itemHeight = with(LocalDensity.current) {
                    itemHeight.toDp()
                })
        }
    }

}

private fun getCategoryIndex(
    contactInfoList: List<ContactInfo>, index: Int
): Int {
    val category = contactInfoList[index].category
    var findIndex = 0
    for (contactInfo in contactInfoList) {
        if (contactInfo.category == category) {
            return findIndex
        }
        findIndex += contactInfo.contactList.size + 1
    }
    return 0
}

@Composable
private fun CategoryIndicator(
    category: String,
    index: Int,
    itemHeight: Dp,
) {
    val color = WeTheme.colorScheme.categoryBackground
    Box(
        modifier = Modifier
            .size(itemHeight)
            .offset(
                x = -itemHeight * 2, y = itemHeight * index
            )
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val radius = size.width
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = color, radius = radius, center = center
            )

            val trianglePath = Path().apply {
                moveTo(center.x + radius + size.width / 2, center.y)
                lineTo(center.x + radius - size.width / 2, center.y - size.width / 4)
                lineTo(center.x + radius - size.width / 2, center.y + size.width / 4)
                close()
            }
            drawPath(
                path = trianglePath, color = color
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = category,
            style = WeTheme.typography.title,
            color = WeTheme.colorScheme.categoryTextColor,
        )
    }
}

@Composable
fun WeContactItem(
    avatar: Uri,
    text: String,
    onClick: () -> Unit,
) {
    WeRow(
        start = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(avatar)
                    .diskCacheKey(avatar.toString()).build(),
                contentDescription = null,
                placeholder = painterResource(id = R.mipmap.ic_launcher),
                modifier = Modifier
                    .size(WeTheme.dimens.rowIconSize)
                    .clip(RoundedCornerShape(WeTheme.dimens.rowIconRoundedCornerDp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(WeTheme.dimens.rowPaddingHorizontal))
        },
        center = {
            Column {
                Text(
                    text = text,
                    style = WeTheme.typography.title,
                    color = WeTheme.colorScheme.fontColorHeavy
                )
            }
        },
        weRowType = WeRowType.Single,
        onClick = onClick,
    )
}

@PreviewLightDark
@Composable
fun PreviewContactsScreenUi() {
    QuicklyTheme {
        ContactsScreenUi(
            contactInfoList = emptyList(), onContactClick = {})
    }
}