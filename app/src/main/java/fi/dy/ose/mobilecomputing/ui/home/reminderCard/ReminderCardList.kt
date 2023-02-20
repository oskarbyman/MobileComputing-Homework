package fi.dy.ose.mobilecomputing.ui.home.reminderCard

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
    viewModel: ReminderCardListViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.reloadReminders()
    }


    ReminderList(
        list = viewState.reminders,
        navController = navController,
        viewModel = viewModel
    )
}

@Composable
private fun ReminderList(
    list: List<Reminder>,
    navController: NavController,
    viewModel: ReminderCardListViewModel
) {

    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {

            items(list) { reminder ->
                ReminderListItem(
                    reminder = reminder,
                    onClick = {},
                    modifier = Modifier.fillParentMaxWidth(),
                    navController = navController,
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
    navController: NavController,
    viewModel: ReminderCardListViewModel
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
            onClick = {viewModel.deleteReminder(reminder.reminderId)},
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

private fun LocalDateTime.formatToString(): String {
    val pattern = "uuuu-MM-dd HH:mm"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}