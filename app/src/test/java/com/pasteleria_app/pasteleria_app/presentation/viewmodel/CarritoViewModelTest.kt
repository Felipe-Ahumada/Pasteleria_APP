package com.pasteleria_app.pasteleria_app.presentation.viewmodel

import com.pasteleria_app.pasteleria_app.data.local.dao.CarritoUsuarioDao
import com.pasteleria_app.pasteleria_app.domain.model.Producto
import com.pasteleria_app.pasteleria_app.domain.repository.CarritoRepository
import com.pasteleria_app.pasteleria_app.presentation.ui.viewmodel.CarritoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CarritoViewModelTest {

    private lateinit var viewModel: CarritoViewModel
    private val repository: CarritoRepository = mockk(relaxed = true)
    private val dao: CarritoUsuarioDao = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.obtenerProductos() } returns flowOf(emptyList())
        viewModel = CarritoViewModel(repository, dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agregarProducto calls repository`() = runTest {
        val p = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        viewModel.agregarProducto(p)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.agregarProducto(p) }
    }

    @Test
    fun `aumentarCantidad calls repository`() = runTest {
        val p = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        viewModel.aumentarCantidad(p)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.actualizarProducto(any()) }
    }

    @Test
    fun `disminuirCantidad calls repository update`() = runTest {
        val p = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO", cantidad = 2
        )
        viewModel.disminuirCantidad(p)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.actualizarProducto(any()) }
    }

    @Test
    fun `disminuirCantidad calls repository delete when 1`() = runTest {
        val p = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO", cantidad = 1
        )
        viewModel.disminuirCantidad(p)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.eliminarProducto(p) }
    }

    @Test
    fun `eliminarProducto calls repository`() = runTest {
        val p = Producto(
            id = 1, productoId = 1L, nombre = "Pastel", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        viewModel.eliminarProducto(p)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.eliminarProducto(p) }
    }

    @Test
    fun `vaciarCarrito calls repository`() = runTest {
        viewModel.vaciarCarrito()
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.vaciarCarrito() }
    }

    @Test
    fun `calcularTotal returns correct sum`() = runTest {
        val p1 = Producto(
            id = 1, productoId = 1L, nombre = "P1", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO", cantidad = 2
        )
        val p2 = Producto(
            id = 2, productoId = 2L, nombre = "P2", precio = 500, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO", cantidad = 1
        )
        coEvery { repository.obtenerProductos() } returns flowOf(listOf(p1, p2))
        
        // Re-init to pick up flow
        viewModel = CarritoViewModel(repository, dao)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2500, viewModel.calcularTotal())
    }

    @Test
    fun `guardarCarritoUsuario calls dao`() = runTest {
        val p = Producto(
            id = 1, productoId = 1L, nombre = "P1", precio = 1000, imagenUrl = "url",
            codigoProducto = "code", descripcion = "desc", stock = 10, stockCritico = 5, categoria = null, estado = "ACTIVO"
        )
        coEvery { repository.obtenerProductos() } returns flowOf(listOf(p))
        viewModel = CarritoViewModel(repository, dao)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.guardarCarritoUsuario("email")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { dao.vaciarCarritoUsuario("email") }
        coVerify { dao.agregarProductoAlCarrito(any()) }
    }

    @Test
    fun `cargarCarritoUsuario calls repository`() = runTest {
        coEvery { dao.obtenerCarritoPorUsuario("email") } returns emptyList()
        viewModel.cargarCarritoUsuario("email")
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.vaciarCarrito() }
    }
    
    @Test
    fun `init loads products`() = runTest {
        coEvery { repository.obtenerProductos() } returns flowOf(emptyList())
        viewModel = CarritoViewModel(repository, dao)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { repository.obtenerProductos() }
    }
}
