package com.pasteleria_app.pasteleria_app.presentation.viewmodel

import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductoViewModelTest {

    private lateinit var viewModel: ProductoViewModel
    private val repository: ProductoRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getProductos() } returns emptyList()
        viewModel = ProductoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductos success`() = runTest {
        val p = Producto(
            id = 1,
            productoId = 100L,
            nombre = "P",
            precio = 100,
            imagenUrl = "url",
            codigoProducto = "c",
            descripcion = "d",
            stock = 10,
            stockCritico = 5,
            categoria = null,
            estado = "A"
        )
        coEvery { repository.getProductos() } returns listOf(p)

        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.productos.value.size)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `cargarProductos error`() = runTest {
        coEvery { repository.getProductos() } throws Exception("Error")

        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `cargarProductosPorCategoria success`() = runTest {
        val p = Producto(
            id = 1,
            productoId = 100L,
            nombre = "P",
            precio = 100,
            imagenUrl = "url",
            codigoProducto = "c",
            descripcion = "d",
            stock = 10,
            stockCritico = 5,
            categoria = null,
            estado = "A"
        )
        coEvery { repository.getProductosPorCategoria(1) } returns listOf(p)

        viewModel.cargarProductosPorCategoria(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.productos.value.size)
    }

    @Test
    fun `cargarProductosPorCategoria error`() = runTest {
        coEvery { repository.getProductosPorCategoria(1) } throws Exception("Error")

        viewModel.cargarProductosPorCategoria(1)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.error.value)
    }
    
    @Test
    fun `init calls cargarProductos`() = runTest {
        // Setup new VM to test init
        coEvery { repository.getProductos() } returns emptyList()
        val vm = ProductoViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        io.mockk.coVerify { repository.getProductos() }
    }
    
    @Test
    fun `loading state updates`() = runTest {
        // This is tricky with runTest as it skips delays, but we can verify it was set to false at end
        coEvery { repository.getProductos() } returns emptyList()
        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `error state clears on success`() = runTest {
        // First fail
        coEvery { repository.getProductos() } throws Exception("Fail")
        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.error.value)
        
        // Then success
        coEvery { repository.getProductos() } returns emptyList()
        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNull(viewModel.error.value)
    }
    
    @Test
    fun `cargarProductosPorCategoria updates loading`() = runTest {
        coEvery { repository.getProductosPorCategoria(1) } returns emptyList()
        viewModel.cargarProductosPorCategoria(1)
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `cargarProductosPorCategoria updates error`() = runTest {
        coEvery { repository.getProductosPorCategoria(1) } throws Exception("Fail")
        viewModel.cargarProductosPorCategoria(1)
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.error.value)
    }
    
    @Test
    fun `products state updates`() = runTest {
        val p = Producto(
            id = 1,
            productoId = 100L,
            nombre = "P",
            precio = 100,
            imagenUrl = "url",
            codigoProducto = "c",
            descripcion = "d",
            stock = 10,
            stockCritico = 5,
            categoria = null,
            estado = "A"
        )
        coEvery { repository.getProductos() } returns listOf(p)
        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(listOf(p), viewModel.productos.value)
    }
}
