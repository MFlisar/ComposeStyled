package com.michaelflisar.demo.demos.libraries

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuScope
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHostState
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composetable.ColumnWidth
import com.michaelflisar.composetable.ProviderTableModel
import com.michaelflisar.composetable.Table
import com.michaelflisar.composetable.TableCache
import com.michaelflisar.composetable.TableColumn
import com.michaelflisar.composetable.TableDataProvider
import com.michaelflisar.composetable.TableDisplay
import com.michaelflisar.composetable.TableEmptyContent
import com.michaelflisar.composetable.TableErrorContent
import com.michaelflisar.composetable.TableFilter
import com.michaelflisar.composetable.TableFilterPresentation
import com.michaelflisar.composetable.TableFooter
import com.michaelflisar.composetable.TableHeaderInteraction
import com.michaelflisar.composetable.TableInteraction
import com.michaelflisar.composetable.TableItemCount
import com.michaelflisar.composetable.TableLazyConfig
import com.michaelflisar.composetable.TableModel
import com.michaelflisar.composetable.TablePagination
import com.michaelflisar.composetable.TableQuery
import com.michaelflisar.composetable.TableResetSortButton
import com.michaelflisar.composetable.TableSearchBar
import com.michaelflisar.composetable.TableSelectionCount
import com.michaelflisar.composetable.TableState
import com.michaelflisar.composetable.TableTexts
import com.michaelflisar.composetable.TableToolbar
import com.michaelflisar.composetable.applyTableQuery
import com.michaelflisar.composetable.booleanColumn
import com.michaelflisar.composetable.customColumn
import com.michaelflisar.composetable.enumColumn
import com.michaelflisar.composetable.numberColumn
import com.michaelflisar.composetable.numberFilter
import com.michaelflisar.composetable.rememberTable
import com.michaelflisar.composetable.rememberTableState
import com.michaelflisar.composetable.textColumn
import com.michaelflisar.composetable.textFilter
import com.michaelflisar.demo.helper.DemoTopBar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun DemoPageComposeTable(onBack: () -> Unit) {
    val snackbarState = remember { StyledSnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showMessage: (String) -> Unit = { message ->
        scope.launch {
            snackbarState.currentSnackbarData?.dismiss()
            snackbarState.showSnackbar(message)
        }
    }
    val providerMode = rememberSaveable { mutableStateOf(false) }

    val scaffoldColor = StyledTheme.colorScheme.surface
    val topBarColor = StyledTheme.colorScheme.primary
    SystemBarStyleEffectIfNeeded(
        statusBarColor = topBarColor,
        navigationBarColor = scaffoldColor,
        backgroundColor = scaffoldColor,
    )

    StyledScaffold(
        containerColor = scaffoldColor,
        topBar = {
            DemoTopBar(
                containerColor = topBarColor,
                title = "ComposeTable",
                onBack = onBack,
            )
        },
        snackbarHost = { StyledSnackbarHost(snackbarState) },
    ) { paddingValues ->
        key(providerMode.value) {
            ComposeTableDemo(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(StyledTheme.padding.content),
                providerMode = providerMode,
                showMessage = showMessage,
            )
        }
    }
}

@Composable
private fun ComposeTableDemo(
    modifier: Modifier,
    providerMode: MutableState<Boolean>,
    showMessage: (String) -> Unit,
) {
    val autoWidth = rememberSaveable { mutableStateOf(false) }
    val headerInteractionMode = rememberSaveable { mutableStateOf(1) }
    val interactionMode = rememberSaveable { mutableStateOf(0) }
    val failNextCount = remember { mutableStateOf(false) }
    val failNextLoad = remember { mutableStateOf(false) }
    val showTextSearch = rememberSaveable { mutableStateOf(true) }
    val showToolbarFilters = rememberSaveable { mutableStateOf(true) }
    val showHeaderFilters = rememberSaveable { mutableStateOf(true) }
    val showHeaderFilterIcon = rememberSaveable { mutableStateOf(true) }
    val toolbarFilterPresentation = rememberSaveable {
        mutableStateOf(TableFilterPresentation.Dialog)
    }
    val headerFilterPresentation = rememberSaveable {
        mutableStateOf(TableFilterPresentation.Dialog)
    }
    val customEmpty = rememberSaveable { mutableStateOf(false) }
    val customErrors = rememberSaveable { mutableStateOf(false) }

    val state = rememberTableState(TableDisplay.Paged())
    val columns = tableColumns(autoWidth.value, showMessage)
    val currentColumns = rememberUpdatedState(columns)
    val table: TableModel<TableItem> = if (providerMode.value) {
        val provider = remember {
            val generation = ++tableGeneration
            DemoTableProvider(
                size = 1000,
                createItems = { start, count -> tableItems(start, count, generation) },
                failNextCount = failNextCount,
                failNextLoad = failNextLoad,
            ) { currentColumns.value }
        }
        rememberTable(
            provider = provider,
            columns = columns,
            state = state,
            lazyConfig = TableLazyConfig(cache = TableCache.Lru(5)),
        ) { it.id }
    } else {
        rememberTable(
            initialItems = { tableItems(0, 1000) },
            columns = columns,
            state = state,
        ) { it.id }
    }

    StyledColumn(
        modifier = modifier,
        itemSpacing = StyledTheme.spacing.medium,
    ) {
        StyledSurface(
            modifier = Modifier.fillMaxWidth(),
            color = StyledTheme.colorScheme.surfaceVariant,
            shape = StyledTheme.shapes.medium,
        ) {
            TableSettings(
                modifier = Modifier.fillMaxWidth().padding(StyledTheme.spacing.medium),
                table = table,
                state = state,
                providerMode = providerMode,
                autoWidth = autoWidth,
                headerInteractionMode = headerInteractionMode,
                interactionMode = interactionMode,
                failNextCount = failNextCount,
                failNextLoad = failNextLoad,
                showTextSearch = showTextSearch,
                showToolbarFilters = showToolbarFilters,
                showHeaderFilters = showHeaderFilters,
                showHeaderFilterIcon = showHeaderFilterIcon,
                toolbarFilterPresentation = toolbarFilterPresentation,
                headerFilterPresentation = headerFilterPresentation,
                customErrors = customErrors,
                customEmpty = customEmpty,
                showMessage = showMessage,
            )
        }

        Table(
            table = table,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            interaction = when (interactionMode.value) {
                1 -> TableInteraction.Select()
                2 -> TableInteraction.RowClick {
                    showMessage("Row clicked: ${it.name}")
                }

                3 -> TableInteraction.CellClick { item, column ->
                    showMessage("Cell clicked: ${item.name}, ${column.title}")
                }

                else -> TableInteraction.None()
            },
            texts = TableTexts.English,
            showHeaderFilters = showHeaderFilters.value,
            showHeaderFilterIcon = showHeaderFilterIcon.value,
            headerFilterPresentation = headerFilterPresentation.value,
            emptyContent = {
                StyledColumn {
                    if (customEmpty.value) StyledText("No data in the current provider.")
                    TableEmptyContent()
                }
            },
            errorContent = { error ->
                StyledColumn {
                    if (customErrors.value) {
                        StyledText(
                            "Provider ${error.kind.name.lowercase()} error" +
                                    (error.index?.let { " at row $it" } ?: "")
                        )
                    }
                    TableErrorContent(error)
                }
            },
            headerInteraction = when (headerInteractionMode.value) {
                1 -> TableHeaderInteraction.SortOnClick
                2 -> TableHeaderInteraction.MenuOnClick
                3 -> TableHeaderInteraction.Click { column ->
                    showMessage("Header clicked: ${column.title}")
                }

                else -> TableHeaderInteraction.None
            },
            header = {
                TableToolbar {
                    StyledText(
                        text = "ComposeTable demo",
                        style = StyledTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.weight(1f))
                    TableResetSortButton()
                }
                TableSearchBar(
                    showTextSearch = showTextSearch.value,
                    showColumnFilters = showToolbarFilters.value,
                    filterPresentation = toolbarFilterPresentation.value,
                )
            },
            footer = {
                TableFooter {
                    TableItemCount()
                    Spacer(Modifier.width(8.dp))
                    StyledText("|")
                    Spacer(Modifier.width(8.dp))
                    TableSelectionCount()
                    Spacer(Modifier.weight(1f))
                    TablePagination()
                }
            },
            cell = { it.Content() },
        )

        StyledText(
            "Selected keys: ${state.selectedKeys.take(10).joinToString()} | " +
                    "Cached selected items: ${table.loadedSelectedItems.size}",
            style = StyledTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun TableSettings(
    modifier: Modifier,
    table: TableModel<TableItem>,
    state: TableState,
    providerMode: MutableState<Boolean>,
    autoWidth: MutableState<Boolean>,
    headerInteractionMode: MutableState<Int>,
    interactionMode: MutableState<Int>,
    failNextCount: MutableState<Boolean>,
    failNextLoad: MutableState<Boolean>,
    showTextSearch: MutableState<Boolean>,
    showToolbarFilters: MutableState<Boolean>,
    showHeaderFilters: MutableState<Boolean>,
    showHeaderFilterIcon: MutableState<Boolean>,
    toolbarFilterPresentation: MutableState<TableFilterPresentation>,
    headerFilterPresentation: MutableState<TableFilterPresentation>,
    customErrors: MutableState<Boolean>,
    customEmpty: MutableState<Boolean>,
    showMessage: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var replacing by remember { mutableStateOf(false) }

    fun replaceData(empty: Boolean) {
        scope.launch {
            replacing = true
            try {
                if (empty) table.clear() else table.replaceAll(tableItems(0, 1000))
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                showMessage("Data replacement failed: ${error.message ?: error}")
            } finally {
                replacing = false
            }
        }
    }

    StyledColumn(
        modifier = modifier,
        itemSpacing = StyledTheme.spacing.small,
    ) {
        StyledRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            itemSpacing = StyledTheme.spacing.medium
        ) {
            StyledText(
                text = "Settings",
                modifier = Modifier.weight(1f),
                style = StyledTheme.typography.titleMedium,
            )

            StyledPopupMenu(
                triggerContent = { StyledIcon(StyledIcons.Settings) },
                menuMaxHeight = 600.dp
            ) {
                header("Table")
                checkbox(
                    text = "Suspend provider",
                    checked = providerMode.value,
                    onCheckedChange = { providerMode.value = it },
                )
                checkbox(
                    text = "Paged",
                    checked = state.display is TableDisplay.Paged,
                    onCheckedChange = { paged ->
                        state.setDisplay(
                            if (paged) TableDisplay.Paged() else TableDisplay.Continuous
                        )
                    },
                )
                checkbox(
                    text = "Auto name width",
                    checked = autoWidth.value,
                    onCheckedChange = { autoWidth.value = it },
                )
                header("Interaction")
                selection(
                    title = "Header interaction",
                    items = listOf("None", "Sort on click", "Menu on click", "Header click"),
                    selectedIndex = headerInteractionMode.value,
                    onSelectionChange = { headerInteractionMode.value = it },
                )
                selection(
                    title = "Cell interaction",
                    items = listOf("None", "Select", "Row click", "Cell click"),
                    selectedIndex = interactionMode.value,
                    onSelectionChange = { interactionMode.value = it },
                )
                header("Presentation and Options")
                selection(
                    title = "Toolbar filter presentation",
                    items = TableFilterPresentation.entries,
                    selected = toolbarFilterPresentation.value,
                    label = { it.name },
                    onSelectionChange = { toolbarFilterPresentation.value = it },
                )
                selection(
                    title = "Header filter presentation",
                    items = TableFilterPresentation.entries,
                    selected = headerFilterPresentation.value,
                    label = { it.name },
                    onSelectionChange = { headerFilterPresentation.value = it },
                )
                submenu("Options") {
                    checkbox(
                        text = "Text search",
                        checked = showTextSearch.value,
                        onCheckedChange = { showTextSearch.value = it },
                    )
                    checkbox(
                        text = "Toolbar filters",
                        checked = showToolbarFilters.value,
                        onCheckedChange = { showToolbarFilters.value = it },
                    )
                    checkbox(
                        text = "Header filters",
                        checked = showHeaderFilters.value,
                        onCheckedChange = { showHeaderFilters.value = it },
                    )
                    checkbox(
                        text = "Header filter icon",
                        checked = showHeaderFilterIcon.value,
                        onCheckedChange = { showHeaderFilterIcon.value = it },
                    )
                    checkbox(
                        text = "Custom errors",
                        checked = customErrors.value,
                        onCheckedChange = { customErrors.value = it },
                    )
                    checkbox(
                        text = "Custom empty",
                        checked = customEmpty.value,
                        onCheckedChange = { customEmpty.value = it },
                    )
                }
            }

            // Actions
            StyledPopupMenu(
                triggerContent = { StyledIcon(Icons.AutoMirrored.Filled.DirectionsRun.toIconComposable()) },
                enabled = !replacing,
                menuMaxHeight = 600.dp
            ) {
                item(
                    text = "New data",
                    enabled = !replacing,
                    onClick = { replaceData(false) },
                )
                item(
                    text = "Empty data",
                    enabled = !replacing,
                    onClick = { replaceData(true) },
                )
                if (table is ProviderTableModel) {
                    item(
                        text = "Refresh",
                        leadingIcon = {
                            StyledIcon(
                                icon = Icons.Default.Refresh.toIconComposable(),
                                contentDescription = null,
                            )
                        },
                        onClick = { table.refresh() },
                    )
                    item(
                        text = "Fail next count",
                        onClick = {
                            failNextCount.value = true
                            table.refresh()
                        },
                    )
                    item(
                        text = "Fail next range",
                        onClick = {
                            failNextLoad.value = true
                            table.refresh()
                        },
                    )
                }
                divider()
                item(
                    text = "Reset all state",
                    onClick = { state.reset() },
                )
                item(
                    text = "Reset search",
                    onClick = { state.reset(search = true) },
                )
                item(
                    text = "Reset filters",
                    onClick = { state.reset(filters = true) },
                )
                item(
                    text = "Reset sorts",
                    onClick = { state.reset(sorts = true) },
                )
                item(
                    text = "Reset selection",
                    onClick = { state.reset(selection = true) },
                )
                item(
                    text = "Reset position",
                    onClick = { state.reset(position = true) },
                )
            }
        }
    }
}

private data class TableItem(
    val id: Int,
    val name: String,
    val description: String,
    val type: TableType,
    val checked: MutableState<Boolean>,
)

private enum class TableType { First, Second, Third }

private var tableGeneration = 0L

private fun tableItems(
    from: Int,
    size: Int,
    generation: Long = ++tableGeneration,
): List<TableItem> {
    require(from >= 0 && size >= 0 && from.toLong() + size <= Int.MAX_VALUE.toLong() + 1)
    return List(size) { offset ->
        val id = from + offset
        TableItem(
            id = id,
            name = "Item $id - dataset $generation",
            description = "Description of item $id from dataset $generation",
            type = TableType.entries[id % TableType.entries.size],
            checked = mutableStateOf(id % 2 == 0),
        )
    }
}

@Composable
private fun tableColumns(
    autoWidth: Boolean,
    showMessage: (String) -> Unit,
): List<TableColumn<TableItem, *>> = remember(autoWidth) {
    listOf(
        numberColumn(
            id = "id",
            title = "ID",
            value = { it.id },
            width = ColumnWidth.Fixed(80.dp),
            filter = numberFilter(String::toIntOrNull),
        ),
        textColumn(
            id = "name",
            title = "Name",
            value = { it.name },
            width = if (autoWidth) ColumnWidth.Auto() else ColumnWidth.Weight(minWidth = 160.dp),
            filter = textFilter(),
        ),
        textColumn(
            id = "description",
            title = "Description",
            value = { it.description },
            width = ColumnWidth.Weight(minWidth = 200.dp),
            filter = textFilter(),
        ),
        enumColumn(
            id = "type",
            title = "Type",
            value = { item: TableItem -> item.type },
            width = ColumnWidth.Fixed(150.dp),
            comparator = compareBy { it.ordinal },
            filter = TableFilter.Choice(TableType.entries, { it.name }, multiSelect = true),
            cellContent = { StyledText(it.value.name) },
        ),
        booleanColumn(
            id = "checked",
            title = "Checked",
            value = { item: TableItem -> item.checked.value },
            width = ColumnWidth.Fixed(130.dp),
            comparator = naturalOrder(),
            filter = TableFilter.BooleanFilter(),
            searchText = null,
            cellContent = { cell ->
                StyledCheckbox(
                    checked = cell.value,
                    onCheckedChange = { cell.item.checked.value = it },
                )
            },
            cellContentAlignment = Alignment.Center,
        ),
        customColumn(
            id = "icon",
            title = "Icon",
            value = { _: TableItem -> Icons.Default.Info },
            width = ColumnWidth.Fixed(64.dp),
            searchText = null,
            headerContent = {
                StyledIcon(
                    icon = Icons.Default.Info.toIconComposable(),
                    contentDescription = "Information",
                )
            },
            cellContent = {
                StyledIcon(
                    icon = it.value.toIconComposable(),
                    contentDescription = null,
                )
            },
            headerContentAlignment = Alignment.Center,
            cellContentAlignment = Alignment.Center,
        ),
        customColumn(
            id = "delete",
            title = "Delete",
            value = { item: TableItem -> item.id },
            width = ColumnWidth.Fixed(64.dp),
            searchText = null,
            headerContent = {
                StyledIcon(
                    icon = Icons.Default.Delete.toIconComposable(),
                    contentDescription = "Delete",
                )
            },
            cellContent = { cell ->
                val scope = rememberCoroutineScope()
                StyledIconButton(
                    onClick = {
                        scope.launch {
                            try {
                                cell.table.remove(cell.value)
                            } catch (error: CancellationException) {
                                throw error
                            } catch (error: Exception) {
                                showMessage("Delete failed: ${error.message ?: error}")
                            }
                        }
                    },
                    icon = Icons.Default.Delete.toIconComposable(),
                    contentDescription = "Delete ${cell.item.name}",
                )
            },
            headerContentAlignment = Alignment.Center,
            cellContentAlignment = Alignment.Center,
        ),
    )
}

private class DemoTableProvider(
    size: Int,
    private val createItems: (start: Int, count: Int) -> List<TableItem>,
    private val failNextCount: MutableState<Boolean>,
    private val failNextLoad: MutableState<Boolean>,
    private val columns: () -> List<TableColumn<TableItem, *>>,
) : TableDataProvider<TableItem> {

    private var generatedSize = size
    private val removed = mutableSetOf<Int>()
    private val overrides = linkedMapOf<Int, TableItem>()
    private val checkedStates = mutableMapOf<Int, MutableState<Boolean>>()

    init {
        require(size >= 0)
    }

    private fun contains(id: Int) =
        id in overrides || (id in 0 until generatedSize && id !in removed)

    private fun ids(): List<Int> =
        (0 until generatedSize).filter { it !in removed } +
                overrides.keys.filter { it !in 0 until generatedSize }

    private fun itemsFor(ids: List<Int>): List<TableItem> {
        val result = ArrayList<TableItem>(ids.size)
        var position = 0
        while (position < ids.size) {
            val id = ids[position]
            val replacement = overrides[id]
            if (replacement != null) {
                result += replacement
                position++
            } else {
                var end = position + 1
                while (
                    end < ids.size &&
                    ids[end] == ids[end - 1] + 1 &&
                    ids[end] !in overrides
                ) {
                    end++
                }
                val generated = createItems(id, end - position)
                require(
                    generated.size == end - position &&
                            generated.indices.all { generated[it].id == ids[position + it] }
                ) {
                    "Generator must return the requested number of items with absolute index IDs"
                }
                result += generated.map { item ->
                    item.copy(checked = checkedStates.getOrPut(item.id) { item.checked })
                }
                position = end
            }
        }
        return result
    }

    override suspend fun insert(item: TableItem) {
        require(!contains(item.id))
        overrides[item.id] = item
        removed.remove(item.id)
    }

    override suspend fun update(key: Any, item: TableItem) {
        require(key == item.id && contains(item.id))
        overrides[item.id] = item
    }

    override suspend fun remove(key: Any) {
        require(key is Int && contains(key))
        overrides.remove(key)
        checkedStates.remove(key)
        if (key in 0 until generatedSize) removed += key
    }

    override suspend fun replaceAll(items: List<TableItem>) {
        require(items.map { it.id }.distinct().size == items.size)
        generatedSize = 0
        removed.clear()
        checkedStates.clear()
        overrides.clear()
        items.forEach { overrides[it.id] = it }
    }

    override suspend fun count(query: TableQuery): Long {
        delay(250.milliseconds)
        if (failNextCount.value) {
            failNextCount.value = false
            error("Simulated count failure")
        }
        val ids = ids()
        return if (query.search.isEmpty() && query.filters.isEmpty()) {
            ids.size.toLong()
        } else {
            applyTableQuery(
                itemsFor(ids),
                columns(),
                query.copy(sorts = emptyList()),
            ).size.toLong()
        }
    }

    override suspend fun load(query: TableQuery, offset: Long, limit: Int): List<TableItem> {
        delay(350.milliseconds)
        if (failNextLoad.value) {
            failNextLoad.value = false
            error("Simulated range failure")
        }
        require(offset >= 0 && offset <= Int.MAX_VALUE && limit > 0)
        val ids = ids()
        return if (
            query.search.isEmpty() &&
            query.filters.isEmpty() &&
            query.sorts.isEmpty()
        ) {
            itemsFor(ids.drop(offset.toInt()).take(limit))
        } else {
            applyTableQuery(itemsFor(ids), columns(), query)
                .drop(offset.toInt())
                .take(limit)
        }
    }
}
