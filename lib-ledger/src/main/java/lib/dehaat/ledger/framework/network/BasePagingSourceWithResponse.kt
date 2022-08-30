package lib.dehaat.ledger.framework.network

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSourceWithResponse<D : Any, R>(
    private val apiCall: suspend (Int, Int) -> R,
    private val pageSize: Int = 10,
    private val parseDataList: (R) -> List<D>,
    private val onResponse: suspend (Int, R) -> Unit = { _, _ -> }
) : PagingSource<Int, D>() {
    override fun getRefreshKey(state: PagingState<Int, D>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>) = try {
        val currentPageToLoad = params.key ?: 1
        val response = apiCall.invoke(currentPageToLoad, pageSize)
        onResponse.invoke(currentPageToLoad, response)
        val data = parseDataList(response)
        LoadResult.Page(
            data = data,
            prevKey = null, // Only paging forward.
            nextKey = if (data.isEmpty() || data.size < pageSize) null else currentPageToLoad.plus(
                1
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
        LoadResult.Error(e)
    }
}
