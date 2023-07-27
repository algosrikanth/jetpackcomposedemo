package com.square.employeedirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.square.employeedirectory.employee.DaggerEmployeeComponent
import com.square.employeedirectory.employee.EmployeeComponent
import com.square.employeedirectory.employee.EmployeeModule
import com.square.employeedirectory.employee.EmployeeViewModel
import com.square.employeedirectory.network.Employee
import com.square.employeedirectory.network.EmployeeRepo
import com.square.employeedirectory.ui.theme.SquareEmployeeDirectoryTheme
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject


class EmployeeScreen : ComponentActivity() {

    private var employeeComponent: EmployeeComponent? = null

    @Inject
    lateinit var repo: EmployeeRepo

    lateinit var viewModel: EmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeeComponent = DaggerEmployeeComponent.builder().employeeModule(
            EmployeeModule()
        ).build();

        // we are injecting the shared preference dependent object
        employeeComponent!!.inject(this);

        viewModel = ViewModelProvider(this)[EmployeeViewModel::class.java]
        viewModel.init(repo)
        setContent {
            val isLoading by viewModel.isLoading.collectAsState()
            val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

            val showError = remember { viewModel.showError }
            showDialog(isShow = showError, getString(R.string.error_message))

            val showEmpty = remember { viewModel.noEmployeesFound }
            showDialog(isShow = showEmpty, getString(R.string.no_employees_found))

            SquareEmployeeDirectoryTheme {
                // A surface container using the 'background' color from the theme
                SwipeRefresh(state = swipeRefreshState, onRefresh = {
                    viewModel.getEmployeeList()
                }) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        EmployeeListScreen(viewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun showDialog(isShow: MutableState<Boolean>, message: String) {
        if (isShow.value) {
            AlertDialog(
                onDismissRequest = {
                    isShow.value = false
                },
                title = {
                    Text(text = "Message")
                },
                text = {
                    Text(
                        message
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { isShow.value = false }
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getEmployeeList()
    }

    override fun onDestroy() {
        super.onDestroy()
        
    }
}

@Composable
fun EmployeeListScreen(viewModel: EmployeeViewModel) {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colorScheme.background) {
        EmployeeList(viewModel.employeeList)
    }
}

@Composable
fun EmployeeList(list: List<Employee>) {
    LazyColumn {
        items(list) {
            EmployeeCard(employee = it, onItemClicked = {})
        }
    }
}

@Composable
fun EmployeeCard(
    employee: Employee,
    onItemClicked: (employee: Employee) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked(employee) }),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val image: Painter = rememberAsyncImagePainter(
                employee.photoUrlSmall,
                placeholder = rememberAsyncImagePainter(R.drawable.placeholder)
            )
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = employee.fullName,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    style = typography.subtitle1
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = employee.team,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = typography.caption
                )
            }
        }
    }
}
