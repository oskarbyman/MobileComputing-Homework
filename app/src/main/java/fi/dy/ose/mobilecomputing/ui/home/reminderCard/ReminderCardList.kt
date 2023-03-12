package fi.dy.ose.mobilecomputing.ui.home.reminderCard

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fi.dy.ose.code.domain.entity.Reminder
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fi.dy.ose.mobilecomputing.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ReminderCardList(
    modifier: Modifier = Modifier,
    navController: NavController,
    latitude: Float?,
    longitude: Float?,
    viewModel: ReminderCardListViewModel = hiltViewModel()
) {

    val location = if (latitude != null && longitude != null) {
        Location("").apply {
            this.latitude = latitude.toDouble()
            this.longitude = longitude.toDouble()
        }
    } else {
        null
    }
    val viewState by viewModel.state.collectAsState()
    val tabs = listOf<String>( "Occurred", "Upcoming", "All")
    val selectedTab = remember { mutableStateOf("Occurred")}

    LaunchedEffect(key1 = Unit) {
        viewModel.reloadReminders(selectedTab.value, location)
    }


        Column() {
        val selectedIndex = tabs.indexOfFirst { it == selectedTab.value }
        TabRow(
            selectedTabIndex = 0,
            indicator = emptyTabIndicator,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, tabName ->
                Tab(
                    selected = index == selectedIndex,
                    onClick = {
                        selectedTab.value = tabName
                        viewModel.reloadReminders(tabName, location)
                    }
                ) {
                    ChoiceChipContent(
                        text = tabName,
                        selected = index == selectedIndex,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                    )
                }
            }
        }
        ReminderList(
            list = viewState.reminders,
            navController = navController,
            tabSelected = selectedTab.value,
            viewModel = viewModel,
            location = location
        )

    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>,
    navController: NavController,
    tabSelected: String,
    location: Location?,
    viewModel: ReminderCardListViewModel
) {

    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {

            items(list) { reminder ->
                ReminderListItem(
                    reminder = reminder,
                    onClick = { viewModel.setReminderAsSeen(reminder.reminderId) },
                    modifier = Modifier.fillParentMaxWidth(),
                    navController = navController,
                    tabSelected = tabSelected,
                    location = location,
                    viewModel = viewModel
                )

        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tabSelected: String,
    navController: NavController,
    viewModel: ReminderCardListViewModel,
    location: Location?
) {
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, reminderTitle, reminderCategory, editIcon, deleteIcon, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )
        //Title
        Text(
            text = reminder.title,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderTitle) {
                linkTo(
                    start = parent.start,
                    end = editIcon.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
        //Creator
        Text(
            text = reminder.creator_id,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderCategory) {
                linkTo(
                    start = parent.start,
                    end = editIcon.start,
                    startMargin = 24.dp,
                    endMargin = 8.dp,
                    bias = 0f
                )
                top.linkTo(reminderTitle.bottom, margin = 6.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )
        //datetime
        Text(
            text = when {
                reminder.reminder_time != null -> { reminder.reminder_time.formatToString() }
                else -> "Error"
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = reminderCategory.end,
                    end = editIcon.start,
                    startMargin = 8.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                centerVerticallyTo(reminderCategory)
                top.linkTo(reminderTitle.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )
        //icon
        IconButton(
            onClick = { navController.navigate("reminder?reminderId=${reminder.reminderId}") },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(editIcon) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(deleteIcon.start)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit_icon)
            )
        }
        //icon
        IconButton(
            onClick = {viewModel.deleteReminder(reminder.reminderId, tabName = tabSelected, location)},
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(deleteIcon) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete)
            )
        }

    }
}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

private fun LocalDateTime.formatToString(): String {
    val pattern = "uuuu-MM-dd HH:mm"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}